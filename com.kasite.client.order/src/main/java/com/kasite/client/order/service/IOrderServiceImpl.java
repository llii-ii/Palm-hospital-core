package com.kasite.client.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kasite.client.order.bean.dbo.OrderItem;
import com.kasite.client.order.bean.dbo.OrderOutBiz;
import com.kasite.client.order.bean.dbo.RefundOrder;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.dto.OrderTransLogVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderVo;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqForceCorrectOrderBiz;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsPayment;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderListLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderOutBizNotify;
import com.kasite.core.serviceinterface.module.order.req.ReqPayForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderItemList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPayState;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderProcess;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSubList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryRefundableOrderList;
import com.kasite.core.serviceinterface.module.order.req.ReqRevokeOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryLocalTransLogInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderItemList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryQLCOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespRefund;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * @author linjf TODO
 */
@Service("order.orderApi")
public class IOrderServiceImpl extends AbstractOrderCommonService implements IOrderService {
	
	@Override
	public String AddOrderLocal(InterfaceMessage msg) throws Exception {
		return super.addOrderLocal(new CommonReq<ReqAddOrderLocal>(new ReqAddOrderLocal(msg))).toResult();
	}
	
	@Override
	public String OrderListLocal(InterfaceMessage msg) throws Exception {
		return super.orderListLocal(new CommonReq<ReqOrderListLocal>(new ReqOrderListLocal(msg))).toResult();
	}

	@Override
	public String OrderDetailLocal(InterfaceMessage msg) throws Exception {
		return super.orderDetailLocal(new CommonReq<ReqOrderDetailLocal>(new ReqOrderDetailLocal(msg))).toResult();
	}

	@Override
	public String OrderIsPayment(InterfaceMessage msg) throws Exception {
		return super.orderIsPayment(new CommonReq<ReqOrderIsPayment>(new ReqOrderIsPayment(msg))).toResult();
	}
	
	@Override
	public String PayForCompletion(InterfaceMessage msg) throws Exception {
		return super.payForCompletion(new CommonReq<ReqPayForCompletion>(new ReqPayForCompletion(msg))).toResult();
	}
	
	@Override
	public String BizForCompletion(InterfaceMessage msg) throws Exception {
		return super.bizForCompletion(new CommonReq<ReqBizForCompletion>(new ReqBizForCompletion(msg))).toResult();
	}

	@Override
	public String BizForCancel(InterfaceMessage msg) throws Exception{
		return super.bizForCancel(new CommonReq<ReqBizForCancel>(new ReqBizForCancel(msg))).toResult();
	}
	
	@Override
	public String OrderIsCancel(InterfaceMessage msg) throws Exception{
		return super.orderIsCancel(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg))).toResult();
	}
	@Override
	public String OrderIsCancel_V1(InterfaceMessage msg) throws Exception{
		return super.orderIsCancel(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg))).toResult();
	}
	@Override
	public String CancelForCompletion(InterfaceMessage msg) throws Exception{
		return super.cancelForCompletion(new CommonReq<ReqCancelForCompletion>(new ReqCancelForCompletion(msg))).toResult();
	}

	/**
	 * 业务订单执行取消
	 * 内部执行业务取消逻辑 并如果有退费的调用退费业务
	 * 
	 * 1.先调用取消业务逻辑
	 * 2.取消成功后调用退费逻辑
	 * 3.退费完成后调用执行订单取消完成逻辑
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String BizOrderCancel(InterfaceMessage msg) throws Exception{
		
		
		
		
		return null;
	}
	
	
	@Override
	public String CancelOrder(InterfaceMessage msg) throws Exception{
		return super.cancelOrder(new CommonReq<ReqCancelOrder>(new ReqCancelOrder(msg))).toResult();
	}
	
	@Override
	public String RevokeOrder(InterfaceMessage msg) throws Exception{
		return super.revokeOrder(new CommonReq<ReqRevokeOrder>(new ReqRevokeOrder(msg))).toResult();
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOrderProcess(InterfaceMessage msg) throws Exception {
		return super.queryOrderProcess(new CommonReq<ReqQueryOrderProcess>(new ReqQueryOrderProcess(msg))).toResult();
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOrderState(InterfaceMessage msg) throws Exception {
		return super.queryOrderState(new CommonReq<ReqQueryOrderPayState>(new ReqQueryOrderPayState(msg))).toResult();
	}
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOrderState_V1(InterfaceMessage msg) throws Exception {
		return super.queryOrderState(new CommonReq<ReqQueryOrderPayState>(new ReqQueryOrderPayState(msg))).toResult();
	}
	/**
	 * 查询退款订单 兼容附二
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryTuiFeiOrderState_V1(InterfaceMessage msg) throws Exception {
		 CommonReq<ReqQueryOrderPayState> req = new CommonReq<ReqQueryOrderPayState>(new ReqQueryOrderPayState(msg));
		 CommonResp<RespMap> commMap = super.queryOrderState(new CommonReq<ReqQueryOrderPayState>(new ReqQueryOrderPayState(msg)));
		 String code = commMap.getCode();
		 RespMap resp = new RespMap();
		if (commMap.getResultData() != null) {
			resp.put(ApiKey.SweepCodePay.TransactionId, commMap.getResultData().getString(ApiKey.SweepCodePay.TransactionId));
			Integer state = commMap.getResultData().getInteger(ApiKey.QueryOrderState.OrderState);
			if(state==4){
				resp.put(ApiKey.QueryOrderState.OrderState,1);
			}
			else{
				resp.put(ApiKey.QueryOrderState.OrderState,-1);
			}
		}
		if (RetCode.Success.RET_10000.getCode().toString().equals(code)) {
			return new CommonResp<>(req, RetCode.Success.RET_10000, resp).toNotDataResult();
		} else {
			return commMap.toNotDataResult();
		}
	}
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String ForceCorrectOrderBiz(InterfaceMessage msg) throws Exception {
		return super.forceCorrectOrderBiz(new CommonReq<ReqForceCorrectOrderBiz>(new ReqForceCorrectOrderBiz(msg))).toResult();
	}

	
	@Override
	public String OrderOutBizNotify(InterfaceMessage msg) throws Exception {
		CommonReq<ReqOrderOutBizNotify> commReq = new CommonReq<ReqOrderOutBizNotify>(new ReqOrderOutBizNotify(msg));
		ReqOrderOutBizNotify reqOrderOutBizNotify = commReq.getParam();
		
		OrderOutBiz queryParam = new OrderOutBiz();
		queryParam.setRefundOrderId(reqOrderOutBizNotify.getRefundOrderId());
		queryParam.setOrderId(reqOrderOutBizNotify.getOrderId());
		queryParam.setOutBizType(reqOrderOutBizNotify.getOutBizType());
		int count = orderOutBizMapper.selectCount(queryParam);
		if( count>0) {
			//如果已经回调过，则直接返回成功。
			return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,"已经通知成功！").toResult(); 
		}
		
		OrderOutBiz orderOutBiz = new OrderOutBiz();
		orderOutBiz.setOrderId(reqOrderOutBizNotify.getOrderId());
		orderOutBiz.setChannelId(reqOrderOutBizNotify.getClientId());
		orderOutBiz.setCreateDate(DateOper.getNowDateTime());
		orderOutBiz.setUpdateDate(DateOper.getNowDateTime());
		orderOutBiz.setOperatorId(reqOrderOutBizNotify.getOperatorId());
		orderOutBiz.setOperatorName(reqOrderOutBizNotify.getOperatorName());
		orderOutBiz.setOutBizType(reqOrderOutBizNotify.getOutBizType());
		orderOutBiz.setRefundOrderId(reqOrderOutBizNotify.getRefundOrderId());
		orderOutBizMapper.insert(orderOutBiz);
		return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000).toResult();
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryRefundableOrderList(InterfaceMessage msg) throws Exception {
		return this.queryRefundableOrderList( new CommonReq<ReqQueryRefundableOrderList>(new ReqQueryRefundableOrderList(msg))).toResult();
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> queryRefundableOrderList(CommonReq<ReqQueryRefundableOrderList> commReq)
			throws Exception {
		ReqQueryRefundableOrderList req = commReq.getParam();
		String refundLimitDates = KasiteConfig.getHistoryOrderListDays().toString();//历史订单限制条件
		if( !StringUtil.isEmpty(req.getRefundLimitDates())) {
			refundLimitDates = req.getRefundLimitDates();
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("cardNo", req.getCardNo());
		queryMap.put("cardType", req.getCardType());
		queryMap.put("hisMemberId", req.getHisMemberId());
		queryMap.put("serviceId", req.getServiceId());
		queryMap.put("transactionNo", req.getTransactionNo());
		queryMap.put("channelId", req.getChannelId());
		queryMap.put("limitDate", refundLimitDates);
		List<OrderView> list =orderMapper.queryRefundableOrderList(queryMap);
		List<RespMap> respMapList = new ArrayList<RespMap>();
		if( !CollectionUtils.isEmpty(list)) {
			for(OrderView orderView : list) {
				RespMap respMap = new RespMap();
				respMap.put(ApiKey.QueryRefundableOrderListResp.OrderId, orderView.getOrderId());
				respMap.put(ApiKey.QueryRefundableOrderListResp.ServiceId, orderView.getServiceId());
				respMap.put(ApiKey.QueryRefundableOrderListResp.Price, orderView.getPrice());
				respMap.put(ApiKey.QueryRefundableOrderListResp.TotalPrice, orderView.getTotalPrice());
				respMap.put(ApiKey.QueryRefundableOrderListResp.ChannelId, orderView.getChannelId());
				respMap.put(ApiKey.QueryRefundableOrderListResp.PayState,KstHosConstant.ORDERPAY_2);//可退订单，只能是已支付的
				respMap.put(ApiKey.QueryRefundableOrderListResp.TransactionNo, orderView.getTransactionNo());
				respMap.put(ApiKey.QueryRefundableOrderListResp.BeginDate, DateOper.formatDate(orderView.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
				respMap.put(ApiKey.QueryRefundableOrderListResp.ConfigKey, orderView.getConfigKey());
				respMapList.add(respMap);
			}
		}
		return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMapList);
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryRefundableOrderList_V1(InterfaceMessage msg) throws Exception {
		return this.queryRefundableOrderList( new CommonReq<ReqQueryRefundableOrderList>(new ReqQueryRefundableOrderList(msg))).toResult();
	}
	
	@Override
	public CommonResp<RespQueryLocalTransLogInfo> queryTransLogInfo(CommonReq<ReqQueryLocalOrderInfo> commReq)
			throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String orderId = req.getOrderId();
		RespQueryLocalTransLogInfo resp = new RespQueryLocalTransLogInfo();
		OrderTransLogVo orderTransLogInfo = orderMapper.findLocalTransLogInfo(orderId);
		if(orderTransLogInfo != null) {
			BeanCopyUtils.copyProperties(orderTransLogInfo, resp, null);
			resp.setTransTime(DateUtils.getTimestampToStr(orderTransLogInfo.getTransTime()));
			resp.setPayTime(DateUtils.getTimestampToStr(orderTransLogInfo.getPayTime()));
			resp.setBizTime(DateUtils.getTimestampToStr(orderTransLogInfo.getBizTime()));
			String configKey = orderTransLogInfo.getConfigKey();
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
			if(payInfo != null) {
				resp.setPayMethod(payInfo.getTitle());
			}
			Example example = new Example(RefundOrder.class);
			example.createCriteria().andEqualTo("orderId", orderId);
			List<RefundOrder> refundOrderList = refundOrderMapper.selectByExample(example);
			if(refundOrderList != null) {
				List<RespRefund> refundList = new ArrayList<>();
				for (RefundOrder refundOrder : refundOrderList) {
					RespRefund refund = new RespRefund();
					refund.setRefundOrderId(refundOrder.getRefundOrderId());
					refund.setRefundNo(refundOrder.getRefundNo());
					refund.setRefundPrice(refundOrder.getRefundPrice() + "");
					refund.setRefundTime(DateUtils.getTimestampToStr(refundOrder.getBeginDate()));
					refund.setOperatorName(refundOrder.getOperatorName());
					refund.setPayState(refundOrder.getPayState());
					String channelId = refundOrder.getChannelId();
					if(StringUtil.isNotBlank(channelId)) {
						refund.setRefundChannelName(KasiteConfig.getChannelById(channelId));
					}
					refundList.add(refund);
				}
				resp.setRefundList(refundList);
			}
		}else {
			orderTransLogInfo = orderMapper.findOutTransLogInfo(orderId);
			if(orderTransLogInfo != null) {
				BeanCopyUtils.copyProperties(orderTransLogInfo, resp, null);
				resp.setTransTime(DateUtils.getTimestampToStr(orderTransLogInfo.getTransTime()));
				resp.setPayTime(DateUtils.getTimestampToStr(orderTransLogInfo.getPayTime()));
				resp.setBizTime(DateUtils.getTimestampToStr(orderTransLogInfo.getBizTime()));
				String configKey = orderTransLogInfo.getConfigKey();
				ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
				if(payInfo != null) {
					resp.setPayMethod(payInfo.getTitle());
				}
				List<RefundOrder> refundOrderList = orderMapper.findOutRefundOrderInfo(orderId);
				if(refundOrderList != null) {
					List<RespRefund> refundList = new ArrayList<>();
					for (RefundOrder refundOrder : refundOrderList) {
						RespRefund refund = new RespRefund();
						refund.setRefundOrderId(refundOrder.getRefundOrderId());
						refund.setRefundNo(refundOrder.getRefundNo());
						refund.setRefundPrice(refundOrder.getRefundPrice() + "");
						refund.setRefundTime(DateUtils.getTimestampToStr(refundOrder.getBeginDate()));
						refund.setOperatorName(refundOrder.getOperatorName());
						String channelId = refundOrder.getChannelId();
						if(StringUtil.isNotBlank(channelId)) {
							refund.setRefundChannelName(KasiteConfig.getChannelById(channelId));
						}
						refundList.add(refund);
					}
					resp.setRefundList(refundList);
				}
			}
		}
		return new CommonResp<RespQueryLocalTransLogInfo>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,resp);
	}

	@Override
	public CommonResp<RespQueryQLCOrder> queryLocalQLCOrder(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String orderId = req.getOrderId();
		String transationNo = req.getTransationNo();
		
		RespQueryQLCOrder resp = new RespQueryQLCOrder();
		OrderVo vo = orderMapper.findLocalOrder(orderId, transationNo);
		if(vo == null) {
			List<OrderVo> voList = orderMapper.findOrderCheck(orderId, transationNo);
			if(voList != null && voList.size() > 0) {
				vo = voList.get(0);
			}
		}
		if(vo != null) {
			BeanCopyUtils.copyProperties(vo, resp, null);
		}
		return new CommonResp<RespQueryQLCOrder>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespQueryQLCOrder> queryLocalRefundOrder(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String orderId = req.getOrderId();
		String transationNo = req.getTransationNo();
		String refundOrderId = req.getRefundOrderId();
		
		List<OrderVo> voList = orderMapper.findLocalRefundOrder(orderId, transationNo, refundOrderId);
		if(voList == null) {
			voList = orderMapper.findRefundOrderCheck(orderId, transationNo, refundOrderId);
		}
		List<RespQueryQLCOrder> respList = new ArrayList<>();
		if(voList != null && voList .size() > 0) {
			for (OrderVo vo : voList) {
				RespQueryQLCOrder resp = new RespQueryQLCOrder();
				BeanCopyUtils.copyProperties(vo, resp, null);
				
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryQLCOrder>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, respList);
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOrderSubList(InterfaceMessage msg) throws Exception {
		return super.queryOrderSubList(new CommonReq<ReqQueryOrderSubList>(new ReqQueryOrderSubList(msg))).toResult();
	}

	@Override
	public CommonResp<RespQueryOrderItemList> queryOrderItemList(CommonReq<ReqQueryOrderItemList> commReq)
			throws Exception {
		ReqQueryOrderItemList req = commReq.getParam();
		Example example = new Example(OrderItem.class);
		example.createCriteria().andEqualTo("orderId", req.getOrderId());
		List<OrderItem> list = orderItemMapper.selectByExample(example);
		List<RespQueryOrderItemList> respList = new ArrayList<>();
		for (OrderItem orderItem : list) {
			RespQueryOrderItemList resp = new RespQueryOrderItemList();
			BeanCopyUtils.copyProperties(orderItem, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}


}
