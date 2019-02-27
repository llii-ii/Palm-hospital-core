package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderReceiptList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 合并支付订单
 * 
 * @author daiyanshui
 *
 */
public interface IMergeSettledPayReceiptService extends ICallHis{
	/**
	 * 查询HIS订单结算支付列表(订单结算支付)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOrderReceiptList> queryOrderReceiptList(InterfaceMessage msg, Map<String, String> map)throws Exception;
	/**
	 * 单据合并结算
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<RespMap> mergeSettledPayReceipt(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询合并支付结算的结果
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<RespMap> queryMergeSettledPayReceipt(InterfaceMessage msg,Map<String, String> map) throws Exception;

}
