/**
 * 
 */
package com.kasite.client.yy.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.coreframework.util.Escape;
import com.kasite.client.yy.bean.dbo.YyCancel;
import com.kasite.client.yy.bean.dbo.YyHistoryDoctor;
import com.kasite.client.yy.bean.dbo.YyLimit;
import com.kasite.client.yy.bean.dbo.YyLock;
import com.kasite.client.yy.bean.dbo.YyRule;
import com.kasite.client.yy.bean.dbo.YyUnLock;
import com.kasite.client.yy.bean.dbo.YyWater;
import com.kasite.client.yy.constant.Constant;
import com.kasite.client.yy.dao.IYyCancelMapper;
import com.kasite.client.yy.dao.IYyLimitMapper;
import com.kasite.client.yy.dao.IYyLockMapper;
import com.kasite.client.yy.dao.IYyUnlockMapper;
import com.kasite.client.yy.dao.IYyWaterMapper;
import com.kasite.client.yy.util.YuYueUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.constant.RetCode.YY;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.sys.service.RedisUtil;
import com.kasite.core.common.util.AmountUtils;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.FormatUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.cache.IDoctorLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.CallHisCacheMap;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.his.handler.ILockAndUnLockService;
import com.kasite.core.serviceinterface.module.his.handler.IPayOfferNumberService;
import com.kasite.core.serviceinterface.module.his.handler.IPushRegWaterToJkzlService;
import com.kasite.core.serviceinterface.module.his.handler.ISearchClinicDeptDoctorService;
import com.kasite.core.serviceinterface.module.his.handler.ISyncDoctorService;
import com.kasite.core.serviceinterface.module.his.handler.IYjyyService;
import com.kasite.core.serviceinterface.module.his.req.ReqHisLock;
import com.kasite.core.serviceinterface.module.his.req.ReqHisUnLock;
import com.kasite.core.serviceinterface.module.his.resp.HisBookService;
import com.kasite.core.serviceinterface.module.his.resp.HisGetAppointReceiptInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisLockOrder;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryExamItemList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryNumbers;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryRegInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryScheduleDate;
import com.kasite.core.serviceinterface.module.his.resp.HisQuerySignalSourceList;
import com.kasite.core.serviceinterface.module.his.resp.HisRegFlag;
import com.kasite.core.serviceinterface.module.his.resp.HisSchIsHalt;
import com.kasite.core.serviceinterface.module.his.resp.HisSchTimeId;
import com.kasite.core.serviceinterface.module.his.resp.HisSearchClinicDeptAndDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.kasite.core.serviceinterface.module.his.resp.HisUnlock;
import com.kasite.core.serviceinterface.module.his.resp.HisYYCancel;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqGetOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderListLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqSyncLocalOrderState;
import com.kasite.core.serviceinterface.module.order.resp.RespGetOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.yy.IYYRuleService;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqAddOrderAndBookService;
import com.kasite.core.serviceinterface.module.yy.req.ReqBookService;
import com.kasite.core.serviceinterface.module.yy.req.ReqCancelAppoint;
import com.kasite.core.serviceinterface.module.yy.req.ReqGetAppointReceiptInfo;
import com.kasite.core.serviceinterface.module.yy.req.ReqLockOrder;
import com.kasite.core.serviceinterface.module.yy.req.ReqMedicalAppoint;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDept;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryExamItemList;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryHistoryDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryNumbers;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryScheduleDate;
import com.kasite.core.serviceinterface.module.yy.req.ReqQuerySignalSourceList;
import com.kasite.core.serviceinterface.module.yy.req.ReqSearchClinicDeptAndDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqStop;
import com.kasite.core.serviceinterface.module.yy.req.ReqStopSchedule;
import com.kasite.core.serviceinterface.module.yy.req.ReqUnlock;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateYyWater;
import com.kasite.core.serviceinterface.module.yy.req.ReqYYCancel;
import com.kasite.core.serviceinterface.module.yy.req.ReqYyLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYRule;
import com.kasite.core.serviceinterface.module.yy.resp.RespSearchClinicDeptAndDoctor;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
/**
 * @author lsq version 1.0 2017-6-20下午2:59:57
 */
@Service("yy.yygh")
public class IYYServiceImpl extends AbstractYYService implements IYYService {
	/** 出诊1 */
	final static int VISITING = 1;
	/** 停诊2 */
	final static int CLOSURE = 2;
	/** 替诊3 */
	final static int MEDICAL_EXAMINATION = 3;
	/** 申请6 */
	final static int APPLICATION = 6;
	/** 预约取消2 */
	final static int YY_CANCEL = 2;

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_YY);

	@Autowired
	private IMsgService msgService;
	
	@Autowired
	private IReportFormsService reportFormsService;
	
	@Autowired
	private YuYueUtil yuyueUtil;
	
	@Autowired
	IBasicService basicService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IPayService payService;
	@Autowired
	IDoctorLocalCache doctorLocalCache;
	@Autowired
	IYyWaterMapper waterMapper;
	@Autowired
	IYyLimitMapper limitMapper;
	@Autowired
	IYyLockMapper lockMapper;
	@Autowired
	IYyCancelMapper cancelMapper;
	@Autowired
	IYyUnlockMapper unlockMapper;
	@Autowired
	IYYRuleService yyRuleService;
	
	/**
	 * 查询号源
	 * 
	 * @param msg
	 * @return his查询出的数据
	 * @throws AbsHosException 
	 */
	@Override
	public String QueryNumbers(InterfaceMessage msg) throws Exception {
		return this.queryNumbers(new CommonReq<ReqQueryNumbers>(new ReqQueryNumbers(msg))).toResult();
	}

	/**
	 * 查询门诊科室
	 * 
	 * @param msg
	 * @return his查询出的数据
	 */
	@Override
	public String QueryClinicDept(InterfaceMessage msg) throws Exception {
		return this.queryClinicDept(new CommonReq<ReqQueryClinicDept>(new ReqQueryClinicDept(msg))).toResult();
	}

	/**
	 * 查询门诊医生
	 * 
	 * @param msg
	 * @return his查询出的数据
	 */
	@Override
	public String QueryClinicDoctor(InterfaceMessage msg) throws Exception {
		return this.queryClinicDoctor(new CommonReq<ReqQueryClinicDoctor>(new ReqQueryClinicDoctor(msg))).toResult();
	}

	/**
	 * 查询门诊排班
	 * 
	 * @param msg
	 * @return his查询出的数据
	 */
	@Override
	public String QueryClinicSchedule(InterfaceMessage msg) throws Exception {
		return this.queryClinicSchedule(new CommonReq<ReqQueryClinicSchedule>(new ReqQueryClinicSchedule(msg))).toResult();
	}

	/**
	 * 查询历史医生 查询本地数据库，返回doctorCode,以doctorCode入参调用his接口查询医生
	 * 
	 * @param msg
	 * @return 返回his查询到的医生数据
	 */
	@Override
	public String QueryHistoryDoctor(InterfaceMessage msg) throws Exception {
		return this.queryHistoryDoctor(new CommonReq<ReqQueryHistoryDoctor>(new ReqQueryHistoryDoctor(msg))).toResult();
	}

	/**
	 * 锁号
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String LockOrder(InterfaceMessage msg) throws Exception {
		return this.lockOrder(new CommonReq<ReqLockOrder>(new ReqLockOrder(msg))).toResult();
	}

	/**
	 * 释号
	 * 
	 * @param msg
	 * @return 返回释号成功或失败
	 */
	@Override
	public String Unlock(InterfaceMessage msg) throws Exception {
		return this.unlock(new CommonReq<ReqUnlock>(new ReqUnlock(msg))).toResult();
	}



//	/**
//	 * 取消挂号
//	 * @Description: 
//	 * @param msg
//	 * @return
//	 * @throws ParamException 
//	 */
//	@Override
//	public CommonResp<IRespElement> cancelRegister(ReqYYCancel reqVo,String channelId,boolean sendMsg) throws Exception {
//		try {
//			YyWater water = waterMapper.selectByPrimaryKey(reqVo.getOrderId());
//			if(water == null) {
//				//没有查询到预约记录，返回异常，暂不支持线下预约，线上取消
//				return new CommonResp<IRespElement>(Constant.DEFAULTTRAN, YY.ERROR_NOTWATER, "取消失败没有找到预约信息，请确认该预约信息是线上预约。");
//			}
//			if (water.getState() == YY_CANCEL) {
//				return new CommonResp<IRespElement>(Constant.DEFAULTTRAN, YY.ERROR_CANCELCHECK, "该预约已取消，请勿重复取消。");
//			}
//			
//			// 退号限制验证
//			yuyueUtil.checkRule(water, 2);
//			
//			// 调用his接口取消预约
//			Map<String, String> map = new HashMap<String, String>(16);
//			map.put("operatorId", reqVo.getOperatorId());
//			map.put("operatorName", reqVo.getOperatorName());
//			map.put("orderId", reqVo.getOrderId());
//			map.put("reason", reqVo.getReason());
//			map.put("channelId", channelId);
//			HisYYCancel cancelServiceResp = getCallHisService(req.getAuthInfo()).yyCancel(map);
//			if (StringUtil.isEmpty(cancelServiceResp)) {
//				return new CommonResp<IRespElement>(Constant.DEFAULTTRAN, YY.ERROR_CALLHIS, "失败：返回值为空.");
//			}
//			if (!Constant.SUCCESSCODE.equals(cancelServiceResp.getRespCode())) {
//				return new CommonResp<IRespElement>(Constant.DEFAULTTRAN, YY.ERROR_CALLHIS, cancelServiceResp.getRespMessage());
//			}
//			// 更新预约流水信息、号源信息、剩余号源数
//			YyCancel cancel = new YyCancel();
//			cancel.setYyOrderId(reqVo.getOrderId());
//			cancel.setCancelReason(reqVo.getReason());
//			cancel.setOperatorId(reqVo.getOperatorId());
//			cancel.setOperatorName(reqVo.getOperatorName());
//			cancel.setOperator(channelId);
//
//			YyWater newWater = new YyWater();
//			newWater.setState(Integer.valueOf(Constant.BOOK_2));
//			newWater.setOperatorId(reqVo.getOperatorId());
//			newWater.setOperatorName(reqVo.getOperatorName());
//			
//			cancelMapper.insert(cancel);
//			example = new Example(YyWater.class);
//			example.createCriteria().andEqualTo("yyOrderId", reqVo.getOrderId());
//			waterMapper.updateByExampleSelective(newWater, example);
//			
//			// 挂号笔数-1
//			reportFormsService.dataCloudCollection(reqVo.getMsg(), channelId, 105, -1, "1");
//
//			if(sendMsg) {
//				try {
//					InterfaceMessage msgParam = new InterfaceMessage();
//					msgParam.setParam(yuyueUtil.getYuYueMsgParam(channelId, "10101112", water));
//					msgParam.setParamType(KstHosConstant.INTYPE);
//					msgParam.setOutType(KstHosConstant.OUTTYPE);
//					String respStr = msgService.SendMsg(msgParam);
//					CommonResp<IRespElement> commResp = new CommonResp<IRespElement>(respStr);
//					if (!Constant.SUCCESSCODE.equals(commResp.getRespCode())) {
//						LogUtil.error(log, "发送预约消息异常：Param="+msgParam.getParam()+"|||Result="+respStr);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					LogUtil.error(log, e);
//				}
//			}
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,RetCode.Common.RET_10000);
//		}catch (BusinessException e) {
//			LogUtil.error(log, StringUtil.getExceptionStack(e));
//			e.printStackTrace();
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,YY.ERROR_CANCELCHECK,e.getLocalizedMessage());
//		}catch (AbsHosException e) {
//			LogUtil.error(log, StringUtil.getExceptionStack(e));
//			e.printStackTrace();
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,RetCode.Common.ERROR_PARAM,e.getMessage());
//		}catch (ParseException e) {
//			LogUtil.error(log, StringUtil.getExceptionStack(e));
//			e.printStackTrace();
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,YY.ERROR_PARSEDATE,e.getLocalizedMessage());
//		}catch (SQLException e) {
//			e.printStackTrace();
//			LogUtil.error(log, StringUtil.getExceptionStack(e));
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,RetCode.Common.ERROR_SQLEXECERROR,e.getLocalizedMessage());
//		}catch (Exception e) {
//			e.printStackTrace();
//			return new CommonResp<IRespElement>(Constant.DEFAULTTRAN,RetCode.Common.ERROR_UNKNOWN,e.getLocalizedMessage());
//		}
//	}
	
	/**
	 * 预约信息查询
	 * 
	 * @param msg
	 * @return 返回预约记录数据
	 */
	@Override
	public String QueryRegInfo(InterfaceMessage msg) throws Exception {
		return this.queryRegInfo(new CommonReq<ReqQueryRegInfo>(new ReqQueryRegInfo(msg))).toResult();
	}

	/**
	 * 查询预约规则
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:37:12
	 */
	@Override
	public String QueryYYRule(InterfaceMessage msg) throws Exception {
		return yyRuleService.queryYYRule(new CommonReq<ReqString>(new ReqString(msg))).toResult();
	}

	
	@Override
	public CommonResp<RespMap> queryClinicDept(CommonReq<ReqQueryClinicDept> commReq) throws Exception {
		
		ReqQueryClinicDept req = commReq.getParam();
		String channelId = req.getClientId();
		// 入参xml转map格式
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisQueryClinicDept.deptCode.getName(), req.getDeptCode());
		map.put(ApiKey.HisQueryClinicDept.deptName.getName(), req.getDeptName());
		map.put(ApiKey.HisQueryClinicDept.hosId.getName(), req.getHosId());
		map.put(ApiKey.HisQueryClinicDept.reqType.getName(), req.getReqType() + "");
		map.put(ApiKey.HisQueryClinicDept.workDateEnd.getName(), req.getWorkDateEnd());
		map.put(ApiKey.HisQueryClinicDept.workDateStart.getName(), req.getWorkDateStart());
		map.put(ApiKey.HisQueryClinicDept.workTime.getName(), req.getWorkTime() + "");
		map.put(ApiKey.HisQueryClinicDept.channelId.getName(), channelId);
					
		List<HisQueryClinicDept> deptsList = getCallHisService(req.getAuthInfo())
				.queryClinicDept(req.getMsg(), map)
				.getListCaseRetCode();
		if(deptsList==null) {
			return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisQueryClinicDept dept : deptsList) {
			ValidatorUtils.validateEntity(dept,AddGroup.class);
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.QueryClinicDept.DeptCode, dept.getDeptCode());
			respMap.put(ApiKey.QueryClinicDept.DeptName, dept.getDeptName());
			respMap.put(ApiKey.QueryClinicDept.ParentID, dept.getParentID());
			respMap.put(ApiKey.QueryClinicDept.Intro, dept.getIntro());
			respMap.put(ApiKey.QueryClinicDept.Remark, dept.getRemark());
			respMap.put(ApiKey.QueryClinicDept.Address, dept.getAddress());
			respMap.put(ApiKey.QueryClinicDept.DeptDoctors, dept.getDeptDoctors()!=null?dept.getDeptDoctors().toString():"");
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespQueryClinicDoctor> queryClinicDoctor(CommonReq<ReqQueryClinicDoctor> commReq) throws Exception {
		ReqQueryClinicDoctor req = commReq.getParam();
		ICallHisService callHisService = getCallHisService(req.getAuthInfo());
		ISyncDoctorService service = getCallHisService(req.getAuthInfo(), ISyncDoctorService.class);
		if(service!=null && req.getSyncDoctor()!=null && req.getSyncDoctor()==1) {
			return service.queryAndSyncClinicDoctor(commReq);
		}
		String doctorTitleCode = req.getDoctorTitleCode();
		String doctorTitle = req.getDoctorTitle();
		String channelId = req.getClientId();
		Map<String, String> map = new HashMap<String, String>(16);
		// 入参xml转map格式
		map.put(ApiKey.HisQueryClinicDoctor.hosId.getName(), req.getHosId());
		map.put(ApiKey.HisQueryClinicDoctor.deptCode.getName(), req.getDeptCode());
		map.put(ApiKey.HisQueryClinicDoctor.deptName.getName(), req.getDeptName());
		map.put(ApiKey.HisQueryClinicDoctor.doctorCode.getName(), req.getDoctorCode());
		map.put(ApiKey.HisQueryClinicDoctor.doctorCodes.getName(), req.getDoctorCodes());
		map.put(ApiKey.HisQueryClinicDoctor.doctorName.getName(), req.getDoctorName());
		map.put(ApiKey.HisQueryClinicDoctor.doctorTitleCode.getName(), doctorTitleCode);
		map.put(ApiKey.HisQueryClinicDoctor.scheduleId.getName(), req.getScheduleId());
		map.put(ApiKey.HisQueryClinicDoctor.workDateEnd.getName(), req.getWorkDateEnd());
		map.put(ApiKey.HisQueryClinicDoctor.workDateStart.getName(), req.getWorkDateStart());
		map.put(ApiKey.HisQueryClinicDoctor.channelId.getName(), channelId);
		map.put(ApiKey.HisQueryClinicDoctor.deptCodes.getName(), req.getDeptCodes());
		map.put(ApiKey.HisQueryClinicDoctor.isQueryHistory.getName(), req.getIsQueryHistory()+"");
		map.put("hisType", req.getHisType()+"");
		
		List<RespQueryClinicDoctor> respList = new ArrayList<RespQueryClinicDoctor>();
		
		String doctorCode = req.getDoctorCode();
		String deptCode = req.getDeptCode();
		//如果是查询科室代码和医生工号查询条件查询 在本地缓存中已经有这个医生的情况就直接查询本地的医生，不再调用HIS接口查询
		if(StringUtil.isNotBlank(deptCode) && StringUtil.isNotBlank(doctorCode)) {
			Doctor doc = doctorLocalCache.get(deptCode,doctorCode);
			RespQueryClinicDoctor resp = new RespQueryClinicDoctor();
			resp.setDeptCode(doc.getDeptCode());
			BeanCopyUtils.copyProperties(doc, resp, null);
			resp.setSex(doc.getDoctorSex());
			resp.setLevel(doc.getLevelName());
			resp.setLevelId(doc.getLevelId());
			resp.setUrl(doc.getPhotoUrl());
			resp.setDoctorTitle(doc.getTitle());
			resp.setDoctorTitleCode(doc.getTitleCode());
			resp.setIntro(doc.getIntroduction());
			respList.add(resp);
		//如果是根据医生职称查询  直接查询本地的医生，不再调用HIS接口查询
		}else if(StringUtil.isNotBlank(deptCode) && StringUtil.isNotBlank(doctorTitleCode) || StringUtil.isNotBlank(doctorTitle)) {
			if(StringUtil.isNotBlank(doctorTitle) && doctorTitle.indexOf("全部") != -1) {
				doctorTitle = "";
			}
			if(StringUtil.isNotBlank(doctorTitleCode) && doctorTitleCode.equals(doctorTitle)) {
				doctorTitleCode = "";
			}
			List<RespQueryBaseDoctorLocal> list = basicService.queryBaseDoctorLocal(new CommonReq<ReqQueryBaseDoctorLocal>(new ReqQueryBaseDoctorLocal(commReq.getMsg(), req.getHosId(), deptCode, null, null, null, null, null, doctorTitleCode, doctorTitle, null))).getData();
			if(list != null && list.size() > 0) {
				for (RespQueryBaseDoctorLocal respQueryBaseDoctorLocal : list) {
					RespQueryClinicDoctor resp = new RespQueryClinicDoctor();
					resp.setDeptCode(respQueryBaseDoctorLocal.getDeptCode());
					resp.setDeptName(respQueryBaseDoctorLocal.getDeptName());
					resp.setDoctorCode(respQueryBaseDoctorLocal.getDoctorCode());
					resp.setDoctorName(respQueryBaseDoctorLocal.getDoctorName());
					resp.setDoctorTitle(respQueryBaseDoctorLocal.getDoctorTitle());
					resp.setDoctorTitleCode(respQueryBaseDoctorLocal.getDoctorTitleCode());
					resp.setIntro(respQueryBaseDoctorLocal.getIntro());
					resp.setLevel(respQueryBaseDoctorLocal.getLevel());
					resp.setLevelId(respQueryBaseDoctorLocal.getLevelId());
					resp.setRemark(respQueryBaseDoctorLocal.getRemark());
					resp.setSex(respQueryBaseDoctorLocal.getSex());
					resp.setSpec(respQueryBaseDoctorLocal.getSpec());
					resp.setUrl(respQueryBaseDoctorLocal.getPhotoUrl());
					respList.add(resp);
				}
			}
		}else {
			List<HisQueryClinicDoctor> doctors = callHisService
					.queryClinicDoctor(req.getMsg(),map)
					.getListCaseRetCode();
			if(doctors==null) {
				return new CommonResp<RespQueryClinicDoctor>(commReq, Constant.QUERYCLINICDOCTORCODE, RetCode.YY.CallHosClientError);
			}
			for (HisQueryClinicDoctor dr : doctors) {
				ValidatorUtils.validateEntity(dr,AddGroup.class);
				RespQueryClinicDoctor resp = new RespQueryClinicDoctor();
				resp.setDeptCode(dr.getDeptCode());
				resp.setDeptName(dr.getDeptName());
				resp.setDoctorCode(dr.getDoctorCode());
				resp.setDoctorIsHalt(dr.getDoctorIsHalt());
				resp.setDoctorName(dr.getDoctorName());
				resp.setDoctorTitle(dr.getDoctorTitle());
				resp.setDoctorTitleCode(dr.getDoctorTitleCode());
				resp.setIntro(dr.getIntro());
				resp.setLevel(dr.getLevel());
				resp.setLevelId(dr.getLevelId());
				resp.setPrice(dr.getPrice());
				resp.setRemark(dr.getRemark());
				resp.setSex(dr.getSex());
				resp.setSpec(dr.getSpec());
				resp.setUrl(dr.getUrl());
				deptCode = dr.getDeptCode();
				doctorCode = dr.getDoctorCode();
				
				if(StringUtil.isBlank(deptCode) || StringUtil.isBlank(doctorCode)) {
					throw new RRException("门诊科室代码（deptCode）或 门诊医生代码（doctorCode）不能为空!");
				}
				Doctor doc = doctorLocalCache.get(deptCode,doctorCode);
				if(null == doc) {
					ReqQueryBaseDoctor t = new ReqQueryBaseDoctor(req.getMsg());
					t.setDoctorCode(doctorCode);
					t.setDeptCode(deptCode);
					CommonReq<ReqQueryBaseDoctor> reqCommBaseDoctor = new CommonReq<ReqQueryBaseDoctor>(t);
					CommonResp<RespQueryBaseDoctor>  queryDoctorResp = basicService.queryLocalDoctor(reqCommBaseDoctor);
					List<RespQueryBaseDoctor> data = queryDoctorResp.getListCaseRetCode();
					if( null == data || data.size() <= 0) {
						ReqAddDoctor reqAddDoctor = new ReqAddDoctor(req.getMsg(), null, dr.getDeptCode(), dr.getDeptName(),dr.getDoctorCode(),
								dr.getDoctorName(), dr.getSpec(), dr.getDoctorTitle(), dr.getDoctorTitleCode(), dr.getSex(), dr.getLevel(), dr.getLevelId(),
								dr.getUrl(), 1, dr.getRemark(), req.getHosId(), null, null, dr.getIntro(), null, null, null);
						CommonReq<ReqAddDoctor> reqComm = new CommonReq<ReqAddDoctor>(reqAddDoctor);
						basicService.addDoctor(reqComm);
					}
					doctorLocalCache.load(req.getHosId(),dr.getDeptCode(),dr.getDoctorCode());
					doc = doctorLocalCache.get(dr.getDeptCode(),dr.getDoctorCode());
				}
				// 查询本地医生头像、级别、职称、简介等信息
				if(doc!=null) {
					if (StringUtil.isBlank(resp.getDoctorTitle())) {
						resp.setDoctorTitle(doc.getTitle());
					}
					if (StringUtil.isBlank(resp.getIntro())) {
						resp.setIntro(doc.getIntroduction());
					}
					if (StringUtil.isBlank(resp.getLevel())) {
						resp.setLevel(doc.getLevelName());
					}
					if (StringUtil.isBlank(resp.getLevelId())) {
						resp.setLevelId(doc.getLevelId());
					}
					if (StringUtil.isBlank(resp.getDoctorTitleCode())) {
						resp.setDoctorTitleCode(doc.getTitleCode());
					}
					if (StringUtil.isBlank(resp.getSpec())) {
						resp.setSpec(doc.getSpec());
					}
					if (StringUtil.isBlank(resp.getUrl())) {
						resp.setUrl(doc.getPhotoUrl());
					}
					if (StringUtil.isBlank(resp.getDoctorDegree())) {
						resp.setDoctorDegree(doc.getDoctorDegree());
					}
				}
				respList.add(resp);
			}
			
			
			
			
		}
		
		
		return new CommonResp<RespQueryClinicDoctor>(commReq, Constant.QUERYCLINICDOCTORCODE, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> queryNumbers(CommonReq<ReqQueryNumbers> commReq) throws Exception {
		ReqQueryNumbers req = commReq.getParam();
		String channelId = req.getClientId();
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisQueryNumbers.hosId.getName(), req.getHosId());
		map.put(ApiKey.HisQueryNumbers.deptCode.getName(), req.getDeptCode());
		map.put(ApiKey.HisQueryNumbers.doctorCode.getName(), req.getDoctorCode());
		map.put(ApiKey.HisQueryNumbers.regDate.getName(), req.getRegDate());
		map.put(ApiKey.HisQueryNumbers.scheduleId.getName(), req.getScheduleId());
		map.put(ApiKey.HisQueryNumbers.timeSlice.getName(), req.getTimeSlice() + "");
		map.put(ApiKey.HisQueryNumbers.channelId.getName(), channelId);
		map.put(ApiKey.HisQueryNumbers.memberName.getName(), req.getMemberName());

		String arraySn = req.getScheduleId();
		HisQueryClinicSchedule sch = CallHisCacheMap.get().getArray(arraySn);
		if(null == sch) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.YY.CallHosClientError,"排班已失效，请重新查询排班后查询号源。");
		}
		List<HisQueryNumbers> nums = getCallHisService(req.getAuthInfo()).queryNumbers(req.getMsg(),sch,map).getListCaseRetCode();
		if(nums==null) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisQueryNumbers num : nums) {
			RespMap respMap = new RespMap();
			String numberSn = num.getSourceCode();
			if(StringUtil.isBlank(numberSn)) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.YY.CallHosClientError,"号源不存在，或者号源ID异常，请重新查询号源。");
			}
			CallHisCacheMap.get().setNumber(numberSn, num);
			respMap.put(ApiKey.QueryNumbers.SourceCode, numberSn);
			respMap.put(ApiKey.QueryNumbers.StartTime, num.getStartTime());
			respMap.put(ApiKey.QueryNumbers.EndTime, num.getEndTime());
			respMap.put(ApiKey.QueryNumbers.SqNo, num.getSqNo());
			respMap.put(ApiKey.QueryNumbers.NumberInfo, num.getNumberInfo());
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespQueryClinicSchedule> queryClinicSchedule(CommonReq<ReqQueryClinicSchedule> commReq) throws Exception {
		ReqQueryClinicSchedule req = commReq.getParam();
		String channelId = req.getClientId();
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisQueryClinicSchedule.deptCode.getName(), req.getDeptCode());
		map.put(ApiKey.HisQueryClinicSchedule.doctorCode.getName(), req.getDoctorCode());
		map.put(ApiKey.HisQueryClinicSchedule.hosId.getName(), req.getHosId());
		map.put(ApiKey.HisQueryClinicSchedule.scheduleId.getName(), req.getScheduleId());
		if (StringUtil.isBlank(req.getWorkDateStart())) {
			req.setWorkDateStart(DateOper.getNow("yyyy-MM-dd"));
		}
		if (StringUtil.isBlank(req.getWorkDateEnd())) {
			
			CommonResp<RespQueryYYRule> resp = yyRuleService.queryYYRule(new CommonReq<ReqString>(new ReqString(commReq.getMsg(), req.getHosId())));
			if(!KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				return new CommonResp<RespQueryClinicSchedule>(commReq, resp.getRetCode(),resp.getMessage());
			}
			RespQueryYYRule rule = resp.getResultData();
			String lastDate = "";
			if (rule==null || rule.getStartDay() == null) {
				lastDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), 15);
			} else {
				lastDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), rule.getStartDay());
			}
			req.setWorkDateEnd(lastDate);
		}
		map.put(ApiKey.HisQueryClinicSchedule.workDateEnd.getName(), req.getWorkDateEnd());
		map.put(ApiKey.HisQueryClinicSchedule.workDateStart.getName(), req.getWorkDateStart());
		map.put(ApiKey.HisQueryClinicSchedule.channelId.getName(), channelId);
		List<HisQueryClinicSchedule> schedules = getCallHisService(req.getAuthInfo()).queryClinicSchedule(req.getMsg(),map).getListCaseRetCode();
		if(schedules==null) {
			return new CommonResp<RespQueryClinicSchedule>(commReq, Constant.QUERYCLINICSCHEDULECODE, RetCode.YY.CallHosClientError);
		}	
		List<RespQueryClinicSchedule> respList = new ArrayList<RespQueryClinicSchedule>();
		for (HisQueryClinicSchedule sch : schedules) {
			ValidatorUtils.validateEntity(sch,AddGroup.class);
			RespQueryClinicSchedule resp = new RespQueryClinicSchedule();
			resp.setDeptCode(sch.getDeptCode());
			resp.setDeptName(sch.getDeptName());
			resp.setDrawPoint(sch.getDrawPoint());
			Integer schIsHalt = sch.getIsHalt();
			HisSchIsHalt isHalt = HisSchIsHalt.valuesOf(schIsHalt);
			resp.setIsHalt(schIsHalt);
			resp.setIsHaltStr(isHalt.getName());
			//是否分时段预约
			Integer isTimeFlag = sch.getIsTimeFlag();
			HisSchIsHalt tmeFlag = HisSchIsHalt.valuesOf(schIsHalt);
			resp.setIsTimeFlag(isTimeFlag);
			resp.setIsTimeFlagStr(tmeFlag.getName());
			//时段
			Integer timeSlice = sch.getTimeSlice();
			HisSchTimeId timeid = HisSchTimeId.valuesOf(timeSlice);
			resp.setTimeSlice(timeSlice);
			resp.setTimeSliceStr(timeid.getName());
			
			resp.setLeaveCount(sch.getLeaveCount());
			resp.setOtherFee(sch.getOtherFee());
			resp.setRegDate(sch.getRegDate());
			resp.setRegFee(sch.getRegFee());
			resp.setRegType(sch.getRegType());
			resp.setRemark(sch.getRemark());
			resp.setTimeSlice(sch.getTimeSlice());
			resp.setTreatFee(sch.getTreatFee());
			resp.setWorkPlace(sch.getWorkPlace());
			resp.setDiyJson(sch.getDiyJson());
			//排班唯一ID 如果医院有排班唯一ID则使用医院排班唯一ID 如果没有则通过拼接的方式 医院+科室+医生+日期+时段
			String arraySn = sch.getScheduleId();
			if(StringUtil.isBlank(arraySn)) {
				return new CommonResp<RespQueryClinicSchedule>(commReq, Constant.QUERYCLINICSCHEDULECODE, RetCode.YY.CallHosClientError,"排班ID为空，请重新查询排班。");
			}
			//通过这个id 将对象缓存起来，后续通过页面进行操作挂号的时候都可以通过缓存再将其取出。缓存中默认保存1分钟，如果1分钟后没有页面执行查询则直接从内存中移除
			CallHisCacheMap.get().setArray(arraySn, sch);
			resp.setScheduleId(arraySn);
			respList.add(resp);
		}
		return new CommonResp<RespQueryClinicSchedule>(commReq, Constant.QUERYCLINICSCHEDULECODE, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespQueryClinicDoctor> queryHistoryDoctor(CommonReq<ReqQueryHistoryDoctor> commReq) throws Exception {
		ReqQueryHistoryDoctor req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>();
		map.put("hosId", req.getHosId());
		map.put("openId", req.getOpId());
		List<YyHistoryDoctor> hisDocList = waterMapper.queryHistoryDoctorForMap(map);
		if (null == hisDocList || hisDocList.size() <= 0) {
			return new CommonResp<RespQueryClinicDoctor>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		StringBuffer subff_deptCodes = new StringBuffer();
		StringBuffer subff = new StringBuffer();
		for (YyHistoryDoctor doc : hisDocList) {
			subff_deptCodes.append(doc.getDeptCode()).append(",");
			subff.append(doc.getDoctorCode()).append(",");
		}
		ReqQueryClinicDoctor docReq  = new ReqQueryClinicDoctor(req.getMsg(), 
				null, null, null, null, null, null, null, null, req.getHosId(),null);
		if(subff.length()>0) {
			docReq.setDoctorCodes(subff.substring(0, subff.length()-1));
			docReq.setDeptCodes(subff_deptCodes.substring(0, subff_deptCodes.length()-1));
		}
		return this.queryClinicDoctor(new CommonReq<ReqQueryClinicDoctor>(docReq));
	}

	@Override
	public CommonResp<RespMap> lockOrder(CommonReq<ReqLockOrder> commReq) throws Exception {
		ReqLockOrder req = commReq.getParam();
		String channelId = req.getClientId();
		String memberId = req.getMemberId();
		String openId = req.getOpenId();
		String cardNo ="";
		Integer cardType = null;
		String mobile = "";
		String clinicCard = "";
		Integer fee = 0;
		String feeInfo = null;
		Integer isOnlinePay = KasiteConfig.getIsOnlinePay(channelId, BusinessTypeEnum.ORDERTYPE_0);
		// 查询本地医生
		Doctor doc = doctorLocalCache.get(req.getDeptCode(),req.getDoctorCode());
		String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		String orderId = "YYGH"+uuid.substring("YYGH".length(), uuid.length());
//		String orderId = KasiteConfig.getRandomId(); 
		if(StringUtil.isBlank(memberId)) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,"未查到用户信息。memberId = "+memberId);
		}
		YyLock lock = new YyLock();
		
		ReqQueryMemberList t = new ReqQueryMemberList(commReq.getMsg(),memberId,req.getCardNo(),KstHosConstant.CARDTYPE_1, openId);
		CommonReq<ReqQueryMemberList> reqCommM = new CommonReq<ReqQueryMemberList>(t);
		t.setHosId(req.getHosId());
		CommonResp<RespQueryMemberList> mlist = basicService.queryMemberList(reqCommM);
		
		if(!mlist.getCode().equals(KstHosConstant.SUCCESSCODE )) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,mlist.getMessage());
		}
		RespQueryMemberList member = mlist.getResultData();
		if(null == member) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,"未查到用户信息。");
		}
		cardNo = member.getCardNo();
		if(StringUtil.isNotBlank(member.getCardType())) {
			cardType = Integer.parseInt(member.getCardType());
		}
		memberId = member.getMemberId();
		clinicCard = member.getCardNo();
		mobile = member.getMobile();
//			idcardno = member.getIdCardNo();
		lock.setMemberId(memberId);
		// 查询锁号记录
		List<YyLock> qLocks = lockMapper.queryYyLock(lock, 1);
		YyLock qLock = null;
		if(qLocks.size() > 0) {
			qLock = qLocks.get(0);
		}
		HisQueryClinicSchedule schedule = CallHisCacheMap.get().getArray(req.getScheduleId());
		if(null == schedule) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS,"排班信息已经过时，请重新查询排班后再进行挂号.");
		}
		//一般排班是一定要有的，但是号源不一定要有，如果是只预约排班的  就不用号所以不用对号做非空判断
		HisQueryNumbers number = CallHisCacheMap.get().getNumber(req.getSourceCode());
		// 重复锁号验证
		if (qLock != null) {
			//!!!
			//如果是需要线上支付的订单判断订单当前状态是否是已经支付才能继续执行业务，担心前端直接绕开业务调用订单支付完成或者调用挂号完成接口
			ReqOrderListLocal orderListReq = new ReqOrderListLocal(req.getMsg(), 
					null, null, null, orderId, null, null, null, null,
					null, null, null, null);
			CommonResp<RespOrderLocalList> orderListResp =  orderService.orderListLocal(new CommonReq<ReqOrderListLocal>(orderListReq));
			if(orderListResp !=null  && KstHosConstant.SUCCESSCODE.equals(orderListResp.getCode())) {
				if(orderListResp.getData().size() > 0) {
					//判断订单是否已支付，如果已支付，就允许继续挂其它号
					//0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
					Integer payState = orderListResp.getDataCaseRetCode().getPayState();
					if(null != payState && payState == 0) {
						RespMap respMap = new RespMap();
						respMap.put(ApiKey.LockOrder.RegisterDate, qLock.getRegisterDate());//就诊日期
						respMap.put(ApiKey.LockOrder.TimeId, qLock.getTimeId());//时段 0全天，1上午，2下午
						respMap.put(ApiKey.LockOrder.DoctorName, qLock.getDoctorName());
						respMap.put(ApiKey.LockOrder.DeptName, qLock.getDeptName());
						respMap.put(ApiKey.LockOrder.CommendTime, qLock.getCommendTime());//候诊时间
						respMap.put(ApiKey.LockOrder.IsOnlinePay, KasiteConfig.getIsOnlinePay(channelId, BusinessTypeEnum.ORDERTYPE_0));//是否在线支付
						respMap.put(ApiKey.LockOrder.OrderId, qLock.getOrderId());//锁号订单号
						respMap.put(ApiKey.LockOrder.SqNo, qLock.getSqNo()!=null?qLock.getSqNo().toString():"");//就诊序号
						return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.YY.ERROR_LOCKAGAIN, respMap);
					}
				}
			}
		}
		//费用相关
		//fee = regfee + treatfee + othrefee
		Integer regfee = schedule.getRegFee();//挂号费
		Integer treatfee = schedule.getTreatFee();//诊查费
		Integer othrefee = schedule.getOtherFee();//其他费用
		if(null == regfee) regfee = 0;
		if(null == treatfee) treatfee = 0;
		if(null == othrefee) othrefee = 0;
		fee = regfee + treatfee + othrefee;
		//费用描述
		StringBuffer sbf = new StringBuffer();
		if(regfee > 0 ) {
			sbf.append("挂号费：¥ ").append(AmountUtils.changeF2Y(regfee)).append(" 元；");
		}
		if(treatfee > 0 ) {
			sbf.append("诊查费：¥ ").append(AmountUtils.changeF2Y(regfee)).append(" 元；");
		}
		if(othrefee > 0 ) {
			sbf.append("其他费用：¥ ").append(AmountUtils.changeF2Y(regfee)).append(" 元。");
		}
		feeInfo = sbf.toString();
		
		// 锁号有效时间（单位：分钟）
		int timeOut = Constant.I10;
		// 入参xml转map格式
		Map<String, String> map = new HashMap<>();
		member.toMap(map);
		map.put(ApiKey.ReqHisLockOrder.deptCode.getName(), schedule.getDeptCode());
		map.put(ApiKey.ReqHisLockOrder.doctorCode.getName(), doc.getDoctorCode());
		map.put(ApiKey.ReqHisLockOrder.regDate.getName(), schedule.getRegDate());
		map.put(ApiKey.ReqHisLockOrder.timeSlice.getName(), schedule.getTimeSlice() + "");
		map.put(ApiKey.ReqHisLockOrder.sqNo.getName(), req.getSqNo()+"");
		map.put(ApiKey.ReqHisLockOrder.sourceCode.getName(), req.getSourceCode());
		map.put(ApiKey.ReqHisLockOrder.scheduleId.getName(), req.getScheduleId());
		map.put(ApiKey.ReqHisLockOrder.cardType.getName(), cardType + "");
		map.put(ApiKey.ReqHisLockOrder.cardNo.getName(), cardNo);
		map.put(ApiKey.ReqHisLockOrder.regType.getName(), req.getRegType());
		map.put(ApiKey.ReqHisLockOrder.mobile.getName(), mobile);
		map.put(ApiKey.ReqHisLockOrder.clinicCard.getName(), clinicCard);
		map.put(ApiKey.ReqHisLockOrder.channelId.getName(), channelId);
		map.put(ApiKey.ReqHisLockOrder.orderId.getName(), orderId);
		
		ILockAndUnLockService lockService = getLockAndUnLockService(req.getAuthInfo());
		String hisLockOrderId = null;
		String lockStoreInfo = null;
		if(null == lockService) {
			logger.debug("该机构未实现锁号逻辑，不执行his的锁号方法，直接本地空锁一个号。");
		}else {
			ReqHisLock t2 = new ReqHisLock(req.getMsg(), orderId, schedule, number, map);
			CommonReq<ReqHisLock> hisLockReq = new CommonReq<ReqHisLock>(t2);
//			lockService.lockOrder(hisLockReq);
			CommonResp<HisLockOrder> comlockOrderResp = lockService.lockOrder(hisLockReq);
			if (!Constant.SUCCESSCODE.equals(comlockOrderResp.getCode())) {
				return new CommonResp<RespMap>(commReq, Constant.LOCKORDERCODE, YY.ERROR_CALLHIS, comlockOrderResp.getMessage());
			}
			HisLockOrder lockOrderResp = comlockOrderResp.getResultData();
			if(lockOrderResp==null) {
				return new CommonResp<RespMap>(commReq, Constant.LOCKORDERCODE, YY.ERROR_CALLHIS,"调用his锁号返回的锁号结果集对象为空，请联系管理员确认。");
			}
			ValidatorUtils.validateEntity(lockOrderResp,AddGroup.class);
			hisLockOrderId = lockOrderResp.getHisOrderId();
			if(StringUtil.isBlank(hisLockOrderId)) {
				return new CommonResp<RespMap>(commReq, Constant.LOCKORDERCODE, YY.ERROR_CALLHIS,"调用his锁号逻辑失败，未返回锁号结果中的HIS锁号ID。");
			}
			//如果HIS有返回费用相关描述则使用HIS的返回的费用
			if(StringUtil.isNotBlank(lockOrderResp.getFee())){
				fee = lockOrderResp.getFee();
			}else {
				lockOrderResp.setFee(fee);
			}
			if(StringUtil.isNotBlank(lockOrderResp.getFeeInfo())){
				feeInfo = lockOrderResp.getFeeInfo();
			}else {
				lockOrderResp.setFeeInfo(feeInfo);
			}
			lockStoreInfo = lockOrderResp.getStore();
			if(StringUtil.isNotBlank(lockStoreInfo)) {
				CallHisCacheMap.get().setLock(orderId, lockOrderResp);
			}
			Integer onPay = lockOrderResp.getIsOnlinePay();
			if(null != onPay && (KstHosConstant.I1.equals(onPay) || KstHosConstant.I2.equals(onPay))) {
				isOnlinePay = onPay;
			}
		}
		// 保存锁号信息
		YyLock lo = new YyLock();
		lo.setOrderId(orderId);
		lo.setHislockOrderId(hisLockOrderId);
		lo.setCardTypeCode(cardType);
		lo.setCardNo(cardNo);
		lo.setSourceCode(req.getSourceCode());
		lo.setScheduleId(req.getScheduleId());
		lo.setDoctorCode(doc.getDoctorCode());
		lo.setDoctorName(doc.getDoctorName());
		if(StringUtil.isNotBlank(req.getRegDate()) && req.getRegDate().length()==8) {
			try {
				lo.setRegisterDate(DateOper.formatDate(req.getRegDate(), "yyyyMMdd", "yyyy-MM-dd"));
			}catch (Exception e) {
				lo.setRegisterDate(req.getRegDate());
			}
		}else {
			lo.setRegisterDate(req.getRegDate());
		}
		lo.setTimeId(req.getTimeSlice());
		lo.setSqNo(req.getSqNo()!=null?Integer.valueOf(req.getSqNo()):null);
		lo.setCommendTime(req.getCommendTime());
		lo.setDeptCode(schedule.getDeptCode());
		lo.setDeptName(schedule.getDeptName());
		lo.setMemberId(memberId);
		lo.setOpenId(openId);
		
		JSONObject storeJson = new JSONObject(); 
		if(number!=null && StringUtil.isNotBlank(number.getStore())) {
			storeJson.put(ApiKey.Cache_His_Lock_Store.numberStore.getName(), number.getStore());
		}
		if(number!=null && StringUtil.isNotBlank(schedule.getStore())) {
			storeJson.put(ApiKey.Cache_His_Lock_Store.scheduleStore.getName(), schedule.getStore());
		}
		if(number!=null && StringUtil.isNotBlank(lockStoreInfo)) {
			storeJson.put(ApiKey.Cache_His_Lock_Store.lockStore.getName(), lockStoreInfo);
		}
		storeJson.put(ApiKey.Cache_His_Lock_Store.isHalt.getName(), schedule.getIsHalt());
		storeJson.put(ApiKey.Cache_His_Lock_Store.regFee.getName(), schedule.getRegFee());
		storeJson.put(ApiKey.Cache_His_Lock_Store.treatFee.getName(), schedule.getTreatFee());
		storeJson.put(ApiKey.Cache_His_Lock_Store.otherFee.getName(), schedule.getOtherFee());
		storeJson.put(ApiKey.Cache_His_Lock_Store.drawPoint.getName(), schedule.getDrawPoint());
		//注意这个字段一定需要  挂号的时候通过这个字段来进行扣费
		storeJson.put(ApiKey.Cache_His_Lock_Store.fee.getName(), fee);
		lo.setStore(storeJson.toJSONString());
		lo.setHosId(req.getHosId());
		try {
			String nowDate = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
			lo.setCreateTime(DateOper.parse2Timestamp(nowDate));
			String invalidDate = DateOper.addMinute(nowDate, timeOut, "yyyy-MM-dd HH:mm:ss");
			lo.setInvalidDate(DateOper.parse2Timestamp(invalidDate));
			lo.setUpdateTime(DateOper.getNowDateTime());
		} catch (ParseException e) {
			LogUtil.error(log, req.getAuthInfo(),e);
			e.printStackTrace();
			return new CommonResp<RespMap>(commReq, Constant.LOCKORDERCODE, YY.ERROR_PARSEDATE);
		}
		lo.setOperatorId(commReq.getParam().getOpenId());
		lo.setOperatorName(commReq.getParam().getOperatorName());
		//保存锁号记录
		lockMapper.insertSelective(lo);
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.LockOrder.IsOnlinePay, isOnlinePay);
		respMap.put(ApiKey.LockOrder.OrderId, lo.getOrderId());
		//费用
		respMap.put(ApiKey.LockOrder.Fee, fee);
		respMap.put(ApiKey.LockOrder.FeeInfo, feeInfo);
		respMap.put(ApiKey.LockOrder.SqNo, lo.getSqNo()!=null?lo.getSqNo().toString():"");
		return new CommonResp<RespMap>(commReq, Constant.LOCKORDERCODE, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespMap> unlock(CommonReq<ReqUnlock> commReq) throws Exception {
		ReqUnlock req = commReq.getParam();
		String channelId = req.getClientId();
		
		YyLock qLock = new YyLock();
		qLock.setOrderId(req.getOrderId());
		YyLock lock = lockMapper.getYyLock(qLock, 1);
		if (lock == null) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"解锁失败，没有找到有效的锁号信息.");
		}
		/*
		 * -----------HIS 解锁逻辑 start
		 */
		ILockAndUnLockService lockService = getLockAndUnLockService(req.getAuthInfo());
		if(null == lockService) {
			LogUtil.info(log,"该机构未实现"+ILockAndUnLockService.class+"逻辑，不执行his的解锁方法，直接本地解锁。");
		}else {
			// 入参xml转map格式
			Map<String, String> map = new HashMap<String, String>(16);
			map.put("orderId", req.getOrderId());
			map.put("hislockOrderId", lock.getHislockOrderId());
			map.put("channelId", channelId);
			map.put("store", lock.getStore());
			ReqHisUnLock t = new ReqHisUnLock(req.getMsg(), req.getOrderId(), map);
			CommonReq<ReqHisUnLock> unLockReq = new CommonReq<ReqHisUnLock>(t);
			CommonResp<HisUnlock> unLockResp = lockService.unLockOrder(unLockReq);
			if(unLockResp==null) {
				return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS,"失败:返回值为空.");
			}
			if (!Constant.SUCCESSCODE.equals(unLockResp.getCode())) {
				return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS,unLockResp.getMessage());
			}
		}
		/*
		 * -----------HIS 解锁逻辑 end 
		 */
		
		YyUnLock unLock = new YyUnLock();
		unLock.setOrderId(lock.getOrderId());
		// 保存解锁信息
		unlockMapper.insertSelective(unLock);
		//如果是有支付并且是已经生成订单的 取消订单 否则直接返回成功
		List<RespGetOrder> o = orderService.getOrder(new CommonReq<ReqGetOrder>(new ReqGetOrder(req.getMsg(), lock.getOrderId()))).getData();
		if(null != o && o.size() > 0) {
			return orderService.cancelOrder(new CommonReq<ReqCancelOrder>(new ReqCancelOrder(req.getMsg(), lock.getOrderId(), req.getOpenId(), req.getOperatorName())));
		}else {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}	
	}
		

	@Override
	public CommonResp<RespQueryRegInfo> queryRegInfo(CommonReq<ReqQueryRegInfo> commReq) throws Exception {
		ReqQueryRegInfo req = commReq.getParam();
		String channelId = req.getClientId();
		ReqOrderListLocal orderListReq = new ReqOrderListLocal(req.getMsg(), 
				null, null, null, req.getOrderId(), null, 
				null, null, null, null, null, null, null);
		CommonResp<RespOrderLocalList> orderListResp =  orderService.orderListLocal(new CommonReq<ReqOrderListLocal>(orderListReq));
		if(orderListResp==null || !KstHosConstant.SUCCESSCODE.equals(orderListResp.getCode())) {
			return new CommonResp<RespQueryRegInfo>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"查询订单异常:"+(orderListResp!=null?orderListResp.getMessage():""));
		}
		if(orderListResp.getData().size()<=0) {
			return new CommonResp<RespQueryRegInfo>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"没有找到有效的订单信息。");
		}
		//查询本地订单
		RespOrderLocalList orderLocal = orderListResp.getData().get(0);
		int bizState = orderLocal.getBizState();
		int payState = orderLocal.getPayState();
		int overState = orderLocal.getOverState();
		String orderNum = orderLocal.getOrderNum();
		String beginDate = orderLocal.getBeginDate();
		String cardNo = orderLocal.getCardNo();
		
		String hisOrderId = null;
		String bookServiceStore = null;
		String orderId = orderLocal.getOrderId();
		Integer payMoney = orderLocal.getPayMoney();
		Integer isOnlinePay = orderLocal.getIsOnlinePay();
		String deptCode = null;
		String deptName = null;
		String doctorCode = null;
		String doctorName = null;
		String scheduleId = null;
		Integer sqNo = null;
		Integer timeId = null;
		String regDate = null;
		
		String orderMemo = orderLocal.getOrderMemo();
		if(orderMemo!=null) {
			JSONObject js = JSONObject.parseObject(orderMemo);
			if(js!=null && !js.isEmpty()) {
				deptCode = js.getString("DeptCode");
				deptName = js.getString("DeptName");
				doctorName = js.getString("DoctorName");
				doctorCode = js.getString("DoctorCode");
			}
		}
		//查询预约记录
		Example example = new Example(YyWater.class);
		example.createCriteria().andEqualTo("orderId", req.getOrderId());
		YyWater water = waterMapper.selectOneByExample(example);
		
		if(water==null) {
			//查询锁号记录，当还在挂号中时，预约记录表没有数据，需从锁号表查询
			YyLock lock = lockMapper.selectByPrimaryKey(orderId);
			if (lock == null) {
				return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"预约失败，没有找到有效的锁号信息.");
			}
			//如果不是在线缴费模式并订单为挂号的 表示订单异常
			if(null == water && KstHosConstant.OFFLINEPAY.equals(isOnlinePay)) {
				long t = lock.getInvalidDate().getTime();
				long now = DateOper.getNowDate().getTime();
				//如果当前时间小于有效时间说明锁号信息已经失效
				if(now > t) {
					return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"预约失败，锁定的号源已经过期，请重新挂号.");
				}
			}
			scheduleId = lock.getScheduleId();
			if(StringUtil.isBlank(deptCode) && StringUtil.isNotBlank(lock.getDeptCode())) {
				deptCode = lock.getDeptCode();
			}
			if(StringUtil.isBlank(deptName) && StringUtil.isNotBlank(lock.getDeptName())) {
				deptName = lock.getDeptName();
			}
			if(StringUtil.isBlank(doctorCode) && StringUtil.isNotBlank(lock.getDoctorCode())) {
				doctorCode = lock.getDoctorCode();
			}
			if(StringUtil.isBlank(doctorName) && StringUtil.isNotBlank(lock.getDoctorName())) {
				doctorName = lock.getDoctorName();
			}
			hisOrderId = lock.getHislockOrderId();
			timeId = lock.getTimeId();
			regDate = lock.getRegisterDate();
			sqNo = lock.getSqNo();
			long t = lock.getInvalidDate().getTime();
			long now = DateOper.getNowDate().getTime();
			//如果当前时间小于有效时间说明锁号信息已经失效
			if(now > t) {
				if(bizState == KstHosConstant.ORDERBIZSTATE_0 && overState == KstHosConstant.ORDEROVER_0) {
					ReqSyncLocalOrderState rso = new ReqSyncLocalOrderState(req.getMsg(), req.getOrderId(),
							HisRegFlag.state_2.getState(), -1, BusinessTypeEnum.ORDERTYPE_0, req.getOpenId(), req.getOperatorName());
					rso.setOrderLocal(orderLocal);
					CommonReq<ReqSyncLocalOrderState> reqParam = new CommonReq<ReqSyncLocalOrderState>(rso);
					orderService.syncLocalOrderState(reqParam);
				}
				return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"预约失败，锁定的号源已经过期，请重新挂号.");
			}else {
				if(bizState == KstHosConstant.ORDERBIZSTATE_0 && payState == KstHosConstant.ORDERPAY_1 && overState == KstHosConstant.ORDEROVER_0 ) {
					return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"支付中，在等待挂号结果...");
				}
				if(bizState == KstHosConstant.ORDERBIZSTATE_0 && payState == KstHosConstant.ORDERPAY_2 && overState == KstHosConstant.ORDEROVER_0 ) {
					return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"支付已成功，在等待挂号结果...");
				}
			}
			
		}else {
			bookServiceStore = water.getStore();
			hisOrderId = water.getHisOrderId();
			if(StringUtil.isBlank(deptCode) && StringUtil.isNotBlank(water.getDeptCode())) {
				deptCode = water.getDeptCode();
			}
			if(StringUtil.isBlank(deptName) && StringUtil.isNotBlank(water.getDeptName())) {
				deptName = water.getDeptName();
			}
			if(StringUtil.isBlank(doctorCode) && StringUtil.isNotBlank(water.getDoctorCode())) {
				doctorCode = water.getDoctorCode();
			}
			if(StringUtil.isBlank(doctorName) && StringUtil.isNotBlank(water.getDoctorName())) {
				doctorName = water.getDoctorName();
			}
			if(StringUtil.isBlank(timeId) && StringUtil.isNotBlank(water.getTimeId())) {
				timeId = water.getTimeId();
			}
			if(StringUtil.isBlank(regDate) && StringUtil.isNotBlank(water.getRegisterDate())) {
				regDate = water.getRegisterDate();
			}
			scheduleId = water.getScheduleId();
			sqNo = water.getQueueNo();
		}
		String cardType = orderLocal.getCardType();
		Map<String, String> map = new HashMap<String, String>(16);
		String memberId = orderLocal.getMemberId();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, req.getOpenId(), map);
		
		if(StringUtil.isNotBlank(cardNo)) {
			map.put(ApiKey.HisQueryRegInfo.cardNo.getName(), cardNo);
		}
		map.put(ApiKey.HisQueryRegInfo.beginDate.getName(), beginDate);
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put(ApiKey.HisQueryRegInfo.cardNo.getName(), cardNo);
		}
//		map.put(ApiKey.HisQueryRegInfo.hosId.getName(), req.getHosId());
//		map.put(ApiKey.HisQueryRegInfo.clinicCard.getName(), cardNo);
		map.put(ApiKey.HisQueryRegInfo.hisOrderId.getName(), hisOrderId);
		map.put(ApiKey.HisQueryRegInfo.idCardNo.getName(), req.getIdCardNo());
		map.put(ApiKey.HisQueryRegInfo.orderId.getName(), orderId);
		if(StringUtil.isBlank(req.getTimeSlice())) {
			req.setTimeSlice(timeId+"");
		}
		map.put(ApiKey.HisQueryRegInfo.deptCode.getName(), deptCode);
		map.put(ApiKey.HisQueryRegInfo.doctorName.getName(), doctorName);
		map.put(ApiKey.HisQueryRegInfo.deptName.getName(), deptName);
		map.put(ApiKey.HisQueryRegInfo.regDate.getName(),regDate);
		map.put(ApiKey.HisQueryRegInfo.regFee.getName(), payMoney+"");
		map.put(ApiKey.HisQueryRegInfo.timeSlice.getName(), req.getTimeSlice());
		if (StringUtil.isNotEmpty(req.getCardType())) {
			map.put(ApiKey.HisQueryRegInfo.cardType.getName(), String.valueOf(req.getCardType()));
		} else {
			map.put(ApiKey.HisQueryRegInfo.cardType.getName(), "");
		}
		if (StringUtil.isNotEmpty(req.getRegFlag()) && req.getRegFlag() != 0) {
			map.put(ApiKey.HisQueryRegInfo.regFlag.getName(), String.valueOf(req.getRegFlag()));
		} else {
			map.put(ApiKey.HisQueryRegInfo.regFlag.getName(), "");
		}
		map.put(ApiKey.HisQueryRegInfo.startTime.getName(), req.getStartTime());
		map.put(ApiKey.HisQueryRegInfo.endTime.getName(), req.getEndTime());
		map.put(ApiKey.HisQueryRegInfo.channelId.getName(), channelId);
		map.put(ApiKey.HisQueryRegInfo.bookServiceStore.getName(), bookServiceStore);
		List<HisQueryRegInfo> regInfoResps = getCallHisService(req.getAuthInfo()).queryRegInfo(req.getMsg(),map)
				.getListCaseRetCode();
		if (regInfoResps == null) {
			return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, RetCode.YY.CallHosClientError);
		}
		if(regInfoResps.size() > 1) {
			return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, RetCode.YY.CallHosClientError,"调用HIS查询预约详情异常，HIS中存在多笔订单，请核实HIS查询结果。");
		}
		//查不到HIS订单信息的时候 前端页面安装订单状态为准，不用再封装一层状态。
		// 当订单号不为空，接口未查到数据时，查询本地预约记录  如果his那边没有订单，但是本地有订单，说明已经锁号 待支付订单
		if(StringUtil.isNotBlank(req.getOrderId()) && regInfoResps.size() <= 0) {
			//如果是在线缴费模式 HIS中不存在预约挂号订单，则直接返回需要支付的订单
			HisQueryRegInfo resp = new HisQueryRegInfo();
			resp.setTimeSlice(timeId);
			resp.setRegDate(regDate);
			yuyueUtil.yyOrderToResp(resp, orderLocal, deptCode, deptName, doctorCode, doctorName, 
					hisOrderId, cardNo, scheduleId, sqNo);
			regInfoResps.add(resp);
		}
		
		// 如果his没有返回取号说明，取号地点，从本地获取 本地缓存 30分钟 的取号地点状态
		if (regInfoResps != null && regInfoResps.size() > 0) {
			if ((StringUtil.isBlank(regInfoResps.get(0).getTakeNoPalce())
					&& StringUtil.isBlank(regInfoResps.get(0).getTakeNoDesc()))) {
				String key = "YY_Rule_"+orderLocal.getHosId();
				JSONObject r = RedisUtil.create().getValue(key);
				if(null == r) {
					r = new JSONObject();
					
					CommonResp<RespQueryYYRule> yyRuleResp = yyRuleService.queryYYRule(new CommonReq<ReqString>(new ReqString(req.getMsg(),orderLocal.getHosId())));
					if(KstHosConstant.SUCCESSCODE.equals(yyRuleResp.getCode())) {
						RespQueryYYRule yyRule = yyRuleResp.getResultData();
						if(yyRule!=null) {
							r.put("DrawPoint", yyRule.getDrawPoint());
							r.put("NumberDes", yyRule.getNumberDesc());
						}
					}
//					example = new Example(YyRule.class);
//					example.createCriteria().andEqualTo("state", 1).andEqualTo("hosId",orderLocal.getHosId());
//					YyRule rule = ruleMapper.selectOneByExample(example);
//					if (null != rule) {
//						r.put("DrawPoint", rule.getDrawPoint());
//						r.put("NumberDes", rule.getNumberDes());
//					}
					RedisUtil.create().setJsonObj(key, r);
				}
				regInfoResps.get(0).setTakeNoPalce(r.getString("DrawPoint"));
				regInfoResps.get(0).setTakeNoDesc(r.getString("NumberDes"));
			}
		}
		
		if (regInfoResps == null || regInfoResps.size() <= 0) {
			return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERNOTFIND);
		}
		List<RespQueryRegInfo> respList = new ArrayList<RespQueryRegInfo>();
		for (HisQueryRegInfo regInfo : regInfoResps) {
			RespQueryRegInfo resp = new RespQueryRegInfo();
			BeanCopyUtils.copyProperties(regInfo, resp, null);
			
			Integer timeSlice = regInfo.getTimeSlice();
			Integer flag = regInfo.getRegFlag();
			if(null != flag) {
				HisRegFlag f = HisRegFlag.valuesOf(flag);
				if(null != f) {
					//判断本地订单和HIS的订单 的BizState状态是否一致
					//如果HIS订单为已退号  本地BizState为 未取消 则 更新本地订单状态
					ReqSyncLocalOrderState t = new ReqSyncLocalOrderState(req.getMsg(), req.getOrderId(),
							flag, -1, BusinessTypeEnum.ORDERTYPE_0, req.getOpenId(), req.getOperatorName());
					t.setOrderLocal(orderLocal);
					CommonReq<ReqSyncLocalOrderState> reqParam = new CommonReq<ReqSyncLocalOrderState>(t);
					orderService.syncLocalOrderState(reqParam);
				}
			}else {
				//如果已经锁号  但是没有挂号  状态  bizState = 0 and payState = 0 并且存在 lock
				if(bizState == 2) {
					flag = 2;
				}else if(bizState == 0 && payState == 0) {
					flag = 0;
				}
				
			} 
				
			resp.setTimeSlice(timeSlice);
			resp.setOrderNum(orderNum);
			resp.setOrderId(orderId);
			resp.setPayState(payState);
			resp.setBizState(bizState);
			resp.setOverState(overState);
			resp.setPayFee(payMoney);
			
			resp.setRegFlag(flag);
			//HIS未返回属性默认获取本地的属性值
			if(water!=null ) {
				yuyueUtil.yyWaterToRespForProIsNull(resp, water);
			}
			
			ValidatorUtils.validateEntity(resp,AddGroup.class);
			
			
//			if(mm.getCertType()!=null && "01".equals(mm.getCertType()) && StringUtil.isNotBlank(mm.getCertNum())) {
//				resp.setCertNum(FormatUtils.idCardFormat(mm.getCertNum()));
//			}else {
//				resp.setCertNum(mm.getCertNum());
//			}
//			if(mm.getGuardianCertType()!=null && "01".equals(mm.getGuardianCertType()) && StringUtil.isNotBlank(mm.getGuardianCertNum())) {
//				resp.setGuardianCertNum(FormatUtils.idCardFormat(mm.getGuardianCertNum()));
//			}else {
//				resp.setGuardianCertNum(mm.getGuardianCertNum());
//			}
			//加密敏感信息
			resp.setIdCardNo(FormatUtils.idCardFormat(resp.getIdCardNo()));
			resp.setMobile(FormatUtils.mobileFormat(resp.getIdCardNo()));
			respList.add(resp);
		}
		return new CommonResp<RespQueryRegInfo>(commReq, Constant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	
	private CommonResp<RespMap> cancelRegister(YyWater water,RespOrderLocalList orderLocal,CommonReq<ReqYYCancel> commReq,String channelId,boolean isSendMsg) throws Exception {
		ReqYYCancel req = commReq.getParam();
		// 退号限制验证
		yuyueUtil.checkRule(commReq.getMsg(),water, 2);
//		//TODO 查询订单，判断是否在线支付 
//		CommonReq<ReqGetOrder> getOrderReq = new CommonReq<ReqGetOrder>(new ReqGetOrder(commReq.getMsg(),orderId));
//		CommonResp<RespGetOrder> getOrderResp = orderService.getOrder(getOrderReq);
//		RespGetOrder o = getOrderResp.getResultData();
//		//未找到订单抛出异常
//		if(!KstHosConstant.SUCCESSCODE.equals(getOrderResp.getCode()) || null == o) {
//			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERNOTFIND, "未找到对应到订单.OrderId="+orderId);	
//		}
//		
		//查询成员信息
		ReqQueryMemberList t = new ReqQueryMemberList(commReq.getMsg(), water.getMemberId(), null, null, orderLocal.getOpenId(), null, null, null, null,true);
		t.setHosId(water.getHosId());
		CommonReq<ReqQueryMemberList> reqCommM = new CommonReq<ReqQueryMemberList>(t);
		CommonResp<RespQueryMemberList> mlist = basicService.queryMemberList(reqCommM);
		if(!mlist.getCode().equals(KstHosConstant.SUCCESSCODE )) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,mlist.getMessage());
		}
		RespQueryMemberList member = mlist.getResultData();
		if(null == member) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,"未查到用户信息。");
		}
		
		//查询医院信息
		ReqQueryHospitalLocal reqQueryHos = new ReqQueryHospitalLocal(commReq.getMsg(),water.getHosId());
		CommonResp<RespQueryHospitalLocal> hosResp = basicService.queryHospitalLocal(new CommonReq<ReqQueryHospitalLocal>(reqQueryHos));
		if(hosResp==null || !KstHosConstant.SUCCESSCODE.equals(hosResp.getCode())) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLBASIC,"取消失败，没有找到医院信息.");
		}
		if(hosResp.getData()==null || hosResp.getData().size()<=0) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLBASIC,"取消失败，没有找到医院信息.");
		}
		
		
		//判断订单状态，如果订单状态在HIS那边已经取消了 则直接调用退费接口进行退费
		
		int bizState = orderLocal.getBizState();
		//订单已经取消
		if(bizState !=  KstHosConstant.ORDERBIZSTATE_2) {
			// 调用his接口取消预约
			Map<String, String> map = new HashMap<String, String>(16);
			map.put(ApiKey.HisYyCancel.hisOrderId.getName(), water.getHisOrderId());
			map.put(ApiKey.HisYyCancel.orderId.getName(), req.getOrderId());
			map.put(ApiKey.HisYyCancel.operatorId.getName(), req.getOperatorId());
			map.put(ApiKey.HisYyCancel.operatorName.getName(), req.getOperatorName());
			map.put(ApiKey.HisYyCancel.reason.getName(), req.getReason());
			map.put(ApiKey.HisYyCancel.channelId.getName(), channelId);
			
			map.put(ApiKey.HisYyCancel.patientName.getName(), water.getUserName());
			map.put(ApiKey.HisYyCancel.hisPatientId.getName(), member.getHisMemberId());
			map.put(ApiKey.HisYyCancel.mobile.getName(), water.getUserMobile());
			map.put(ApiKey.HisYyCancel.idCardNo.getName(), water.getIdCardNo());
			map.put(ApiKey.HisYyCancel.clinicCard.getName(), water.getClinicCard());
			
			map.put(ApiKey.HisYyCancel.hosId.getName(), water.getHosId());
			map.put(ApiKey.HisYyCancel.beginDate.getName(), orderLocal.getBeginDate());
			
			map.put(ApiKey.HisYyCancel.hospitalCode.getName(), hosResp.getResultData().getHospitalCode());
			map.put(ApiKey.HisYyCancel.deptCode.getName(), water.getDeptCode());
			map.put(ApiKey.HisYyCancel.deptName.getName(), water.getDeptName());
			map.put(ApiKey.HisYyCancel.doctorCode.getName(), water.getDoctorCode());
			map.put(ApiKey.HisYyCancel.doctorName.getName(), water.getDoctorName());
			map.put(ApiKey.HisYyCancel.scheduleId.getName(), water.getScheduleId());
			map.put(ApiKey.HisYyCancel.regDate.getName(), water.getRegisterDate());
			map.put(ApiKey.HisYyCancel.timeSlice.getName(), water.getTimeId()!=null?water.getTimeId().toString():null);
			map.put(ApiKey.HisYyCancel.timeName.getName(),FormatUtils.getTimeName(water.getTimeId()));
			map.put(ApiKey.HisYyCancel.sourceCode.getName(), water.getSourceCode());
			map.put(ApiKey.HisYyCancel.sqNo.getName(), water.getQueueNo()!=null?water.getQueueNo().toString():null);
			if(StringUtil.isNotBlank(water.getCommendTime())) {
				if(water.getCommendTime().contains("-")) {
					map.put(ApiKey.HisYyCancel.startTime.getName(), water.getCommendTime().split("-")[0]);
					map.put(ApiKey.HisYyCancel.endTime.getName(), water.getCommendTime().split("-")[1]);
				}else {
					map.put(ApiKey.HisYyCancel.startTime.getName(), water.getCommendTime());
				}
			}
			HisYYCancel cancelServiceResp = getCallHisService(req.getAuthInfo()).yyCancel(req.getMsg(),req.getOrderId(),map,water.getStore()).getDataCaseRetCode();
			if (StringUtil.isEmpty(cancelServiceResp)) {
				return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS, "失败：返回值为空.");
			}
			if (!Constant.SUCCESSCODE.equals(cancelServiceResp.getRespCode())) {
				return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS, cancelServiceResp.getRespMessage());
			}
			// 更新预约流水信息、号源信息、剩余号源数
			YyCancel cancel = new YyCancel();
			cancel.setOrderId(req.getOrderId());
			cancel.setCancelReason(req.getReason());
			cancel.setOperatorId(req.getOperatorId());
			cancel.setOperatorName(req.getOperatorName());
			cancel.setOperator(channelId);

			YyWater newWater = new YyWater();
			newWater.setState(Integer.valueOf(Constant.BOOK_2));
			newWater.setOperatorId(req.getOperatorId());
			newWater.setOperatorName(req.getOperatorName());

			cancelMapper.insert(cancel);
			Example example = new Example(YyWater.class);
			example.createCriteria().andEqualTo("orderId", req.getOrderId());
			waterMapper.updateByExampleSelective(newWater, example);
		}
		// 挂号笔数-1
		reportFormsService.dataCloudCollection(req.getMsg(), channelId, 105, -1, "1");
		//判断订单中的状态是否是线上支付
		Integer isOnLinePay = orderLocal.getIsOnlinePay();
		String refundOrderId = CommonUtil.getGUID();
		String refundNo = null;
		//1线上支付 2线下支付
		if(KstHosConstant.ONLINEPAY.equals(isOnLinePay) && orderLocal.getPayState()!=null && orderLocal.getPayState()==2) {
			//如果这个订单是线上支付的，并且订单状态为已支付完成 则需要调用退费接口，如果不是则直接退号后，完成订单
			// 取消订单
			ReqOrderIsCancel orderIsCancelReq = new ReqOrderIsCancel(req.getMsg(), orderLocal.getOrderId(), orderLocal.getPayMoney(), orderLocal.getPayMoney(), req.getOperatorId(), req.getOperatorName(), channelId, req.getReason());
			CommonResp<RespMap> orderIsCancelResp = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(orderIsCancelReq));
			if (orderIsCancelResp == null || !KstHosConstant.SUCCESSCODE.equals(orderIsCancelResp.getCode())) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANCELORDER, "取消挂号异常：" + (orderIsCancelResp != null ? orderIsCancelResp.getMessage() : ""));
			}
			RespMap respMap = orderIsCancelResp.getData().get(0);
			refundOrderId = respMap.getString(ApiKey.OrderIsCancel.RefundOrderId);
			refundNo = respMap.getString(ApiKey.OrderIsCancel.RefundNo);
		}
		// 退费完成，回调订单退费完成接口
		ReqCancelForCompletion reqCancelForCom = new ReqCancelForCompletion(req.getMsg(), orderLocal.getOrderId(), refundOrderId, 
				refundNo,req.getOperatorId(),req.getOperatorName(),orderLocal.getPayMoney(),orderLocal.getPayMoney());
		CommonResp<RespMap> cancelResp = orderService.cancelForCompletion(new CommonReq<ReqCancelForCompletion>(reqCancelForCom));
		if (cancelResp == null || !Constant.SUCCESSCODE.equals(cancelResp.getCode())) {
			LogUtil.info(log, "回调订单退费完成接口失败：Param=" + reqCancelForCom.getHisReqXml() + "|||Result=" + (cancelResp != null ? cancelResp.toResult() : ""),req.getAuthInfo());
		}
		
		//执行业务取消完成
		ReqBizForCancel reqBizForCancel = new ReqBizForCancel(req.getMsg(),orderLocal.getOrderId(),req.getOperatorId(),req.getOperatorName());
		CommonReq<ReqBizForCancel> bizCancelReq = new CommonReq<ReqBizForCancel>(reqBizForCancel);
		CommonResp<RespMap> bizCancelResp = orderService.bizForCancel(bizCancelReq);
		if (cancelResp == null || !Constant.SUCCESSCODE.equals(cancelResp.getCode())) {
			LogUtil.info(log, "回调订单取消业务订单接口失败：Param=" + reqCancelForCom.getHisReqXml() + "|||Result=" + (cancelResp != null ? cancelResp.toResult() : ""),req.getAuthInfo());
			return bizCancelResp;
		}
		try {
			Map<String, Object> pushMap = new HashMap<String, Object>();
			water.setPushState(-1);
			water.setNum(1);
			pushMap.put("yyWater", water);
			pushMap.put("jkzlHosId", req.getHosId());
			IPushRegWaterToJkzlService pushService = HandlerBuilder.get().getCallHisService(KasiteConfig.getAuthInfo(req.getMsg()), IPushRegWaterToJkzlService.class);
			if(pushService!=null) {
				pushService.pushRegWaterToJkzl(req.getMsg(), pushMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,req.getAuthInfo());
		}
		// 发送通知短信
		if(isSendMsg) {
			try {
				Element data1 = DocumentHelper.createElement(Constant.DATA_1);
				String modetype = "";
				if(StringUtil.isNotBlank(orderLocal.getServiceId()) && BusinessTypeEnum.ORDERTYPE_009.getCode().equals(orderLocal.getServiceId())) {
					modetype = KstHosConstant.MODETYPE_10101113;
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.UserName, water.getUserName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.CardNo, water.getClinicCard());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.DeptName, water.getDeptName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.DoctorName, water.getDoctorName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.RegisterDate, water.getRegisterDate());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.HospitalName, water.getHosName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.CommendTime, water.getCommendTime());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.SqNo, water.getQueueNo());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.TimeIdStr, KasiteConfig.getTimeIdStr(water.getTimeId()));
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101113.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.ORDERTYPE_009, 
							req.getClientId(),req.getConfigKey(),water.getOrderId()));
				}else {
					modetype = KstHosConstant.MODETYPE_10101112;
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.UserName, water.getUserName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.CardNo, water.getClinicCard());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.DeptName, water.getDeptName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.DoctorName, water.getDoctorName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.RegisterDate, water.getRegisterDate());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.HospitalName, water.getHosName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.CommendTime, water.getCommendTime());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.SqNo, water.getQueueNo());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.TimeIdStr, KasiteConfig.getTimeIdStr(water.getTimeId()));
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101112.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.ORDERTYPE_0, 
							req.getClientId(),req.getConfigKey(),water.getOrderId()));
				}
				//消息推送走消息中心
				ReqSendMsg queue = new ReqSendMsg(req.getMsg(), water.getcNo(), water.getcType(), channelId, "", water.getUserMobile(),
						"", modetype, data1.asXML(), orderLocal.getOperatorId(), 1, water.getOperatorId(), water.getOperatorName(), orderLocal.getOrderId(), "", water.getMemberId(), "");
				CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
				CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
				if (!Constant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
					LogUtil.info(log, "发送取消预约消息异常：YYOrderId="+orderLocal.getOrderId()+"|||Result="+addMsgQueue.getMessage(),req.getAuthInfo());
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(log, "发送取消预约消息异常："+e.getLocalizedMessage());
			}
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public String AddOrderAndBookService(InterfaceMessage msg) throws Exception {
		ReqAddOrderAndBookService req = new ReqAddOrderAndBookService(msg);
		CommonReq<ReqAddOrderAndBookService> commReq = new CommonReq<ReqAddOrderAndBookService>(req);
		Integer isOnlinePay = KasiteConfig.getIsOnlinePay(req.getClientId(), BusinessTypeEnum.ORDERTYPE_0);
		if(isOnlinePay == null || isOnlinePay!=2) {
			return new CommonResp<RespMap>(commReq, YY.ERROR_BOOK,"预约失败，该渠道暂不支持下单并挂号业务.").toResult();
		}
		YyLock lock = lockMapper.selectByPrimaryKey(req.getOrderId());
		if (lock == null) {
			return new CommonResp<RespMap>(commReq, YY.ERROR_NOTLOCK,"预约失败，没有找到有效的锁号信息.").toResult();
		} 
		CommonResp<RespQueryClinicSchedule> schResp = queryClinicSchedule(new CommonReq<ReqQueryClinicSchedule>(new ReqQueryClinicSchedule(msg, 
				lock.getHosId(), lock.getDeptCode(), lock.getDoctorCode(), lock.getScheduleId(), 
				lock.getRegisterDate(), lock.getRegisterDate())));
		if(!KstHosConstant.SUCCESSCODE.equals(schResp.getCode()) || schResp.getData()==null || schResp.getData().size()<=0 || schResp.getData().size()>1) {
			return new CommonResp<RespMap>(commReq, YY.ERROR_NOTSCHEDULE,"预约失败，没有找到排班信息.").toResult();
		}
		RespQueryClinicSchedule sch = schResp.getResultData();
		//3替诊，4可约，5:可挂
		if(sch.getIsHalt()!=3 && sch.getIsHalt()!=4 && sch.getIsHalt()!=5) {
			return new CommonResp<RespMap>(commReq, YY.ERROR_NOTSCHEDULE,"预约失败，当前排班不可预约.").toResult();
		}
		int totalFee = 0;
		if(sch.getRegFee()!=null) {
			totalFee += sch.getRegFee();
		}
		if(sch.getOtherFee()!=null) {
			totalFee += sch.getOtherFee();
		}	
		if(sch.getTreatFee()!=null) {
			totalFee += sch.getTreatFee();
		}	
		
		ReqAddOrderLocal addOrderReq = new ReqAddOrderLocal(msg,
				req.getOrderId(), null, totalFee, totalFee, "挂号支付", req.getCardNo(), req.getCardType(), req.getOperatorId(), 
				req.getOperatorName(), "0", isOnlinePay, 1, req.getMemberId(), null, null);
		if(StringUtil.isNotBlank(lock.getHosId())) {
			addOrderReq.setHosId(lock.getHosId());
		}
		CommonResp<RespMap> orderResp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderReq));
		if(!KstHosConstant.SUCCESSCODE.equals(orderResp.getCode())) {
			return new CommonResp<RespMap>(commReq, YY.ERROR_BOOK,"预约失败："+orderResp.getMessage()).toResult();
		}
		CommonResp<RespMap> resp = bookService(new CommonReq<ReqBookService>(new ReqBookService(msg, req.getOrderId(), req.getOperatorId(), req.getOperatorName())));
		return resp.toResult();
	}

	@Transactional
	@Override
	public String BookService(InterfaceMessage msg) throws Exception {
		return this.bookService(new CommonReq<ReqBookService>(new ReqBookService(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> bookService(CommonReq<ReqBookService> commReq) throws Exception {
		ReqBookService reqVo = commReq.getParam();
		String channelId = reqVo.getClientId();
		YyLock lock = lockMapper.selectByPrimaryKey(reqVo.getOrderId());
		if (lock == null) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_NOTLOCK,"预约失败，没有找到有效的锁号信息.");
		} 
		ReqQueryHospitalLocal reqQueryHos = new ReqQueryHospitalLocal(reqVo.getMsg(),lock.getHosId());
		CommonResp<RespQueryHospitalLocal> hosResp = basicService.queryHospitalLocal(new CommonReq<ReqQueryHospitalLocal>(reqQueryHos));
		if(hosResp==null || !KstHosConstant.SUCCESSCODE.equals(hosResp.getCode())) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLBASIC,"预约失败，没有找到医院信息.");
		}
		if(hosResp.getData()==null || hosResp.getData().size()<=0) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLBASIC,"预约失败，没有找到医院信息.");
		}
		RespQueryHospitalLocal hos = hosResp.getData().get(0);

		String orderId = lock.getOrderId();
		String numberSn = lock.getSourceCode();
		String scheduleId = lock.getScheduleId();
		String store = lock.getStore();
		String cardNo = lock.getCardNo();
		
		// 根据scheduleId查询排班信息
//		List<HisQueryClinicSchedule> scheduleResp = getCallHisService(req.getAuthInfo()).queryClinicSchedule(reqVo.getMsg(),map);
//		if (StringUtil.isEmpty(scheduleResp) || scheduleResp.size() <= 0) {
//			// 预约失败解锁
//			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS,"没有查询到可预约排班信息.");
//		}
		
		//!!!
		//如果是需要线上支付的订单判断订单当前状态是否是已经支付才能继续执行业务，担心前端直接绕开业务调用订单支付完成或者调用挂号完成接口
		ReqOrderListLocal orderListReq = new ReqOrderListLocal(reqVo.getMsg(), 
				null, null, null, orderId, null, null, null, null,
				null, null, null, null);
		CommonResp<RespOrderLocalList> orderListResp =  orderService.orderListLocal(new CommonReq<ReqOrderListLocal>(orderListReq));
		if(orderListResp==null || !KstHosConstant.SUCCESSCODE.equals(orderListResp.getCode())) {
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"查询订单异常:"+(orderListResp!=null?orderListResp.getMessage():""));
		}
		if(orderListResp.getData().size()<=0) {
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"没有找到有效的订单信息。");
		}
		RespOrderLocalList orderLocal = orderListResp.getData().get(0);
				
		String memberId = orderLocal.getMemberId();
		if(StringUtil.isBlank(memberId)) {
			memberId = lock.getMemberId();
		}
		String scheduleStore = null;
		String numberStore = null;
		String lockStore = null;
		Integer isHalt = null;
		Integer regFee = null;
		Integer treatFee = null;
		Integer otherFee = null;
		Integer fee = null;
		String drawPoint = null;
		
		//先从本地map缓存中获取，如果未获取到 或者过期无数据了就从 锁号对象的store进行解析
		HisQueryClinicSchedule schedule = CallHisCacheMap.get().getArray(scheduleId);
		HisQueryNumbers num = CallHisCacheMap.get().getNumber(numberSn);
		HisLockOrder hisLock = CallHisCacheMap.get().getLock(reqVo.getOrderId());
		//防止服务重启或者 用户锁号后很久都不支付，号源和锁号信息都从缓存中失效的情况
		if(null != schedule && null != num && null != hisLock) {
			scheduleStore = schedule.getStore();
			numberStore = num.getStore();
			lockStore = hisLock.getStore();
			isHalt = schedule.getIsHalt();
			regFee =  schedule.getRegFee();
			treatFee = schedule.getTreatFee();
			otherFee = schedule.getOtherFee();
			drawPoint = schedule.getDrawPoint();
			fee = hisLock.getFee();
			
		}else if(StringUtil.isNotBlank(store)) {
			JSONObject storeJson = JSONObject.parseObject(store);
			scheduleStore = storeJson.getString(ApiKey.Cache_His_Lock_Store.scheduleStore.getName());
			numberStore = storeJson.getString(ApiKey.Cache_His_Lock_Store.numberStore.getName());
			lockStore = storeJson.getString(ApiKey.Cache_His_Lock_Store.lockStore.getName());
			isHalt = storeJson.getInteger(ApiKey.Cache_His_Lock_Store.isHalt.getName());
			regFee = storeJson.getInteger(ApiKey.Cache_His_Lock_Store.regFee.getName());
			fee = storeJson.getInteger(ApiKey.Cache_His_Lock_Store.fee.getName());
			treatFee = storeJson.getInteger(ApiKey.Cache_His_Lock_Store.treatFee.getName());
			otherFee = storeJson.getInteger(ApiKey.Cache_His_Lock_Store.otherFee.getName());
			drawPoint = storeJson.getString(ApiKey.Cache_His_Lock_Store.drawPoint.getName());
		}else {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_CALLHIS,"排班已经过期，请重新查询排班再进行挂号.");
		}
		/**
		 * 挂号费不能小于0元 也不能大于2000元
		 */
		if( null == fee || fee < 0 || fee > 200000) {
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_Fee,"挂号支付金额异常.(单位：分）fee = "+ fee);
		}
		// 出诊
		if (isHalt == 1) {
			// 预约失败解锁
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_YYCHECK,"预约失败该医生已经出诊。");
		}
		// 停诊
		if (isHalt == 2) {
			// 预约失败解锁
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_YYCHECK,"预约失败该医生已经停诊。");
		}
		// 替诊
		if (isHalt == 3) {
			// 预约失败解锁
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_YYCHECK,"预约失败该医生排班为替诊排班，暂不能预约");
		}
		// 申请
		if (isHalt == 6) {
			// 预约失败解锁
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_YYCHECK,"预约失败该医生排班为申请排班，暂不能预约");
		}
		ReqQueryMemberList memberReq = new ReqQueryMemberList(reqVo.getMsg(),memberId,cardNo,KstHosConstant.CARDTYPE_1,orderLocal.getOpenId());
		memberReq.setHosId(orderLocal.getHosId());
		CommonResp<RespQueryMemberList> memberResp = basicService.queryMemberListToCache(new CommonReq<ReqQueryMemberList>(memberReq));
		if(memberResp==null || !KstHosConstant.SUCCESSCODE.equals(memberResp.getCode()) || memberResp.getData()==null || memberResp.getData().size()<=0) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST,"没有找到用户信息，挂号失败");
		}
		RespQueryMemberList member = memberResp.getData().get(0);
		// 预约流水
		YyWater water = new YyWater();
		// 患者信息
		water.setMemberId(orderLocal.getMemberId());
		water.setUserName(member.getMemberName());
		water.setUserMobile(member.getMobile());
		water.setClinicCard(member.getCardNo());
		water.setAddress(member.getAddress());
		water.setIdCardNo(member.getIdCardNo());
		water.setSex(member.getSex());
		// 预约信息
		water.setDoctorCode(lock.getDoctorCode());
		water.setDeptCode(lock.getDeptCode());
		water.setDeptName(lock.getDeptName());
		water.setDoctorName(lock.getDoctorName());
		water.setRegisterDate(lock.getRegisterDate());
		water.setTimeId(lock.getTimeId());
		water.setScheduleId(lock.getScheduleId());
		water.setState(1);
		water.setSourceCode(lock.getSourceCode());
		water.setHosId(lock.getHosId());
		water.setHosName(hos.getHosName());
		water.setRegistrationFee(regFee);
		try {// 挂号限制验证
			yuyueUtil.checkRule(commReq.getMsg(),water, 1);
		} catch (RRException e) {
			LogUtil.error(log, reqVo.getAuthInfo(),e);
			e.printStackTrace();
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, YY.ERROR_CANCELCHECK,e.getLocalizedMessage());
		}
		try {
			// 预约限制验证
			yuyueUtil.checkLimit(water);
		} catch (RRException e) {
			// 预约失败解锁
			LogUtil.error(log, reqVo.getAuthInfo(),e);
			e.printStackTrace();
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, YY.ERROR_YYCHECK,e.getLocalizedMessage());
		}
		Integer isOnlyPay = orderLocal.getIsOnlinePay();
		Integer payState = orderLocal.getPayState();//0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成
		
		IPayOfferNumberService offerNumberService = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(), IPayOfferNumberService.class);
		//需要在线支付的订单，但是支付状态不是支付完成的则抛出异常  //判断这笔订单如果是需要后支付的 则调用 预约挂号逻辑
		if(KstHosConstant.ONLINEPAY.equals(isOnlyPay) && !KstHosConstant.ORDERPAY_2.equals(payState) && null == offerNumberService) {
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"该订单需要支付后才能执行，请先进行支付。");
		}
		//判断订单金额是否就是挂号金额 如果不是的话 提示异常。
		
		Integer payMoney = orderLocal.getPayMoney();
		//支付金额与订单的金额比对 不一致的时候抛出异常 不能支付完成: 不执行挂号业务
//		if(null == payMoney || !payMoney.equals(fee)) {
//			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, YY.ERROR_Fee,"挂号支付金额异常,订单金额与业务金额不一致.(单位：分）fee = "+ fee +" payMoney = "+ payMoney);
//		}
		String openId = member.getOpId();
		water.setRegistrationFee(regFee);
		water.setClinicFee(treatFee);
		water.setOtherFee(otherFee);
		water.setFee(fee);
		
		water.setCreateTime(DateOper.getNowDateTime());
		water.setOperator(channelId);
		water.setOperatorName(reqVo.getOperatorName());
		water.setOperatorId(reqVo.getAuthInfo().getSign());
		water.setcType(lock.getCardTypeCode());
		water.setcNo(lock.getCardNo());
		water.setServiceFee(0);
		water.setDrawPoint(drawPoint);
		// 挂号
		Map<String, String> map = new HashMap<String, String>(16);
		member.toMap(map);
		/**基础信息**/
		map.put(ApiKey.HisBookService.hosId.name(), hos.getHosId());
		map.put(ApiKey.HisBookService.hospitalCode.name(), hos.getHospitalCode());
		map.put(ApiKey.HisBookService.sourceCode.name(), lock.getSourceCode());
		map.put(ApiKey.HisBookService.scheduleId.name(), lock.getScheduleId());
		map.put(ApiKey.HisBookService.deptCode.name(), lock.getDeptCode());
		map.put(ApiKey.HisBookService.deptName.name(), lock.getDeptName());
		map.put(ApiKey.HisBookService.doctorCode.name(), lock.getDoctorCode());
		map.put(ApiKey.HisBookService.doctorName.name(), lock.getDoctorName());
		map.put(ApiKey.HisBookService.timeSlice.name(), lock.getTimeId()!=null?String.valueOf(lock.getTimeId()):null);
		map.put(ApiKey.HisBookService.timeName.name(), FormatUtils.getTimeName(lock.getTimeId()));
		map.put(ApiKey.HisBookService.regDate.name(), lock.getRegisterDate());
		if(StringUtil.isNotBlank(lock.getCommendTime())) {
			if(lock.getCommendTime().contains("-")) {
				map.put(ApiKey.HisBookService.startTime.name(), lock.getCommendTime().split("-")[0]);
				map.put(ApiKey.HisBookService.endTime.name(), lock.getCommendTime().split("-")[1]);
			}else {
				map.put(ApiKey.HisBookService.startTime.name(), lock.getCommendTime());
			}
		}
		/**订单信息**/
		map.put(ApiKey.HisBookService.hisOrderId.name(), lock.getHislockOrderId());
		map.put(ApiKey.HisBookService.orderId.name(), reqVo.getOrderId());
		
		map.put(ApiKey.HisBookService.payFee.name(), String.valueOf(orderLocal.getPayState()));
		map.put(ApiKey.HisBookService.transNo.name(), orderLocal.getTransactionNo());
		
		
		map.put(ApiKey.HisBookService.operatorId.name(), reqVo.getAuthInfo().getSign());
		map.put(ApiKey.HisBookService.operatorName.name(), reqVo.getOperatorName());
//		map.put(ApiKey.HisBookService.regFlag.name(), String.valueOf(reqVo.getRegFlag()));
		map.put(ApiKey.HisBookService.channelId.name(), channelId);
		
		/**患者信息**/
		map.put(ApiKey.HisBookService.idCardNo.name(), water.getIdCardNo());
		map.put(ApiKey.HisBookService.patientName.name(), water.getUserName());
		map.put(ApiKey.HisBookService.hisPatientId.name(), member.getHisMemberId());
		map.put(ApiKey.HisBookService.cardType.name(), String.valueOf(member.getCardType()));
		map.put(ApiKey.HisBookService.cardNo.name(), member.getCardNo());
		if(StringUtil.isNotBlank(member.getCardType()) && "1".equals(member.getCardType())) {
			map.put(ApiKey.HisBookService.clinicCard.name(), member.getCardNo());
		}
		map.put(ApiKey.HisBookService.certNum.name(), member.getCertNum());
		map.put(ApiKey.HisBookService.certType.name(), member.getCertType());
		map.put(ApiKey.HisBookService.mobile.name(), member.getMobile());
		map.put(ApiKey.HisBookService.sex.name(), String.valueOf(member.getSex()));
		map.put(ApiKey.HisBookService.address.name(), member.getAddress());
		map.put(ApiKey.HisBookService.payMoney.name(), payMoney+"");
		
		// 调用his挂号
		HisBookService bookServiceResp = null;
		if(null != offerNumberService) { //先预约不支付的
			bookServiceResp = offerNumberService.regbookService(reqVo.getMsg(),reqVo.getOrderId(),
					scheduleStore,
					numberStore,
					lockStore,map).getDataCaseRetCode();
		}else {//预约挂号 ／或者 立即支付的
			// 调用his挂号
			bookServiceResp = getCallHisService(reqVo.getAuthInfo()).bookService(reqVo.getMsg(),reqVo.getOrderId(),
					scheduleStore,
					numberStore,
					lockStore,map).getDataCaseRetCode();
		}
		if (StringUtil.isBlank(bookServiceResp)) {
			// 预约失败解锁
			return new CommonResp<RespMap>(commReq, Constant.BOOKSERVICECODE, YY.ERROR_CALLHIS,"失败：返回值为空.");
		}
		if (Constant.UNLOCKSUCC.equals(bookServiceResp.getRespCode())) {
			return new CommonResp<RespMap>(commReq, Constant.BOOKSERVICECODE, YY.ERROR_BOOK_UNLOCK,bookServiceResp.getRespMessage());
		} else if (!Constant.SUCCESSCODE.equals(bookServiceResp.getRespCode())) {
			return new CommonResp<RespMap>(commReq, Constant.BOOKSERVICECODE, YY.ERROR_CALLHIS,bookServiceResp.getRespMessage());
		}
		ValidatorUtils.validateEntity(bookServiceResp,AddGroup.class);
		String hisOrderId = bookServiceResp.getHisOrderId();
		String bookStore = bookServiceResp.getStore();
		
		Integer no = lock.getSqNo();
		if (StringUtil.isNotBlank(no)) {
			water.setQueueNo(no);
		}
		water.setCommendTime(lock.getCommendTime());
		water.setOrderId(orderId);
		water.setHisOrderId(hisOrderId);
		water.setWorkPlace(drawPoint);
		water.setPushState(-1);
		water.setNum(0);
		water.setUpdateTime(DateOper.getNowDateTime());
		water.setStore(bookStore);
		
		YyUnLock unLock = new YyUnLock();
		unLock.setOrderId(orderId);
		unLock.setCreateTime(DateOper.getNowDateTime());
		unLock.setRemark("预约成功");
		try {
			// 保存预约流水
			waterMapper.insert(water);
			unlockMapper.insert(unLock);
			ReqBizForCompletion param = new ReqBizForCompletion(reqVo.getMsg(),orderId,reqVo.getOperatorId(),reqVo.getOperatorName());
			CommonReq<ReqBizForCompletion> req = new CommonReq<ReqBizForCompletion>(param);
			//调用订单完成接口完成订单
			CommonResp<RespMap> bizForCompletionResp = orderService.bizForCompletion(req);
			if(null == bizForCompletionResp || !KstHosConstant.SUCCESSCODE.equals(bizForCompletionResp.getCode())) {
				return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN,RetCode.Common.ERROR_SQLEXECERROR,"挂号成功，但是执行订单完成失败");
			}
		}catch (Exception e) {
			e.printStackTrace();
			//挂号异常，退号操作 
			LogUtil.error(log, e,reqVo.getAuthInfo());
			ReqYYCancel yyCancelReq = new ReqYYCancel(reqVo.getMsg(), 
					water.getOrderId(), "调用添加订单接口异常：" + e.getLocalizedMessage(), 
					water.getOperatorId(), water.getOperatorName());
			this.cancelRegister(water, orderLocal, new CommonReq<ReqYYCancel>(yyCancelReq), channelId, true);
			return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN,RetCode.Common.ERROR_SQLEXECERROR,"保存预约流水信息异常："+e.getLocalizedMessage());
		}
		try {
			Map<String, Object> pushMap = new HashMap<String, Object>();
			pushMap.put("yyWater", water);
			pushMap.put("jkzlHosId", reqVo.getHosId());
			IPushRegWaterToJkzlService pushService = HandlerBuilder.get().getCallHisService(KasiteConfig.getAuthInfo(reqVo.getMsg()), IPushRegWaterToJkzlService.class);
			if(pushService!=null) {
				pushService.pushRegWaterToJkzl(reqVo.getMsg(), pushMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,reqVo.getAuthInfo());
		}
		try {
			// 云报表采集
			reportFormsService.dataCloudCollection(reqVo.getMsg(),channelId, 105, 1, "1");
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,reqVo.getAuthInfo());
		}
		try {
			if(StringUtil.isNotBlank(orderLocal.getServiceId()) && BusinessTypeEnum.ORDERTYPE_009.getCode().equals(orderLocal.getServiceId())) {
				Element data1 = DocumentHelper.createElement(Constant.DATA_1);
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.RegisterNo, water.getHisOrderId());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.UserName, water.getUserName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.CardNo, water.getClinicCard());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.DeptName, water.getDeptName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.DoctorName, water.getDoctorName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.RegisterDate, water.getRegisterDate());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.CommendTime, water.getCommendTime());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.TimeIdStr, KasiteConfig.getTimeIdStr(water.getTimeId()));
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.HospitalName, water.getHosName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.QueueNo, water.getQueueNo());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101110.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.ORDERTYPE_009, reqVo.getClientId(),reqVo.getConfigKey(),water.getOrderId()));
				//  /business/yygh/yyghAppointmentDetails.html?orderId=<OrderId>
				//消息推送走消息中心
				ReqSendMsg queue = new ReqSendMsg(reqVo.getMsg(), water.getcNo(), water.getcType(), channelId, "", water.getUserMobile(),
						"", KstHosConstant.MODETYPE_10101110, data1.asXML(), openId, 1, water.getOperatorId(), water.getOperatorName(), orderId, "", water.getMemberId(), "");
				//设置该参数时，消息中心会获取该参数存入发送记录表中的医院ID，这样消息中心管理后台可以通过机构ID查询，有多院区医院时，医院ID可能和机构ID不一致需要做处理
				queue.setOrgId(KasiteConfig.getOrgCode());
				CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
				CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
				if (!Constant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
					LogUtil.info(log, "发送预约消息异常：YYOrderId="+orderId+"|||Result="+addMsgQueue.getMessage(),reqVo.getAuthInfo());
				}
			}else {
				Element data1 = DocumentHelper.createElement(Constant.DATA_1);
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.RegisterNo, water.getHisOrderId());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.UserName, water.getUserName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.CardNo, water.getClinicCard());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.DeptName, water.getDeptName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.DoctorName, water.getDoctorName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.RegisterDate, water.getRegisterDate());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.CommendTime, water.getCommendTime());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.TimeIdStr, KasiteConfig.getTimeIdStr(water.getTimeId()));
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.HospitalName, water.getHosName());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.QueueNo, water.getQueueNo());
				XMLUtil.addElement(data1, ApiKey.MODETYPE_10101111.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.ORDERTYPE_0, reqVo.getClientId(),reqVo.getConfigKey(),water.getOrderId()));
				//  /business/yygh/yyghAppointmentDetails.html?orderId=<OrderId>
				//消息推送走消息中心
				ReqSendMsg queue = new ReqSendMsg(reqVo.getMsg(), water.getcNo(), water.getcType(), channelId, "", water.getUserMobile(),
						"", KstHosConstant.MODETYPE_10101111, data1.asXML(), openId, 1, water.getOperatorId(), water.getOperatorName(), orderId, "", water.getMemberId(), "");
				//设置该参数时，消息中心会获取该参数存入发送记录表中，这样消息中心管理后台可以通过机构ID查询，有多院区医院时，医院ID可能和机构ID不一致需要做处理
				queue.setOrgId(KasiteConfig.getOrgCode());
				CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
				CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
				if (!Constant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
					LogUtil.info(log, "发送预约消息异常：YYOrderId="+orderId+"|||Result="+addMsgQueue.getMessage(),reqVo.getAuthInfo());
				}
			}
			
			
		} catch (Exception e) {
			LogUtil.error(log, e,reqVo.getAuthInfo());
			e.printStackTrace();
		}
		return new CommonResp<RespMap>(commReq, Constant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String YYCancel(InterfaceMessage msg) throws Exception{
		return this.yYCancel(new CommonReq<ReqYYCancel>(new ReqYYCancel(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> yYCancel(CommonReq<ReqYYCancel> commReq) throws Exception {
		ReqYYCancel req = commReq.getParam();
		String channelId = req.getClientId();
		YyWater water = waterMapper.selectByPrimaryKey(req.getOrderId());
		
		ReqOrderListLocal orderListReq = new ReqOrderListLocal(req.getMsg(), 
				null, null, null, req.getOrderId(), null, null, null, null,
				null, null, null, null);
		CommonResp<RespOrderLocalList> orderListResp =  orderService.orderListLocal(new CommonReq<ReqOrderListLocal>(orderListReq));
		if(orderListResp==null || !KstHosConstant.SUCCESSCODE.equals(orderListResp.getCode())) {
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"查询订单异常:"+(orderListResp!=null?orderListResp.getMessage():""));
		}
		if(orderListResp.getData().size()<=0) {
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.Order.ERROR_CANNOTEXIST,"没有找到有效的订单信息。");
		}
		int bizState = orderListResp.getResultData().getBizState();
		int payStete = orderListResp.getResultData().getPayState();
		//如果是未支付直接取消，锁号后直接取消的情况直接解锁
		if(water==null) {
			if(bizState == 0) {
				CommonReq<ReqUnlock> unLockReq = new CommonReq<ReqUnlock>(new ReqUnlock(commReq.getMsg()));
				return unlock(unLockReq);
			}
			if(payStete == 2) {
				CommonReq<ReqUnlock> unLockReq = new CommonReq<ReqUnlock>(new ReqUnlock(commReq.getMsg()));
				unlock(unLockReq);
				return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.YY.ERROR_NOTWATER,"订单已经支付但是未执行挂号，请联系管理员退费。");
			}
			
			return new CommonResp<RespMap>(commReq, Constant.CANCELSERVICECODE, RetCode.YY.ERROR_NOTWATER,"没有找到预约信息。");
		}
		RespOrderLocalList orderLocal = orderListResp.getData().get(0);
		
		//取消挂号
		return cancelRegister(water, orderLocal, commReq, channelId, true);
		
	}

	@Override
	public CommonResp<RespQueryRegInfo> queryLocalRegInfo(CommonReq<ReqQueryRegInfo> commReq) throws Exception {
		
		ReqQueryRegInfo req = commReq.getParam();
		Example example = new Example(YyWater.class);
		Criteria criteria = example.createCriteria();
		if(req.getCardType()!=null) {
			criteria.andEqualTo("cType", req.getCardType());
		}
		if(StringUtil.isNotBlank(req.getCardNo())) {
			criteria.andEqualTo("cNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getOrderId())) {
			criteria.andEqualTo("orderId", req.getOrderId());
		}
		if(StringUtil.isNotBlank(req.getIdCardNo())) {
			criteria.andEqualTo("idCardNo", req.getIdCardNo());
		}
		if(StringUtil.isNotBlank(req.getClinicCard())) {
			criteria.andEqualTo("clinicCard", req.getClinicCard());
		}
		if(StringUtil.isNotBlank(req.getStartTime())) {
			criteria.andGreaterThanOrEqualTo("registerDate", req.getStartTime());
		}
		if(StringUtil.isNotBlank(req.getEndTime())) {
			criteria.andLessThanOrEqualTo("registerDate", req.getEndTime());
		}
		if(StringUtil.isNotBlank(req.getTimeSlice())) {
			criteria.andEqualTo("timeId", req.getTimeSlice());
		}
		List<RespQueryRegInfo> respList = new ArrayList<RespQueryRegInfo>();
		List<YyWater> waterList = waterMapper.selectByExample(example);
		for (YyWater water : waterList) {
			RespQueryRegInfo resp = new RespQueryRegInfo();
			yuyueUtil.yyWaterToResp(resp, water);
			respList.add(resp);
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	
	@Override
	public List<RespQueryYYLimit> queryYYLimit(String ruleId, Integer state) throws Exception {
		Example example = new Example(YyRule.class);
		if(state != null) {
			example.createCriteria().andEqualTo("state", state);
		}
		if(StringUtil.isNotBlank(ruleId)) {
			example.createCriteria().andEqualTo("ruleId", state);
		}
		List<RespQueryYYLimit> respList = new Vector<RespQueryYYLimit>();
		List<YyLimit> yyLimitList = limitMapper.selectByExample(example);
		if(yyLimitList != null) {
			for (YyLimit yyLimit : yyLimitList) {
				RespQueryYYLimit resp = new RespQueryYYLimit();
				BeanCopyUtils.copyProperties(yyLimit, resp, null);
				respList.add(resp);
			}
		}
		return respList;
	}

	@Override
	public int updateYYLimit(ReqYyLimit reqYyLimit) throws Exception {
		YyLimit yyLimit = new YyLimit();
		BeanCopyUtils.copyProperties(reqYyLimit, yyLimit, null);
		return limitMapper.updateByPrimaryKeySelective(yyLimit);
	}


	@Override
	public String SearchClinicDeptAndDoctor(InterfaceMessage msg) throws Exception {
		return this.searchClinicDeptAndDoctor(new CommonReq<ReqSearchClinicDeptAndDoctor>(new ReqSearchClinicDeptAndDoctor(msg))).toResult();
	}

	@Override
	public CommonResp<RespSearchClinicDeptAndDoctor> searchClinicDeptAndDoctor(CommonReq<ReqSearchClinicDeptAndDoctor> commReq) throws Exception {
		ReqSearchClinicDeptAndDoctor req = commReq.getParam();
		@SuppressWarnings("unchecked")
		CommonResp<RespSearchClinicDeptAndDoctor> commResp = (CommonResp<RespSearchClinicDeptAndDoctor>) yyCacheMap.get(commReq.getMsg().getParam());
		if(commResp!=null) {
			//缓存中已存在数据  直接返回
			return commResp;
		}
		ISearchClinicDeptDoctorService service = getCallHisService(req.getAuthInfo(), ISearchClinicDeptDoctorService.class);
		if(service!=null) {
			CommonResp<RespQueryHospitalLocal> hosResp = basicService.queryHospitalLocal(new CommonReq<ReqQueryHospitalLocal>(new ReqQueryHospitalLocal(req.getMsg(), req.getHosId())));
			if(!KstHosConstant.SUCCESSCODE.equals(hosResp.getCode())) {
				return new CommonResp<RespSearchClinicDeptAndDoctor>(commReq,  hosResp.getRetCode(), hosResp.getMessage());
			}
			if(hosResp.getResultData()==null) {
				return new CommonResp<RespSearchClinicDeptAndDoctor>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Basic.ERROR_CANNOTEXIST, "搜索失败，没有找到医院信息。");
			}
			RespQueryHospitalLocal hos = hosResp.getResultData();
			//医院有实现接口时，直接调用医院接口查询
			Map<String, String> map = new HashMap<String, String>();
			map.put("hosId", hos.getHosId());
			map.put("hospitalCode", hos.getHospitalCode());
			map.put("searchLike", req.getSearchLike());
			StringBuffer docs = new StringBuffer();
			List<HisSearchClinicDeptAndDoctor> list = service.searchClinicDeptAndDoctor(req.getMsg(), map);
			List<RespSearchClinicDeptAndDoctor> respList = new ArrayList<RespSearchClinicDeptAndDoctor>();
			for (HisSearchClinicDeptAndDoctor hisDepAndDoc : list) {
				RespSearchClinicDeptAndDoctor resp = new RespSearchClinicDeptAndDoctor();
				BeanCopyUtils.copyProperties(hisDepAndDoc, resp, null);
				if(StringUtil.isBlank(resp.getUrl())) {
					docs.append(resp.getDoctorCode()).append(",");
				}
				respList.add(resp);
			}
			if(docs.length()>0) {
				//调用本地数据库查询医生头像
				CommonResp<RespQueryBaseDoctorLocal> baseDocResp = basicService.queryBaseDoctorLocal(new CommonReq<ReqQueryBaseDoctorLocal>(new ReqQueryBaseDoctorLocal(req.getMsg(), req.getHosId(), null, null, null, null, null,docs.substring(0,docs.length()-1),null)));
				if(KstHosConstant.SUCCESSCODE.equals(baseDocResp.getCode())) {
					List<RespQueryBaseDoctorLocal> ll = baseDocResp.getData();
					if(ll!=null) {
						for (RespQueryBaseDoctorLocal localDoc : ll) {
							for (RespSearchClinicDeptAndDoctor resp : respList) {
								if(localDoc.getDoctorCode().equals(resp.getDoctorCode()) && StringUtil.isNotBlank(localDoc.getPhotoUrl())) {
									resp.setUrl(localDoc.getPhotoUrl());
									break;
								}
							}
						}
					}
				}
			}
			commResp = new CommonResp<RespSearchClinicDeptAndDoctor>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
			//将结果集保存到缓存中
			yyCacheMap.put(req.getMsg().getParam(), commResp);
			return commResp;
		}else {
			//未实现接口时，直接门诊科室排班接口
			List<RespSearchClinicDeptAndDoctor> respList = new ArrayList<RespSearchClinicDeptAndDoctor>();
			CommonResp<RespMap> deptResp = this.queryClinicDept(new CommonReq<ReqQueryClinicDept>(new ReqQueryClinicDept(req.getMsg(), req.getHosId(), req.getSearchLike(), null, null, null,null, null)));
			if(KstHosConstant.SUCCESSCODE.equals(deptResp.getCode())) {
				if(deptResp.getData()!=null) {
					for (RespMap dept : deptResp.getData()) {
						RespSearchClinicDeptAndDoctor resp = new RespSearchClinicDeptAndDoctor();
						resp.setDeptCode(dept.getString(ApiKey.QueryClinicDept.DeptCode));
						resp.setDeptName(dept.getString(ApiKey.QueryClinicDept.DeptName));
						resp.setSearchType(1);
						respList.add(resp);
					}
				}
			}
			CommonResp<RespQueryClinicDoctor> docResp = this.queryClinicDoctor(new CommonReq<ReqQueryClinicDoctor>(new ReqQueryClinicDoctor(req.getMsg(), req.getSearchLike(), null, null, null, null, null, null, null, req.getHosId(),null)));
			if(KstHosConstant.SUCCESSCODE.equals(docResp.getCode())) {
				if(docResp.getData()!=null) {
					for (RespQueryClinicDoctor doc : docResp.getData()) {
						RespSearchClinicDeptAndDoctor resp = new RespSearchClinicDeptAndDoctor();
						resp.setDeptName(doc.getDeptName());
						resp.setDeptCode(doc.getDeptCode());
						resp.setDoctorCode(doc.getDoctorCode());
						resp.setDoctorName(doc.getDoctorName());
						resp.setDoctorTitle(doc.getDoctorTitle());
						resp.setDoctorIsHalt(doc.getDoctorIsHalt());
						resp.setSpec(doc.getSpec());
						resp.setSearchType(2);
						respList.add(resp);
					}
				}
			}
			commResp = new CommonResp<RespSearchClinicDeptAndDoctor>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
			//将结果集保存到缓存中
			yyCacheMap.put(req.getMsg().getParam(), commResp);
			return commResp;
		}
	}

	@Override
	public String PushStop(InterfaceMessage msg) throws Exception {
		//TODO 院方推送都停诊内容
		ReqStopSchedule req = new ReqStopSchedule(msg);
		if(null != req && null != req.getPushStop()) {
			CommonResp<HisStopClinicList> resp = req.getPushStop();
			ReqStop stop = new ReqStop(msg,resp.getData());
			return this.pushStop(new CommonReq<ReqStop>(stop)).toResult();
		}
		throw new RRException("推送的内容无法解析："+ msg.getParam());
	}

	public CommonResp<RespMap> pushStop(CommonReq<ReqStop> req) throws Exception{
		InterfaceMessage msg = req.getMsg();
		List<HisStopClinicList> list = req.getParam().getList();
		for (HisStopClinicList sch : list) {
			if(StringUtil.isBlank(sch.getScheduleId()) && StringUtil.isBlank(sch.getSourceCode())) {
				if(StringUtil.isBlank(sch.getDoctorCode())){
					throw new RRException("排班ID/医生工号 不能同时为空");
				}
				if(StringUtil.isBlank(sch.getDoctorCode())
						&& StringUtil.isBlank(sch.getDeptCode())
						&& StringUtil.isBlank(sch.getRegDate())
						) {
					throw new RRException("排班ID／科室代码／医生工号／就诊日期 不能同时为空");
				}
			}
			
			Example example = new Example(YyWater.class);
			Criteria criteria = example.createCriteria();
			if(StringUtil.isNotBlank(sch.getDeptCode())) {
				criteria.andEqualTo("deptCode", sch.getDeptCode());
			}
			if(StringUtil.isNotBlank(sch.getDoctorCode())) {
				criteria.andEqualTo("doctorCode", sch.getDoctorCode());
			}
			if(StringUtil.isNotBlank(sch.getRegDate())) {
				criteria.andEqualTo("registerDate", sch.getRegDate());
			}
			if(StringUtil.isNotBlank(sch.getTimeSlice())) {
				criteria.andEqualTo("timeId", sch.getTimeSlice());
			}
			if(StringUtil.isNotBlank(sch.getScheduleId())) {
				criteria.andEqualTo("scheduleId", sch.getScheduleId());
			}
			if(StringUtil.isNotBlank(sch.getSourceCode())) {
				criteria.andEqualTo("sourceCode", sch.getSourceCode());
			}
			if(StringUtil.isNotBlank(sch.getHisOrderId())) {
				criteria.andEqualTo("hisOrderId", sch.getHisOrderId());
			}
			if(criteria.getAllCriteria()==null || criteria.getAllCriteria().isEmpty()) {
				throw new RRException("发送停诊通知异常：查询停诊预约记录条件不能为空。");
			}
			List<YyWater> regList = waterMapper.selectByExample(example);
			for (YyWater water : regList) {
				if(water.getState()==2) {
					//已取消预约的不推送消息
					continue;
				}else {
					//取消处理
					YyWater upWater = new YyWater();
					upWater.setOrderId(water.getOrderId());
					upWater.setState(2);
					waterMapper.updateByPrimaryKeySelective(upWater);
					
					try {
						CommonResp<RespOrderDetailLocal> orderResp = orderService.orderDetailLocal(new CommonReq<ReqOrderDetailLocal>(new ReqOrderDetailLocal(msg, water.getOrderId(), null)));
						if(KstHosConstant.SUCCESSCODE.equals(orderResp.getCode())) {
							RespOrderDetailLocal localOrder = orderResp.getDataCaseRetCode();
							if(localOrder.getIsOnlinePay()!=null && localOrder.getIsOnlinePay()==1) {
								//TODO 支付订单可能涉及退费操作等，需要调用另外的接口
								
							}else {
								//未支付订单 停诊直接取消订单，
								orderService.bizForCancel(new CommonReq<ReqBizForCancel>(new ReqBizForCancel(msg, water.getOrderId(), "SYSTEM", "停诊取消订单")));
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						LogUtil.error(log, e);
					}
					if(KstHosConstant.JKZL_CHANNEL_ID.equals(water.getOperator())) {
						//健康之路预约的跳过通知
						continue;
					}
					//下发消息
					Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
					XMLUtil.addElement(data1, "OpenId", water.getOperatorId());
					XMLUtil.addElement(data1, "UserName", water.getUserName());
					XMLUtil.addElement(data1, "HosName", water.getHosName());
					XMLUtil.addElement(data1, "DeptName", water.getDeptName());
					XMLUtil.addElement(data1, "DoctorName", water.getDoctorName());
					XMLUtil.addElement(data1, "Time", water.getCommendTime());
					//消息推送走消息中心
					ReqSendMsg queue = new ReqSendMsg(msg, water.getcNo(), water.getcType(), water.getOperator(), "", water.getUserMobile(),
							"", KstHosConstant.MODETYPE_10101114, data1.asXML(), water.getOperatorId(), 1, water.getOperatorId(), water.getOperatorName(), water.getOrderId(), "", water.getMemberId(), "");
					CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
					CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
					if (!Constant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
						LogUtil.info(log, "发送停诊消息异常：YYOrderId="+water.getOrderId()+"|||Result="+addMsgQueue.getMessage(),msg.getAuthInfo());
					}
				}
			}
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	
	@Override
	public CommonResp<RespMap> updateYyWater(CommonReq<ReqUpdateYyWater> commReq) throws Exception {
		ReqUpdateYyWater req = commReq.getParam();
		YyWater water = new YyWater();
		water.setPushRemark(req.getPushRemark());
		water.setPushState(req.getPushState());
		water.setOrderId(req.getOrderId());
		waterMapper.updateByPrimaryKeySelective(water);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	
	
	
	
	@Override
	public String QueryExamItemList(InterfaceMessage msg) throws Exception{
		return this.queryExamItemList(new CommonReq<ReqQueryExamItemList>(new ReqQueryExamItemList(msg))).toResult();
		
	}

	@Override
	public CommonResp<RespMap> queryExamItemList(CommonReq<ReqQueryExamItemList> commReq) throws Exception {
		ReqQueryExamItemList req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = KstHosConstant.CARDTYPE_1;
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		String name = ApiKey.YjYY.CardNo.getName();
		// 入参xml转map格式
		if(StringUtil.isNotBlank(cardNo)) {
			map.put(name, req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put(ApiKey.YjYY.CardType.getName(), req.getCardType()+"");
		}
		
		List<HisQueryExamItemList> list = getCallHisService(req.getAuthInfo(),IYjyyService.class)
				.QueryExamItemList(req.getMsg(), map)
				.getListCaseRetCode();
		if(list==null) {
			return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisQueryExamItemList vo : list) {
			ValidatorUtils.validateEntity(vo,AddGroup.class);
			RespMap respMap = new RespMap();
			String key =  vo.getHisKey();
			respMap.put(ApiKey.YjYY.HisKey, key );
			respMap.put(ApiKey.YjYY.BillDate, vo.getDate());
			respMap.put(ApiKey.YjYY.LabName, vo.getLabName());
			respMap.put(ApiKey.YjYY.FriendlyReminder, vo.getFriendlyReminder());
			respMap.put(ApiKey.YjYY.PrintAppointmentID, vo.getPrintAppointmentID());
			respMap.put(ApiKey.YjYY.AppointmentStatus, vo.getAppointmentStatus());
			respMap.put(ApiKey.YjYY.AppointmentTime, vo.getAppointmentTime());
			respMap.put(ApiKey.YjYY.IsBook, vo.getIsBook());
			respMap.put(ApiKey.YjYY.LabPrice, vo.getLabPrice());
			respMap.put(ApiKey.YjYY.OrderId, vo.getOrderID());
			respMap.put(ApiKey.YjYY.BillDept, vo.getBillDept());
			respMap.put(ApiKey.YjYY.ExamDept, vo.getExamDept());
			respMap.put(ApiKey.YjYY.LabCode, vo.getLabCode());
			CallHisCacheMap.get().setExamItem(key, vo);
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.Success.RET_10000, respList);
	
	}

	@Override
	public String QuerySignalSourceList(InterfaceMessage msg) throws Exception{
		return this.querySignalSourceList(new CommonReq<ReqQuerySignalSourceList>(new ReqQuerySignalSourceList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> querySignalSourceList(CommonReq<ReqQuerySignalSourceList> commReq)
			throws Exception {
		ReqQuerySignalSourceList req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		basicService.addMemberInfo2Map(commReq, req.getMemberId(), req.getCardNo(), "1", req.getOpenId(), map);
		map.put(ApiKey.YjYY.CardNo.getName(), req.getCardNo());
		map.put(ApiKey.YjYY.CardType.getName(), req.getCardType()+"");
		map.put(ApiKey.YjYY.LabCode.getName(), req.getLabCode());
		map.put(ApiKey.YjYY.BillDept.getName(), req.getDepartCode());
		map.put(ApiKey.YjYY.ExamDept.getName(), req.getExamDept());
		map.put(ApiKey.YjYY.BookDate.getName(), req.getBookDate());
		List<HisQuerySignalSourceList> list = getCallHisService(req.getAuthInfo(),IYjyyService.class)
				.QuerySignalSourceList(req.getMsg(), map)
				.getListCaseRetCode();
		if(list==null) {
			return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisQuerySignalSourceList vo : list) {
			ValidatorUtils.validateEntity(vo,AddGroup.class);
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.YjYY.SourceId, vo.getSourceId());
			respMap.put(ApiKey.YjYY.Sequence, vo.getSequence());
			respMap.put(ApiKey.YjYY.Time, vo.getTime());
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.Success.RET_10000, respList);
	
	}

	@Override
	public String MedicalAppoint(InterfaceMessage msg)throws Exception {
		return this.medicalAppoint(new CommonReq<ReqMedicalAppoint>(new ReqMedicalAppoint(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> medicalAppoint(CommonReq<ReqMedicalAppoint> commReq) throws Exception {
		ReqMedicalAppoint req = commReq.getParam();
		String openId = req.getOpenId();
		String clientId = req.getClientId();
		Map<String, String> map = new HashMap<String, String>(16);
		basicService.addMemberInfo2Map(commReq, req.getMemberId(), req.getCardNo(), "1", req.getOpenId(), map);
		map.put(ApiKey.YjYY.CardNo.getName(), req.getCardNo());
		map.put(ApiKey.YjYY.CardType.getName(), req.getCardType()+"");
		map.put(ApiKey.YjYY.HisKey.getName(), req.getHisKey());
		map.put(ApiKey.YjYY.SourceId.getName(), req.getSourceCode());
		CommonResp<RespMap> medicalAppoint = getCallHisService(req.getAuthInfo(),IYjyyService.class).MedicalAppoint(req.getMsg(), map);
		try {
			if(medicalAppoint.getCode().equals(Constant.SUCCESSCODE)){
				Map<String, String> map2 = new HashMap<String, String>(16);
				String HisKeys = map.get(ApiKey.YjYY.HisKey.getName());
				String[] split = HisKeys.split(",");
				for (String HisKey : split) {
					map2.put(ApiKey.YjYY.HisKey.getName(), HisKey);
					List<HisGetAppointReceiptInfo> list = getCallHisService(req.getAuthInfo(),IYjyyService.class)
							.GetAppointReceiptInfo(req.getMsg(), map2)
							.getListCaseRetCode();
					HisGetAppointReceiptInfo vo = new HisGetAppointReceiptInfo();
					vo = list.get(0);
					String ExDate = vo.getStartAppointmentTime().substring(0,10);
					String time = vo.getStartAppointmentTime().substring(10,18)+"-"+vo.getEndAppointmentTime().substring(10,18);
					Element data1 = DocumentHelper.createElement(Constant.DATA_1);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.CardNo, vo.getPatientName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.HisKey.getName(), HisKey);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.Address.getName(), vo.getAddress());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.BookDate.getName(), ExDate);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.AppointmentTime.getName(), time);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.LabName.getName(), vo.getHisName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.FriendlyReminder.getName(), vo.getReminderMsg());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_20101113.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.ORDERTYPE_012, req.getClientId(),req.getConfigKey(),req.getHisKey().split(",")[0]));
					
					//消息推送走消息中心
					ReqSendMsg queue = new ReqSendMsg(req.getMsg(),"", 1, clientId, "", "",
							"", KstHosConstant.MODETYPE_20101113, data1.asXML(), openId, 1, openId, openId, "", "", "", "");
					CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
					CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
					if (!KstHosConstant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
						LogUtil.info(log, "发送医技异常：||Result="+addMsgQueue.getMessage(),req.getMsg().getAuthInfo());
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return medicalAppoint;
	
	}

	@Override
	public String CancelAppoint(InterfaceMessage msg)throws Exception {
		return this.cancelAppoint(new CommonReq<ReqCancelAppoint>(new ReqCancelAppoint(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> cancelAppoint(CommonReq<ReqCancelAppoint> commReq) throws Exception {
		ReqCancelAppoint req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.YjYY.CardType.getName(), req.getCardType()+"");
		map.put(ApiKey.YjYY.HisKey.getName(), req.getHisKey());
		CommonResp<RespMap> medicalAppoint = getCallHisService(req.getAuthInfo(),IYjyyService.class).CancelAppoint(req.getMsg(), map);
		String clientId = req.getClientId();
		String openId = req.getOpenId();
		if(medicalAppoint.getCode().equals(Constant.SUCCESSCODE)){
			Element data1 = DocumentHelper.createElement(Constant.DATA_1);
			XMLUtil.addElement(data1, "LabName", Escape.unescape(req.getLabName()));
			ReqSendMsg queue = new ReqSendMsg(req.getMsg(),"", 1, clientId, "", "",
					"", KstHosConstant.MODETYPE_20101114, data1.asXML(), openId, 1, openId, openId, "", "", "", "");
			CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
			CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
			if (!KstHosConstant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
				LogUtil.info(log, "发送医技取消预约消息异常：||Result="+addMsgQueue.getMessage(),req.getMsg().getAuthInfo());
			}
		}
		return medicalAppoint;
	}

	@Override
	public String GetAppointReceiptInfo(InterfaceMessage msg)throws Exception {
		return this.getAppointReceiptInfo(new CommonReq<ReqGetAppointReceiptInfo>(new ReqGetAppointReceiptInfo(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> getAppointReceiptInfo(CommonReq<ReqGetAppointReceiptInfo> commReq)
			throws Exception {
		ReqGetAppointReceiptInfo req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.YjYY.HisKey.getName(), req.getHisKey());
		List<HisGetAppointReceiptInfo> list = getCallHisService(req.getAuthInfo(),IYjyyService.class)
				.GetAppointReceiptInfo(req.getMsg(), map)
				.getListCaseRetCode();
		if(list==null) {
			return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisGetAppointReceiptInfo vo : list) {
			ValidatorUtils.validateEntity(vo,AddGroup.class);
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.YjYY.CardNo, vo.getSerialNo());
			respMap.put(ApiKey.YjYY.StartAppointmentTime, vo.getStartAppointmentTime());
			respMap.put(ApiKey.YjYY.EndAppointmentTime, vo.getEndAppointmentTime());
			respMap.put(ApiKey.YjYY.ExamDept, vo.getDepartmentName());
			respMap.put(ApiKey.YjYY.Address, vo.getAddress());
			respMap.put(ApiKey.YjYY.FriendlyReminder, vo.getReminderMsg());
			respMap.put(ApiKey.YjYY.LabName, vo.getHisName());
			respMap.put(ApiKey.YjYY.Sequence, vo.getEnqueueNum());
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.Success.RET_10000, respList);
	
	}

	@Override
	public String QueryScheduleDate(InterfaceMessage msg)throws Exception {
		return this.queryScheduleDate(new CommonReq<ReqQueryScheduleDate>(new ReqQueryScheduleDate(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> queryScheduleDate(CommonReq<ReqQueryScheduleDate> commReq) throws Exception {
		ReqQueryScheduleDate req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		basicService.addMemberInfo2Map(commReq, req.getMemberId(), req.getCardNo(), "1", req.getOpenId(), map);
		map.put(ApiKey.YjYY.CardNo.getName(), req.getCardNo());
		map.put(ApiKey.YjYY.LabCode.getName(), req.getLabCode());
		map.put(ApiKey.YjYY.CardType.getName(), req.getCardType()+"");
		map.put(ApiKey.YjYY.ExamDept.getName(), req.getDepartCode());
		List<HisQueryScheduleDate> list = getCallHisService(req.getAuthInfo(),IYjyyService.class)
				.QueryScheduleDate(req.getMsg(), map)
				.getListCaseRetCode();
		if(list==null) {
			return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.YY.CallHosClientError);
		}
		List<RespMap> respList = new ArrayList<RespMap>();
		for (HisQueryScheduleDate vo : list) {
			ValidatorUtils.validateEntity(vo,AddGroup.class);
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.YjYY.BookDate, vo.getBookDate());
			respMap.put(ApiKey.YjYY.WeekDay, vo.getWeekDay());
			respMap.put(ApiKey.YjYY.IsBook, vo.getHaveNo());
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, Constant.QUERYCLINICDEPTCODE, RetCode.Success.RET_10000, respList);
	
	}
	
	
}