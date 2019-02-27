package com.kasite.client.yy.util;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.yy.bean.dbo.YyLimit;
import com.kasite.client.yy.bean.dbo.YyWater;
import com.kasite.client.yy.constant.Constant;
import com.kasite.client.yy.dao.IYyLimitMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.constant.RetCode.YY;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryRegInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.kasite.core.serviceinterface.module.yy.IYYRuleService;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYRule;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * @author linjf TODO
 */
@Component
public class YuYueUtil {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_YY);

	/** 预约验证:1 */
	final static int CHECKTYPE_1 = 1;
	/** 退号验证:2 */
	final static int CHECKTYPE_2 = 2;
//	@Autowired
//	private IYYService yyService;
	
//	@Autowired
//	IYyRuleMapper ruleMapper;
	
	@Autowired
	IYYRuleService yyRuleService;
	
	@Autowired
	IYyLimitMapper limitMapper;
	
//	public void unlock(String orderId) throws BusinessException, ParamException, SQLException {
//		Document document = DocumentHelper.createDocument();
//		Element req = document.addElement(Constant.REQ);
//		XMLUtil.addElement(req, Constant.TRANSACTIONCODE, Constant.UNLOCKCODE);
//		Element service = req.addElement(Constant.DATA);
//		XMLUtil.addElement(service, "OrderId", orderId);
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setAuthInfo("{ClientVersion:1,ClientId:'100123',Sign:'vzncp1lSloKF2V2CVYjmoQ==',SessionKey:'SessionKey'}");
//		msg.setParam(document.asXML());
//		msg.setApiName(ApiModule.YY.UnLock.getName());
//		msg.setOutType(1);
//		msg.setParamType(1);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		msg.setSeq(sdf.format(new Date()));
//		String resp = yyService.Unlock(msg);
//		LogUtil.error(log, new LogBody().set("OrderId", orderId).set("req", req.asXML()).set("resp", resp));
//		CommonResp commResp = new CommonResp(resp);
//		if (!Constant.SUCCESSCODE.equals(commResp.getRespCode())) {
//			LogUtil.error(log, "OrderUtil--Unlock-req-" + "" + document.asXML());
//			LogUtil.error(log, "OrderUtil--Unlock-resp-" + "" + resp);
//			throw new BusinessException(RetCode.Common.CallHOPClientError, commResp.getRespMessage());
//		}
//	}

//	/**
//	 * 预约失败解锁操作
//	 * 
//	 * @param yyOrderId
//	 * @throws ParseException
//	 */
//	public void dealUnlock(String yyOrderId, String channelId) {
//		Document inXml = DocumentHelper.createDocument();
//		Element req = inXml.addElement(Constant.REQ);
//		XMLUtil.addElement(req, Constant.TRANSACTIONCODE, Constant.UNLOCKCODE);
//		Element ser = req.addElement(Constant.DATA);
//		XMLUtil.addElement(ser, "OrderId", yyOrderId);
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setAuthInfo("{ClientVersion:1,ClientId:'" + channelId + "',Sign:'Sign',SessionKey:'SessionKey'}");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String seq = sdf.format(new Date());
//		msg.setApiName(ApiModule.YY.UnLock.getName());
//		msg.setSeq(seq);
//		msg.setParamType(1);
//		msg.setOutType(KstHosConstant.OUTTYPE);
//		msg.setParam(inXml.asXML());
//		String result = null;
//		try {
//			result = yyService.Unlock(msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		LogUtil.error(log, new LogBody().set("OrderId", yyOrderId).set("req", req.asXML()).set("resp", result));
//	}

	/**
	 * 预约限制验证
	 * 
	 * @param water
	 * @throws Exception
	 */
	public void checkLimit(YyWater water) throws Exception {
		Example example = new Example(YyLimit.class);
		example.createCriteria().andEqualTo("state", 1);
		List<YyLimit> list = limitMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				YyLimit ll = list.get(i);
				String sqlMs = getLimitSql(water, ll.getSqlMessage());
				int count = limitMapper.queryCount(sqlMs);
//				Sql countSql = new Sql(sqlMs);
//				int count = DB.me().queryForInteger(MyDatabaseEnum.hos, countSql);
				if (count >= ll.getCountNum()) {
					throw new RRException(YY.ERROR_YYCHECK,
							ll.getTextMessage().replace(",", ll.getCountNum() + " "));
				}
			}
		}
	}

	/**
	 * 获取限制Sql
	 * {0:身份证},{1:联系电话},{2:就诊卡号},{3:患者姓名},{4:就诊日期},{5:时段},{6:排班id},{7:医生工号},{8:
	 * 科室代码}
	 * 
	 * @param water
	 * @param GhmessageFormat
	 * @return
	 * @throws ParseException
	 */
	public String getLimitSql(YyWater water, String ghmessageFormat) throws ParseException {

		String idCardNo = water.getIdCardNo();
		String mobile = water.getUserMobile();
		String clinicCard = water.getClinicCard();
		String userName = water.getUserName();
		String registerDate = null;
		if (StringUtil.isNotBlank(water.getRegisterDate())) {
			registerDate = water.getRegisterDate();
		}
		Integer timeId = water.getTimeId();
		String scheduleId = water.getScheduleId();
		String deptCode = water.getDeptCode();
		String doctorCode = water.getDoctorCode();

		Object[] params = new Object[] { idCardNo, mobile, clinicCard, userName, registerDate, timeId, scheduleId,
				doctorCode, deptCode, };

		StringBuilder sms = new StringBuilder();
		if (ghmessageFormat == null) {
			return null;
		}
		MessageFormat formatMsg = new MessageFormat(ghmessageFormat);
		sms.append(formatMsg.format(params));

		return sms.toString();
	}

//	public String addOrder(AuthInfoVo authInfo,YyWater water, YyLock lock, String clientId)
//			throws RemoteException, BusinessException {
//		Document inXml = DocumentHelper.createDocument();
//		Element resp = inXml.addElement(Constant.REQ);
//		XMLUtil.addElement(resp, Constant.TRANSACTIONCODE, Constant.ADDORDERCODE);
//		Element ser = resp.addElement(Constant.DATA);
//		XMLUtil.addElement(ser, "OrderId", water.getYyOrderId());
//		XMLUtil.addElement(ser, "PayMoney", water.getFee());
//		XMLUtil.addElement(ser, "TotalMoney", water.getFee());
//		XMLUtil.addElement(ser, "PriceName", "挂号支付");
//		XMLUtil.addElement(ser, "OrderMemo", "挂号订单");
//		XMLUtil.addElement(ser, "CardNo", lock.getCardNo());
//		XMLUtil.addElement(ser, "CardType", lock.getCardTypeCode());
//		XMLUtil.addElement(ser, "OperatorId", water.getOperatorId());
//		XMLUtil.addElement(ser, "OperatorName", water.getOperatorName());
//		XMLUtil.addElement(ser, "ServiceId", "0");
//		XMLUtil.addElement(ser, "IfOnlinePay", "1");
//		XMLUtil.addElement(ser, "PayFeeItem", "0");
//		Element data = ser.addElement(Constant.DATA_1);
//		XMLUtil.addElement(data, "DoctorCode", water.getDoctorCode());
//		XMLUtil.addElement(data, "DoctorName", water.getDoctorName());
//		XMLUtil.addElement(data, "DeptCode", water.getDeptCode());
//		XMLUtil.addElement(data, "DeptName", water.getDeptName());
//		XMLUtil.addElement(data, "MedicalExtension", "");
//		String result = null;
//		try {
//			result = RpcUtil.callRpc(ApiModule.Order.AddOrderLocal.getName(), authInfo, inXml.asXML(), 1, 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//			LogUtil.error(log, e);
//			throw new BusinessException(RetCode.YY.ERROR_CALLORDER,"新增订单失败");
//		}
//		LogUtil.error(log, new LogBody().set("挂号订单", water.getYyOrderId()).set("req", inXml.asXML()).set("resp", resp));
//		return result;
//	}

	/**
	 * @param regTimeStamp
	 *            就诊日期
	 * @param checkType
	 *            1挂号验证 2退号验证
	 * @throws Exception
	 */
	public void checkRule(InterfaceMessage msg,YyWater water, int checkType) throws Exception {
		Timestamp regTimeStamp = DateOper.parse2Timestamp(water.getRegisterDate());
//		YyRule query = new YyRule();
//		query.setState(1);
//		query.setHosId(water.getHosId());
//		YyRule rule = ruleMapper.selectOne(query);
		
		CommonResp<RespQueryYYRule> ruleResp = yyRuleService.queryYYRule(new CommonReq<ReqString>(new ReqString(msg, water.getHosId())));
		if(!KstHosConstant.SUCCESSCODE.equals(ruleResp.getCode())) {
			throw new Exception("验证规则信息异常，没有找到规则信息");
		}
		
		if (ruleResp.getData()!=null && ruleResp.getResultData()!=null) {
			RespQueryYYRule  rule = ruleResp.getResultData();
			// 预约提前天数
			int startDay = rule.getStartDay();
			// 提前时间
			String startTime = rule.getStartTime();
			// 退号截止提前天数
			int endDay = rule.getEndDay();
			// 退号截止时间
			String endTime = rule.getEndTime();
			String nowDate = DateOper.getNow("yyyy-MM-dd");
			String nowTime = DateOper.getNow("HH:mm");
			// 预约验证
			if (checkType == CHECKTYPE_1) {
				String[] starTimes = startTime.split(":");
				String[] nowTimes = nowTime.split(":");
				// 当前时间小于提前时间时，只能预约预约提前天数前一天的号
				boolean isValid = Integer.parseInt(nowTimes[0]) <= Integer.parseInt(starTimes[0])
						&& Integer.parseInt(nowTimes[1]) < Integer.parseInt(starTimes[1]);
				if (isValid) {
					startDay = startDay - 1;
				}
				// 当前最迟可以预约的时间
				String lastDate = DateOper.addDate(nowDate, startDay);
				Timestamp lastTime = DateOper.parse2Timestamp(lastDate);
				if (regTimeStamp.after(lastTime)) {
					throw new RRException(RetCode.YY.ERROR_YYCHECK, "当前时间不可预约");
				}
			} else if (checkType == CHECKTYPE_2) {
				// 退号验证
				String registerDate = DateOper.formatDate(regTimeStamp, "yyyy-MM-dd");
				// 当前预约记录退号截止时间
				String cancelStopDate = DateOper.addDate(registerDate, -endDay);
				Timestamp cancelStopTime = DateOper.parse2Timestamp(cancelStopDate);
				Timestamp nowTimeStamp = DateOper.parse2Timestamp(nowDate);

				String[] endTimes = endTime.split(":");
				String[] nowTimes = nowTime.split(":");
				// 当前时间跟就诊日期时间
				String effectTime = water.getRegisterDate();
				String commendTime = water.getCommendTime();
				String[] commendTimeArray = commendTime.split("-");
				if (commendTimeArray.length == KstHosConstant.NUMBER_2) {
					commendTime = commendTimeArray[1];
				}
				Timestamp efffectTime = DateOper.parse2Timestamp(effectTime);
				if (nowTimeStamp.after(efffectTime)) {
					throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "已过就诊时间，不允许退号");
				} else if (nowTimeStamp.equals(efffectTime)) {
					if (Integer.valueOf(nowTime.replace(KstHosConstant.SYMBOL_COLON, "")) > Integer
							.valueOf(commendTime.replace(KstHosConstant.SYMBOL_COLON, ""))) {
						throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "已过就诊时间，不允许退号");
					}
				}
				// 当前时间大于退号截止时间时不可退号
				if (nowTimeStamp.after(cancelStopTime)) {
					throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "已过退号截止时间，不允许退号");
				} else if (nowTimeStamp.equals(cancelStopTime)) {
					boolean isLate = Integer.parseInt(nowTimes[0]) > Integer.parseInt(endTimes[0])
							|| Integer.parseInt(nowTimes[0]) == Integer.parseInt(endTimes[0])
									&& Integer.parseInt(nowTimes[1]) >= Integer.parseInt(endTimes[1]);
					if (isLate) {
						throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "已过退号截止时间，不允许退号");
					}
				}
			}
		} else {
			// 规则没配置默认退号截止时间为提前0天的00：00
			// 退号截止提前天数
			int endDay = 0;
			// 退号截止时间
			String endTime = "00:00";
			String nowDate = DateOper.getNow("yyyy-MM-dd");
			String nowTime = DateOper.getNow("HH:mm");
			if (checkType == CHECKTYPE_2) {
				// 退号验证
				String registerDate = DateOper.formatDate(regTimeStamp, "yyyy-MM-dd");
				// 当前预约记录退号截止时间
				String cancelStopDate = DateOper.addDate(registerDate, endDay);
				Timestamp cancelStopTime = DateOper.parse2Timestamp(cancelStopDate);
				Timestamp nowTimeStamp = DateOper.parse2Timestamp(nowDate);

				String[] endTimes = endTime.split(":");
				String[] nowTimes = nowTime.split(":");
				// 当前时间大于退号截止时间时不可退号
				if (nowTimeStamp.after(cancelStopTime)) {
					throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "当前时间不可退号");
				} else if (nowTimeStamp.equals(cancelStopTime)) {
					boolean isLate = Integer.parseInt(nowTimes[0]) > Integer.parseInt(endTimes[0])
							|| Integer.parseInt(nowTimes[0]) == Integer.parseInt(endTimes[0])
									&& Integer.parseInt(nowTimes[1]) >= Integer.parseInt(endTimes[1]);
					if (isLate) {
						throw new RRException(RetCode.YY.ERROR_CANCELCHECK, "当前时间不可退号");
					}
				}
			}
		}
	}
	
	public String getYuYueMsgParam(String channelId, String modeType, YyWater water)
			throws Exception {
		Document document = DocumentHelper.createDocument();
		Element req = document.addElement(Constant.REQ);
		XMLUtil.addElement(req, Constant.TRANSACTIONCODE, Constant.ORDERISCANCELCODE);
		Element data = req.addElement(Constant.DATA);
		Element data1 = data.addElement(Constant.DATA_1);
		XMLUtil.addElement(data, "ChannelId", channelId);
		XMLUtil.addElement(data, "ModeType", modeType);
		XMLUtil.addElement(data, "OrderId", water.getOrderId());
		XMLUtil.addElement(data, "CardNo", water.getcNo());
		XMLUtil.addElement(data, "CardType", String.valueOf(water.getcType()));
		XMLUtil.addElement(data, "Mobile", water.getUserMobile());
		XMLUtil.addElement(data, "OperatorId", water.getOperatorId());
		XMLUtil.addElement(data, "OperatorName", water.getOperatorName());
		XMLUtil.addElement(data, "OpenId", water.getOperatorId());
		XMLUtil.addElement(data1, "UserName", water.getUserName());
		XMLUtil.addElement(data1, "CardNo", water.getClinicCard());
		XMLUtil.addElement(data1, "DeptName", water.getDeptName());
		XMLUtil.addElement(data1, "DoctorName", water.getDoctorName());
		XMLUtil.addElement(data1, "RegisterDate", water.getRegisterDate());
		
		return document.asXML();
//		ApiModule api = ApiModule.Msg.SendMsg;
//		// String resp = HopWsUtil.callSendMsg(api, req.asXML(),channelId);
//		String resp = RpcUtil.callRpc(ApiModule.Msg.SendMsg.getName(), channelId, req.asXML(), 1, 1);
//		LogUtil.error(log,
//				new LogBody().set("挂号推送orderid", water.getYyOrderId()).set("req", req.asXML()).set("resp", resp));
//		CommonResp commResp = new CommonResp(resp);
//		if (!Constant.SUCCESSCODE.equals(commResp.getRespCode())) {
//			throw new BusinessException(RetCode.Common.CallHOPClientError, commResp.getRespMessage());
//		}
	}
	
	public void yyWaterToResp(RespQueryRegInfo info,YyWater yyWater) {
		info.setOrderId(yyWater.getOrderId());
		info.setCardNo(yyWater.getcNo());
		info.setIdCardNo(yyWater.getIdCardNo());
		info.setBirthDay(yyWater.getBirthday());
		info.setMobile(yyWater.getUserMobile());
		info.setName(yyWater.getUserName());
		info.setSex(yyWater.getSex());
		info.setAddress(yyWater.getAddress());
		info.setClinicCard(yyWater.getClinicCard());
		info.setDoctorName(yyWater.getDoctorName());
		info.setDoctorCode(yyWater.getDoctorCode());
		info.setDeptCode(yyWater.getDeptCode());
		info.setDeptName(yyWater.getDeptName());
		info.setPayFee(yyWater.getFee());
		info.setRegFee(yyWater.getRegistrationFee());
		info.setTreatFee(yyWater.getServiceFee());
		info.setOtherFee(yyWater.getOtherFee());
		info.setOperatorId(yyWater.getOperatorId());
		info.setOperatorName(yyWater.getOperatorName());
		info.setRegDate(yyWater.getRegisterDate());
		info.setTimeSlice(yyWater.getTimeId());
		info.setSqNo(yyWater.getQueueNo());
		info.setRemark(yyWater.getRemark());
		info.setRegFlag(yyWater.getState());
		try {
			if(yyWater.getUpdateTime()!=null) {
				info.setLastModify(DateOper.formatDate(yyWater.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("日期转换异常",e);
		}
		info.setCommendTime(yyWater.getCommendTime());
	}
	public void yyWaterToResp(HisQueryRegInfo info,YyWater yyWater) {
		info.setHisOrderId(yyWater.getHisOrderId());
		info.setCardNo(yyWater.getcNo());
		info.setIdCardNo(yyWater.getIdCardNo());
		info.setBirthDay(yyWater.getBirthday());
		info.setMobile(yyWater.getUserMobile());
		info.setName(yyWater.getUserName());
		info.setSex(yyWater.getSex());
		info.setAddress(yyWater.getAddress());
		info.setClinicCard(yyWater.getClinicCard());
		info.setDoctorName(yyWater.getDoctorName());
		info.setDoctorCode(yyWater.getDoctorCode());
		info.setDeptCode(yyWater.getDeptCode());
		info.setDeptName(yyWater.getDeptName());
		info.setPayFee(yyWater.getFee());
		info.setRegFee(yyWater.getRegistrationFee());
		info.setTreatFee(yyWater.getServiceFee());
		info.setOtherFee(yyWater.getOtherFee());
		info.setOperatorId(yyWater.getOperatorId());
		info.setOperatorName(yyWater.getOperatorName());
		info.setRegDate(yyWater.getRegisterDate());
		info.setTimeSlice(yyWater.getTimeId());
		info.setSqNo(yyWater.getQueueNo());
		info.setRemark(yyWater.getRemark());
		try {
			if(yyWater.getUpdateTime()!=null) {
				info.setLastModify(DateOper.formatDate(yyWater.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("日期转换异常",e);
		}
		info.setCommendTime(yyWater.getCommendTime());
	}
	
	public void yyOrderToResp(HisQueryRegInfo info,RespOrderLocalList orderLocal,
			String deptCode,String deptName,String doctorCode,String doctorName,
			String hisOrderId,String cardNo,String scheduleId,Integer sqNo) {
		String scheduleIdSplit = "_";
		int scheduleArrlenth = 4;
		if (orderLocal == null) {
			return;
		}
		info.setHisOrderId(hisOrderId);
		info.setCardNo(cardNo);
		info.setName(orderLocal.getOperatorName());
		info.setDoctorName(doctorName);
		info.setDoctorCode(doctorCode);
		info.setDeptCode(deptCode);
		info.setDeptName(deptName);
		info.setRegFee(Integer.valueOf(orderLocal.getTotalMoney()));
		info.setOperatorId(orderLocal.getOperatorId());
		info.setOperatorName(orderLocal.getOperatorName());
		info.setSqNo(sqNo);
	}
	
	public void yyWaterToRespForProIsNull(RespQueryRegInfo resp,YyWater water) {
		if(StringUtil.isBlank(resp.getAddress())) {
			resp.setAddress(water.getAddress());
		}
		if(StringUtil.isBlank(resp.getBirthDay())) {
			resp.setBirthDay(water.getBirthday());
		}
		if(StringUtil.isBlank(resp.getCardNo()) 
				&& StringUtil.isBlank(resp.getCardType())
				&& StringUtil.isNotBlank(water.getClinicCard())) {
			resp.setCardType(1);
			resp.setCardNo(water.getClinicCard());
		}
		if(StringUtil.isBlank(resp.getClinicCard())) {
			resp.setClinicCard(water.getClinicCard());
		}
		if(StringUtil.isBlank(resp.getDeptCode())) {
			resp.setDeptCode(water.getDeptCode());
		}
		if(StringUtil.isBlank(resp.getDeptName())) {
			resp.setDeptName(water.getDeptName());
		}
		if(StringUtil.isBlank(resp.getDoctorCode())) {
			resp.setDoctorCode(water.getDoctorCode());
		}
		if(StringUtil.isBlank(resp.getDoctorName())) {
			resp.setDoctorName(water.getDoctorName());
		}
		if(StringUtil.isBlank(resp.getIdCardNo())) {
			resp.setIdCardNo(water.getIdCardNo());
		}
		if(StringUtil.isBlank(resp.getMobile())) {
			resp.setMobile(water.getUserMobile());
		}
		if(StringUtil.isBlank(resp.getName())) {
			resp.setName(water.getUserName());
		}
		if(StringUtil.isBlank(resp.getOperatorId())) {
			resp.setOperatorId(water.getOperatorId());
		}
		if(StringUtil.isBlank(resp.getOperatorName())) {
			resp.setOperatorName(water.getOperatorName());
		}
		if(StringUtil.isBlank(resp.getOrderId())) {
			resp.setOrderId(water.getOrderId());
		}
		if(StringUtil.isBlank(resp.getOtherFee())) {
			resp.setOtherFee(water.getOtherFee());
		}
		int clinicFee = water.getClinicFee()==null?0:water.getClinicFee();
		int regFee = water.getRegistrationFee()==null?0:water.getRegistrationFee();
		int serviceFee = water.getServiceFee()==null?0:water.getServiceFee();
		int otherFee = water.getOtherFee()==null?0:water.getOtherFee();
		
		if(StringUtil.isBlank(resp.getPayFee())) {
			resp.setPayFee(clinicFee+regFee+serviceFee+otherFee);
		}
		if(StringUtil.isBlank(resp.getRegFee())) {
			resp.setRegFee(regFee);
		}
		if(StringUtil.isBlank(resp.getTimeSlice())) {
			resp.setTimeSlice(water.getTimeId());
		}
		if(StringUtil.isBlank(resp.getTreatFee())) {
			resp.setTreatFee(clinicFee);
		}
		if(StringUtil.isBlank(resp.getSex())) {
			resp.setSex(water.getSex());
		}
		if(StringUtil.isBlank(resp.getSqNo())) {
			resp.setSqNo(water.getQueueNo());
		}
		if(StringUtil.isBlank(resp.getTimeSlice())) {
			resp.setTimeSlice(water.getTimeId());
		}
		if(StringUtil.isBlank(resp.getCommendTime())) {
			resp.setCommendTime(water.getCommendTime());
		}
		if(StringUtil.isBlank(resp.getTakeNoPalce())) {
			resp.setTakeNoPalce(water.getDrawPoint());
		}
		if(StringUtil.isBlank(resp.getHisOrderId())) {
			resp.setTakeNoPalce(water.getHisOrderId());
		}
	}
}
