package com.kasite.core.common.util;

import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.service.ICallHis;
/**
 * 保存HIS的接口调用日志，如果有实现该接口则保存HIS相关订单接口的调用日志到 O_ORDER_HISINFO
 * @author daiyanshui
 *
 */
public interface ISaveCallHisOrder extends ICallHis{
	
	
	/**
	 * 保存HIS的接口调用日志，如果有实现该接口则保存HIS相关订单接口的调用日志到 O_ORDER_HISINFO
	 * 只有订单相关，支付／挂号／退号／退费／撤销订单 这些与第三方相关的核心接口的调用才保存，其它接口不保存
	 * 保存的前提是订单号一定要在系统中存在。
	 * @param orderId
	 * @param api
	 * @param params
	 * @param result
	 * @throws Exception 
	 */
	void saveOrderCallHisLog(String orderId,ApiModule.His api,String params,String result) throws Exception;
}
