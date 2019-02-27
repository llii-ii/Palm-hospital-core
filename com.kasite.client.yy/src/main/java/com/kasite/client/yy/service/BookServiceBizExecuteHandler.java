package com.kasite.client.yy.service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.yy.bean.dbo.YyWater;
import com.kasite.client.yy.dao.IYyWaterMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IPayOfferNumberService;
import com.kasite.core.serviceinterface.module.his.resp.HisOfferNumber;
import com.kasite.core.serviceinterface.module.his.resp.HisRegFlag;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqGetOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespGetOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqBookService;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryRegInfo;

/**
 * 预约挂号 支付完成后的业务回调逻辑
 * @author linjf
 * TODO
 */
@Service
public class BookServiceBizExecuteHandler implements IBizExecuteHandler{

	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(BookServiceBizExecuteHandler.class);
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	IYYService yyService;
	@Autowired
	IYyWaterMapper waterMapper;
	/**
	 * @return
	 */
	@Override
	public BusinessTypeEnum accept() {
		return BusinessTypeEnum.ORDERTYPE_0;
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
		ReqGetOrder reqOrderDetailLocal = new ReqGetOrder(reqPayEndBizOrderExecute.getMsg(),orderId);
		CommonResp<RespGetOrder> orderResp = orderService.getOrder(new CommonReq<ReqGetOrder>(reqOrderDetailLocal));
		RespGetOrder respOrderDetailLocal = orderResp.getData().get(0);
		RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
		//如果订单是非线上支付订单的，又触发了支付则直接返回失败，并且触发退费逻辑 TODO 判断是否需要 进入告警
		if (!KstHosConstant.ONLINEPAY.equals(respOrderDetailLocal.getIsOnLinePay())) {
			LogUtil.info(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("Result", "orderid:" + respOrderDetailLocal.getOrderId() + "该预约记录不支持线上支付"));
			respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_0);
			return respPayEndBizOrderExecute;
		}
		IPayOfferNumberService payOfferNumberService = HandlerBuilder.get()
		.getCallHisService(reqPayEndBizOrderExecute.getAuthInfo(), IPayOfferNumberService.class);
		if( payOfferNumberService!=null ) {
			Map<String, String> paramMap = new HashMap<String, String>(16);
			YyWater yyWater = waterMapper.selectByPrimaryKey(respOrderDetailLocal.getOrderId());
			paramMap.put("orderId", respOrderDetailLocal.getOrderId());
			paramMap.put("store", yyWater.getStore());
			paramMap.put("payMoney", respOrderDetailLocal.getPrice().toString());
			paramMap.put("hospitalNo", respOrderDetailLocal.getCardNo());
			paramMap.put("channelId", respOrderDetailLocal.getChannelId());
			paramMap.put("configKey", reqPayEndBizOrderExecute.getConfigKey());
			CommonResp<HisOfferNumber> hisOfferNumberResp  = payOfferNumberService.offerNumber(reqPayEndBizOrderExecute.getMsg(),paramMap);
			if(hisOfferNumberResp == null) {
				logger.info(MessageFormat.format("订单号: {0} 执行业务异常，未获取到执行业务的返回结果。", orderId));
				//返回值为空，未知异常重试
				LogUtil.warn(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId()).set("入参", JSONObject.toJSONString(paramMap))
						.set("出参", "返回值为：NULL"));
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}else if(KstHosConstant.SUCCESSCODE.equals(hisOfferNumberResp.getCode())) {
				String store = hisOfferNumberResp.getDataCaseRetCode().getStore();
				if( !StringUtil.isEmpty(store)) {
					yyWater.setStore(hisOfferNumberResp.getDataCaseRetCode().getStore());
					waterMapper.updateByPrimaryKey(yyWater);
				}
				logger.info(MessageFormat.format("订单号: {0} 执行业务成果，支付取号成功。", orderId));
				LogUtil.debug(logger, "OrderCallback-orderId:" + respOrderDetailLocal.getOrderId() + ",挂号成功。"+hisOfferNumberResp.toJSONResult());
				//挂号成功，返回成功信息
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_1);
				return respPayEndBizOrderExecute;
			}else {
				logger.info(MessageFormat.format("订单号: {0} 执行业务异常，返回的结果内容不符合规范。RespCode = {1}", orderId,hisOfferNumberResp.getCode()));
				//未知异常，返回失败重试
				LogUtil.warn(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId())
						.set("入参", JSONObject.toJSONString(paramMap)).set("出参", hisOfferNumberResp.toResult()));
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}
		}else {
			ReqBookService req = new ReqBookService(reqPayEndBizOrderExecute.getMsg(), orderId, 
					reqPayEndBizOrderExecute.getOperatorId(),reqPayEndBizOrderExecute.getOperatorName());
			LogUtil.info(logger, "OrderCallback-orderId:" + respOrderDetailLocal.getOrderId() + ",调用支付完成后挂号逻辑。");
			CommonResp<RespMap> resp = null;
			try {
				resp = yyService.bookService(new CommonReq<ReqBookService>(req));
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(logger, e);
				/** 失败重试 */
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}
			if(resp == null) {
				logger.info(MessageFormat.format("订单号: {0} 执行业务异常，未获取到执行业务的返回结果。", orderId));
				//返回值为空，未知异常重试
				LogUtil.warn(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId()).set("入参", req.getHisReqXml())
						.set("出参", "返回值为：NULL"));
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}else if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				logger.info(MessageFormat.format("订单号: {0} 执行业务成果，挂号成功。", orderId));
				LogUtil.debug(logger, "OrderCallback-orderId:" + respOrderDetailLocal.getOrderId() + ",挂号成功。"+resp.toJSONResult());
				//挂号成功，返回成功信息
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_1);
				return respPayEndBizOrderExecute;
			}else if(RetCode.YY.ERROR_BOOK_UNLOCK.getCode().toString().equals(resp.getCode())) {
				logger.info(MessageFormat.format("订单号: {0} 执行业务异常，挂号失败，锁号信息过期。根据该状态码判断会退还挂号费用。", orderId));
				// 锁号记录过期，返回明确的失败信息
				LogUtil.warn(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId())
						.set("info", "号源锁号信息过期").set("入参", req)
						.set("出参", resp.toResult()));
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_0);
				return respPayEndBizOrderExecute;
			}else {
				logger.info(MessageFormat.format("订单号: {0} 执行业务异常，返回的结果内容不符合规范。RespCode = {1}", orderId,resp.getCode()));
				//未知异常，返回失败重试
				LogUtil.warn(logger, new LogBody(reqPayEndBizOrderExecute.getAuthInfo()).set("挂号订单", respOrderDetailLocal.getOrderId())
						.set("入参", req).set("出参", resp.toResult()));
				respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
				return respPayEndBizOrderExecute;
			}
		}
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> bizRefundEndExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception {
		return null;
	}

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public BizDealState bizCheckExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception {
		// 预约充值订单TODO
//		if (!KstHosConstant.ONLINEPAY.equals(orderView.getIsOnlinePay())) {
//			LogUtil.error(log, "orderid:" + orderView.getOrderId() + "该预约记录不支持线上支付");
//			return HisDealState.HIS_DEAL_STATE_0;
//		}
//		return orderCallBackUtil.callBookStatus(orderView);
		
		//不管业务如何执行，支付已经完成，锁号表
		//订单发生异常的时候会调用这里确认订单状态。
		CommonReq<ReqQueryRegInfo> commReq = new CommonReq<ReqQueryRegInfo>(new ReqQueryRegInfo(
				req.getMsg(),null, 
				null, null, 
				req.getParam().getOrderId(), null, 
				null, null, 
				null, null));
		CommonResp<RespQueryRegInfo>  regResp = yyService.queryRegInfo(commReq);
		if(regResp==null || !KstHosConstant.SUCCESSCODE.equals(regResp.getCode())) {
			return BizDealState.BIZ_DEAL_STATE_2;
		}
		if(regResp.getData().size()<=0) {
			return  BizDealState.BIZ_DEAL_STATE_2;
		}
		String orderId = regResp.getDataCaseRetCode().getOrderId();
		if(req.getParam().getOrderId().equals(orderId)) {
			Integer flag = regResp.getResultData().getRegFlag();
			HisRegFlag f = HisRegFlag.valuesOf(flag);
			switch (f) {
			case state_1:
				return  BizDealState.BIZ_DEAL_STATE_1;
			case state_2://如果返回的订单在HIS那已经推号，本地直接执行退费
				return  BizDealState.BIZ_DEAL_STATE_0;
			case state_6:
				return  BizDealState.BIZ_DEAL_STATE_1;
			default:
				break;
			}	
		}
		return BizDealState.BIZ_DEAL_STATE_2;
	}

}
