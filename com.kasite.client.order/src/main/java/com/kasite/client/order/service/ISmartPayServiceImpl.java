package com.kasite.client.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.order.bean.dbo.Guide;
import com.kasite.client.order.bean.dbo.SelfRefundRecord;
import com.kasite.client.order.bean.dbo.SelfRefundRecordOrder;
import com.kasite.client.order.bean.dto.RefundOrderVo;
import com.kasite.client.order.dao.IGuideMapper;
import com.kasite.client.order.dao.ISelfRefundRecordMapper;
import com.kasite.client.order.dao.ISelfRefundRecordOrderMapper;
import com.kasite.client.order.job.SelfServiceRefundThread;
import com.kasite.client.order.util.OrderUtil;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.NetworkUtil;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.MatrixToImageWriter;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.cache.IDictLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryCardBalance;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryCardBalance;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.ICreateQrCodeGuidSaveInfoService;
import com.kasite.core.serviceinterface.module.his.handler.IQueryMemberRefundableService;
import com.kasite.core.serviceinterface.module.his.req.ReqHisMemberRefundable;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.ISmartPayService;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsPayment;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryRefundableOrderList;
import com.kasite.core.serviceinterface.module.order.req.ReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.req.ReqApplySelfServiceRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePatientQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePayQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePrescriptionQrCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetGuide;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetPayQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqPrescQrValidateBefore;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMemberRefundableMoney;
import com.kasite.core.serviceinterface.module.pay.req.ReqQuerySelfRefundRecordInfo;
import com.kasite.core.serviceinterface.module.pay.req.ReqQuerySelfRefundRecordList;
import com.kasite.core.serviceinterface.module.pay.req.ReqQuickPaymentQR;
import com.kasite.core.serviceinterface.module.pay.resp.RespQuickPaymentQR;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 智付接口实现类
 * 
 * @author 無
 *
 */
@Service("smartPay.smartPayWs")
public class ISmartPayServiceImpl implements ISmartPayService {
	private final Log log = LogFactory.getLog(KstHosConstant.LOG4J_SMARTPAY);
	@Autowired
	IDictLocalCache dictLocalCache;
	@Autowired
	IGuideMapper guideMapper;
	@Autowired
	IBasicService basicService;
	@Autowired
	IPayService payService;
	@Autowired
	IOrderService orderService;
	@Autowired
	ISelfRefundRecordMapper selfRefundRecordMapper;
	@Autowired
	ISelfRefundRecordOrderMapper selfRefundRecordOrderMapper;
	 
	
	
	/**
	 * 当面付
	 */
	@Override
	public String SweepCodePay(InterfaceMessage msg) throws Exception{
		return this.sweepCodePay(new CommonReq<ReqSweepCodePay>(new ReqSweepCodePay(msg))).toResult();
	}
	/**
	 * API:当面付扫码。新增全流程订单，然后调用pay模块的当面付接口
	 * 针对福建医科大学兼容旧版
	 * @param msg
	 * @return
	 */
	@Override
	public String SweepCodePay_V1(InterfaceMessage msg) throws Exception{
		return this.sweepCodePay(new CommonReq<ReqSweepCodePay>(new ReqSweepCodePay(msg))).toResult();
	}
	/**
	 * 快速支付二维码
	 */
	@Override
	public String QuickPaymentQR(InterfaceMessage msg) throws Exception {
		return this.quickPaymentQR(new CommonReq<ReqQuickPaymentQR>(new ReqQuickPaymentQR(msg))).toResult();
	}

	/**
	 * 生成患者二维码。 新增二维码信息，新增患者信息，生成二维字符串。
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String CreatePatientQRCode(InterfaceMessage msg) throws Exception {
		return this.createPatientQRCode(new CommonReq<ReqCreatePatientQRCode>(new ReqCreatePatientQRCode(msg))).toResult();
	}

	/**
	 * 生成支付二维码。 新增本地订单，生成二维码。
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String CreatePayQRCode(InterfaceMessage msg) throws Exception {
		return this.createPayQRCode(new CommonReq<ReqCreatePayQRCode>(new ReqCreatePayQRCode(msg))).toResult();
	}
	
	/**
	 * 获取二维码信息点信息。 根据二维码信息唯一ID，获取二维码信息数据
	 * 带有脱敏操作
	 * @param msg
	 * @return
	 */
	@Override
	public String GetGuide(InterfaceMessage msg) throws Exception {
		//外部调用此接口的数据，进行脱敏
		CommonResp<RespMap> commResp = this.getGuide(new CommonReq<ReqGetGuide>(new ReqGetGuide(msg)));
		RespMap respMap = commResp.getResultData();
		String content = respMap.getString(ApiKey.GetGuide.Content);
		JSONObject contentJson =  JSON.parseObject(content);
		if( contentJson.containsKey("memberName")) {
			contentJson.put("memberName",StringUtil.nameDesensitization(contentJson.getString("memberName")));
		}
		if( contentJson.containsKey("name")) {
			contentJson.put("name", StringUtil.nameDesensitization(contentJson.getString("name")));
		}
		if( contentJson.containsKey("mobile")) {
			contentJson.put("mobile", StringUtil.mobileDesensitization(contentJson.getString("mobile")));
		}
		if( contentJson.containsKey("idCardNo")) {
			contentJson.put("idCardNo",StringUtil.idCardNoDesensitization(contentJson.getString("idCardNo")));
		}
		respMap.put(ApiKey.GetGuide.Content, contentJson.toJSONString());
		List<RespMap> list = commResp.getData();
		list.clear();
		list.add(respMap);
		commResp.setData(list);
		return commResp.toResult();
	}

	@Override
	public CommonResp<RespQuickPaymentQR> quickPaymentQR(CommonReq<ReqQuickPaymentQR> commReq) throws Exception {
		ReqQuickPaymentQR req = commReq.getParam();
		ReqAddOrderLocal reqAddOrderLocal = new ReqAddOrderLocal(req.getMsg(), null, req.getHisOrderId(), req.getTotalMoney(), req.getTotalMoney(),
				req.getPriceName(), req.getCardNo(), req.getCardType(), req.getOperatorId(), req.getOperatorName(), 
				req.getServiceId(), 1, req.getEqptType(),null);
		CommonReq<ReqAddOrderLocal> commReqAddOrder = new CommonReq<ReqAddOrderLocal>(reqAddOrderLocal);		
		CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(commReqAddOrder);
		
		if( !addOrderResp.getCode().equals(RetCode.Success.RET_10000.getCode().toString())
				|| StringUtil.isEmpty(addOrderResp.getData())) {
			//如果新增本地订单失败
			return new CommonResp<RespQuickPaymentQR>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CALLADDORDER, "新增全流程订单异常！请联系开发！");
		}
		RespMap map = addOrderResp.getData().get(0);
		String orderId = map.getString(ApiKey.AddOrderLocal.OrderId);
				
		// 调用pay.PayWs.GetPayQRCode
		ReqGetPayQRCode getPayQrReq = new ReqGetPayQRCode(req.getMsg(), orderId,
				req.getTotalMoney(), req.getPriceName(), req.getPriceName(), NetworkUtil.getLocalIP(), 0);
		
		CommonResp<RespMap> getPayQrResp = payService.getPayQRCode(new CommonReq<ReqGetPayQRCode>(getPayQrReq));
		if (getPayQrResp == null || !KstHosConstant.SUCCESSCODE.equals(getPayQrResp.getCode())) {
			return new CommonResp<RespQuickPaymentQR>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.Call, (getPayQrResp != null ? getPayQrResp.getMessage() : "获取支付二维码信息异常"));
		}
		List<RespMap> ll = getPayQrResp.getData();
		if (ll == null || ll.size() <= 0) {
			return new CommonResp<RespQuickPaymentQR>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.Call, "获取支付二维码信息异常,返回信息为空");
		}
		RespMap respMap = ll.get(0);
		String qrCodeUrl = respMap.getString(ApiKey.GetPayQRCode.QRCodeUrl);

		RespQuickPaymentQR resp = new RespQuickPaymentQR();
		resp.setOrderId(orderId);
		resp.setTotalFee(req.getTotalMoney());
		resp.setQrContent(qrCodeUrl);
		resp.setPrescNo(req.getHisOrderId());
		return new CommonResp<RespQuickPaymentQR>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> sweepCodePay(CommonReq<ReqSweepCodePay> commReq) throws Exception {
		ReqSweepCodePay req = commReq.getParam();
		ReqAddOrderLocal reqAddOrderLocal = new ReqAddOrderLocal(req.getMsg(), null, req.getHisOrderId(), req.getTotalFee(), req.getTotalFee(),
				req.getSubject(),req.getCardNo(), req.getCardType(), req.getOperatorId(), req.getOperatorName(), 
				req.getServiceId(), 1, req.getEqptType(),req.getHisMemberId(),null);
		reqAddOrderLocal.setMemberName(req.getName());
		reqAddOrderLocal.setIdCardNo(req.getIdCardId());
		reqAddOrderLocal.setSex(req.getSex());
		reqAddOrderLocal.setMobile(req.getMobile());
		reqAddOrderLocal.setIsCheckCardNo(req.getIsCheckCardNo());
		
		CommonReq<ReqAddOrderLocal> commReqAddOrder = new CommonReq<ReqAddOrderLocal>(reqAddOrderLocal);
		
		CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(commReqAddOrder);
		if( !addOrderResp.getCode().equals(RetCode.Success.RET_10000.getCode().toString())
				|| StringUtil.isEmpty(addOrderResp.getData())) {
			//如果新增本地订单失败
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CALLADDORDER, "新增全流程订单异常！请联系开发！");
		}
		String orderId = addOrderResp.getData().get(0).getString(ApiKey.AddOrderLocal.OrderId);
		ReqOrderIsPayment reqOrderIsPayment = new ReqOrderIsPayment(req.getMsg(),orderId,req.getOperatorId(),
				req.getOperatorName(), req.getClientId(), "当面付", req.getTotalFee());
		orderService.orderIsPayment(new CommonReq<ReqOrderIsPayment>(reqOrderIsPayment));
		
		com.kasite.core.serviceinterface.module.pay.req.ReqSweepCodePay reqSweepCodePay = 
				new com.kasite.core.serviceinterface.module.pay.req.ReqSweepCodePay(req.getMsg(), req.getTotalFee(), req.getSubject(),
						req.getOrderMemo(), req.getAuthCode(), req.getDeviceInfo(), orderId );
		return payService.sweepCodePay(new CommonReq<com.kasite.core.serviceinterface.module.pay.req.ReqSweepCodePay>(reqSweepCodePay));
	}

	@Override
	public CommonResp<RespMap> createPatientQRCode(CommonReq<ReqCreatePatientQRCode> commReq) throws Exception {
		ReqCreatePatientQRCode req = commReq.getParam();
		Guide guide = new Guide();
		if(KstHosConstant.CARDTYPE_14.equals(req.getCardType())) {//住院腕带付
			/** 新增信息点表 */
			guide.setStatus(KstHosConstant.QRCODE_STATUS_1);
				guide.setTitle("生成腕带二维码");
			JSONObject contentJson = new JSONObject();
			contentJson.put("hisMemberId", req.getHisMemberId());
			contentJson.put("hospitalNo", req.getCardNo());
			contentJson.put("name", req.getName());
			if(!StringUtil.isEmpty(req.getBirthDate())) {
				contentJson.put("birthDate", req.getBirthDate());
			}
			if(!StringUtil.isEmpty(req.getBirthNumber())) {
				contentJson.put("birthNumber", req.getBirthNumber());
			}
			if(!StringUtil.isEmpty(req.getIdCardNo())) {
				contentJson.put("idCardNo", req.getIdCardNo());
			}
			if(!StringUtil.isEmpty(req.getMobile())) {
				contentJson.put("mobile", req.getMobile());
			}
			if(!StringUtil.isEmpty(req.getIsChildren())) {
				contentJson.put("isChildren", req.getIsChildren());
			}
			String content = contentJson.toJSONString();
			guide.setContent(content);
			guide.setUsageType(KstHosConstant.QR_USAGETYPE_2);
			saveGuide(guide);
		}else {//卡面付二维码
			guide.setStatus(KstHosConstant.QRCODE_STATUS_1);
				guide.setTitle("生成就诊卡二维码");
			JSONObject contentJson = new JSONObject();
			contentJson.put("hisMemberId", req.getHisMemberId());
			contentJson.put("cardNo", req.getCardNo());
			contentJson.put("cardType", req.getCardType());
			contentJson.put("name", req.getName());
			if(!StringUtil.isEmpty(req.getBirthDate())) {
				contentJson.put("birthDate", req.getBirthDate());
			}
			if(!StringUtil.isEmpty(req.getBirthNumber())) {
				contentJson.put("birthNumber", req.getBirthNumber());
			}
			if(!StringUtil.isEmpty(req.getIdCardNo())) {
				contentJson.put("idCardNo", req.getIdCardNo());
			}
			if(!StringUtil.isEmpty(req.getMobile())) {
				contentJson.put("mobile", req.getMobile());
			}
			if(!StringUtil.isEmpty(req.getIsChildren())) {
				contentJson.put("isChildren", req.getIsChildren());
			}
			guide.setContent(contentJson.toJSONString());
			guide.setUsageType(KstHosConstant.QR_USAGETYPE_4);
			saveGuide(guide);
		}
		
		/** 生成二维码图片 */
		String qrUrl = KasiteConfig.getQrPayUrl(req.getClientId(),guide.getId().toString());
		if (StringUtil.isBlank(qrUrl)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN, "不存在二维码地址，无法生成二维码，请联系开发人员！");
		}
		String qrPicUrl = MatrixToImageWriter.CreateQRCodePicture(qrUrl, guide.getId().toString());
		RespMap resp = new RespMap();
		resp.put(ApiKey.CreatePatientQRCode.QRUrl, qrUrl);
		resp.put(ApiKey.CreatePatientQRCode.QRPicUrl, qrPicUrl);
		resp.put(ApiKey.CreatePatientQRCode.GuideId, guide.getId());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	
	}
	/**
	 * 是否需要在扫码的时候新增订单
	 * @param clientId
	 * @return
	 */
	private String isCreateOrder(String clientId) {
		if(KstHosConstant.MINIPAY_CHANNEL_ID.equals(clientId)) {
			return "true";
		}
		return "false";
	}
	
	private void saveGuide(Guide guide) {
		String memo = guide.getContent();
		if(StringUtil.isNotBlank(memo) && memo.length() > 1800) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"扩展字段长度不能超过1800 请核对字段长度");
		}
		guideMapper.insertSelective(guide);
	}
	
	@Override
	public CommonResp<RespMap> createPayQRCode(CommonReq<ReqCreatePayQRCode> commReq) throws Exception {
		ReqCreatePayQRCode req = commReq.getParam();

		//判定业务类型是否存在
		String serviceId = req.getServiceId();
		BusinessTypeEnum t = KasiteConfig.getBusinessTypeEnum(serviceId);
		
		Dictionary dictionary = dictLocalCache.get("serviceid", req.getServiceId());
		String priceName = null;
		if( dictionary != null ) {
			priceName = dictionary.getValue();
		}
		if(StringUtil.isBlank(priceName)) {
			priceName = t.getValue();
		}
		
		
		String clientId = req.getClientId();
		String operatorId = req.getOperatorId();
		String operatroName = req.getOperatorName();
		
		JSONObject orderInfoJson = new JSONObject();
		String orderId = CommonUtil.getGUID();
		orderInfoJson.put("orderId",orderId);
		orderInfoJson.put("priceName", priceName);
		orderInfoJson.put("memberName", req.getMemberName());
		orderInfoJson.put("payMoney", req.getTotalFee());
		orderInfoJson.put("cardNo", req.getCardNo());
		orderInfoJson.put("cardType", req.getCardType());
		orderInfoJson.put("operatorId", operatorId);
		orderInfoJson.put("operatorName", operatroName);
		orderInfoJson.put("serviceId", req.getServiceId());
		orderInfoJson.put("clientId", clientId);
		orderInfoJson.put("createOrder", isCreateOrder(clientId));
		orderInfoJson.put("hisMemberId", req.getHisMemberId());
		//在线支付
		orderInfoJson.put("isOnlinePay", 1);
		//2代表mini机 TODO 根据渠道进行判定
		orderInfoJson.put("eqptType", 2);
		
		
		Integer usageType = KstHosConstant.QR_USAGETYPE_1;
		String cid = clientId;
		if(KstHosConstant.MINIPAY_CHANNEL_ID.equals(clientId)) {
			usageType = KstHosConstant.QR_USAGETYPE_1;
			cid = usageType.toString();
		}
		
		/** 新增信息点表 */
		Guide guide = new Guide();
		guide.setStatus(KstHosConstant.QRCODE_STATUS_1);
		guide.setTitle("生成收银台支付二维码");
		guide.setOperatorId(operatorId);
		guide.setOrderId(orderId);
		guide.setOperatorName(operatroName);
		String content = orderInfoJson.toJSONString();
		guide.setContent(content);
		guide.setUsageType(usageType);
		saveGuide(guide);
		
		/** 生成二维码图片 memberId=-1表示未激活的二维码 */
		String qrUrl = KasiteConfig.getQrPayUrl(cid,guide.getId()+"");
		if (StringUtil.isBlank(qrUrl)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN, "不存在二维码地址，无法生成二维码，请联系开发人员！");
		}
		
		String qrPicUrl = MatrixToImageWriter.CreateQRCodePicture(qrUrl, guide.getId().toString());
		/** 返回 */
		RespMap resp = new RespMap();
		String guid = IDSeed.next();
		resp.put(ApiKey.CreatePayQRCode.QRUrl, qrUrl);
		resp.put(ApiKey.CreatePayQRCode.QRPicUrl, qrPicUrl); 
		resp.put(ApiKey.CreatePayQRCode.GuideId, guid);
		resp.put(ApiKey.CreatePayQRCode.OrderId, orderId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> createWristBandCodePay(CommonReq<ReqCreatePayQRCode> commReq) throws Exception {
		ReqCreatePayQRCode req = commReq.getParam();
		/** 返回 */
		RespMap resp = new RespMap();
		
		Dictionary dictionary = dictLocalCache.get("serviceid", req.getServiceId());
		String priceName = null;
		if( dictionary != null ) {
			priceName = dictionary.getValue();
		}
		//判定业务类型是否存在
		String serviceId = req.getServiceId();
		KasiteConfig.getBusinessTypeEnum(serviceId);
		
		String clientId = req.getClientId();
		String operatorId = req.getOperatorId();
		String operatroName = req.getOperatorName();
		
		JSONObject orderInfoJson = new JSONObject();
		String orderId = CommonUtil.getGUID();
		orderInfoJson.put("orderId",orderId);;
		orderInfoJson.put("priceName", priceName);
		orderInfoJson.put("memberName", req.getMemberName());
		orderInfoJson.put("payMoney", req.getTotalFee());
		orderInfoJson.put("cardNo", req.getCardNo());
		orderInfoJson.put("cardType", req.getCardType());
		orderInfoJson.put("operatorId", operatorId);
		orderInfoJson.put("operatorName", operatroName);
		orderInfoJson.put("serviceId", req.getServiceId());
		orderInfoJson.put("clientId", clientId);
		orderInfoJson.put("createOrder", isCreateOrder(clientId));
		orderInfoJson.put("hisMemberId", req.getHisMemberId());
		orderInfoJson.put("autoBindUser", req.getAutoBindUser());
		
		String addUrlParam = null;
		//创建二维码的时候需要调用HIS接口查询相应信息并缓存到信息点中的业务逻辑
		ICreateQrCodeGuidSaveInfoService service = HandlerBuilder.get().getCallHisService(req.getAuthInfo(), ICreateQrCodeGuidSaveInfoService.class);

		if(null != service) {
			
			Map<String, String> paramMap = new HashMap<String, String>(16);
			for (String key : orderInfoJson.keySet()) {
				paramMap.put(key, orderInfoJson.getString(key));
			}
			CommonResp<RespMap> respMap = service.createQrCodeGuidSaveInfoService(req.getMsg(), paramMap);
			if(null != respMap) {
				RespMap respHis = respMap.getDataCaseRetCode();
				if(null != respHis) { 
//					service.checkCreateQrCodeGuidSaveInfoServiceResult(req.getAuthInfo(), respHis);
					for (Map.Entry<ApiKey, Object> entity : respHis.getMap().entrySet()) {
						orderInfoJson.put(entity.getKey().getName(), entity.getValue());
					}
					addUrlParam = respHis.getString(ApiKey.HISCreateQRCode.addUrlParam);
					resp.put(ApiKey.HISCreateQRCode.store, orderInfoJson.get(ApiKey.HISCreateQRCode.store.getName()));
					orderInfoJson.remove(ApiKey.HISCreateQRCode.store.getName());
					orderInfoJson.remove(ApiKey.HISCreateQRCode.addUrlParam.getName());
				}
			}
		}
		//在线支付
		orderInfoJson.put("isOnlinePay", 1);
		orderInfoJson.put("eqptType", 1);
		
		Integer usageType = KstHosConstant.QR_USAGETYPE_2;
		String cid = usageType.toString();
		
		/** 新增信息点表 */
		Guide guide = new Guide();
		guide.setStatus(KstHosConstant.QRCODE_STATUS_1);
		guide.setTitle("生成收银台支付二维码");
		guide.setOperatorId(operatorId);
		guide.setOrderId(orderId);
		guide.setOperatorName(operatroName);
		guide.setContent(orderInfoJson.toJSONString());
		guide.setUsageType(usageType);
		saveGuide(guide);
		
		/** 生成二维码图片 memberId=-1表示未激活的二维码 */
		String qrUrl = KasiteConfig.getQrPayUrl(cid,guide.getId()+"");
		if (StringUtil.isBlank(qrUrl)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN, "不存在二维码地址，无法生成二维码，请联系开发人员！");
		}
		//判断是否要在现有的接口地址后面加参数
		if(StringUtil.isNotBlank(addUrlParam)) {
			int index = qrUrl.lastIndexOf("?");
			if(index >0) {
				qrUrl = qrUrl+"&"+addUrlParam;
			}else {
				qrUrl = qrUrl+"?"+addUrlParam;
			}
		}
		String qrPicUrl = MatrixToImageWriter.CreateQRCodePicture(qrUrl, guide.getId().toString());

		LogUtil.info(log, "666666666666666");
		String guid = IDSeed.next();
		resp.put(ApiKey.CreatePayQRCode.QRUrl, qrUrl);
		resp.put(ApiKey.CreatePayQRCode.QRPicUrl, qrPicUrl); 
		resp.put(ApiKey.CreatePayQRCode.GuideId, guid);
		resp.put(ApiKey.CreatePayQRCode.OrderId, orderId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespMap> getGuide(CommonReq<ReqGetGuide> commReq) throws Exception {
		ReqGetGuide req = commReq.getParam();
		Guide guide = null;
		String id = req.getGuideId();
		if(StringUtil.isNotBlank(id)) {
			guide = guideMapper.selectByPrimaryKey(req.getGuideId());
		}else if(StringUtil.isNotBlank(req.getOrderId())) {
			Example example = new Example(Guide.class);
			example.createCriteria()
			.andEqualTo("orderId", req.getOrderId());
			guide = guideMapper.selectOneByExample(example);
		}
		if (guide == null) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR, "不存在信息点：id=" + req.getGuideId());
		}
		RespMap resp = new RespMap();
		resp.put(ApiKey.GetGuide.Id, guide.getId());
		resp.put(ApiKey.GetGuide.Content, guide.getContent());
		resp.put(ApiKey.GetGuide.USAGETYPE, guide.getUsageType());
		resp.put(ApiKey.GetGuide.OperatorId, guide.getOperatorId());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String CreatePrescriptionQrCode(InterfaceMessage msg) throws Exception {
		return this.createPrescriptionQrCode(new CommonReq<ReqCreatePrescriptionQrCode>(new ReqCreatePrescriptionQrCode(msg))).toResult();
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> createPrescriptionQrCode(CommonReq<ReqCreatePrescriptionQrCode> commReq)
			throws Exception {
		ReqCreatePrescriptionQrCode req = commReq.getParam();
		
		Guide guide = new Guide();
		guide.setStatus(KstHosConstant.QRCODE_STATUS_1);
		guide.setTitle("生成处方付二维码");
		JSONObject contentJson = new JSONObject();
		contentJson.put("memberName", req.getMemberName());
		contentJson.put("doctorName", req.getDoctorName());
		contentJson.put("idCardNo", req.getIdCardNo());
		contentJson.put("hisOrderId", req.getHisOrderId());
		contentJson.put("prescNo", req.getPrescNo());
		contentJson.put("cardNo", req.getCardNo());
		contentJson.put("cardType", req.getCardType());
		guide.setContent(contentJson.toJSONString());
		guide.setUsageType(KstHosConstant.QR_USAGETYPE_3);
		saveGuide(guide);
		
		/** 生成二维码图片 */
		String qrUrl = KasiteConfig.getQrPayUrl(req.getAuthInfo().getClientId(),guide.getId().toString());
		if (StringUtil.isBlank(qrUrl)) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN, "不存在二维码地址，无法生成二维码，请联系开发人员！");
		}
		String qrPicUrl = MatrixToImageWriter.CreateQRCodePicture(qrUrl, guide.getId().toString());
		
		RespMap resp = new RespMap();
		resp.put(ApiKey.CreatePatientQRCode.QRUrl, qrUrl);
		resp.put(ApiKey.CreatePatientQRCode.QRPicUrl, qrPicUrl);
		resp.put(ApiKey.CreatePatientQRCode.GuideId, guide.getId());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String PrescQrValidateBefore(InterfaceMessage msg) throws Exception {
		CommonReq<ReqPrescQrValidateBefore> commReq =new CommonReq<ReqPrescQrValidateBefore>(new ReqPrescQrValidateBefore(msg));
		ReqPrescQrValidateBefore req = commReq.getParam();
		
		ReqGetGuide reqGetGuide = new ReqGetGuide(msg,req.getGuideId());
		RespMap guideResp = this.getGuide(new CommonReq<ReqGetGuide>(reqGetGuide)).getResultData();
		String content = guideResp.getString(ApiKey.GetGuide.Content);
		JSONObject contentJson =  JSON.parseObject(content);	
		//校验身份证号
		if( contentJson.containsKey("idCardNo")) {
			String idCardNo = contentJson.getString("idCardNo");
			if( idCardNo.toLowerCase().contains(req.getIdCardNo().toLowerCase())) {//都小写，防止大小X
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000).toResult();
			}else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_PRESCQRVALIDATEBEFORE).toResult();
			}
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT,"二维码信息点错误！请重新扫描！").toResult();
		}
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryMemberRefundableMoney(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryMemberRefundableMoney> commReq =new CommonReq<ReqQueryMemberRefundableMoney>(new ReqQueryMemberRefundableMoney(msg));
		ReqQueryMemberRefundableMoney reqQueryMemberRefundableMoney = commReq.getParam();
		
		//查询就诊卡余额
		ReqQueryCardBalance reqQueryCardBalance = new ReqQueryCardBalance(msg,
				reqQueryMemberRefundableMoney.getCardNo(), reqQueryMemberRefundableMoney.getCardType());
		RespQueryCardBalance respQueryCardBalance = basicService.queryCardBalance(new CommonReq<ReqQueryCardBalance>(reqQueryCardBalance)).getDataCaseRetCode();		
		int balance = respQueryCardBalance.getBalance().intValue();//可退(单位分)
		
		int refundableBalance = 0;
		IQueryMemberRefundableService service = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo(), IQueryMemberRefundableService.class);
		if(null != service) {//附二专用，非标流程。建议不走此逻辑
			RespQueryMemberList member =  basicService.queryMemberList(new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(msg,
					null, reqQueryMemberRefundableMoney.getCardNo(), 
					reqQueryMemberRefundableMoney.getCardType(), 
					reqQueryMemberRefundableMoney.getOpenId()))).getDataCaseRetCode();
			//有实现HIS查询用户可退余额接口，直接查询HIS
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("cardNo", reqQueryMemberRefundableMoney.getCardNo());
			paramMap.put("memberName", member.getMemberName());
			paramMap.put("channelId", reqQueryMemberRefundableMoney.getClientId());
//			paramMap.put("mCardNo", member.getMcardNo());
			RespMap respMap = service.queryMemberRefundable(new CommonReq<ReqHisMemberRefundable>(new ReqHisMemberRefundable(commReq.getMsg(), paramMap))).getDataCaseRetCode();
			int orderPriceSum = respMap.getInteger(ApiKey.HisQueryCardBalance.Balance);
			refundableBalance = balance>orderPriceSum? orderPriceSum:balance;
		}else {
			//查询全流程可以余额
			ReqQueryRefundableOrderList reqQueryRefundableOrderList = new ReqQueryRefundableOrderList(msg, 
					reqQueryMemberRefundableMoney.getCardNo(),reqQueryMemberRefundableMoney.getCardType(), reqQueryMemberRefundableMoney.getHisMemberId(), 
					KstHosConstant.ORDERTYPE_006,null,null,KasiteConfig.getSelfRefundLimitDateDiy());
			//该查询结果，已按支付时间倒序排序
			List<RespMap> refundableList = orderService.queryRefundableOrderList(new CommonReq<ReqQueryRefundableOrderList>(reqQueryRefundableOrderList)).getListCaseRetCode();
			List<Integer> refundableOrderFeeList = new ArrayList<Integer>();
			for(RespMap respMap : refundableList) {
				Integer orderPrice = respMap.getInteger(ApiKey.QueryRefundableOrderListResp.Price);
				refundableOrderFeeList.add(orderPrice);
			}
			refundableBalance = OrderUtil.calculateRefundabledFee(balance, refundableOrderFeeList);
		}
		RespMap respMap = new RespMap();
		respMap.add(ApiKey.QueryMemberRefundableMoneyResp.Balance, balance);
		respMap.add(ApiKey.QueryMemberRefundableMoneyResp.RefundableBalance,refundableBalance);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respMap).toResult();
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String ApplySelfServiceRefund(InterfaceMessage msg) throws Exception {
		CommonReq<ReqApplySelfServiceRefund> commReq =new CommonReq<ReqApplySelfServiceRefund>(new ReqApplySelfServiceRefund(msg));
		ReqApplySelfServiceRefund reqApplySelfServiceRefund = commReq.getParam();
		//查询是否存在同一个用户的用户自助退费的申请记录
		List<SelfRefundRecord> recordList = selfRefundRecordMapper.queryUnSuccessSelfRefundRecord(reqApplySelfServiceRefund.getCardNo(),
				reqApplySelfServiceRefund.getCardType(), reqApplySelfServiceRefund.getHisMemberId());
		if( recordList!=null && recordList.size()>0 ) {
			//如果已经存在未成功的操作记录，则直接返回。
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANCELORDER,"该卡号["+reqApplySelfServiceRefund.getCardNo()+"]上次自助退费未执行完成！请勿重复操作！").toResult();
		}
		//查询本地可退订单
		ReqQueryRefundableOrderList reqQueryRefundableOrderList = new ReqQueryRefundableOrderList(msg, 
				reqApplySelfServiceRefund.getCardNo(),reqApplySelfServiceRefund.getCardType(), reqApplySelfServiceRefund.getHisMemberId(), 
				KstHosConstant.ORDERTYPE_006,null,null,KasiteConfig.getSelfRefundLimitDateDiy());
		List<RespMap> refundableList = orderService.queryRefundableOrderList(new CommonReq<ReqQueryRefundableOrderList>(reqQueryRefundableOrderList)).getListCaseRetCode();
		//查询HIS余额
		ReqQueryCardBalance reqQueryCardBalance = new ReqQueryCardBalance(msg,
				reqApplySelfServiceRefund.getCardNo(), reqApplySelfServiceRefund.getCardType());
		RespQueryCardBalance respQueryCardBalance = basicService.queryCardBalance(new CommonReq<ReqQueryCardBalance>(reqQueryCardBalance)).getDataCaseRetCode();		
		int balance = respQueryCardBalance.getBalance().intValue();//卡余额(单位分)
		List<Integer> refundableOrderFeeList = new ArrayList<Integer>();
		for(RespMap respMap : refundableList) {
			Integer orderPrice = respMap.getInteger(ApiKey.QueryRefundableOrderListResp.Price);
			refundableOrderFeeList.add(orderPrice);
		}
		//再次计算可退余额，与前端传入的可退金额进行比较，防止前端篡改
		int calculateRefundabledFee = OrderUtil.calculateRefundabledFee(balance, refundableOrderFeeList);
		if( reqApplySelfServiceRefund.getRefundableBalance().intValue()!= calculateRefundabledFee ) {
			//前端传入可退金额、与后台再次计算的结果不一致
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERPRICE,"可退金额异常，请重新刷新页面再申请!").toResult();
		}
		List<SelfRefundRecordOrder> selfRefundRecordOrderList = new ArrayList<SelfRefundRecordOrder>();
		SelfRefundRecord selfRefundRecord = new SelfRefundRecord();
		selfRefundRecord.setBalance(balance);
		selfRefundRecord.setCardNo(reqApplySelfServiceRefund.getCardNo());
		selfRefundRecord.setCardType(reqApplySelfServiceRefund.getCardType());
		selfRefundRecord.setHisMemberId(reqApplySelfServiceRefund.getHisMemberId());
		selfRefundRecord.setRefundableBalance(reqApplySelfServiceRefund.getRefundableBalance());
		selfRefundRecord.setCreateTime(DateOper.getNowDateTime());
		selfRefundRecord.setUpdateTime(DateOper.getNowDateTime());
		selfRefundRecord.setOperatorId(reqApplySelfServiceRefund.getOpenId());
		selfRefundRecord.setOperatorName(reqApplySelfServiceRefund.getOperatorName());
		selfRefundRecord.setState(0);
		selfRefundRecord.setMemberId(reqApplySelfServiceRefund.getMemberId());
		selfRefundRecord.setRefundableCount(refundableList.size());
		selfRefundRecordMapper.insert(selfRefundRecord);
		for(RespMap respMap : refundableList) {
			String orderId = respMap.getString(ApiKey.QueryRefundableOrderListResp.OrderId);
			SelfRefundRecordOrder refundRecordOrder = new SelfRefundRecordOrder();
			refundRecordOrder.setOrderId(orderId);
			refundRecordOrder.setSelfRefundRecordId(selfRefundRecord.getId());
			selfRefundRecordOrderMapper.insert(refundRecordOrder);
		}
		//先返回用户成功，进入异步退款
		KstHosConstant.cachedThreadPool.execute(new SelfServiceRefundThread(orderService,msg,
				selfRefundRecord,selfRefundRecordMapper,selfRefundRecordOrderMapper,selfRefundRecordOrderList,refundableList));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,"申请成功，正在退费中！").toResult();
	}
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QuerySelfRefundRecordList(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQuerySelfRefundRecordList> commReq =new CommonReq<ReqQuerySelfRefundRecordList>(new ReqQuerySelfRefundRecordList(msg));
		ReqQuerySelfRefundRecordList req = commReq.getParam(); 
		Example example = new Example(SelfRefundRecord.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cardNo", req.getCardNo());
		criteria.andEqualTo("cardType", req.getCardType());
		criteria.andEqualTo("memberId", req.getMemberId());
		if( !StringUtil.isEmpty(req.getHisMemberId())) {
			criteria.andEqualTo("hisMemberId", req.getHisMemberId());
		}
		example.setOrderByClause("CREATETIME DESC");
		
		List<SelfRefundRecord> recordList = selfRefundRecordMapper.selectByExample(example);
		List<RespMap> respMapList = new ArrayList<RespMap>();
		for(SelfRefundRecord selfRefundRecord : recordList) {
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.QuerySelfRefundRecordList.RefundableBalance, selfRefundRecord.getRefundableBalance());
			respMap.put(ApiKey.QuerySelfRefundRecordList.RefundableCount, selfRefundRecord.getRefundableCount());
			respMap.put(ApiKey.QuerySelfRefundRecordList.RefundAmount, selfRefundRecord.getRefundAmount());
			respMap.put(ApiKey.QuerySelfRefundRecordList.RefundCount, selfRefundRecord.getRefundCount());
			respMap.put(ApiKey.QuerySelfRefundRecordList.UpdateTime, DateOper.formatDate(selfRefundRecord.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			respMap.put(ApiKey.QuerySelfRefundRecordList.State, selfRefundRecord.getState());
			respMap.put(ApiKey.QuerySelfRefundRecordList.Id, selfRefundRecord.getId());
			respMapList.add(respMap);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respMapList).toResult();
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QuerySelfRefundRecordInfo(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQuerySelfRefundRecordInfo> commReq =new CommonReq<ReqQuerySelfRefundRecordInfo>(new ReqQuerySelfRefundRecordInfo(msg));
		ReqQuerySelfRefundRecordInfo req = commReq.getParam(); 
		List<RefundOrderVo>  refundList = selfRefundRecordOrderMapper.querySelfRefundOrderInfoList(new Long(req.getSelfRefundRecordId()));
		List<RespMap> respMapList = new ArrayList<RespMap>();
		refundList.forEach(refundOrderVo -> {
			ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(refundOrderVo.getPayChannelId(),refundOrderVo.getConfigKey());
			//ChannelTypeEnum channelTypeEnum = ChannelTypeEnum.zfb;//测试用
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.PayState, refundOrderVo.getPayState());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.OrderNum, refundOrderVo.getOrderNum());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.RefundPrice, refundOrderVo.getRefundPrice());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.ChannelType, channelTypeEnum.getTitle());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.RefundRemark, refundOrderVo.getRemark());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.BeginDate, refundOrderVo.getBeginDate());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.EndDate, refundOrderVo.getEndDate());
			respMap.put(ApiKey.QuerySelfRefundRecordInfo.FailReason,refundOrderVo.getFailReason() );
			respMapList.add(respMap);
		});
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respMapList).toResult();
	}
	@Override
	public String CreateWristBandCodePay(InterfaceMessage msg) throws Exception {
		return this.createWristBandCodePay(new CommonReq<ReqCreatePayQRCode>(new ReqCreatePayQRCode(msg))).toResult();
	}
	
}
