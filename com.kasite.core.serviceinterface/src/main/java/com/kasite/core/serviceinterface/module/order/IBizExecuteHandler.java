package com.kasite.core.serviceinterface.module.order;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;

/**
 * 业务有涉及支付的
 * 业务订单执行接口。
 * 由订单业务的支付回调（微信异步通知／支付宝异步通知）那边发起
 * 确认是钱到账后执行业务完成在 bizPayEndExecute 执行。
 * 确认钱退费后执行的业务可以在  bizRefundEndExecute 执行。
 * @author daiyanshui
 */
public interface IBizExecuteHandler {

	/**
	 * 返回业务类型  在订单表中如果对应的订单类型是相应的订单类型就通知到这类订单类型对应的实现类
	 * @return
	 */
	BusinessTypeEnum accept();
	
	/**
	 * 业务确认接口
	 * @param req
	 * @return
	 * @throws Exception
	 */
	RetCode.BizDealState bizCheckExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception;
	
	/**
	 * 支付完成回调业务
	 * @param req
	 * @return
	 * @throws Exception
	 */
	RespPayEndBizOrderExecute bizPayEndExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception;

	/**
	 * 退款完成回调业务
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> bizRefundEndExecute(CommonReq<ReqPayEndBizOrderExecute> req) throws Exception;
	
	
	
}
