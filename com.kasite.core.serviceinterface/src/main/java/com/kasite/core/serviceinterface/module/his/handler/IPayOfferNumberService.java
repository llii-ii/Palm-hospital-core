package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisBookService;
import com.kasite.core.serviceinterface.module.his.resp.HisOfferNumber;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * 预约挂号支付完成，回调如果有实现这个接口则不调用挂号接口
 * 直接调用这个取号接口。
 * 
 * 使用场景：预约挂号 --》 挂号完成（未支付）  
 * 
 * 当天用户到现场支付的时候  会调用支付接口进行支付（支付完成后直接调用 取号逻辑）
 * 
 * @author linjf
 * 支付后取号接口
 */
public interface IPayOfferNumberService extends ICallHis{
	
	/**预约挂号(不支付，先挂号）**/
	CommonResp<HisBookService> regbookService(InterfaceMessage msg,String orderId,String scheduleStor,String numberStore,String lockStore,Map<String, String> map) throws Exception;
	
	/**取号**/
	CommonResp<HisOfferNumber> offerNumber(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
}
