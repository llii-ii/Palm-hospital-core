package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;

/**
 * 新增全流程订单前校验，如果不需要，则默认返回成功.
 * @author linjf
 * 
 */
public interface IHisOrderValidator extends ICallHis{

	/**
	 * 新增全流程订单前校验，如果不需要，则默认返回成功.
	 * 注意：如果返回null，会影响正常流程！
	 * 请勿修改commonReq对象里面的任何一个属性
	 * @param commonReq
	 * @return
	 * 
	 * ApiKey.AddLocalOrderValidateBefore 返回参数
	 * 
	 */
	CommonResp<RespMap> addLocalOrderValidateBefore(CommonReq<ReqAddOrderLocal> commonReq) throws Exception;
	
}
