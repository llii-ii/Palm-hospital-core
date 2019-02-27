package com.kasite.core.serviceinterface.module.pay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.pay.req.ReqPayStartBizOrderExecute;

/**
 * 下单前完成支付订单的核对校验 并返回要支付的金额
 * 如果系统不实现该接口则订单下单的时候如果没有实现校验接口则直接返回订单异常无法进行统一下单
 * @author daiyanshui
 *
 */
public interface IBizPayStartOrderCheckHandler {

	/**
	 * 做订单确认校验
	 * @param orderId
	 * @return
	 */
	CommonResp<RespMap> checkPayOrder(CommonReq<ReqPayStartBizOrderExecute> req);
	
}
