package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 订单模式-处方支付
 * 只支持以订单的形式支付处方，不支持合并支付。
 * 既HIS订单处方列表出来的结果，每一条结果用户都可以选择单笔支付。
 */
public interface IOrderPrescriptionPaymentService extends ICallHis{
	
	/**
	 * 查询HIS订单处方列表(订单模式)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOrderPrescriptionList> queryOrderPrescriptionList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询HIS订单处方详情(订单模式)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOrderPrescriptionInfo> queryOrderPrescriptionInfo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 支付订单处方(订单模式)
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<RespMap> payOrderPrescription(InterfaceMessage msg,Map<String, String> map) throws Exception;
}
