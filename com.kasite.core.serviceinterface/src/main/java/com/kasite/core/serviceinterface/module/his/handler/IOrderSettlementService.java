package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 结算模式-订单处方结算既处方结算
 * 用户可以针对单笔，或者多笔处方进行线上结算。
 * 该结算操作是利用用户的就诊卡的余额对处方结算，并非线上支付，用户无需支付处方。
 */
public interface IOrderSettlementService extends ICallHis{
	
	/**
	 * 查询HIS订单处方结算列表(预交金结算)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOrderSettlementList> queryOrderSettlementList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询HIS订单处方结算详情(预交金结算)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOrderSettlementInfo> queryOrderSettlementInfo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 结算诊间处方(预交金结算)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<RespMap> settleOrderSettlement(InterfaceMessage msg,Map<String, String> map) throws Exception;
}
