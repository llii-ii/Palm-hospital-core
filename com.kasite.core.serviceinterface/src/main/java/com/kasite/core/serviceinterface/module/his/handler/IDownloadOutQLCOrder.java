package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryOrderCheck;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 收集外部服务的全流程订单列表
 * 
 * @author zhaoy
 *
 */
public interface IDownloadOutQLCOrder extends ICallHis {

	/**
	 * 查询全部订单列表
	 * 
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryOrderCheck> queryOutOrderList(InterfaceMessage msg,Map<String, String> paramMap) throws Exception;
	
	/**
	 * 查询退款订单信息
	 * 
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	RespQueryOrderCheck queryRefundOrderInfo(InterfaceMessage msg,Map<String, String> paramMap) throws Exception;
	
	/**
	 * 查询订单的交易渠道信息
	 * 
	 * @param msg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String queryOrderChannelInfo(InterfaceMessage msg,Map<String, String> paramMap) throws Exception;
}
