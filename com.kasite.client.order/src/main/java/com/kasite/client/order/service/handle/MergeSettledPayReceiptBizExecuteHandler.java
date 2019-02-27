package com.kasite.client.order.service.handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IMergeSettledPayReceiptService;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSubList;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderSubList;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;

/**
 * @author linjf
 * 合并支付单据
 */
@Service
public class MergeSettledPayReceiptBizExecuteHandler implements IBizExecuteHandler{
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
		return BusinessTypeEnum.ORDERTYPE_011;
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
		
		Map<String, String> map = new HashMap<String, String>(16);
		map.put("price", respOrderDetailLocal.getPayMoney().toString());
		map.put("transNo", respOrderDetailLocal.getTransactionNo());
		map.put("transTime", respOrderDetailLocal.getBeginDate().toString());
		map.put("channelId", respOrderDetailLocal.getChannelId());
		map.put("cardNo", respOrderDetailLocal.getCardNo());
		map.put("cardType", respOrderDetailLocal.getCardType());
		map.put("memberName", respOrderDetailLocal.getMemberName());
		map.put("idCardNo", respOrderDetailLocal.getIdCardNo());
		map.put("mobile", respOrderDetailLocal.getMobile());
		map.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		map.put("orderId", respOrderDetailLocal.getOrderId());
		ReqQueryOrderSubList reqQueryOrderSubList = new ReqQueryOrderSubList(reqPayEndBizOrderExecute.getMsg(), respOrderDetailLocal.getOrderId(), null);
		List<RespQueryOrderSubList> list = orderService.queryOrderSubList(new CommonReq<ReqQueryOrderSubList>(reqQueryOrderSubList)).getData();
		if( list!=null && list.size() > 0 ) {
			String subHisOrderIds = "";
			String receiptHisRegIds = "";
			for(RespQueryOrderSubList respOrderSub : list) {
				subHisOrderIds+=respOrderSub.getSubHisOrderId()+",";
				receiptHisRegIds+=respOrderSub.getHisRegId()+",";
			}
			map.put("receiptNos", subHisOrderIds);
			map.put("receiptHisRegIds", receiptHisRegIds);
		}
		IMergeSettledPayReceiptService service = HandlerBuilder.get().getCallHisService(reqPayEndBizOrderExecute.getAuthInfo(),IMergeSettledPayReceiptService.class);
		CommonResp<RespMap> commResp = service.mergeSettledPayReceipt(reqPayEndBizOrderExecute.getMsg(), map);
		String hisFlowNo = commResp.getResultData().getString(ApiKey.HisMergeSettledPayReceiptResp.HisFlowNo);
		RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
		if( KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
			Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101117.transTime, respOrderDetailLocal.getBeginDate());
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101117.orderNum, respOrderDetailLocal.getOrderNum());
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101117.price, StringUtil.fenChangeYuan(respOrderDetailLocal.getPayMoney()));
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101117.operatorName, respOrderDetailLocal.getOperatorName());
			XMLUtil.addElement(data1, ApiKey.MODETYPE_10101117.memberName, respOrderDetailLocal.getMemberName());
			try {
				ReqSendMsg queue = new ReqSendMsg(reqPayEndBizOrderExecute.getMsg(),"", 1, respOrderDetailLocal.getChannelId(), "", "",
						"", KstHosConstant.MODETYPE_10101117, data1.asXML(), respOrderDetailLocal.getOperatorId(), 1, respOrderDetailLocal.getOperatorId(), respOrderDetailLocal.getOperatorId(), orderId, "", "", "");
				CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
				msgService.sendMsg(reqSendMsg);
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log,reqPayEndBizOrderExecute.getAuthInfo(),e);
				//LogUtil.error(log, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId()).set("入参", req).set("出参", resp.toResult()));	
			}
			// 诊间支付笔数+1推送到云报表
			reportFormsUtil.dataCloudCollection(reqPayEndBizOrderExecute.getMsg(),respOrderDetailLocal.getChannelId(), 107, 1, "1");
			respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_1);
			respPayEndBizOrderExecute.setOutBizOrderId(hisFlowNo);
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
		Map<String, String> map = new HashMap<String, String>(16);
		map.put("price", respOrderDetailLocal.getPayMoney().toString());
		map.put("transNo", respOrderDetailLocal.getTransactionNo());
		map.put("transTime", respOrderDetailLocal.getBeginDate().toString());
		map.put("channelId", respOrderDetailLocal.getChannelId());
		map.put("cardNo", respOrderDetailLocal.getCardNo());
		map.put("cardType", respOrderDetailLocal.getCardType());
		map.put("memberName", respOrderDetailLocal.getMemberName());
		map.put("idCardNo", respOrderDetailLocal.getIdCardNo());
		map.put("mobile", respOrderDetailLocal.getMobile());
		map.put("hisMemberId", respOrderDetailLocal.getHisMemberId());
		map.put("orderId", respOrderDetailLocal.getOrderId());
		ReqQueryOrderSubList reqQueryOrderSubList = new ReqQueryOrderSubList(reqPayEndBizOrderExecute.getMsg(), respOrderDetailLocal.getOrderId(), null);
		List<RespQueryOrderSubList> list = orderService.queryOrderSubList(new CommonReq<ReqQueryOrderSubList>(reqQueryOrderSubList)).getData();
		if( list!=null && list.size() > 0 ) {
			String subHisOrderIds = "";
			for(RespQueryOrderSubList respOrderSub : list) {
				subHisOrderIds+=respOrderSub.getSubHisOrderId()+",";
			}
			map.put("receiptNos", subHisOrderIds);
		}
		IMergeSettledPayReceiptService service = HandlerBuilder.get().getCallHisService(reqPayEndBizOrderExecute.getAuthInfo(),IMergeSettledPayReceiptService.class);
		if(null == service) {
			throw new RRException("该医院未实现合并支付的接口： IMergeSettledPayReceiptService ");
		}
		CommonResp<RespMap> hisResp = service.queryMergeSettledPayReceipt(commonReq.getMsg(),map);
		if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode() )) {
			if( !CollectionUtils.isEmpty(hisResp.getData())) {
				String hisOrderState = hisResp.getData().get(0).getString(ApiKey.QueryOrderState.OrderState);	
				if(KstHosConstant.HISORDERSTATE_2.toString().equals(hisOrderState)) {
					//如果HIS已经支付成功，则返回HIS处理结果成功状态
					return BizDealState.BIZ_DEAL_STATE_1;
				}
			}
		}
		return BizDealState.BIZ_DEAL_STATE_2;
	}

}
