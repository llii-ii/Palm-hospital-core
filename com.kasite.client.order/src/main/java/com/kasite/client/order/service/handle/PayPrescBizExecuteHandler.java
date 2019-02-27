package com.kasite.client.order.service.handle;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IOrderPrescriptionPaymentService;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 处方支付
 */
@Service
public class PayPrescBizExecuteHandler implements IBizExecuteHandler{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	IMsgService msgService;
	
	@Autowired
	protected IReportFormsService reportFormsUtil;
	
	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_008;
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public RespPayEndBizOrderExecute bizPayEndExecute(CommonReq<ReqPayEndBizOrderExecute> commonReq) throws Exception {
		ReqPayEndBizOrderExecute reqPayEndBizOrderExecute = commonReq.getParam();
		String orderId = reqPayEndBizOrderExecute.getOrderId();
		ReqOrderDetailLocal reqOrderDetailLocal = new ReqOrderDetailLocal(reqPayEndBizOrderExecute.getMsg(),orderId,null);
		CommonResp<RespOrderDetailLocal> orderResp = orderService.orderDetailLocal(new CommonReq<ReqOrderDetailLocal>(reqOrderDetailLocal));
		RespOrderDetailLocal respOrderDetailLocal = orderResp.getData().get(0);
		/** 诊间支付-校验HIS订单金额与支付交易金额是否一致、不一致直接退费 */
		Map<String, String> map = new HashMap<String, String>(16);
		map.put("hisOrderId", respOrderDetailLocal.getPrescNo());
		map.put("orderId", respOrderDetailLocal.getOrderId());
		map.put("cardNo", respOrderDetailLocal.getCardNo());
		map.put("cardType", respOrderDetailLocal.getCardType());
		map.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		map.put("orderMemo", respOrderDetailLocal.getOrderMemo());
		IOrderPrescriptionPaymentService orderPrescriptionPaymentService = HandlerBuilder.get()
				.getCallHisService(reqPayEndBizOrderExecute.getAuthInfo(),IOrderPrescriptionPaymentService.class);
		if(orderPrescriptionPaymentService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderPrescriptionPaymentService ");
		}else {
			CommonResp<HisQueryOrderPrescriptionInfo> hisResp = orderPrescriptionPaymentService
					.queryOrderPrescriptionInfo(reqPayEndBizOrderExecute.getMsg(),map);
			RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
			if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode() )) {
				if( !CollectionUtils.isEmpty(hisResp.getData())) {
					Integer payMoney = hisResp.getData().get(0).getPayMoney();
					if (respOrderDetailLocal.getPayMoney().equals(payMoney)) {
						try {
							return this.callPayOrderPrescription(reqPayEndBizOrderExecute.getAuthInfo(),reqPayEndBizOrderExecute.getMsg(),respOrderDetailLocal,
									orderPrescriptionPaymentService);
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.error(log, e);
							/** 失败重试 */
							respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
							return respPayEndBizOrderExecute;
						}
					} else {
						/** 不一致退款 */
						respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_0);
						return respPayEndBizOrderExecute;
					}
				}else {
					/** 失败重试 */
					respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
					return respPayEndBizOrderExecute;
				}
			}else {
				/** 失败重试 */
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}
		}
		
	}
	/**
	 * 调用HIS处方支付完成
	 * @param vo
	 * @param msg
	 * @param respOrderDetailLocal
	 * @return
	 * @throws Exception
	 */
	private RespPayEndBizOrderExecute callPayOrderPrescription(AuthInfoVo vo,InterfaceMessage msg,RespOrderDetailLocal respOrderDetailLocal,
			IOrderPrescriptionPaymentService orderPrescriptionPaymentService) throws Exception {		
		Map<String, String> map = new HashMap<String, String>(16);
		map.put("hisOrderId", respOrderDetailLocal.getPrescNo());
		map.put("orderId", respOrderDetailLocal.getOrderId());
		map.put("price", respOrderDetailLocal.getPayMoney().toString());
		map.put("transNo", respOrderDetailLocal.getTransactionNo());
		map.put("transTime", respOrderDetailLocal.getBeginDate().toString());
		map.put("channelId", respOrderDetailLocal.getChannelId());
		map.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		map.put("orderMemo", respOrderDetailLocal.getOrderMemo());
		map.put("cardNo", respOrderDetailLocal.getCardNo());
		//调用his支付完成  这个逻辑是在微信钱收到后调用的
		CommonResp<RespMap> commResp = orderPrescriptionPaymentService.payOrderPrescription(msg, map);
		RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
		if( KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
			
			Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
			XMLUtil.addElement(data1,"OrderId", respOrderDetailLocal.getOrderId());
			XMLUtil.addElement(data1,"Name", respOrderDetailLocal.getMemberName());
			XMLUtil.addElement(data1, "price", CommonUtil.div("" + respOrderDetailLocal.getPayMoney(), "100", 2));
			XMLUtil.addElement(data1, "transTime", respOrderDetailLocal.getBeginDate().toString());
			try {
				ReqSendMsg queue = new ReqSendMsg(msg,respOrderDetailLocal.getCardNo(), 1, respOrderDetailLocal.getChannelId(), "", "",
						"", KstHosConstant.MODETYPE_10101117, data1.asXML(), respOrderDetailLocal.getOperatorId(), 1, respOrderDetailLocal.getOperatorId(), respOrderDetailLocal.getOperatorName(), respOrderDetailLocal.getOrderId(), "", "", "");
				CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
				CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
				if (!KstHosConstant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
					LogUtil.info(log, "发送处方支付消息异常：YYOrderId="+respOrderDetailLocal.getOrderId()+"|||Result="+addMsgQueue.getMessage(),msg.getAuthInfo());
				}
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log,vo,e);
			}
			
			// 诊间支付笔数+1推送到云报表
			reportFormsUtil.dataCloudCollection(msg,respOrderDetailLocal.getChannelId(), 107, 1, "1");
			respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_1);
			return respPayEndBizOrderExecute;
		}else {
			// 失败重试
			respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
			return respPayEndBizOrderExecute;
		}
		
		
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> bizRefundEndExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public BizDealState bizCheckExecute(CommonReq<ReqPayEndBizOrderExecute> commonReq) throws Exception {
		ReqPayEndBizOrderExecute reqPayEndBizOrderExecute = commonReq.getParam();
		String orderId = reqPayEndBizOrderExecute.getOrderId();
		ReqOrderDetailLocal reqOrderDetailLocal = new ReqOrderDetailLocal(reqPayEndBizOrderExecute.getMsg(),orderId,null);
		CommonResp<RespOrderDetailLocal> orderResp = orderService.orderDetailLocal(new CommonReq<ReqOrderDetailLocal>(reqOrderDetailLocal));		
		RespOrderDetailLocal respOrderDetailLocal = orderResp.getData().get(0);
		//处方订单	
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.hisOrderId.name() ,respOrderDetailLocal.getPrescNo());
		//map.put("prescNo", req.getPrescNo());
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.cardNo.name(), respOrderDetailLocal.getCardNo());
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.cardType.name(), respOrderDetailLocal.getCardType());
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.orderMemo.name(), respOrderDetailLocal.getOrderMemo());
		map.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		IOrderPrescriptionPaymentService orderPrescriptionPaymentService = HandlerBuilder.get()
				.getCallHisService(reqPayEndBizOrderExecute.getAuthInfo(),IOrderPrescriptionPaymentService.class);
		if(orderPrescriptionPaymentService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderPrescriptionPaymentService ");
		}else {
			CommonResp<HisQueryOrderPrescriptionInfo> hisResp = orderPrescriptionPaymentService
					.queryOrderPrescriptionInfo(commonReq.getMsg(),map);
			if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode() )) {
				if( !CollectionUtils.isEmpty(hisResp.getData())) {
					String hisOrderState = hisResp.getData().get(0).getOrderState().toString();		
					if(KstHosConstant.HISORDERSTATE_2.toString().equals(hisOrderState)) {
						//如果HIS已经支付成功，则返回HIS处理结果成功状态
						return BizDealState.BIZ_DEAL_STATE_1;
					}
				}
			}
			return BizDealState.BIZ_DEAL_STATE_2;
		}
		
	}

}
