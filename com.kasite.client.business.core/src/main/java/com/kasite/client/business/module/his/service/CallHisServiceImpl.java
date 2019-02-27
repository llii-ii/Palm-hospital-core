package com.kasite.client.business.module.his.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.coreframework.util.DateOper;
import com.coreframework.util.JsonUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteDiyConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.ApiModule.His;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.constant.RetCode.YY;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.HisCallReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.service.CallHis;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.ISaveCallHisOrder;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.his.AbsCallHisService;
import com.kasite.core.serviceinterface.module.his.HisInfStatus;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.his.handler.IDirectCardPayService;
import com.kasite.core.serviceinterface.module.his.handler.IGetHisMemberIdService;
import com.kasite.core.serviceinterface.module.his.handler.IHisOrderValidator;
import com.kasite.core.serviceinterface.module.his.handler.ILockAndUnLockService;
import com.kasite.core.serviceinterface.module.his.handler.IMergeSettledPayReceiptService;
import com.kasite.core.serviceinterface.module.his.handler.IMiniPayService;
import com.kasite.core.serviceinterface.module.his.handler.IOrderPrescriptionPaymentService;
import com.kasite.core.serviceinterface.module.his.handler.IOrderSettlementService;
import com.kasite.core.serviceinterface.module.his.req.ReqHisLock;
import com.kasite.core.serviceinterface.module.his.req.ReqHisUnLock;
import com.kasite.core.serviceinterface.module.his.resp.HisBookService;
import com.kasite.core.serviceinterface.module.his.resp.HisCheckEntityCard;
import com.kasite.core.serviceinterface.module.his.resp.HisGetQueueInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisHosNoRecharge;
import com.kasite.core.serviceinterface.module.his.resp.HisLockOrder;
import com.kasite.core.serviceinterface.module.his.resp.HisOPDRecharge;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicCard;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryEntityCardInfoResp;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryHospitalUserInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalRechargeList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryNumbers;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderReceiptList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientRechargeList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryRegInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryUserInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisRegFlag;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItemDetails;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItemInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItems;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.kasite.core.serviceinterface.module.his.resp.HisUnlock;
import com.kasite.core.serviceinterface.module.his.resp.HisYYCancel;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderHisInfo;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryQLCOrder;
import com.kasite.core.serviceinterface.module.queue.IQueueService;
import com.kasite.core.serviceinterface.module.queue.req.ReqQueryLocalQueue;
import com.kasite.core.serviceinterface.module.queue.resp.RespQueryLocalQueue;
import com.kasite.core.serviceinterface.module.report.resp.RespGetTjReportInfo;
import com.kasite.core.serviceinterface.module.webui.IWebUiDiyConfig;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.util.Md5;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * 
 * @className: CallHisServiceImpl
 * @author: lcz
 * @date: 2018年6月4日 下午6:35:02
 */
/**
 * @author linjf
 * TODO
 */
/**
 * @author linjf
 * TODO
 */
/**
 * @author linjf
 * TODO
 */
/**
 * @author linjf
 * TODO
 */
@Service("callHisServiceImpl")
@SuppressWarnings("unchecked")
public class CallHisServiceImpl extends AbsCallHisService implements
		/**业务接口*/
 		ICallHisService,
 		/**保存订单日志接口*/
 		ISaveCallHisOrder,
 		/**锁号和解锁接口*/
 		ILockAndUnLockService,
 		/**
 		 * 执行业务的时候需要传入HisMemberId 如果不需要则不用查询这个接口，如果需要的话 在执行业务的时候会查询member表查询hisMemberId
 		 * 前提是用户必须先绑卡
 		 * */
 		IGetHisMemberIdService,
 		/**
 		 * mini付相关接口
 		 */
 		IMiniPayService,
 		/**
 		 * 合并支付相关接口
 		 */
 		IMergeSettledPayReceiptService,
 		/**
 		 * 订单校验器
 		 */
 		IHisOrderValidator,
 		/**
 		 * 前端个性化显示页面
 		 */
 		IWebUiDiyConfig,
 		/**
 		 * 直接卡面付相关接口
 		 */
 		IDirectCardPayService,
 		/**
 		 * 订单-处方支付
 		 */
 		IOrderPrescriptionPaymentService,
 		/**
 		 * 订单-处方结算
 		 */
 		IOrderSettlementService
// 		/**
// 		 * 腕带付 生成二维码的时候需要调用意义接口获取 信息，如果不是在生成的时候调用则不需要实现这个接口
// 		 */
// 		,ICreateQrCodeGuidSaveInfoService
	{

	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_HIS);
	private static String token = "";
	/** token失效时间(毫秒) */
	private static long tokenExpire;
	
	public String getUrl() {
//		return KasiteConfig.getDiyVal(DiyConfig.url);
		return "http://119.23.107.53:8080/ButlerClinic/api";
	}
	
	public String getHisUrl() {
//		return KasiteConfig.getDiyal(DiyConfig.hisUrl);
		return "http://120.78.189.153:8081/his/";
	}
	
	public String getCryptKey() {
//		return KasiteConfig.getDiyVal(DiyConfig.cryptKey);
		return "_ButlerClinicJkzl_";
	}
	
	public String getNumberSourceUrl() {
		return KasiteConfig.getDiyVal(DiyConfig.numberSourceUrl);
	}
	public com.alibaba.fastjson.JSONObject getMiniPayConfig() {
		String cf = KasiteConfig.getDiyVal(DiyConfig.miniPayConfig);
		return com.alibaba.fastjson.JSONObject.parseObject(cf);
	}
	enum DiyConfig implements KasiteDiyConfig{
		url,
		hisUrl,
		cryptKey,
		numberSourceUrl,
		miniPayConfig,
		WEB_UI_DIY,
		;
		@Override
		public String getNodeName() {
			return this.name();
		}
	}
	@Autowired
	IQueueService queueService;
	
	@Autowired
	IOrderService orderService;
	
	@Override
	public void saveOrderCallHisLog(String orderId, His api, String params, String result) throws Exception {
		if(api == His.bookService
			|| api == His.cancelOrder
			|| api == His.pay
			|| api == His.refund
				) {
			OrderHisInfo info = new OrderHisInfo();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("params", params);
			json.put("result", result);
			info.setContent(json.toJSONString());
			info.setSid(orderId);
			info.setName(api.getName());
			orderService.saveOrderHisInfo(info);		
		}
	}
	
	@Override
	public CommonResp<HisQueryClinicCard> hisQueryClinicCard(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			HisQueryClinicCard hisResp = new HisQueryClinicCard();
			
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><CardType>{cardType}</CardType><CardNo>{cardNo}</CardNo>"
					+ "<CardTypeName>{cardTypeName}</CardTypeName>"
					+ "<MemberName>{memberName}</MemberName><Mobile>{mobile}</Mobile><IdCardNo>{idCardNo}</IdCardNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String memberId = map.get("memberId");
			String result = call(memberId,ApiModule.His.queryClinicCard, getHisUrl(), "QUERYCARD", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (StringUtil.isBlank(result)) {
				hisResp.setStatus("-1");
			}else {
				hisResp.setClinicCard(map.get("cardNo"));
				hisResp.setStatus("1");
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					hisResp.setStatus("1");
//					Element data = rootEl.element("Data");
//					hisResp.setClinicCard(XMLUtil.getString(data, "CardNo", false));
//				}else {
//					hisResp.setStatus("-1");
//				}
			}
			return new CommonResp<>(msg, map, RetCode.Success.RET_10000,hisResp);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

//	@Override
//	public CommonResp<HisQueryHospitalNo> hisQueryHospitalNo(InterfaceMessage msg, Map<String, String> map) throws Exception {
//		try {
//			HisQueryHospitalNo hisResp = new HisQueryHospitalNo();
//			String reqParam = "<Req><TransactionCode></TransactionCode><Data><HospitalizationNo>{hospitalNo}</HospitalizationNo>"
//					+ "<CardType>{cardType}</CardType><CardNo>{cardNo}</CardNo><Mobile>{mobile}</Mobile><Name>{memberName}</Name><McardNo>{mcardNo}</McardNo><IdCardId>{idCardNo}</IdCardId></Data></Req>";
//			reqParam = CommonUtil.formateReqParam(reqParam, map);
//			String memberId = map.get("memberId");
//			String result = call(memberId,ApiModule.His.queryHospitalNo, getHisUrl(),"QueryHospitalNo", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					hisResp.setHisMemberId("hisMemberId");
//					hisResp.setStatus("1");
//				} else {
//					hisResp.setStatus("-1");
//				}
//			}
//			return new CommonResp<>(msg, map, RetCode.Success.RET_10000,hisResp);
//		}catch (Exception e) {
//			LogUtil.error(log, e);
//			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
//		}
//	
//	}

	@Override
	public CommonResp<HisQueryUserInfo> hisQueryUserinfo(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data>"
					+ "<CardType>{cardType}</CardType><CardNo>{cardNo}</CardNo><Mobile>{mobile}</Mobile><PatientId>{patientId}</PatientId><MemberName>{memberName}</MemberName><IdCardNo>{idCardNo}</IdCardNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.getPatInfo , getHisUrl(),"querymember", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryUserInfo hisQ = new HisQueryUserInfo();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String tmpCode = "-1";
				if (KstHosConstant.SUCCESSCODE.equals(respCode) || tmpCode.equals(respCode)) {
					Element el = rootEl.element(KstHosConstant.DATA);
					hisQ.setPatientId(XMLUtil.getString(el, "PatientId", false));
					hisQ.setName(XMLUtil.getString(el, "Name", false));
					hisQ.setMobile(XMLUtil.getString(el, "Mobile", false));
					hisQ.setCardNo(XMLUtil.getString(el, "CardNo", false));
					hisQ.setFee(Integer.valueOf(XMLUtil.getString(el, "Fee", false)));
					hisQ.setIdCardId(XMLUtil.getString(el, "IdCardId", false));
				}
				hisQ.setRespCode(respCode);
				hisQ.setRespMessage(XMLUtil.getString(rootEl, "RespMessage", false));
				hisQ.setTransactionCode(XMLUtil.getString(rootEl, "TransactionCode", false));
			}
			return new CommonResp<>(msg,map,RetCode.Success.RET_10000, hisQ);
		}catch (Exception e) {
			LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryHospitalUserInfo> queryHospitalUserInfo(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><HospitalNo>{hospitalNo}</HospitalNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String memberId = map.get("memberId");
			// 调用his接口
			String result = call(memberId,ApiModule.His.hospitalUserInfo , getHisUrl(),"QueryHospitalUserInfo", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryHospitalUserInfo hisResp = null;
			if (!StringUtil.isBlank(result)) {
				hisResp = new HisQueryHospitalUserInfo();
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					Element data = rootEl.element(KstHosConstant.DATA);
					hisResp.setIdCardId(XMLUtil.getString(data, "IdCardId", true));
					hisResp.setMobile(XMLUtil.getString(data, "Mobile", false));
					hisResp.setName(XMLUtil.getString(data, "Name", false));
					hisResp.setSex(1);
					hisResp.setAge(20);
					hisResp.setHospitalNo(XMLUtil.getString(data, "HospitalNo", false));
					hisResp.setDeptCode(XMLUtil.getString(data, "DeptCode", false));
					hisResp.setDeptName(XMLUtil.getString(data, "DeptName", false));
					hisResp.setBedNo(XMLUtil.getString(data, "BedNo", false));
					hisResp.setInHospitalDate(XMLUtil.getString(data, "InHospitalDate", false));
					hisResp.setInHospitalDays(XMLUtil.getInt(data, "InHospitalDays", false));
					hisResp.setInHospitalTotalFee(XMLUtil.getInt(data, "TotalFee", false));
					hisResp.setBalance(XMLUtil.getInt(data, "Balance", false));
					return new CommonResp<HisQueryHospitalUserInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,hisResp);
				} else {
					return new CommonResp<HisQueryHospitalUserInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryHospitalUserInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<RespMap> queryCardBalance(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><CardType>{cardType}</CardType><CardNo>{cardNo}</CardNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.queryCardBalance , getHisUrl(),"balance", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					Element el = rootEl.element(KstHosConstant.DATA);
					Integer balance = XMLUtil.getInt(el, "Balance", true);
					RespMap respMap = new RespMap();
					respMap.put(ApiKey.HisQueryCardBalance.Balance, balance);
					return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
				} else {
					return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}
//
//	@Override
//	public int hisCheckHospitalNo(InterfaceMessage msg, Map<String, String> map) throws Exception {
//		try {
//			String reqParam = "<Req><TransactionCode></TransactionCode><Data><HospitalNo>{hospitalNo}</HospitalNo>"
//					+ "<Mobile>{mobile}</Mobile><MemberName>{memberName}</MemberName><IdCardNo>{idCardNo}</IdCardNo></Data></Req>";
//			reqParam = CommonUtil.formateReqParam(reqParam, map);
//			String result = call(null,ApiModule.His.hisCheckHospitalNo , getHisUrl(),"QueryHN", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			int hasHospitalNo = 0;
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					Element data = rootEl.element(KstHosConstant.DATA);
//					hasHospitalNo = XMLUtil.getInt(data, "hasHospitalNo", true);
//				}
//			}
//			return hasHospitalNo;
//		}catch (Exception e) {
//			e.printStackTrace();LogUtil.error(log, e);
//			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
//		}
//	}

	/**
	 * 号源池接口
	 * 
	 * @author 無
	 *
	 */
	enum NumberSource implements ApiModule {
		/**
		 * 查询门诊科室
		 */
		QueryClinicDept("yy.yygh.QueryClinicDept"),
		/**
		 * 查询门诊医生
		 */
		QueryClinicDoctor("yy.yygh.QueryClinicDoctor"),
		/**
		 * 查询门诊排班
		 */
		QueryClinicSchedule("yy.yygh.QueryClinicSchedule"),
		/**
		 * 查询号源信息
		 */
		QueryNumbers("yy.yygh.QueryNumbers"),
		/**
		 * 锁号
		 */
		LockOrder("yy.yygh.LockOrder"),
		/**
		 * 释号
		 */
		Unlock("yy.yygh.Unlock"),
		/**
		 * 挂号
		 */
		BookService("yy.yygh.BookService"),
		/**
		 * 退号
		 */
		YYCancel("yy.yygh.YYCancel"),
		/**
		 * 预约信息查询
		 */
		QueryRegInfo("yy.yygh.QueryRegInfo"),

		/** 查询预约规则信息 */
		QueryRule("yy.yygh.QueryRule"),
		/** 查询历史医生 */
		QueryHistoryDoctor("yy.yygh.QueryHistoryDoctor"), 
		
		QueryYYRule("yy.yygh.QueryYYRule"),;
		@Override
		public String getModuleName() {
			return "_Module_NumberSource";
		}
		private String name;

		NumberSource(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	
	@Override
	public CommonResp<HisQueryClinicDept> queryClinicDept(InterfaceMessage msg, Map<String, String> paramMap) throws Exception{
		try {
			List<HisQueryClinicDept> lists = new ArrayList<HisQueryClinicDept>();
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<HosId>1024807</HosId>"
					+ "<DeptCode>{deptCode}</DeptCode>" + "<DeptName>{deptName}</DeptName>"
					+ "<WorkDateStart>{workDateStart}</WorkDateStart>" + "<WorkDateEnd>{workDateEnd}</WorkDateEnd>"
					+ "<WorkTime>{workTime}</WorkTime>" + "<RegType>{regType}</RegType>" + "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(null,ApiModule.His.queryDept,
					getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null),
					NumberSource.QueryClinicDept, reqParam,String.valueOf(paramMap.get("channelId")),
					KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryClinicDept clinicDept = null;
			if (StringUtil.isNotEmpty(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < el.size(); i++) {
						clinicDept = new HisQueryClinicDept();
						Element element = el.get(i);
						clinicDept.setAddress(XMLUtil.getString(element, "Address", false));
						clinicDept.setDeptCode(XMLUtil.getString(element, "DeptCode", true));
						clinicDept.setDeptDoctors(XMLUtil.getInt(element, "DeptDoctors", false));
						clinicDept.setDeptName(XMLUtil.getString(element, "DeptName", true));
						clinicDept.setIntro(XMLUtil.getString(element, "Intro", false));
						clinicDept.setParientID(XMLUtil.getString(element, "ParentId", false));
						clinicDept.setRemark(XMLUtil.getString(element, "Remark", false));
						clinicDept.setCanAppoint(XMLUtil.getInt(element, "CanAppoint", false));
						lists.add(clinicDept);
					}
				}else {
					
				}
			}
			return new CommonResp<HisQueryClinicDept>(msg,paramMap,RetCode.Success.RET_10000, lists);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryClinicDoctor> queryClinicDoctor(InterfaceMessage msg, Map<String, String> paramMap) throws Exception{
		try {
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<HosId>1024807</HosId>"
					+ "<DoctorName>{doctorName}</DoctorName>" + "<DeptCode>{deptCode}</DeptCode>"
					+ "<DoctorCode>{doctorCode}</DoctorCode>" + "<DoctorCodes>{doctorCodes}</DoctorCodes>"
					+ "<DoctorTitleCode>{doctorTitleCode}</DoctorTitleCode>" + "<ScheduleId>{scheduleId}</ScheduleId>"
					+ "<WorkDateStart>{workDateStart}</WorkDateStart>" + "<WorkDateEnd>{workDateEnd}</WorkDateEnd>"
					+ "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(null,ApiModule.His.queryDoctor,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.QueryClinicDoctor, reqParam,
					String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryClinicDoctor clinicDoctor = null;
			List<HisQueryClinicDoctor> lists = new ArrayList<HisQueryClinicDoctor>();
			if (StringUtil.isNotEmpty(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < el.size(); i++) {
						clinicDoctor = new HisQueryClinicDoctor();
						Element element = el.get(i);
						clinicDoctor.setSex(XMLUtil.getString(element, "DoctorSex", false));
						clinicDoctor.setDoctorCode(XMLUtil.getString(element, "DoctorCode", true));
						clinicDoctor.setDoctorName(XMLUtil.getString(element, "DoctorName", true));
						clinicDoctor.setDeptCode(XMLUtil.getString(element, "DeptCode", true));
						clinicDoctor.setDeptName(XMLUtil.getString(element, "DeptName", false));
						clinicDoctor.setDoctorTitle(XMLUtil.getString(element, "DoctorTitle", false));
						clinicDoctor.setDoctorTitleCode(XMLUtil.getString(element, "DoctorTitleCode", false));
						clinicDoctor.setDoctorIsHalt(XMLUtil.getInt(element, "DoctorIsHalt", false));
						clinicDoctor.setUrl(XMLUtil.getString(element, "Url", false));
						clinicDoctor.setIntro(XMLUtil.getString(element, "Intro", false));
						clinicDoctor.setSpec(XMLUtil.getString(element, "Spec", false));
						clinicDoctor.setRemark(XMLUtil.getString(element, "Remark", false));
						clinicDoctor.setLevelId(XMLUtil.getString(element, "LevelId", false));
						clinicDoctor.setLevel(XMLUtil.getString(element, "Level", false));
						clinicDoctor.setPrice(XMLUtil.getString(element, "Price", false));
						lists.add(clinicDoctor);
					}
				}
			}
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, lists);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryClinicSchedule> queryClinicSchedule(InterfaceMessage msg, Map<String, String> paramMap) throws Exception {
		try {
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<HosId>1024807</HosId>"
					+ "<DeptCode>{deptCode}</DeptCode>" + "<DoctorCode>{doctorCode}</DoctorCode>"
					+ "<ScheduleId>{scheduleId}</ScheduleId>" + "<WorkDateStart>{workDateStart}</WorkDateStart>"
					+ "<WorkDateEnd>{workDateEnd}</WorkDateEnd>" + "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(null,ApiModule.His.queryArrange,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.QueryClinicSchedule, reqParam,
					String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryClinicSchedule schedule = null;
			List<HisQueryClinicSchedule> lists = new ArrayList<HisQueryClinicSchedule>();
			if (StringUtil.isNotEmpty(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> list = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < list.size(); i++) {
						schedule = new HisQueryClinicSchedule();
						Element element = list.get(i);
						//！！！重要   排班唯一ID 如果医院有排班唯一ID则使用医院排班唯一ID 如果没有则通过拼接的方式 医院+科室+医生+日期+时段
						String arraySn = XMLUtil.getString(element, "ScheduleId", true);
						schedule.setScheduleId(arraySn);
						//!!! 保存his的结果集 挂号的时候会写到日志中，用于跟踪最后这个号挂号成功后对应的his查询排班的结果是那条记录。如果医院提供的是视图方案，则直接将排班流水对象JSON后保存
						//!!! 千万不要将 root 直接保存。会导致内存益处的，宁可不保存
						schedule.setStore(element.asXML());
						
						schedule.setDeptCode(XMLUtil.getString(element, "DeptCode", false));
						schedule.setDeptName(XMLUtil.getString(element, "DeptName", false));
						schedule.setRegType(XMLUtil.getInt(element, "RegType", false));
						schedule.setRegDate(XMLUtil.getString(element, "RegDate", false));
						schedule.setIsHalt(XMLUtil.getInt(element, "IsHalt", false));
						schedule.setTimeSlice(XMLUtil.getInt(element, "TimeSlice", false));
						schedule.setIsTimeFlag(XMLUtil.getInt(element, "IsTimeFlag", false));
						schedule.setLeaveCount(XMLUtil.getInt(element, "LeaveCount", false));
						schedule.setRegFee(XMLUtil.getInt(element, "RegFee", false));
						schedule.setTreatFee(XMLUtil.getInt(element, "TreatFee", false));
						schedule.setOtherFee(XMLUtil.getInt(element, "OtherFee", false));
						schedule.setWorkPlace(XMLUtil.getString(element, "WorkPlace", false));
						schedule.setDrawPoint(XMLUtil.getString(element, "DrawPoint", false));
						schedule.setRemark(XMLUtil.getString(element, "Remark", false));
						
						//需要个性化显示排班信息 自由拼装
//						com.alibaba.fastjson.JSONObject diyJson = new com.alibaba.fastjson.JSONObject();
//						diyJson.put("scheduleStr", "剩余号源："+schedule.getLeaveCount());
//						schedule.setDiyJson(diyJson.toJSONString());
						
						lists.add(schedule);
					}
				}
			}
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, lists);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryNumbers> queryNumbers(InterfaceMessage msg, HisQueryClinicSchedule sch, Map<String, String> paramMap) throws Exception {
		try {
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<HosId>1024807</HosId>"
					+ "<DeptCode>{deptCode}</DeptCode>" + "<DoctorCode>{doctorCode}</DoctorCode>"
					+ "<RegDate>{regDate}</RegDate>" + "<TimeSlice>{timeSlice}</TimeSlice>"
					+ "<ScheduleId>{scheduleId}</ScheduleId>" + "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(null,ApiModule.His.queryNumber,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.QueryNumbers, reqParam,
					String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryNumbers numbers = null;
			List<HisQueryNumbers> lists = new ArrayList<HisQueryNumbers>();
			if (StringUtil.isNotEmpty(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> list = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < list.size(); i++) {
						numbers = new HisQueryNumbers();
						Element element = list.get(i);
						//！！！重要   号源ID对象 必须保证唯一，每个号必须是唯一的如果不唯一 通过：排班ID + 号序 及时将HIS返回的所有字段拼起来也要保证唯一
						String sourceCode = XMLUtil.getString(element, "SourceCode", true);
						//!!! 千万不要将 root 直接保存。会导致内存益处的，宁可不保存
						numbers.setStore(element.asXML());
						numbers.setEndTime(XMLUtil.getString(element, "EndTime", true));
						numbers.setSourceCode(sourceCode);
						numbers.setSqNo(XMLUtil.getString(element, "SqNo", true));
						numbers.setStartTime(XMLUtil.getString(element, "StartTime", true));
						//* 需要个性化显示号源 列表 请修改这个号源内容 */
						//numbers.setNumberInfo("diy");
						lists.add(numbers);
					}
				}
			}
//			com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
//			json.put("code", -50000);
//			json.put("msg", "提示内容");
//			msg.setVersion(json.toJSONString());
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, lists);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	/**
	 * 处理
	 * @param msg
	 * @param orderId
	 * @param sch
	 * @param num
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public HisLockOrder lockOrder(AuthInfoVo authVo,String orderId,HisQueryClinicSchedule sch,
			HisQueryNumbers num, Map<String, String> paramMap) throws Exception {
		HisLockOrder lockOrder =  new HisLockOrder();
		String clinicCard = paramMap.get("clinicCard");
		if("LockServiceException".equals(clinicCard)) {
			throw new Exception("测试HIS接口异常情况");
		}
		//做调用his订单业务，处理完成后返回锁号成功并返回his的锁号ID 
		String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<DeptCode>{deptCode}</DeptCode>"
				+"<OrderId>{hisOrderId}</OrderId>"
				+ "<DoctorCode>{doctorCode}</DoctorCode>" + "<CardType>{cardType}</CardType>"
				+ "<CardNo>{cardNo}</CardNo>" + "<RegType>{regType}</RegType>" + "<RegDate>{regDate}</RegDate>"
				+ "<TimeSlice>{timeSlice}</TimeSlice>" + "<SqNo>{sqNo}</SqNo>" + "<SourceCode>{sourceCode}</SourceCode>"
				+ "<ScheduleId>{scheduleId}</ScheduleId>" + "<Mobile>{mobile}</Mobile>"
				+ "<ClinicCard>{clinicCard}</ClinicCard>" + "</Data>" + "</Req>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		// 调用his接口
		String result = callNumberSource(orderId,ApiModule.His.lock,
				getToken(authVo,null), NumberSource.LockOrder, reqParam, 
				String.valueOf(paramMap.get("channelId")),
				authVo);
		lockOrder.setStore(result);
		if (StringUtil.isNotEmpty(result)) {
			Document doc = XMLUtil.parseXml(result);
			Element rootEl = doc.getRootElement();
			String respCode = XMLUtil.getString(rootEl, "RespCode", true);
			if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
				Element data = rootEl.element(KstHosConstant.DATA);
				//！！！划重点注意 锁号 医院his必须返回一个唯一ID 并且唯一ID只能有一个，如果没有 通过排班唯一ID+号源唯一ID + 用户ID 来生成
				lockOrder.setHisOrderId(XMLUtil.getString(data, "OrderId", true));
				lockOrder.setSqNo(XMLUtil.getString(data, "SqNo", true));
			}
			lockOrder.setRespCode(respCode);
			lockOrder.setRespMessage(XMLUtil.getString(rootEl, "RespMessage", false));
		}
		return lockOrder;
	}

	public HisUnlock unlock(AuthInfoVo authVo,String orderId,Map<String, String> paramMap) throws Exception {
		HisUnlock unLock =  new HisUnlock();
		unLock.setUnLockSuccess(false);
		
		String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<OrderId>{hislockOrderId}</OrderId>"
				+ "</Data>" + "</Req>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		// 调用his接口
		String result = callNumberSource(orderId,ApiModule.His.unlock,
				getToken(authVo,null), NumberSource.Unlock, reqParam, 
				String.valueOf(paramMap.get("channelId")),authVo);
		unLock.setStore(result);
		if (StringUtil.isNotEmpty(result)) {
			Document doc = XMLUtil.parseXml(result);
			Element rootEl = doc.getRootElement();
			String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//			**调用his解锁如果his已经提前解锁返回代码 */
			if("10000".equals(respCode) || "-1010".equals(respCode)) {
				unLock.setUnLockSuccess(true);
			}else {
				String respMessage = XMLUtil.getString(rootEl, "RespMessage", false);
				unLock.setMessage(respMessage);
			}
		}
		return unLock;
	}

	@Override
	public CommonResp<HisBookService> bookService(InterfaceMessage msg,String orderId,
			String scheduleStore,
			String numStore,
			String lockStore, 
			Map<String, String> paramMap) throws Exception {
		try {
			String clinicCard = paramMap.get("cardNo");
			if("BookServiceException".equals(clinicCard)) {
				throw new Exception("测试HIS接口异常情况");
			}
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<OrderId>{hisOrderId}</OrderId>"
					+ "<IdCardNo>{idCardNo}</IdCardNo>" + "<Mobile>{mobile}</Mobile>" + "<Name>{patientName}</Name>"
					+ "<Sex>0</Sex>" + "<Address>{address}</Address>" + "<CardNo>{cardNo}</CardNo>"
					+ "<CardType>{cardType}</CardType>" + "<PayFee>{payFee}</PayFee>" + "<TransNo>{transNo}</TransNo>"
					+ "<TransTime>{transTime}</TransTime>" + "<RegFlag>2</RegFlag>"
					+ "<OperatorId>{operatorId}</OperatorId>" + "<OperatorName>{operatorName}</OperatorName>" + "</Data>"
					+ "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(orderId,ApiModule.His.bookService,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.BookService, reqParam, String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisBookService bookServiceResp = null;
			if (StringUtil.isNotEmpty(result)) {
				bookServiceResp = new HisBookService();
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					Element data = rootEl.element(KstHosConstant.DATA);
					bookServiceResp.setHisOrderId(XMLUtil.getString(data, "OrderId", true));
					bookServiceResp.setPosition(XMLUtil.getString(data, "Position", false));
					bookServiceResp.setSqNo(XMLUtil.getInt(data, "SqNo", false));
					//保存HIS挂号结果集
					bookServiceResp.setStore(rootEl.asXML());
				}
				bookServiceResp.setRespCode(respCode);
				bookServiceResp.setRespMessage(XMLUtil.getString(rootEl, "RespMessage", false));
			}
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, bookServiceResp);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisYYCancel> yyCancel(InterfaceMessage msg, String orderId,Map<String, String> paramMap,String bookServiceStore) throws Exception {
		try {
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<OrderId>{hisOrderId}</OrderId>"
					+ "<Reason>{reason}</Reason>" + "<OperatorId>{operatorId}</OperatorId>"
					+ "<OperatorName>{operatorName}</OperatorName>" + "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(orderId,ApiModule.His.cancelOrder,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.YYCancel, reqParam, String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisYYCancel yyCancelResp = null;
			if (StringUtil.isNotEmpty(result)) {
				yyCancelResp = new HisYYCancel();
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				yyCancelResp.setStore(rootEl.asXML());
				yyCancelResp.setRespCode(XMLUtil.getString(rootEl, "RespCode", true));
				yyCancelResp.setRespMessage(XMLUtil.getString(rootEl, "RespMessage", false));
			}
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, yyCancelResp);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryRegInfo> queryRegInfo(InterfaceMessage msg, Map<String, String> paramMap) throws Exception {
		try {
			String reqParam = "<Req>" + "<TransactionCode></TransactionCode>" + "<Data>" + "<CardType></CardType>"
					+ "<CardNo></CardNo>" + "<IdCardNo></IdCardNo>" + "<OrderId>{hisOrderId}</OrderId>"
					+ "<ClinicCard></ClinicCard>" + "<TimeSlice></TimeSlice>"
					+ "<StartTime></StartTime>" + "<EndTime></EndTime>" + "<RegFlag></RegFlag>"
					+ "</Data>" + "</Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = callNumberSource(null,ApiModule.His.queryRegWater,getToken(KasiteConfig.getAuthInfo(msg.getAuthInfo()),null), NumberSource.QueryRegInfo, reqParam,
					String.valueOf(paramMap.get("channelId")),KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryRegInfo regInfoResp = null;
			List<HisQueryRegInfo> lists = null;
			lists = new ArrayList<HisQueryRegInfo>();
			if (StringUtil.isNotEmpty(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < el.size(); i++) {
						regInfoResp = new HisQueryRegInfo();
						Element element = el.get(i);
						regInfoResp.setHisOrderId(XMLUtil.getString(element, "OrderId", false));
						regInfoResp.setCardType(XMLUtil.getInt(element, "CardType", false));
						regInfoResp.setCardNo(XMLUtil.getString(element, "CardNo", false));
						regInfoResp.setIdCardNo(XMLUtil.getString(element, "IdCardNo", false));
						regInfoResp.setBirthDay(XMLUtil.getString(element, "BirthDay", false));
						regInfoResp.setMobile(XMLUtil.getString(element, "Mobile", false));
						regInfoResp.setName(XMLUtil.getString(element, "Name", false));
						regInfoResp.setSex(XMLUtil.getInt(element, "Sex", false));
						regInfoResp.setAddress(XMLUtil.getString(element, "Address", false));
						regInfoResp.setClinicCard(XMLUtil.getString(element, "ClinicCard", false));
						regInfoResp.setRegType(XMLUtil.getString(element, "RegType", false));
						regInfoResp.setDoctorName(XMLUtil.getString(element, "DoctorName", false));
						regInfoResp.setDoctorCode(XMLUtil.getString(element, "DoctorCode", false));
						regInfoResp.setDeptCode(XMLUtil.getString(element, "DeptCode", false));
						regInfoResp.setDeptName(XMLUtil.getString(element, "DeptName", false));
						regInfoResp.setPayFee(XMLUtil.getInt(element, "PayFee", false));
						regInfoResp.setRegFee(XMLUtil.getInt(element, "RegFee", false));
						regInfoResp.setTreatFee(XMLUtil.getInt(element, "TreatFee", false));
						regInfoResp.setOtherFee(XMLUtil.getInt(element, "OtherFee", false));
						regInfoResp.setOperatorId(XMLUtil.getString(element, "OperatorId", false));
						regInfoResp.setOperatorName(XMLUtil.getString(element, "OperatorName", false));
						String reg = XMLUtil.getString(element, "RegDate", false);
						if (StringUtil.isNotEmpty(reg) && reg.length() > 10) {
							reg = reg.substring(0, 10);
						}
						regInfoResp.setRegDate(reg);
						regInfoResp.setTimeSlice(XMLUtil.getInt(element, "TimeSlice", false));
						regInfoResp.setSqNo(XMLUtil.getInt(element, "SqNo", false));
						regInfoResp.setRemark(XMLUtil.getString(element, "Remark", false));
						// his状态 1正常，2退号，3停诊，4替诊 
						int regFlag = XMLUtil.getInt(element, "RegFlag", false);
						
						//判断HIS订单状态，后续逻辑判断公共部分会 针对his订单更新本地业务订单状态
						HisRegFlag flage = null;
						switch (regFlag) {
						case 1:	
							flage = HisRegFlag.state_1;
							break;
						case 2:	
							flage = HisRegFlag.state_2;
							break;
						case 3:	
							flage = HisRegFlag.state_3;
							break;
						case 4:	
							flage = HisRegFlag.state_4;
							break;
						default:
							break;
						}
						regInfoResp.setRegFlag(flage.getState());
						regInfoResp.setLastModify(XMLUtil.getString(element, "LastModify", false));
						regInfoResp.setCommendTime(XMLUtil.getString(element, "CommendTime", false));
						regInfoResp.setTakeNoDesc(XMLUtil.getString(element, "TakeNoDesc", false));
						regInfoResp.setTakeNoPalce(XMLUtil.getString(element, "TakeNoPalce", false));
						lists.add(regInfoResp);
					}
				}
			}
			return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, lists);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisHosNoRecharge> hosNoRecharge(InterfaceMessage msg, String orderId,Map<String, String> paramMap)  throws Exception{
		String reqParam = "<Req><TransactionCode></TransactionCode><Data>"
				+ "<OrderId>{orderId}</OrderId><PayWay>{payWay}</PayWay><RechargeMoney>{rechargeMoney}</RechargeMoney>"
				+ "<TransNo>{transNo}</TransNo><HospitalNo>{hospitalNo}</HospitalNo></Data></Req>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		String result = call(null,ApiModule.His.hosNoRecharge , getHisUrl(),"HosNoRecharge", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
		HisHosNoRecharge hisHosNoRechargeResp = new HisHosNoRecharge();
		if (!StringUtil.isBlank(result)) {
			Document doc = XMLUtil.parseXml(result);
			Element rootEl = doc.getRootElement();
			String respCode = XMLUtil.getString(rootEl, "RespCode", true);
			String respMessage = XMLUtil.getString(rootEl, "RespMessage", false);
			Element el = rootEl.element(KstHosConstant.DATA);
			if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
				hisHosNoRechargeResp.setBalance(XMLUtil.getString(el, "Balance", true));
				hisHosNoRechargeResp.setHisDate(XMLUtil.getString(el, "HisDate", true));
				hisHosNoRechargeResp.setHisFlowNo(XMLUtil.getString(el, "HisFlowNo", true));
				hisHosNoRechargeResp.setOrderId(XMLUtil.getString(el, "OrderId", true));
				return new CommonResp<HisHosNoRecharge>(HisCallReq.get(msg,paramMap),RetCode.Success.RET_10000, hisHosNoRechargeResp);
			} else {
				return new CommonResp<HisHosNoRecharge>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMessage);
			}
		} else {
			return new CommonResp<HisHosNoRecharge>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
		}
	}

	@Override
	public CommonResp<HisOPDRecharge> oPDRecharge(InterfaceMessage msg, String orderId,Map<String, String> paramMap) throws Exception {
		String reqParam = "<Req><TransactionCode></TransactionCode><Data><TransNo>{transNo}</TransNo>"
				+ "<OrderId>{orderId}</OrderId><OperatorId>{operatorId}</OperatorId><HisID>{hisID}</HisID>"
				+ "<PayWay>{payWay}</PayWay><TransTime>{transTime}</TransTime><OperatorName>{operatorName}</OperatorName>"
				+ " <ChannelId>{channelId}</ChannelId><Remark>{remark}</Remark>"
				+ "<Price>{price}</Price><CardType>{cardType}</CardType><CardNo>{cardNo}</CardNo></Data></Req>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		// 调用his接口
		String result = call(null,ApiModule.His.oPDRecharge , getHisUrl(),"OPDRecharge", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
		HisOPDRecharge hisOPDRechargeResp = new HisOPDRecharge();
		if (!StringUtil.isBlank(result)) {
			Document doc = XMLUtil.parseXml(result);
			Element rootEl = doc.getRootElement();
			String respCode = XMLUtil.getString(rootEl, "RespCode", true);
			String respMessage = XMLUtil.getString(rootEl, "RespMessage", false);
			Element el = rootEl.element(KstHosConstant.DATA);
			if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
				hisOPDRechargeResp.setBalance(XMLUtil.getString(el, "Balance", true));
				hisOPDRechargeResp.setHisDate(XMLUtil.getString(el, "HisDate", true));
				hisOPDRechargeResp.setHisFlowNo(XMLUtil.getString(el, "HisFlowNo", true));
				hisOPDRechargeResp.setOrderId(XMLUtil.getString(el, "OrderId", true));
				hisOPDRechargeResp.setPrice(XMLUtil.getString(el, "Price", true));
				return new CommonResp<HisOPDRecharge>(HisCallReq.get(msg,paramMap),RetCode.Success.RET_10000, hisOPDRechargeResp);
			} else {
				return new CommonResp<HisOPDRecharge>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMessage);
			}
		}  else {
			return new CommonResp<HisOPDRecharge>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
		}
	}

	@Override
	public CommonResp<HisQueryInHospitalCostList> queryInHospitalCostList(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><Page><PIndex>{pIndex}</PIndex>"
					+ "<PSize>{pSize}</PSize></Page><CardNo>{hospitalNo}</CardNo><CardType>14</CardType>"
					+ "<BeginDate>{beginDate}</BeginDate><EndDate>{endDate}</EndDate></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
//			String result = call(null,ApiModule.His.queryInHospitalCostList , getHisUrl(),"QueryInHospitalCostListByDate", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryInHospitalCostList hisResp = null;
			List<HisQueryInHospitalCostList> lists = new ArrayList<HisQueryInHospitalCostList>();
			
			
			//默认写死返回
			hisResp = new HisQueryInHospitalCostList();
			hisResp.setFee(10000);
			hisResp.setDate(DateOper.getNow("yyyy-MM-dd"));
			hisResp.setDept("测试科室");
			hisResp.setDoctor("测试医生");
			hisResp.setDeptStation("测试科室");
			lists.add(hisResp);
			hisResp = new HisQueryInHospitalCostList();
			hisResp.setFee(12000);
			hisResp.setDate("2018-11-02");
			hisResp.setDept("测试科室2");
			hisResp.setDoctor("测试医生2");
			hisResp.setDeptStation("测试科室2");
			lists.add(hisResp);
			CommonResp<HisQueryInHospitalCostList> commResp =
					new CommonResp<HisQueryInHospitalCostList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
			commResp.setPCount(1l);
			return commResp;
			
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				String respMsg = XMLUtil.getString(rootEl, "RespMsg", false);
//				Long pageCount = new Long(0);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					List<Element> el = rootEl.elements(KstHosConstant.DATA);
//					Element pageEl = rootEl.element(KstHosConstant.PAGE);
//					pageCount = new Long(XMLUtil.getString(pageEl, "PCount", false));
//					String costListData = XMLUtil.getString(el.get(0), "CostListDate", false);
//					if (costListData != null && costListData != "") {
//						for (int i = 0; i < el.size(); i++) {
//							hisResp = new HisQueryInHospitalCostList();
//							Element element = el.get(i);
//							hisResp.setFee(XMLUtil.getInt(element, "Amount", false));
//							hisResp.setDate(XMLUtil.getString(element, "CostListDate", false));
//							hisResp.setDept(XMLUtil.getString(element, "Dept", false));
//							hisResp.setDoctor(XMLUtil.getString(element, "Doctor", false));
//							hisResp.setDeptStation(XMLUtil.getString(element, "DeptStation", false));
//							lists.add(hisResp);
//						}
//					}
//					CommonResp<HisQueryInHospitalCostList> commResp =
//							new CommonResp<HisQueryInHospitalCostList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
//					commResp.setPCount(pageCount);
//					return commResp;
//				}else {
//					return new CommonResp<HisQueryInHospitalCostList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
//				}
//			}else {
//				return new CommonResp<HisQueryInHospitalCostList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
//			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryInHospitalCostType> queryInHospitalCostType(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			//查询住院日清单分类
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><Page><PIndex>{pIndex}</PIndex><PSize>{pSize}</PSize></Page><CardNo>{hospitalNo}</CardNo><CardType></CardType><BeginDate>{beginDate}</BeginDate><EndDate>{endDate}</EndDate></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
//			
			
			List<HisQueryInHospitalCostType> lists = new ArrayList<HisQueryInHospitalCostType>();
			HisQueryInHospitalCostType hisResp = new HisQueryInHospitalCostType();
			/*
			
			@NotBlank(message="日期[date]不能为空", groups = {AddGroup.class})
			private String date;
			@CheckCurrency(message="金额[fee]不能为空", groups = {AddGroup.class})
			private Integer fee;
			private String doctor;
			private String dept;
			private String deptStation;
			@NotBlank(message="费用类型代码[expenseTypeCode]不能为空", groups = {AddGroup.class})
			private String expenseTypeCode;
			@NotBlank(message="费用类型名称[expenseTypeName]不能为空", groups = {AddGroup.class})
			private String expenseTypeName;
			单位
			private String unit;
			/*单价
			private Integer unitPrice;
			/*数量
			private Integer objNumber;
			/*发票项目名称
			private String invoiceItemName;
			/**规格
			private String specifications;
			 */
			//费用名称： 
			hisResp.setExpenseTypeName("手术费");
			//费用代码
			hisResp.setExpenseTypeCode("SS00123");
			//扣费日期	
			hisResp.setDate(DateOper.getNow("yyyy-MM-dd"));
			//费用金额 单位（分）
			hisResp.setFee(10000);
			hisResp.setDept("心血管专科");
			hisResp.setDoctor("潘主任");
			hisResp.setDeptStation("科室位置");
			
			lists.add(hisResp);
			CommonResp<HisQueryInHospitalCostType> commResp = 
					new CommonResp<HisQueryInHospitalCostType>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
			commResp.setPCount(1l);
			return commResp;
			
			// 调用his接口
//			String result = call(null,ApiModule.His.queryInHospitalCostType , getHisUrl(),"QueryInHospitalCostType", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			List<HisQueryInHospitalCostType> lists = new ArrayList<HisQueryInHospitalCostType>();
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				String respMsg = XMLUtil.getString(rootEl, "RespMsg", false);
//				Long pageCount = new Long(0);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					Element pageEl = rootEl.element(KstHosConstant.PAGE);
//					pageCount = new Long(XMLUtil.getString(pageEl, "PCount", false));
//					List<Element> el = rootEl.elements(KstHosConstant.DATA);
//					if (!CollectionUtils.isEmpty(el)) {
//						for (int i = 0; i < el.size(); i++) {
//							Element element = el.get(i);
//							if( CollectionUtils.isEmpty(element.content())) {
//								//空节点
//								continue;
//							}
//							HisQueryInHospitalCostType hisResp = new HisQueryInHospitalCostType();
//							hisResp.setExpenseTypeName(XMLUtil.getString(element, "ExpenseTypeName", false));
//							hisResp.setExpenseTypeCode(XMLUtil.getString(element, "ExpenseTypeCode", false));
//							hisResp.setDate(XMLUtil.getString(element, "Date", false));
//							hisResp.setFee(XMLUtil.getInt(element, "Fee", false));
//							hisResp.setDept(XMLUtil.getString(element, "Dept", false));
//							hisResp.setDoctor(XMLUtil.getString(element, "Doctor", false));
//							hisResp.setDeptStation(XMLUtil.getString(element, "DeptStation", false));
//							lists.add(hisResp);
//						}
//					}
//					CommonResp<HisQueryInHospitalCostType> commResp =
//							new CommonResp<HisQueryInHospitalCostType>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
//					commResp.setPCount(pageCount);
//					return commResp;
//				}else {
//					return new CommonResp<HisQueryInHospitalCostType>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
//				}
//			}else {
//				return new CommonResp<HisQueryInHospitalCostType>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
//			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
//			return new CommonResp<HisQueryInHospitalCostType>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
		}
	}
	
	@Override
	public CommonResp<CommonPrescriptionItem> queryInHospitalCostTypeItem(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><Page><PIndex>{pIndex}</PIndex>"
					+ "<PSize>10</PSize></Page><CardNo>{hospitalNo}</CardNo><CardType>14</CardType>"
					+ "<BeginDate>{date}</BeginDate><EndDate>{date}</EndDate></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			
			List<CommonPrescriptionItem> lists = new ArrayList<CommonPrescriptionItem>();
			CommonPrescriptionItem hisResp = new CommonPrescriptionItem();
			hisResp.setUnit("只");
			hisResp.setUnitPrice(12000);//单价
			lists.add(hisResp);
			return new CommonResp<CommonPrescriptionItem>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
//			// 调用his接口
//			String result = call(null,ApiModule.His.queryInHospitalCostTypeItem , getHisUrl(),"QueryInHospitalCostListDetail", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			List<CommonPrescriptionItem> lists = new ArrayList<CommonPrescriptionItem>();
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				String respMsg = XMLUtil.getString(rootEl, "RespMsg", false);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					List<Element> el = rootEl.elements(KstHosConstant.DATA);
//					if (!CollectionUtils.isEmpty(el)) {
//						for (int i = 0; i < el.size(); i++) {
//							CommonPrescriptionItem hisResp = new CommonPrescriptionItem();
//							Element element = el.get(i);
//							hisResp.setNumber(XMLUtil.getString(element, "Num", false));
//							hisResp.setProject(XMLUtil.getString(element, "ProjName", false));
//							hisResp.setSpecifications(XMLUtil.getString(element, "Specification", false));
//							hisResp.setSumOfMoney(XMLUtil.getInt(element, "TotalAmt", false));
//							hisResp.setUnitPrice(XMLUtil.getInt(element, "UnitAmt", false));
//							lists.add(hisResp);
//						}
//					}
//					return new CommonResp<CommonPrescriptionItem>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,lists);
//				}else {
//					return new CommonResp<CommonPrescriptionItem>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
//				}
//			}else {
//				return new CommonResp<CommonPrescriptionItem>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
//			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
//			return new CommonResp<CommonPrescriptionItem>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
		}
	}

	@Override
	public CommonResp<HisQueryInHospitalRechargeList> queryInHospitalRechargeList(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex><MemberName>{memberName}</MemberName>"
					+ "<CardNo>{hospitalNo}</CardNo><MinTime>{beginDate}</MinTime><MaxTime>{endDate}</MaxTime><PayWay>{chargeType}</PayWay><State>{orderType}</State><SerialNo>{serialNo}</SerialNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);

			// 调用his接口
			String result = call(null,ApiModule.His.queryInHospitalRechargeList , getHisUrl(),"QueryHospitalNoPamentRecords", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			List<HisQueryInHospitalRechargeList> listResp = new ArrayList<HisQueryInHospitalRechargeList>();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				Long pageCount = new Long(XMLUtil.getInt(rootEl, "PCount", false));
				String respMsg = XMLUtil.getString(rootEl, "RespMsg", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					if (StringUtil.isNotBlank(el)) {
						for (int i = 0; i < el.size(); i++) {
							HisQueryInHospitalRechargeList resp = new HisQueryInHospitalRechargeList();
							Element element = el.get(i);
							resp.setChargeType(XMLUtil.getInt(element, "PayWay", true));
							resp.setPayMoney(XMLUtil.getInt(element, "Price", true));
							resp.setTotalMoney(XMLUtil.getInt(element, "Price", true));
							resp.setChannelId(null);//微信，或者支付宝渠道
							resp.setHisOrderId(null);//his的充值订单号
							resp.setHisOrderState(null);//his的订单状态
							resp.setHisRechargeDate(XMLUtil.getString(element, "RechargeTime", true));
							resp.setIsAllowRefund(null);//是否允许退费
							resp.setMerchOrderNo(XMLUtil.getString(element, "SerialNo", true));
							resp.setOrderId(null);//如果是全流程充值的，应该有全流程的订单号
							resp.setOrderMemo(XMLUtil.getString(element, "PriceName", false));
							resp.setOrderType(null);//订单类型，充值/退费
							resp.setRefundMoney(null);//退费金额
							resp.setRefundOrderId(null);//全流程退费订单号ID
							listResp.add(resp);
						}
					}
					CommonResp<HisQueryInHospitalRechargeList> commResp = new CommonResp<>(msg,map,RetCode.Success.RET_10000, listResp);
					commResp.setPCount(pageCount);
					//commResp.setPCount( new Long(12));
					return commResp;
				}else {
					return new CommonResp<HisQueryInHospitalRechargeList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryInHospitalRechargeList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryOutpatientRechargeList> queryHosOutpatientRecords(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><Page><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex></Page><Name>{name}</Name>"
					+ "<CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><MinTime>{beginDate}</MinTime><MaxTime>{endDate}</MaxTime><PayWay>{chargeType}</PayWay><State>{orderType}</State><SerialNo>{serialNo}</SerialNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
			String result = call(null,ApiModule.His.queryHosOutpatientRecords , getHisUrl(),"QueryHosOutpatientRecords", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			List<HisQueryOutpatientRechargeList> listResp = new ArrayList<HisQueryOutpatientRechargeList>();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				Long pageCount = new Long(XMLUtil.getInt(rootEl, "PCount", false));
				String respMsg = XMLUtil.getString(rootEl, "RespMsg", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					if (StringUtil.isNotBlank(el)) {
						for (int i = 0; i < el.size(); i++) {
							HisQueryOutpatientRechargeList resp = new HisQueryOutpatientRechargeList();
							Element element = el.get(i);
							resp.setChargeType(XMLUtil.getInt(element, "PayWay", true));
							resp.setPayMoney(XMLUtil.getInt(element, "Price", true));
							resp.setTotalMoney(XMLUtil.getInt(element, "Price", true));
							resp.setChannelId(null);//微信，或者支付宝渠道
							resp.setHisOrderId(null);//his的充值订单号
							resp.setHisOrderState(null);//his的订单状态
							resp.setHisRechargeDate(XMLUtil.getString(element, "RechargeTime", true));
							resp.setIsAllowRefund(null);//是否允许退费
							resp.setMerchOrderNo(XMLUtil.getString(element, "SerialNo", true));
							resp.setOrderId(null);//如果是全流程充值的，应该有全流程的订单号
							resp.setOrderMemo(XMLUtil.getString(element, "PriceName", false));
							resp.setOrderType(null);//订单类型，充值/退费
							resp.setRefundMoney(null);//退费金额
							resp.setRefundOrderId(null);//全流程退费订单号ID
							listResp.add(resp);
						}
					}
					CommonResp<HisQueryOutpatientRechargeList> commResp = new CommonResp<HisQueryOutpatientRechargeList>(msg,map,RetCode.Success.RET_10000, listResp);
					commResp.setPCount(pageCount);
					//commResp.setPCount( new Long(12));
					return commResp;
				}else {
					return new CommonResp<HisQueryOutpatientRechargeList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryOutpatientRechargeList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryOrderPrescriptionList> queryOrderPrescriptionList(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex>"
					+ "<ServiceId>{serviceId}</ServiceId><CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><OrderState>{orderState}</OrderState><BeginDate>{beginDate}</BeginDate><EndDate>{endDate}</EndDate></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
			String result = call(null,ApiModule.His.queryOrderPrescriptionList , getHisUrl(),"OrderList", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			List<HisQueryOrderPrescriptionList> listResp = new ArrayList<HisQueryOrderPrescriptionList>();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				Long pageCount = new Long(XMLUtil.getInt(rootEl, "PCount", false));
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					if (null != el) {
						for (int i = 0; i < el.size(); i++) {
							HisQueryOrderPrescriptionList resp = new HisQueryOrderPrescriptionList();
							Element element = el.get(i);
							resp.setCardNo(XMLUtil.getString(element, "CardNo", true));
							resp.setCardType(XMLUtil.getInt(element, "CardType", true));
							resp.setOrderTime(XMLUtil.getString(element, "Begin", true));
							resp.setPrescNo(XMLUtil.getString(element, "PrescNo", true));
							resp.setPayMoney(XMLUtil.getInt(element, "PayMoney", true));
							resp.setTotalMoney(XMLUtil.getInt(element, "TotalMoney", true));
							resp.setOrderMemo(XMLUtil.getString(element, "OrderMemo", true));
							resp.setIfOnlinePay(XMLUtil.getInt(element, "IfOnlinePay", true));
							resp.setOrderState(XMLUtil.getInt(element, "OrderState", true));
							resp.setDoctorCode(XMLUtil.getString(element, "DoctorCode", true));
							resp.setDoctorName(XMLUtil.getString(element, "DoctorName", true));
							resp.setDeptCode(XMLUtil.getString(element, "DeptCode", true));
							resp.setDeptName(XMLUtil.getString(element, "DeptName", true));
							resp.setHisOrderId(XMLUtil.getString(element, "OrderId", true));
							resp.setServiceId(XMLUtil.getString(element, "ServiceId", true));
							listResp.add(resp);
						}
					}
					CommonResp<HisQueryOrderPrescriptionList> commResp =
							new CommonResp<HisQueryOrderPrescriptionList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,listResp);
					commResp.setPCount(pageCount);
					return commResp;
				}else {
					return new CommonResp<HisQueryOrderPrescriptionList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryOrderPrescriptionList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<HisQueryOrderPrescriptionList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
		
	}

	@Override
	public CommonResp<HisQueryOrderPrescriptionInfo> queryOrderPrescriptionInfo(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			HisQueryOrderPrescriptionInfo prescInfo = new HisQueryOrderPrescriptionInfo();
			
			prescInfo.setCardNo("C123456");
			prescInfo.setCardType(1);
			prescInfo.setDeptCode("DeptCode001");
			prescInfo.setDeptName("测试科室1");
			prescInfo.setDoctorCode("TestDoc001");
			prescInfo.setDoctorName("测试医生1");
			prescInfo.setHisOrderId("HIS123456789");
			prescInfo.setIfOnlinePay(1);//订单模式的处方，默认在线支付。
			prescInfo.setMemberName("测试病人");
			prescInfo.setOrderMemo("测试订单描述");
			prescInfo.setOrderState(0);
			prescInfo.setOrderTime("2019-01-20");
			prescInfo.setPayMoney(10);
			prescInfo.setPrescNo("PrescNo"+IDSeed.next());
			prescInfo.setPriceName("测试处方单1");
			prescInfo.setServiceId("008");
			prescInfo.setTotalMoney(10);
			List<CommonPrescriptionItem> itemList = new ArrayList<CommonPrescriptionItem>();
			for (int i = 0; i < 4; i++) {
				CommonPrescriptionItem item = new CommonPrescriptionItem();
				item.setNumber(i+"");
				item.setProject("测试项目");
				item.setSpecifications("瓶");
				item.setSumOfMoney(100);
				item.setUnit("毫升");
				item.setUnitPrice(100);
				itemList.add(item);
			}
			prescInfo.setData_1(itemList);
			return new CommonResp<HisQueryOrderPrescriptionInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,prescInfo);
			
//			String reqParam = "<Req><TransactionCode></TransactionCode><Data><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex>"
//					+ "<ServiceId>{serviceId}</ServiceId><OrderId>{hisOrderId}</OrderId><CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><OrderState>{orderState}</OrderState><BeginDate>{beginDate}</BeginDate><EndDate>{endDate}</EndDate></Data></Req>";
//			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
//			String result = call(null,ApiModule.His.queryOrderPrescriptionInfo , getHisUrl(),"OrderList", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			String itemResp = call(null,ApiModule.His.queryOrderPrescriptionInfoItems , getHisUrl(),"OrderDetail", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//			HisQueryOrderPrescriptionInfo prescInfo = new HisQueryOrderPrescriptionInfo();
//			if (!StringUtil.isBlank(result)) {
//				Document doc = XMLUtil.parseXml(result);
//				Element rootEl = doc.getRootElement();
//				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
//				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
//				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//					Element element = rootEl.element(KstHosConstant.DATA);
//					if (StringUtil.isNotBlank(element)) {
//						prescInfo.setCardNo(XMLUtil.getString(element, "CardNo", true));
//						prescInfo.setCardType(XMLUtil.getInt(element, "CardType", true));
//						prescInfo.setDeptCode(XMLUtil.getString(element, "DeptCode", true));
//						prescInfo.setDeptName(XMLUtil.getString(element, "DeptName", true));
//						prescInfo.setDoctorCode(XMLUtil.getString(element, "DoctorCode", true));
//						prescInfo.setDoctorName(XMLUtil.getString(element, "DoctorName", true));
//						prescInfo.setHisOrderId(XMLUtil.getString(element, "OrderId", true));
//						prescInfo.setIfOnlinePay(1);//订单模式的处方，默认在线支付。
//						prescInfo.setMemberName(XMLUtil.getString(element, "Member", true));
//						prescInfo.setOrderMemo(XMLUtil.getString(element, "OrderMemo", true));
//						prescInfo.setOrderState(XMLUtil.getInt(element, "OrderState", true));
//						prescInfo.setOrderTime(XMLUtil.getString(element, "Begin", true));
//						prescInfo.setPayMoney(XMLUtil.getInt(element, "PayMoney", true));
//						prescInfo.setPrescNo(XMLUtil.getString(element, "PrescNo", true));
//						prescInfo.setPriceName(XMLUtil.getString(element, "PriceName", true));
//						prescInfo.setServiceId(XMLUtil.getString(element, "ServiceId", true));
//						prescInfo.setTotalMoney(XMLUtil.getInt(element, "TotalMoney", true));
//						List<CommonPrescriptionItem> itemList = new ArrayList<CommonPrescriptionItem>();
//						if (!StringUtil.isBlank(itemResp)) {
//							Document itemDoc = XMLUtil.parseXml(itemResp);
//							Element itemRootEl = itemDoc.getRootElement();
//							if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
//								List<Element> el = itemRootEl.elements(KstHosConstant.DATA);
//								if (StringUtil.isNotBlank(el)) {
//									for (int i = 0; i < el.size(); i++) {
//										Element itemEl = el.get(i);
//										CommonPrescriptionItem item = new CommonPrescriptionItem();
//										item.setUnitPrice(XMLUtil.getInt(itemEl, "ItemPrice", true));
//										item.setProject(XMLUtil.getString(itemEl, "ItemName", true));
//										item.setSpecifications(XMLUtil.getString(itemEl, "Unit", true));
//										item.setSumOfMoney(XMLUtil.getInt(itemEl, "Money", true));
//										item.setNumber(XMLUtil.getString(itemEl, "Account", true));
//										itemList.add(item);
//									}
//								}
//							}
//						}
//						prescInfo.setData_1(itemList);
//					}
//					return new CommonResp<HisQueryOrderPrescriptionInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,prescInfo);
//				}else {
//					return new CommonResp<HisQueryOrderPrescriptionInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
//				}
//			}else {
//				return new CommonResp<HisQueryOrderPrescriptionInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
//			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<HisQueryOrderPrescriptionInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}
	
	/**
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public CommonResp<HisQueryOrderSettlementList> queryOrderSettlementList(InterfaceMessage msg,
			Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex>"
					+ "<CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><IsSettlement>{isSettlement}</IsSettlement><BeginDate>{beginDate}</BeginDate><EndDate>{endDate}</EndDate></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
			String result = call(null,ApiModule.His.queryOrderSettlementList , getHisUrl(),"queryYizhuList",reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			List<HisQueryOrderSettlementList> listResp = new ArrayList<HisQueryOrderSettlementList>();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				Long pageCount = new Long(XMLUtil.getInt(rootEl, "PCount", false));
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> el = rootEl.elements(KstHosConstant.DATA);
					if (!CollectionUtils.isEmpty(el)) {
						for (int i = 0; i < el.size(); i++) {
							HisQueryOrderSettlementList resp = new HisQueryOrderSettlementList();
							Element element = el.get(i);
							resp.setDeptName(XMLUtil.getString(element, "DeptName", false,"演示科室"));
							resp.setDoctorName(XMLUtil.getString(element, "Doctor", false));
							resp.setHisOrderId(XMLUtil.getString(element, "OrderId", true));
							resp.setIsSettlement(XMLUtil.getInt(element, "IsSettlement", true));
							resp.setPrescNo(XMLUtil.getString(element, "PrescriptionNumber", true));
							resp.setPrescTime(XMLUtil.getString(element, "OrderTime", true));
							resp.setPrescType(XMLUtil.getString(element, "Prescription", true));
							resp.setPrice(XMLUtil.getInt(element, "Price", true));
							listResp.add(resp);
						}
					}
					CommonResp<HisQueryOrderSettlementList> commResp =
							new CommonResp<HisQueryOrderSettlementList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,listResp);
					commResp.setPCount(pageCount);
					return commResp;
				}else {
					return new CommonResp<HisQueryOrderSettlementList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryOrderSettlementList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<HisQueryOrderSettlementList>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	/**
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@Override
	public CommonResp<HisQueryOrderSettlementInfo> queryOrderSettlementInfo(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><PSize>{pSize}</PSize><PIndex>{pIndex}</PIndex>"
					+ "<OrderId>{orderId}</OrderId><PrescNo>{prescNo}</PrescNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			// 调用his接口
			String infoResp = call(null,ApiModule.His.queryOrderSettlementInfo , getHisUrl(),"queryOrderDetail2",reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			String itemResp = call(null,ApiModule.His.queryOrderSettlementInfo , getHisUrl(),"queryYizhuDetail",reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisQueryOrderSettlementInfo info = new HisQueryOrderSettlementInfo();
			if (!StringUtil.isBlank(infoResp)) {
				Document doc = XMLUtil.parseXml(infoResp);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					Element element = rootEl.element(KstHosConstant.DATA);
					if (StringUtil.isNotBlank(element)) {
						info.setCardNo(XMLUtil.getString(element, "CardNo", true));
						info.setCardType(XMLUtil.getInt(element, "CardType", true));
						info.setDeptName("演示科室");
						info.setDoctorName(XMLUtil.getString(element, "Doctor", true));
						info.setHisOrderId(XMLUtil.getString(element, "OrderId", true));
						info.setIsSettlement(XMLUtil.getInt(element, "IsSettlement", true));
						info.setMemberName(XMLUtil.getString(element, "MemberName", true));
						info.setPrescNo(XMLUtil.getString(element, "PrescriptionNumber", true));
						info.setPrescTime(XMLUtil.getString(element, "OrderTime", true));
						info.setPrescType(XMLUtil.getString(element, "Prescription", true));
						info.setPrice(XMLUtil.getInt(element, "Price", true));
						List<CommonPrescriptionItem> itemList = new ArrayList<CommonPrescriptionItem>();
						if (!StringUtil.isBlank(itemResp)) {
							Document itemDoc = XMLUtil.parseXml(itemResp);
							Element itemRootEl = itemDoc.getRootElement();
							if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
								List<Element> el = itemRootEl.elements(KstHosConstant.DATA);
								if (StringUtil.isNotBlank(el)) {
									for (int i = 0; i < el.size(); i++) {
										Element itemEl = el.get(i);
										CommonPrescriptionItem item = new CommonPrescriptionItem();
										item.setUnitPrice(XMLUtil.getInt(itemEl, "UnitPrice", true));
										item.setProject(XMLUtil.getString(itemEl, "Project", true));
										item.setSpecifications(XMLUtil.getString(itemEl, "Specifications", true));
										item.setSumOfMoney(XMLUtil.getInt(itemEl, "SumOfMoney", true));
										item.setNumber(XMLUtil.getString(itemEl, "Number", true));
										itemList.add(item);
									}
								}
							}
						}
						info.setData_1(itemList);
					}
					return new CommonResp<HisQueryOrderSettlementInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,info);
				}else {
					return new CommonResp<HisQueryOrderSettlementInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryOrderSettlementInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<HisQueryOrderSettlementInfo>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}
	
	@Override
	public CommonResp<RespMap> settleOrderSettlement(InterfaceMessage msg,Map<String, String> paramMap) throws Exception{
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data>"
					+ "<CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><Price>{price}</Price><HisOrderIds>{hisOrderIds}</HisOrderIds>"
					+ "<PrescNos>{prescNos}</PrescNos></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			// 调用his接口
			String result = call(null,ApiModule.His.settleOrderSettlement , getHisUrl(),"yizhuPay",reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
				}else {
					return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<RespMap> payOrderPrescription(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data>"
					+ "<OrderId>{hisOrderId}</OrderId><TransNo>{transNo}</TransNo><Price>{price}</Price><TransTime>{transTime}</TransTime><ChannelId>{channelId}</ChannelId></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.payOrderPrescription , getHisUrl(),"UpdateOrder", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				RespMap respMap = new RespMap();
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					Element element = rootEl.element(KstHosConstant.DATA);
					if (StringUtil.isNotBlank(element)) {
						respMap.put(ApiKey.HisUpdateOrder.HisOrderId, XMLUtil.getString(element, "OrderId", true));
						respMap.put(ApiKey.HisUpdateOrder.HisFlowNo, XMLUtil.getString(element, "HisFlowNo", true));
						respMap.put(ApiKey.HisUpdateOrder.HisDate, XMLUtil.getString(element, "HisDate", true));
						respMap.put(ApiKey.HisUpdateOrder.PharmacyPosition, XMLUtil.getString(element, "PharmacyPosition", false));
						respMap.put(ApiKey.HisUpdateOrder.ExamineRoomPosition, XMLUtil.getString(element, "ExamineRoomPosition", false));
						respMap.put(ApiKey.HisUpdateOrder.PharmacyWaitings, XMLUtil.getString(element, "PharmacyWaitings", false));
						respMap.put(ApiKey.HisUpdateOrder.ExamineRoomWaitings, XMLUtil.getString(element, "ExamineRoomWaitings", false));
					}
					return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
				}else {
					return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
//			return new CommonResp<RespMap>(HisCallReq.get(msg,map),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}	

	@Override
	public CommonResp<HisGetQueueInfo> queryQueue(InterfaceMessage msg, Map<String, String> map) throws AbsHosException {
		// cardNo 就诊卡号 doctorCode 医生id
				String reqParam = "<Req><TransactionCode></TransactionCode><Data>"
						+ "<CardNo>{cardNo}</CardNo><CardType>{cardType}</CardType><PatientName>{patientName}</PatientName>"
						+ "<QueryId>{queryId}</QueryId></Data></Req>";
				reqParam = CommonUtil.formateReqParam(reqParam, map);
				String result = call(null,ApiModule.His.queryQueue , getHisUrl(),"GetQueue", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
				try {
					Document doc = XMLUtil.parseXml(result);
					Element root = doc.getRootElement();
					String respCode = XMLUtil.getString(root, "RespCode", true);
					String respMessage = XMLUtil.getString(root, "RespMessage", true);
					if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
						List<Element> datas = root.elements(KstHosConstant.DATA);
						if (StringUtil.isNotBlank(datas)) {
							List<HisGetQueueInfo> queues = new ArrayList<>();
							for (Element data : datas) {
								HisGetQueueInfo queue = new HisGetQueueInfo();
								queue.setDoctorName(XMLUtil.getString(data, "DoctorName", false));
								queue.setNo(XMLUtil.getString(data, "OrderNum", false));
								queue.setDeptName(XMLUtil.getString(data, "DeptName", false));
								queue.setPatientName(XMLUtil.getString(data, "Name", false));
								queue.setCardType(XMLUtil.getString(data, "CardType", false));
								queue.setCardNo(XMLUtil.getString(data, "CardNo", false));
								queue.setNextNo(XMLUtil.getString(data, "nextNo", false));
								queue.setMaxNo(XMLUtil.getString(data, "NowOrderNum", false));
								queue.setQueryId(XMLUtil.getString(data, "QueryId", false));
								queue.setPhoneNo(XMLUtil.getString(data, "MobilePhone", false));
								queue.setLocation(XMLUtil.getString(data, "Location", false));

								try {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									queue.setRegisterDate(sdf.format(sdf.parse(XMLUtil.getString(data, "EffectTime", false))));
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								queue.setTimeId(XMLUtil.getString(data, "Timeid", false));
								try {
									String queryId = XMLUtil.getString(data, "QueryId", false);
									String cardNo = XMLUtil.getString(data, "CardNo", false);
									ReqQueryLocalQueue req = new ReqQueryLocalQueue(msg, queryId, cardNo, null);
									CommonResp<RespQueryLocalQueue> localQueueResp = queueService.queryLocalQueue(new CommonReq<ReqQueryLocalQueue>(req));
									if(localQueueResp!=null && KstHosConstant.SUCCESSCODE.equals(localQueueResp.getCode())&& localQueueResp.getData().size()>0) {
										RespQueryLocalQueue resp = localQueueResp.getData().get(0);
										if(resp!=null) {
											queue.setIfMsg(resp.getIfMsg());
											queue.setReMindNo(resp.getReMindNo());
										}
									}
								} catch (Exception e) {
									e.printStackTrace();LogUtil.error(log, e);
								}
								queues.add(queue);
							}

							return new CommonResp<HisGetQueueInfo>(msg,map,RetCode.Success.RET_10000,queues);
						}
					}
					return new CommonResp<HisGetQueueInfo>(HisCallReq.get(msg,map),
							KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMessage);
				} catch (AbsHosException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();LogUtil.error(log, e);
					throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
				}
	}

	@Override
	public int queryReportCount(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><CardType>{cardType}</CardType><PatientName>{patientName}</PatientName><IdCardNo>{idCardNo}</IdCardNo><CardNo>{cardNo}</CardNo><ReportType>{reportType}</ReportType></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.queryReportCount , getHisUrl(),"QueryReportCount", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				List<Element> list = rootEl.elements(KstHosConstant.DATA);
				Element data = list.get(0);
				int count = XMLUtil.getInt(data, "Count", true);
				if (StringUtil.isNotBlank(count)) {
					return count;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
		return 0;
	}

	@Override
	public CommonResp<HisReportItems> queryReportList(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><Page><Pindex>{pindex}</Pindex><PSize>{pSize}</PSize><PCount>{pCount}</PCount></Page><ReportType>{reportType}</ReportType><CardType>{cardType}</CardType><PatientName>{patientName}</PatientName><IdCardNo>{idCardNo}</IdCardNo><CardNo>{cardNo}</CardNo></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.queryCheckReportList , getHisUrl(),"QueryReportsByPageAndName", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			List<HisReportItems> reportList = new ArrayList<>();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> list = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < list.size(); i++) {
						Element element = list.get(i);
						HisReportItems schedule = new HisReportItems();
						schedule.setClinicCard(XMLUtil.getString(element, "CardNo", true));
						schedule.setIdCardNo(XMLUtil.getString(element, "IdCardNo", true));
						schedule.setItemName(XMLUtil.getString(element, "ItemName", true));
						// schedule.setMobile(XMLUtil.getString(element, "Mobile",
						// true));
						schedule.setSubmissionTime(XMLUtil.getString(element, "SubmissionTime", false));
						if (StringUtil.isNotBlank(schedule.getSubmissionTime())) {
							try {
								schedule.setSubmissionTime(DateOper.formatDate(schedule.getSubmissionTime(),
										"yyyy-MM-dd HH:mm:ss.0", "yyyy-MM-dd HH:mm:ss"));
							} catch (ParseException e) {
								// 格式化异常按照获得的值输出
								e.printStackTrace();LogUtil.error(log, e);
							}
						}
						schedule.setPatientName(XMLUtil.getString(element, "PatientName", true));
						schedule.setState(XMLUtil.getString(element, "State", true));
						schedule.setReportId(XMLUtil.getString(element, "ReportId", true));
						schedule.setReportType(XMLUtil.getString(element, "ReportType", true));
						// schedule.setSubmissionTime(XMLUtil.getString(element,
						// "DeptCode", true));

						reportList.add(schedule);
					}
				}
			}

			return new CommonResp<>(msg,map,RetCode.Success.RET_10000,reportList);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
//			return null;
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}
	
	
	@Override
	public CommonResp<RespQueryHisBill> queryHisOrderBillList(InterfaceMessage msg, Map<String, String> paramMap) throws Exception{
		String reqParam = "<Req><TransactionCode></TransactionCode><Data>"+
				"<BeginTime>{beginTime}</BeginTime><EndTime>{endTime}</EndTime>"+
				"<OrderId>{orderId}</OrderId><Type>{type}</Type>"+
				"</Data></Req>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		String result = call(null,ApiModule.His.queryhisorderbilllist ,  getHisUrl(), "QueryHisOrder", reqParam, KasiteConfig.getAuthInfo(msg.getAuthInfo()));
//		String result = "<Resp> <TransactionCode></TransactionCode> <RespCode>10000</RespCode><RespMessage>成功</RespMessage>"
//				+"<Data><OperateTime>2018-10-11 15:09:38</OperateTime><HisOrderId>9100020323</HisOrderId><OrderId>2C9649198302464E9E4D45A35F106C0F</OrderId><TransNo>4200000170201810111416469523</TransNo><ChannelId>100123</ChannelId><TotalMoney>0.01</TotalMoney><RefundMoney>0</RefundMoney><Type>1</Type></Data>"
//				+"<Data><OperateTime>2018-10-11 15:20:31</OperateTime><HisOrderId>9100020324</HisOrderId><OrderId>A56F96644FD74CC08FE4E407FEDEFD9E</OrderId><TransNo>4200000188201810119889182913</TransNo><ChannelId>100123</ChannelId><TotalMoney>0.01</TotalMoney><RefundMoney>0</RefundMoney><Type>1</Type></Data>"
//				+"<Data><OperateTime>2018-10-11 15:11:21</OperateTime><HisOrderId>9100020346</HisOrderId><OrderId>FCBB16136C2B4D8E91C4669DE927A9F0</OrderId><TransNo>4200000166201810110326222528</TransNo><ChannelId>100123</ChannelId><TotalMoney>0.01</TotalMoney><RefundMoney>0</RefundMoney><Type>1</Type></Data></Resp>";
		
		List<RespQueryHisBill> hisBillList = new ArrayList<RespQueryHisBill>();
		if (!StringUtil.isBlank(result)) {
			Document doc = XMLUtil.parseXml(result);
			Element rootEl = doc.getRootElement();
			String respCode = XMLUtil.getString(rootEl, "RespCode", true);
			if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
				List<Element> list = rootEl.elements(KstHosConstant.DATA);
				for (int i = 0; i < list.size(); i++) {
					Element element = list.get(i);
					RespQueryHisBill hisBillInfo = new RespQueryHisBill();
					hisBillInfo.setHisOrderId(XMLUtil.getString(element, "HisOrderId", true));
					hisBillInfo.setHisBizState(XMLUtil.getInt(element,"HisBizState",false));
					hisBillInfo.setMerchOrderNo(XMLUtil.getString(element,"TransNo",false));
					String type = XMLUtil.getString(element,"Type",true);
					if(KstHosConstant.HIS_TYPE_1.toString().equals(type)){
						hisBillInfo.setPriceName("门诊充值");
						hisBillInfo.setOrderMemo("门诊充值");
					}else if(KstHosConstant.HIS_TYPE_2.toString().equals(type)){
						hisBillInfo.setPriceName("住院充值");
						hisBillInfo.setOrderMemo("住院充值");
					}
					hisBillInfo.setRefundMoney((int)CommonUtil.mul(XMLUtil.getString(element,"RefundMoney",false,"0"),"100") + "");
					hisBillInfo.setTotalMoney((int)CommonUtil.mul(XMLUtil.getString(element,"TotalMoney",false,"0"),"100") + "");
					if("0".equals(hisBillInfo.getTotalMoney()) && !"0".equals(hisBillInfo.getRefundMoney())){
						//如果his传出的是退款金额，这该笔订单是退款订单
						hisBillInfo.setHisOrderType(2);//1支付2退费
						hisBillInfo.setRefundOrderId(XMLUtil.getString(element,"OrderId",false));//全流程退款订单号
						CommonResp<RespQueryQLCOrder> respRefundOrder = orderService.queryLocalRefundOrder(
								new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg, null, null, hisBillInfo.getRefundOrderId())));
						if(respRefundOrder.getCode().equals(KstHosConstant.SUCCESSCODE)) {
							List<RespQueryQLCOrder> refundOrderList = respRefundOrder.getData();
							if(refundOrderList != null && refundOrderList.size() > 0) {
								RespQueryQLCOrder refundOrder = refundOrderList.get(0);
								hisBillInfo.setOrderId(refundOrder.getOrderId());
								hisBillInfo.setPayMoney(refundOrder.getPayMoney()+"");
								hisBillInfo.setTotalMoney(refundOrder.getOrderMoney()+"");
								hisBillInfo.setTransDate(refundOrder.getRefundDate());
							}
						}
					}else{

						hisBillInfo.setPayMoney((int)CommonUtil.mul(XMLUtil.getString(element,"TotalMoney",false),"100") + "");
						hisBillInfo.setHisOrderType(1);//1支付2退费
						hisBillInfo.setOrderId(XMLUtil.getString(element,"OrderId",false));
						String transDateStr = XMLUtil.getString(element,"OperateTime",true);
						if( !StringUtil.isBlank(transDateStr) ){
							try {
								hisBillInfo.setTransDate(DateOper.parse2Timestamp(transDateStr));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					hisBillInfo.setChannelId(XMLUtil.getString(element,"ChannelId",false));
					hisBillList.add(hisBillInfo);
				}
			}
		}
		return new CommonResp<RespQueryHisBill>(msg, paramMap, RetCode.Success.RET_10000, hisBillList);
	}
	
	@Override
	public CommonResp<RespQueryHisBill> queryHisOrderBillByPayNo(InterfaceMessage msg, Map<String, String> paramMap)
			throws Exception {
		return null;
	}

	@Override
	public CommonResp<HisReportItemInfo> queryReportDetail(InterfaceMessage msg, Map<String, String> map) throws Exception {
		try {
			String reqParam = "<Req><TransactionCode></TransactionCode><Data><ReportType>{reportType}</ReportType><ReportId>{reportId}</ReportId></Data></Req>";
			reqParam = CommonUtil.formateReqParam(reqParam, map);
			String result = call(null,ApiModule.His.queryCheckReportItemList , getHisUrl(),"GetReportInfo", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			HisReportItemInfo reportItemInfo = new HisReportItemInfo();
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element root = doc.getRootElement();
				String respCode = XMLUtil.getString(root, "RespCode", true);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					List<Element> datas = root.elements(KstHosConstant.DATA);
					for (int i = 0; i < datas.size(); i++) {
						Element data = datas.get(i);
						reportItemInfo.setReportTitle(XMLUtil.getString(data, "ReportTitle", false));
						reportItemInfo.setClinicCard(XMLUtil.getString(data, "ClinicCard", false));
						reportItemInfo.setUserType(XMLUtil.getString(data, "UserType", false));
						reportItemInfo.setBarCode(XMLUtil.getString(data, "BarCode", false));
						reportItemInfo.setCheckSee(XMLUtil.getString(data, "CheckToSee", false));
						reportItemInfo.setSex(XMLUtil.getString(data, "Sex", false));
						reportItemInfo.setPatientName(XMLUtil.getString(data, "PatientName", false));
						reportItemInfo.setSampleNumber(XMLUtil.getString(data, "SampleNumber", false));
						reportItemInfo.setSampleType(XMLUtil.getString(data, "SampleType", false));
						reportItemInfo.setApplicationDepartment(XMLUtil.getString(data, "ApplicationDepartment", false));
						reportItemInfo.setSubmissionTime(XMLUtil.getString(data, "SubmissionTime", false));
						if (StringUtil.isNotBlank(reportItemInfo.getSubmissionTime())) {
							try {
								reportItemInfo.setSubmissionTime(DateOper.formatDate(reportItemInfo.getSubmissionTime(),
										"yyyy-MM-dd HH:mm:ss.0", "yyyy-MM-dd HH:mm:ss"));
							} catch (ParseException e) {
								// 格式化异常按照获得的值输出
								e.printStackTrace();LogUtil.error(log, e);
							}
						}
						reportItemInfo.setRmark(XMLUtil.getString(data, "Rmark", false));
						reportItemInfo.setReportingPhysicians(XMLUtil.getString(data, "ReportingPhysicians", false));
						reportItemInfo.setReportTime(XMLUtil.getString(data, "ReportDate", false));
						if (StringUtil.isNotBlank(reportItemInfo.getReportTime())) {
							try {
								reportItemInfo.setReportTime(DateOper.formatDate(reportItemInfo.getReportTime(),
										"yyyy-MM-dd HH:mm:ss.0", "yyyy-MM-dd HH:mm:ss"));
							} catch (ParseException e) {
								// 格式化异常按照获得的值输出
								e.printStackTrace();LogUtil.error(log, e);
							}
						}
						reportItemInfo.setInspector(XMLUtil.getString(data, "Inspector", false));
						reportItemInfo.setChecker(XMLUtil.getString(data, "Checker", false));
						reportItemInfo.setItemNum(XMLUtil.getString(data, "ItemNum", false));
						reportItemInfo.setClinicNo(XMLUtil.getString(data, "ClinicNo", false));
						reportItemInfo.setAge(XMLUtil.getString(data, "Age", false));
						reportItemInfo.setIsEmergency(XMLUtil.getString(data, "IsEmergency", false));
						reportItemInfo.setHosUserNo(XMLUtil.getString(data, "HosUserNo", false));
						reportItemInfo.setHosBedNo(XMLUtil.getString(data, "HosBedNo", false));
						reportItemInfo.setWords(XMLUtil.getString(data, "Words", false));
						List<Element> data1s = data.elements("Data_1");
						if (data1s.size() > 0) {
							List<HisReportItemDetails> reportDetail = new ArrayList<>();
							for (int j = 0; j < data1s.size(); j++) {
								Element data1 = data1s.get(j);
								HisReportItemDetails reportItemDetails = new HisReportItemDetails();
								reportItemDetails.setItemDetailsName(XMLUtil.getString(data1, "ItemDetailsName", false));
								reportItemDetails.setResultValue(XMLUtil.getString(data1, "ResultValue", false));
								reportItemDetails.setUnit(XMLUtil.getString(data1, "Unit", false));
								reportItemDetails.setReferenceValues(XMLUtil.getString(data1, "ReferenceValues", false));
								reportItemDetails.setIsNormal(XMLUtil.getString(data1, "IsNormal", false));
								reportItemDetails.setGermName(XMLUtil.getString(data1, "GermName", false));
								reportDetail.add(reportItemDetails);
							}
							reportItemInfo.setReportItemDetails(reportDetail);
						}
					}
				}

			}

			return new CommonResp<>(msg,map,RetCode.Success.RET_10000,reportItemInfo);
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}
	/**
	 *  调用HIS接口
	 * @param orderId		订单号
	 * @param hisApiModule	方法名称
	 * @param callUrl		his接口地址
	 * @param method		his方法名称
	 * @param reqNode		Soap请求入参节点后缀名
	 * @param params		入参
	 * @param vo			鉴权对象
	 * @return
	 * @throws Exception
	 */
	public String call(Map<String, String> paramMap,String orderId,ApiModule.His hisApiModule,
			String callUrl,String method,String reqNode,String params,AuthInfoVo vo) throws Exception{
		String result = null;
		long startTime = System.currentTimeMillis();
		RequestType type = RequestType.soap2;
		boolean isSuccess = false;
		int httpStausCode = -14444;
		SoapResponseVo response = null;
		try {
			HttpRequstBusSender sender = HttpRequestBus.create(callUrl, RequestType.soap2);
			sender.setParam(getSoap2ReqParams(callUrl,method,reqNode, params));
			response =  sender.send();
			httpStausCode = response.getCode();
			if(response==null || httpStausCode!=200) {
				//HTTP 请求失败
				throw new RRException(RetCode.HIS.ERROR_14444,"调用HIS接口异常。HttpStatus:"+ httpStausCode +" 返回："+ response.getResult());
			}
			isSuccess = true;
			result = response.getResult();
			//解析soap请求返回串，去除soap格式
			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			Element body = root.element("Body");
			
			if(result.contains("<ns:return>")) {
				result = body.element(method+"Response").elementTextTrim("return");
			}else if(result.contains("<ns2:getZyxxResponse")){
				result = body.element(method+"Response").elementTextTrim("return");
			}else {
				result = body.element(method+"Response").elementTextTrim(method+"Result");
			}
			return result;
		}catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			LogUtil.error(log, vo, callUrl+method, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}finally {
			String retVal = "未获取到结果";
			if(null != response) {
				retVal = response.getResult();
			}
			LogUtil.info(log, new LogBody(vo).set("hisUrl", callUrl).set("method", method)
					.set("reqParm", params).set("respParam", retVal));
			if(StringUtil.isBlank(orderId) && null != paramMap) {
				orderId = paramMap.get("orderId");
			}
			LogUtil.saveCallHisLog(this,vo, orderId, hisApiModule, params, retVal,  "callNumberSource" ,
					System.currentTimeMillis() - startTime, callUrl ,type,isSuccess,httpStausCode);
		}
	}
	/**
	 * 调用HIS接口
	 * @param hisUrl
	 * @param method
	 * @param param
	 * @param vo
	 * @return
	 */
	public String call(String orderId,ApiModule.His hisApiModule,String hisUrl,String method,String param,AuthInfoVo vo) {
		String hisResp = "";
		long startTime = System.currentTimeMillis();
		String reqUrl = hisUrl;
		RequestType type = RequestType.post;
		boolean isSuccess = false;
		int httpStausCode = -14444;
		try {
			HttpRequstBusSender sender = HttpRequestBus.create(hisUrl+method, RequestType.post);
			sender.contentType("application/xml");
			sender.setParam(param);
			SoapResponseVo resp = sender.send();
			httpStausCode = resp.getCode();
			if(httpStausCode ==200) {
				hisResp = resp.getResult();
			}else {
				throw new RRException(RetCode.HIS.ERROR_14444,"调用HIS接口异常。HttpStatus:"+ httpStausCode +" 返回："+ resp.getResult());
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			LogUtil.error(log, vo, hisUrl+method, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}finally {
			//TODO 记录his接口日志，根据实际情况和需求记录his的日志
			//hisurl、his的方法、入参、出参、AuthInfoVo,五个个为必须记录的字段
			LogUtil.info(log, new LogBody(vo).set("hisUrl", hisUrl).set("method", method)
					.set("reqParm", param).set("respParam", hisResp));
			
			LogUtil.saveCallHisLog(this,vo, orderId, hisApiModule, param, hisResp,  "callNumberSource" ,
					System.currentTimeMillis() - startTime, reqUrl ,type,isSuccess,httpStausCode);
		}
		return hisResp;
	}
	
	
	/**
	 * 调用号源池接口<br/>
	 * 标准全流程演示版，挂号走号源池接口，其他业务走his接口，特此说明
	 * @param tokenTemp
	 * @param api
	 * @param param
	 * @param clientId
	 * @return
	 * @throws Exception 
	 */
	public String callNumberSource( String orderId, ApiModule.His hisApiModule, 
			String tokenTemp,ApiModule api, String param, String clientId,AuthInfoVo vo) throws Exception {
		String respResult = "";
		String reqParam = "";
		long startTime = System.currentTimeMillis();
		String reqUrl = getUrl()+"/call";
		RequestType type = RequestType.post;
		boolean isSuccess = false;
		int httpStausCode = -14444;
		try {
			StringBuffer reqSbf = new StringBuffer();
			reqSbf.append("token=").append(tokenTemp);// 鉴权会话ID
			reqSbf.append("&authInfo=").append("{ClientVersion:'1',ClientId:'100123',Sign:'Sign',SessionKey:'" + tokenTemp + "'}");
			reqSbf.append("&sequenceNo=").append(System.currentTimeMillis() + "");// 时间戳
			reqSbf.append("&api=").append(api.getName());	// 需要调用的业务API接口
			reqSbf.append("&param=").append(DesUtil.encrypt(param,"UTF-8",getCryptKey()));// 调用接口的入参 请求参数默认xml
			reqSbf.append("&paramType=1");// 参数类型 1 xml 0 json 默认实现 xml
			reqSbf.append("&outType=1");// 出参类型 1 xml 0 json 默认实现 xml
			reqSbf.append("&v=1");// 接口版本号 默认 1 可以不填
			HttpRequstBusSender sender = HttpRequestBus.create(reqUrl, type);
			reqParam = reqSbf.toString();
			sender.setParam(reqParam);
			SoapResponseVo resp = sender.send();
			httpStausCode = resp.getCode();
			if(resp.getCode()!=200 || resp.getResult().indexOf("\"code\":500")>=0) {
				KasiteConfig.print("HIS返回token失效,重新获取token...");
				// 设置token过期
				tokenExpire = 0;
				// 获取token
				getToken(vo,null);
				// 重新调用一次
				// 鉴权参数 token 默认1天有效 根据系统配置不同 需要找技术核对 1天允许获取5次 超过5次
				// 需要最后一次获取超过24小时后才能再获取
				KasiteConfig.print("新token:" + token);
				sender.addHttpParam("token", token);
				sender.setHeaderHttpParam("token", token);
				resp = sender.send();
				httpStausCode = resp.getCode();
				if(resp.getCode() != 200) {
					isSuccess = false;
					throw new RRException(RetCode.Common.ERROR_CALLHIS,resp.getResult());
				}
				respResult = resp.getResult();
				isSuccess = true;
			}else {
				isSuccess = true;
				respResult = resp.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			LogUtil.error(log, vo, api.getName(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}finally {
			LogUtil.saveCallHisLog(this,vo, orderId, hisApiModule, tokenTemp+"|"+param, respResult,  "callNumberSource" ,
					System.currentTimeMillis() - startTime, reqUrl ,type,isSuccess,httpStausCode);
		}
		return respResult;
	}
	
	public String getToken(AuthInfoVo authVo,String orderId) throws Exception {
		if (!tokenIsExpire()) {
			log.debug("未过期token=" + token);
			return token;
		}
		KasiteConfig.print("获取Token...");
		HttpClient httpClient = new HttpClient();
		PostMethod post = null;
		long startTime = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParam = "";
		String response = "";
		int httpStausCode = -14444;
		try {
			reqParam = "mobile="+"18695728056&password="+Md5.getMd5Value("!Q83Ei@sXlHYihu.com");
			SoapResponseVo vo = HttpRequestBus.create(getUrl()+"/login", RequestType.post).setParam(reqParam).send();
			int code = vo.getCode();
			httpStausCode = code;
//			post = new PostMethod(url+"/login");
//			post.addParameter("mobile", "18695728056");
//			post.addParameter("password", Md5.getMd5Value("!Q83Ei@sXlHYihu.com"));
//			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(KstHosConstant.CONNECTION_TIMEOUT);
//			httpClient.getHttpConnectionManager().getParams().setSoTimeout(KstHosConstant.SO_TIMEOUT);
//			int code = httpClient.executeMethod(post);
			if (code == KstHosConstant.STATUS_CODE) {
				response = vo.getResult();
				KasiteConfig.print("返回值:" + response);
				if (StringUtil.isNotEmpty(response)) {
					JSONObject tokenJson = JSONObject.fromObject(response);
					if (tokenJson != null && tokenJson.size() > 0) {
						String tokenCode = JsonUtil.getJsonString(tokenJson, "code");
						if (StringUtil.isNotEmpty(tokenCode) && tokenCode.equals(KstHosConstant.SUCCESSCODE)) {
							// 保存token
							token = tokenJson.getString("token");
							long tokenPeriod = tokenJson.getLong("expire");
							String hisNow = tokenJson.getString("now");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// 过期时间 = HIS系统时间 + 过期间隔 + 本地与his的时间差 -
							// 1小时(提起1小时强制过期)
							long differenceTime = System.currentTimeMillis() - sdf.parse(hisNow).getTime();
							KasiteConfig.print("系统时间差=" + differenceTime / 1000 + "秒");
							tokenExpire = sdf.parse(hisNow).getTime() + tokenPeriod * 1000 + differenceTime - 3600000;
							KasiteConfig.print("token过期时间=" + sdf.format(tokenExpire));
							isSuccess = true;
							return token;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		} finally {
			// 释放连接
			if (null != post) {
				post.releaseConnection();
			}
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
			LogUtil.saveCallHisLog(this,authVo, orderId, His.getToken, reqParam, response,  "getToken", 
					System.currentTimeMillis() - startTime, getUrl() + "/login",RequestType.post,isSuccess,httpStausCode);
		}
		return "";
	}
	
	
	
	/**
	 * 判断token是否失效
	 * 
	 * @return
	 */
	public boolean tokenIsExpire() {
		if (System.currentTimeMillis() >= tokenExpire) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CommonResp<HisLockOrder> lockOrder(CommonReq<ReqHisLock> req) throws Exception {
		HisLockOrder lockOrder = this.lockOrder(req.getParam().getAuthInfo(), req.getParam().getOrderId(), req.getParam().getSch(), req.getParam().getNum(), req.getParam().getParamMap());
		if(StringUtil.isNotBlank(lockOrder.getHisOrderId())) {
			//如果锁号成功则返回成功状态，锁号失败则返回失败状态
			return new CommonResp<HisLockOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,lockOrder);
		} else if(StringUtil.isNotBlank(lockOrder.getRespMessage())) {
			//如果his有返回锁号异常说明则返回给前端的也是his返回的锁号异常说明
			return new CommonResp<HisLockOrder>(req, KstHosConstant.DEFAULTTRAN, YY.ERROR_CALLHIS,
					lockOrder.getRespMessage());
		} else {
			return new CommonResp<HisLockOrder>(req, KstHosConstant.DEFAULTTRAN, YY.ERROR_CALLHIS,
					"锁号失败:His返回的锁号订单为空. his返回值："+ lockOrder.getStore());
		}
	}

	@Override
	public CallHis accept() {
		return CallHis.yanshiyiyuan;
	}

	@Override
	public CommonResp<HisUnlock> unLockOrder(CommonReq<ReqHisUnLock> req) throws Exception {
		HisUnlock resp = this.unlock(req.getParam().getAuthInfo(), req.getParam().getOrderId(),req.getParam().getParamMap());
		if(resp.isUnLockSuccess()) {
			//如果锁号成功则返回成功状态，锁号失败则返回失败状态
			return new CommonResp<HisUnlock>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else if(StringUtil.isNotBlank(resp.getMessage())) {
			return new CommonResp<HisUnlock>(req, KstHosConstant.DEFAULTTRAN, YY.ERROR_CALLHIS,resp.getMessage());
		}else {
			String result = resp.getStore();
			return new CommonResp<HisUnlock>(req, KstHosConstant.DEFAULTTRAN, YY.ERROR_CALLHIS,"解锁失败:His返回的锁号订单为空. his返回值："+result);
		}
	}

	/**
	 * @param msg
	 * @param paramList
	 * @return
	 */
	@Override
	public CommonResp<RespMap> oPDRechargeRefundFreeze(InterfaceMessage msg,Map<String, String> paramMap) {
//		<Req>
//		  <TransactionCode></TransactionCode>
//		  <Data>
//		  <OrderId>订单号</OrderId>
//		  <CardNo>卡号</CardNo>
//		  <CardType>卡类型</CardType>
//		  <HisMemberId>his就诊人唯一ID</HisMemberId>
//		  <TransNo>商户交易流水号</TransNo>
//		  <Price>充值金额</Price>
//		      <OperatorId>操作人id</OperatorId>
//		      <OperatorName>操作人姓名</OperatorName>
//		    </Data>
//		  </Req>
		paramMap.get("orderId");
		paramMap.get("cardNo");
		paramMap.get("cardType");
		paramMap.get("transNo");
		paramMap.get("hisMemeberId");
		paramMap.get("price");
		paramMap.get("operatorId");
		paramMap.get("operatorName");
		//TODO 调用HIS冻结接口，没有模拟接口，将就下
		//his需要返回his的退款订单号
		String outRefundOrderId = StringUtil.getUUID();
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.oPDRechargeRefundFreezeResp.OutRefundOrderId,outRefundOrderId);
		return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
	}

	/**
	 * @param msg
	 * @param paramList
	 * @return
	 */
	@Override
	public CommonResp<RespMap> oPDRechargeRefundWriteOff(InterfaceMessage msg,Map<String, String> paramMap) {
//		<Req>
//		  <TransactionCode></TransactionCode>
//		<Data>
//		<OrderId>订单号</OrderId>
//		<CardNo>卡号</CardNo>
//		<CardType>卡类型</CardType>
//		<TransNo>商户交易流水号</TransNo>
//		<HisMemeberId>his就诊人唯一ID</HisMemeberId>
//		<RefundOrderId>全流程退款订单号</RefundOrderId>
//		<Price>充值金额</Price>
//		    <OperatorId>操作人id</OperatorId>
//		    <OperatorName>操作人姓名</OperatorName>
//		  </Data>
//		</Req>
		paramMap.get("orderId");
		paramMap.get("cardNo");
		paramMap.get("cardType");
		paramMap.get("transNo");
		paramMap.get("hisMemeberId");
		paramMap.get("price");
		paramMap.get("operatorId");
		paramMap.get("operatorName");
		paramMap.get("refundOrderId");
		//TODO 调用HIS核销，没有模拟接口，将就下
		return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<HisCheckEntityCard> CheckEntityCard(InterfaceMessage msg, Map<String, String> paramMap)
			throws Exception {
		HisCheckEntityCard obj = new HisCheckEntityCard();
		obj.setAge(18);
		obj.setBalance(100);
		obj.setCardNo("A123456");
		obj.setCardType("1");
		obj.setIdCardId("350426198808050028");
		obj.setHisMemberId("testHisMiniPayMemberId");
		obj.setMemberName("潘某峰");
		obj.setPhone("18600000123");
		obj.setSex(1);
		CommonResp<HisCheckEntityCard>  resp = new CommonResp<HisCheckEntityCard>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,obj);
		resp.setMessage("提示消息");
		return resp;
		//		return new CommonResp<HisCheckEntityCard>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,obj);
	}

	/* (non-Javadoc)
	 * @see com.kasite.core.serviceinterface.module.his.ICallHisService#queryOutpatientCostList(com.yihu.wsgw.api.InterfaceMessage, java.util.Map)
	 */
	@Override
	public CommonResp<HisQueryOutpatientCostList> queryOutpatientCostList(InterfaceMessage msg, Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.kasite.core.serviceinterface.module.his.ICallHisService#queryOutpatientCostType(com.yihu.wsgw.api.InterfaceMessage, java.util.Map)
	 */
	@Override
	public CommonResp<HisQueryOutpatientCostType> queryOutpatientCostType(InterfaceMessage msg, Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.kasite.core.serviceinterface.module.his.ICallHisService#queryOutpatientCostTypeItem(com.yihu.wsgw.api.InterfaceMessage, java.util.Map)
	 */
	@Override
	public CommonResp<CommonPrescriptionItem> queryOutpatientCostTypeItem(InterfaceMessage msg,
			Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<HisQueryOrderReceiptList> queryOrderReceiptList(InterfaceMessage msg,
			Map<String, String> paramMap) throws Exception {
		paramMap.get("cardNo");
		paramMap.get("cardType");
		paramMap.get("hisMemberId");//数据库有存，就有
		paramMap.get("receiptNos");//需要支持多个单据号查询
		List<HisQueryOrderReceiptList> list = new ArrayList<HisQueryOrderReceiptList>();
		
		for(int i=0;i<5;i++) {
			HisQueryOrderReceiptList unsettledReceipt = new HisQueryOrderReceiptList();		
			unsettledReceipt.setReceiptName("西药费");
			unsettledReceipt.setReceiptNo(i+"_"+i);
			unsettledReceipt.setReceiptTime(DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
			unsettledReceipt.setIsSettlement(0);
			unsettledReceipt.setTotalPrice(1001);
			List<CommonPrescriptionItem> itemList = new ArrayList<CommonPrescriptionItem>();
			for(int j=0;j<1;j++) {
				CommonPrescriptionItem item = new CommonPrescriptionItem();
				item.setNumber("1");
				item.setProject("药药");
				item.setSpecifications("规格");
				item.setSumOfMoney(20);
				item.setUnitPrice(20);
				itemList.add(item);
			}
			unsettledReceipt.setItemList(itemList);
			list.add(unsettledReceipt);
		}
		return new CommonResp<HisQueryOrderReceiptList>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,list);
	}

	/**
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> mergeSettledPayReceipt(InterfaceMessage msg, Map<String, String> paramMap) throws Exception {
		paramMap.get("price");//总金额
		paramMap.get("transNo");//商户订单号
		paramMap.get("transTime");//支付时间
		paramMap.get("channelId");//渠道
		paramMap.get("cardNo");//卡号
		paramMap.get("cardType");//卡类型
		paramMap.get("memberName");//用户姓名
		paramMap.get("idCardNo");//身份证
		paramMap.get("mobile");//手机号
		paramMap.get("hisMemberId");//hisMemberId
		paramMap.get("receiptNos");//需要合并的单据号多个，隔开
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.HisMergeSettledPayReceiptResp.HisFlowNo, "his结算成功流水号，非必填");
		return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
	}

	/**
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> queryMergeSettledPayReceipt(InterfaceMessage msg, Map<String, String> paramMap)
			throws Exception {
		paramMap.get("price");//总金额
		paramMap.get("transNo");//商户订单号
		paramMap.get("transTime");//支付时间
		paramMap.get("channelId");//渠道
		paramMap.get("cardNo");//卡号
		paramMap.get("cardType");//卡类型
		paramMap.get("memberName");//用户姓名
		paramMap.get("idCardNo");//身份证
		paramMap.get("mobile");//手机号
		paramMap.get("hisMemberId");//hisMemberId
		paramMap.get("receiptNos");//需要合并的单据号多个，隔开
		//TODO 必须实现
		
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.QueryOrderState.OrderState, 2);//2成功，其他都 是失败
		return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> GetConfigKeyByGuidInfo(InterfaceMessage msg, Map<String, String> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
//		RespMap resp = new RespMap();
//		String operatorId = paramMap.get(ApiKey.GetGuide.OperatorId.name());
//		if(operatorId.equals("00000904ZT123175000008")) {
//			resp.put(ApiKey.HisGetConfigKeyByGuidInfo.WxPayConfigKey, "WX1000002");
//			resp.put(ApiKey.HisGetConfigKeyByGuidInfo.ZfbConfigKey, "2017102309469076");
//			return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,resp);
//		}
//		throw new RRException("该设备未注册无法进行支付，请联系管理员。");
//		
		RespMap resp = new RespMap();
		com.alibaba.fastjson.JSONObject json = getMiniPayConfig();
		String operatorId = paramMap.get(ApiKey.GetGuide.OperatorId.name());
		if(null != json) {
			com.alibaba.fastjson.JSONObject jsonConfig = json.getJSONObject(operatorId);
			if(null != jsonConfig) {
				resp.put(ApiKey.HisGetConfigKeyByGuidInfo.WxPayConfigKey, jsonConfig.getString("WxPay"));
				resp.put(ApiKey.HisGetConfigKeyByGuidInfo.ZfbConfigKey, jsonConfig.getString("ZfbPay"));
				return new CommonResp<RespMap>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,resp);
			}
		}
		throw new RRException("该设备未注册无法进行支付，请联系管理员。operatorId="+operatorId);
	}

	/**
	 * @param commonReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> addLocalOrderValidateBefore(CommonReq<ReqAddOrderLocal> commonReq) throws Exception {
		return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}

	@Override
	public com.alibaba.fastjson.JSONObject getDiyConfig() {
		//使用这个接口需要  从这个接口走  getWebUiDiy();
		//================必须 start==============
		com.alibaba.fastjson.JSONObject diyJson = getWebUiDiy();
		//================必须 end==============
		// checkPorvingCode 新增用户／绑定就诊卡／绑定住院号 是否要使用验证码  true / false
		// diyJson.put("checkPorvingCode", false);
		
		//
		com.alibaba.fastjson.JSONObject memberPicker =  new com.alibaba.fastjson.JSONObject();
		memberPicker.put("orderSettlementList", true);
		diyJson.put("memberPicker", memberPicker);
		
		//订单列表页面查询的业务订单分类 查询本地订单的时候支持查询哪些分类的订单
		//==============orderLocalList.html ==============start
		com.alibaba.fastjson.JSONObject openServices =  new com.alibaba.fastjson.JSONObject();
		openServices.put("0", "挂号");
		openServices.put("008", "医嘱");
		openServices.put("011", "医嘱");
		openServices.put("007", "住院预交");
		diyJson.put("openServices", openServices);
		//==============orderLocalList.html ==============end
		/**
		 * outpatientPayRecordListHis.html 查询HIS待缴费订单列表  订单列表状态 默认充值和代缴费2个查询状态
		 */
		//=========================outpatientPayRecordListHis config start =================
		//支付状态：1 充值 2退费 
		com.alibaba.fastjson.JSONObject hisOrderPayType =  new com.alibaba.fastjson.JSONObject();
		hisOrderPayType.put("1", "充值");
		hisOrderPayType.put("2", "退费");
		//支付方式： 0现金，1支付宝，2银行卡，3微信
		com.alibaba.fastjson.JSONObject hisOrderChargeType =  new com.alibaba.fastjson.JSONObject();
		hisOrderChargeType.put("0", "现金");
		hisOrderChargeType.put("2", "银行卡");
		hisOrderChargeType.put("3", "微信");
		
		com.alibaba.fastjson.JSONObject config =  new com.alibaba.fastjson.JSONObject();
		config.put("hisOrderPayType", hisOrderPayType);
		config.put("hisOrderChargeType", hisOrderChargeType);
		//如果不做此节点个性化，前端按照默认的配置走
		diyJson.put("outpatientPayRecordListHis", config);
		//=========================outpatientPayRecordListHis config end =================
		
		// outpatientSelfServiceRefund.html 就诊卡充值，线上退款时间限制
		//============== selfRefundTimeLimit 自动退费时间限制 start =================
		diyJson.put("selfRefundTimeLimit", "10:00~22:00");
		//自助退费说明
		diyJson.put("selfRefundExplain", "本次退费只可退微信、支付宝充值金额，退款金额将会原路退回您的账户。退款只允许退7天内的充值。退款时间（10:00到22:00），其余时间请到收费处退款。谢谢！");
		//============== selfRefundTimeLimit 自动退费时间限制 end =================
		
		return diyJson;
	}


	@Override
	public CommonResp<HisStopClinicList> queryStopClinicList(InterfaceMessage msg, Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResp<HisQueryHospitalUserInfo> queryHospitalUserInfoByHospiatlNo(InterfaceMessage msg,
			Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HisInfStatus infStatus(InterfaceMessage msg,BusinessTypeEnum bt) {
		//TODO  注意 演示环境这里默认返回ok 但是在各家医院的逻辑判断里要自行判断
		//判断HIS接口是否能够正常通讯  如果不通的业务场景需要支付的时候判断不同的接口 请各自进行判断
		return HisInfStatus.ok;
	}

	/**
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<HisQueryEntityCardInfoResp> queryEntityCardInfo(InterfaceMessage msg,
			Map<String, String> paramMap) throws Exception {
		paramMap.get("cardId");//卡上的ID/卡上的卡号
		
		HisQueryEntityCardInfoResp cardInfo = new HisQueryEntityCardInfoResp();
		cardInfo.setBalance(10000);
		cardInfo.setCardNo("A47025555");
		cardInfo.setCardType("1");
		cardInfo.setHisMemberId("F1D1C064203540C1895A560F6ECADyyy");
		cardInfo.setIdCardId("350181198901042276");
		cardInfo.setMemberName("林强");
		cardInfo.setPhone("13960317626");
		return new CommonResp<HisQueryEntityCardInfoResp>(HisCallReq.get(msg,paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,cardInfo);
	}


//	@Override
//	public CommonResp<RespMap> createQrCodeGuidSaveInfoService(InterfaceMessage msg, Map<String, String> paramMap)
//			throws Exception {
//		String cardNo = paramMap.get("cardNo");
//		String cardType = "14";
//		if(StringUtil.isBlank(cardNo)){//卡号为空不让充值
//			// CommonResp(InterfaceMessage msg,Map<String, String> map,IRetCode retCode,String message) 
//			return new CommonResp<RespMap>(msg,paramMap,RetCode.Order.ERROR_CALLHIS,"卡号不能为空");
//		}
//		
//		//http://j22591v670.51mypc.cn:47361/jeecg/cxf/hello?wsdl
////		call(Map<String, String> paramMap,String orderId,ApiModule.His hisApiModule,
////		String callUrl,String method,String reqNode,String params,AuthInfoVo vo)
//		String result = call(paramMap,null,ApiModule.His.queryPatientInfoByNos,
//				getQueryHospitalNoUrl2CreateQrCodeUrl(), "getZyxx","getZyxx",
//				cardNo,KasiteConfig.getAuthInfo(msg));
//		RespMap respMap = new RespMap();
//		if(StringUtil.isNotBlank(result)) {
//			try {
//				com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(result);
//				String code = json.getString("code");
//				if(null != code && "200".equals(code)) {
//					com.alibaba.fastjson.JSONArray resultArr = json.getJSONArray("data");
//					if(resultArr == null || resultArr.size() != 1) {
//						return new CommonResp<RespMap>(msg,paramMap,RetCode.Common.ERROR_CALLHIS,
//								"医院接口返回多个病人ID："+result);
//					}
//					com.alibaba.fastjson.JSONObject patientInfo = resultArr.getJSONObject(0);
//					String hisMemberId = patientInfo.getString("BRID00");
//					String patientName = patientInfo.getString("XM0000");
//					String cardNoZyh = patientInfo.getString("ZYH000");
//					String idCardNo = patientInfo.getString("BRZJBH");
////					String cardType = patientInfo.getString("BRZJLX");
//					String sex = patientInfo.getString("XB0000");
//					if(StringUtil.isBlank(patientName) || StringUtil.isBlank(cardNoZyh) 
//							|| StringUtil.isBlank(hisMemberId) ) {
//						return new CommonResp<RespMap>(msg,paramMap,RetCode.Common.ERROR_CALLHIS,
//								"医院接口返回的病人信息不全无住院号、姓名、病人ID："+result);
//					}
//					cardNoZyh = cardNoZyh.trim();
//					hisMemberId = hisMemberId.trim();
//					respMap.add(ApiKey.HISCreateQRCode.addUrlParam, "ZYH000="+cardNoZyh+"&patient_id="+hisMemberId);
//					respMap.add(ApiKey.HISCreateQRCode.memberName, patientName);
//					respMap.add(ApiKey.HISCreateQRCode.hisMemberId, hisMemberId);
//					respMap.add(ApiKey.HISCreateQRCode.idCardNo, idCardNo);
//					respMap.add(ApiKey.HISCreateQRCode.hospitalNo, cardNo);
//					respMap.add(ApiKey.HISCreateQRCode.sex, sex);
//					respMap.add(ApiKey.HISCreateQRCode.cardType, "14");
//					respMap.add(ApiKey.HISCreateQRCode.cardTypeName, "住院号");
//					respMap.add(ApiKey.HISCreateQRCode.store, patientInfo.toJSONString());
//				}else {
//					String respMsg = json.getString("msg");
//					return new CommonResp<RespMap>(msg,paramMap,RetCode.Common.ERROR_CALLHIS,
//							"医院接口返回异常："+respMsg);
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//				throw new RRException(RetCode.Common.ERROR_CALLHIS,"调用HIS接口返回结果异常："+result);
//			}
//		}else {
//			throw new RRException(RetCode.Common.ERROR_CALLHIS,"调用HIS接口返回结果异常："+result);
//		}
//		return new CommonResp<>(msg,paramMap,RetCode.Success.RET_10000, respMap);
//		return null;
//	} 
	public String getQueryHospitalNoUrl2CreateQrCodeUrl() {
		return "http://j22591v670.51mypc.cn:47361/jeecg/cxf/hello?wsdl";
	}
	/**
	 * 拼装His接口soap2请求参数
	 * @Description: 
	 * @param method 
	 * @param params
	 * @return
	 */
	public String getSoap2ReqParams(String callUrl,String method,String reqNode,String params) {
		StringBuffer soapParams = new StringBuffer();
		if(callUrl.equals(getQueryHospitalNoUrl2CreateQrCodeUrl())) {
			soapParams.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.main.yun.com/\">");
			soapParams.append("<soapenv:Header/>");
			soapParams.append("<soapenv:Body>");
//			soapParams.append("<web:getZyxx>");
			soapParams.append("<web:").append(reqNode).append(">");
			soapParams.append("<arg0>");
			soapParams.append("<![CDATA["); 
			soapParams.append(params);
			soapParams.append("]]>"); 
			soapParams.append("</arg0>");
			soapParams.append("</web:").append(reqNode).append(">");
			soapParams.append("</soapenv:Body>");
			soapParams.append("</soapenv:Envelope>");
		}
		return soapParams.toString();
	}

	@Override
	public CommonResp<HisQueryBaseDept> queryBaseDept(InterfaceMessage msg, Map<String, String> paramMap) throws Exception {
		List<HisQueryBaseDept> respList;
		String reqParam = "<Req><TransactionCode></TransactionCode><Data><HosId>{hosId}</HosId><DeptCode>{deptCode}" +
				"</DeptCode><DeptName>{deptName}</DeptName><DeptType>{deptType}</DeptType></Data></Req>";
		try {
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			String result = call(null,ApiModule.His.queryDept, getHisUrl(),"queryDept", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					respList = new ArrayList<>();
					List<Element> list = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < list.size(); i++) {
						Element element = list.get(i);
						HisQueryBaseDept deptInfo = new HisQueryBaseDept();
						deptInfo.setDeptAddr(XMLUtil.getString(element, "Address", true));
						deptInfo.setDeptCode(XMLUtil.getString(element, "DeptCode", false));
						deptInfo.setDeptName(XMLUtil.getString(element, "DeptName", true));
						deptInfo.setDeptBrief(XMLUtil.getString(element, "Intro", false));
						deptInfo.setParentDeptCode(XMLUtil.getString(element, "ParentId", true));
						deptInfo.setRemark(XMLUtil.getString(element, "Remark", true));
						
						respList.add(deptInfo);
					}
					return new CommonResp<HisQueryBaseDept>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, respList);
				} else {
					return new CommonResp<HisQueryBaseDept>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryBaseDept>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		} catch (Exception e) {
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<HisQueryBaseDoctor> queryBaseDoctor(InterfaceMessage msg, Map<String, String> paramMap)
			throws Exception {
		List<HisQueryBaseDoctor> respList;
		String reqParam = "<Req><TransactionCode></TransactionCode><Data><HosId>{hosId}</HosId><DeptCode>{deptCode}" +
				"</DeptCode><DoctorCode>{doctorCode}</DoctorCode><DoctorName>{doctorName}</DoctorName><DoctorType>{doctorType}</DoctorType></Data></Req>";
		try {
			reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
			String result = call(null,ApiModule.His.queryDoctor, getHisUrl(),"queryDoctor", reqParam,KasiteConfig.getAuthInfo(msg.getAuthInfo()));
			if (!StringUtil.isBlank(result)) {
				Document doc = XMLUtil.parseXml(result);
				Element rootEl = doc.getRootElement();
				String respCode = XMLUtil.getString(rootEl, "RespCode", true);
				String respMsg = XMLUtil.getString(rootEl, "RespMessage", false);
				if (KstHosConstant.SUCCESSCODE.equals(respCode)) {
					respList = new ArrayList<>();
					List<Element> list = rootEl.elements(KstHosConstant.DATA);
					for (int i = 0; i < list.size(); i++) {
						Element element = list.get(i);
						HisQueryBaseDoctor doctorInfo = new HisQueryBaseDoctor();
						doctorInfo.setDeptCode(XMLUtil.getString(element, "DeptCode", true));
						doctorInfo.setDeptName(XMLUtil.getString(element, "DeptName", false));
						doctorInfo.setDoctorCode(XMLUtil.getString(element, "DoctorCode", true));
						doctorInfo.setDoctorName(XMLUtil.getString(element, "DoctorName", false));
						doctorInfo.setTitle(XMLUtil.getString(element, "DoctorTitle", true));
						doctorInfo.setTitleCode(XMLUtil.getString(element, "DoctorTitleCode", true));
						doctorInfo.setIntroduction(XMLUtil.getString(element, "Intro", true));
						doctorInfo.setLevelName(XMLUtil.getString(element, "Level", false));
						doctorInfo.setLevelId(XMLUtil.getString(element, "LevelId", false));
						doctorInfo.setPrice(XMLUtil.getInt(element, "Price", false));
						doctorInfo.setRemark(XMLUtil.getString(element, "Remark", false));
						doctorInfo.setDoctorSex(XMLUtil.getString(element, "Sex", true));
						doctorInfo.setSpec(XMLUtil.getString(element, "Spec", true));
						doctorInfo.setPhotoUrl(XMLUtil.getString(element, "Url", false));
						
						respList.add(doctorInfo);
					}
					return new CommonResp<HisQueryBaseDoctor>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, respList);
				} else {
					return new CommonResp<HisQueryBaseDoctor>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,respMsg);
				}
			}else {
				return new CommonResp<HisQueryBaseDoctor>(HisCallReq.get(msg, paramMap),KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS);
			}
		} catch (Exception e) {
			LogUtil.error(log, KasiteConfig.getAuthInfo(msg.getAuthInfo()),  getHisUrl(), e);
			throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
		}
	}

	@Override
	public CommonResp<RespGetTjReportInfo> queryTjReportDetail(InterfaceMessage msg, Map<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
