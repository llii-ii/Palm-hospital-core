package com.kasite.client.order.service.handle;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisOPDRecharge;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;

/**
 * @author linjf
 * 门诊充值
 */
@Service
public class OPRechargeBizExecuteHandler implements IBizExecuteHandler{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	IMsgService msgService;
	
	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_006;
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
		String payWay = null;
		
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(respOrderDetailLocal.getPayChannelId(),reqPayEndBizOrderExecute.getConfigKey());
		if (ChannelTypeEnum.wechat.equals(channelTypeEnum)) {
			payWay = "3";
		} else if (ChannelTypeEnum.zfb.equals(channelTypeEnum)) {
			payWay = "1";
		}
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("orderId", respOrderDetailLocal.getOrderId());
		paramMap.put("cardType", respOrderDetailLocal.getCardType());
		paramMap.put("cardNo", respOrderDetailLocal.getCardNo());
		paramMap.put("price", respOrderDetailLocal.getPayMoney() + "");
		paramMap.put("payWay", payWay);
		paramMap.put("transNo", respOrderDetailLocal.getTransactionNo());
		paramMap.put("memberName", respOrderDetailLocal.getMemberName());
		paramMap.put("mobile", respOrderDetailLocal.getMobile());
		paramMap.put("idCardNo", respOrderDetailLocal.getIdCardNo());
		paramMap.put("channelId", respOrderDetailLocal.getChannelId());
		paramMap.put("configKey", reqPayEndBizOrderExecute.getConfigKey());
		paramMap.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		// 调用His充值接口
		HisOPDRecharge hisOPDRechargeResp = HandlerBuilder.get().getCallHisService(reqPayEndBizOrderExecute.getAuthInfo())
				.oPDRecharge(reqPayEndBizOrderExecute.getMsg(), respOrderDetailLocal.getOrderId(), paramMap).getDataCaseRetCode();
		
		RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
		if (hisOPDRechargeResp == null) {
			//异常返回失败
			respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_0);
			return respPayEndBizOrderExecute;
		}
		try {
			//发送充值成功消息
			String memberName = respOrderDetailLocal.getMemberName();
			if(StringUtil.isBlank(memberName)) {
				memberName = respOrderDetailLocal.getCardNo();
			}
			Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
			
			
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101119.CardTypeName, "就诊卡");
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101119.Price, CommonUtil.div("" + respOrderDetailLocal.getPayMoney(), "100", 2));
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101119.UserName, memberName);
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101119.URL, KasiteConfig.getServiceSuccessMessageUrl(accept(),
					reqPayEndBizOrderExecute.getClientId(), reqPayEndBizOrderExecute.getConfigKey(), orderId));
			//消息推送走消息中心
			ReqSendMsg queue = new ReqSendMsg(reqPayEndBizOrderExecute.getMsg(),respOrderDetailLocal.getCardNo(), 1, respOrderDetailLocal.getChannelId(), "", "",
					"", KstHosConstant.MODETYPE_10101119, data1.asXML(), respOrderDetailLocal.getOperatorId(), 1, respOrderDetailLocal.getOperatorId(), respOrderDetailLocal.getOperatorName(), respOrderDetailLocal.getOrderId(), "", "", "");
			CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
			CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
			if (!KstHosConstant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
				LogUtil.info(log, "发送门诊充值消息异常：YYOrderId="+respOrderDetailLocal.getOrderId()+"|||Result="+addMsgQueue.getMessage(),reqPayEndBizOrderExecute.getMsg().getAuthInfo());
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_1);
		respPayEndBizOrderExecute.setOutBizOrderId(hisOPDRechargeResp.getHisFlowNo());
		return respPayEndBizOrderExecute;
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
	public BizDealState bizCheckExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception {
		ReqPayEndBizOrderExecute reqPayEndBizOrderExecute = req.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("orderId", reqPayEndBizOrderExecute.getOrderId());
		paramMap.put("serialNo", reqPayEndBizOrderExecute.getTransActionNo());
		paramMap.put("cardNo", reqPayEndBizOrderExecute.getCardNo());
		paramMap.put("cardType", reqPayEndBizOrderExecute.getCardType());
		paramMap.put("orderBeginDate", reqPayEndBizOrderExecute.getOrderBeginDate());
		HandlerBuilder.get().getCallHisService(reqPayEndBizOrderExecute.getAuthInfo()).
		queryHosOutpatientRecords(reqPayEndBizOrderExecute.getMsg(), paramMap).getDataCaseRetCode();
		return BizDealState.BIZ_DEAL_STATE_1;
	}

}
