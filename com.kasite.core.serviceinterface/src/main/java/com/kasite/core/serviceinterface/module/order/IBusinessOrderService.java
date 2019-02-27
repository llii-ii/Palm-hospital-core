package com.kasite.core.serviceinterface.module.order;

import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 业务订单接口
 * 处方结算，处方支付，充值记录等
 */
public interface IBusinessOrderService {

	/**
	 * API:查询住院费用列表 <br/> 
	 * 1.查询住院费用列表  ★ <br/> 
	 * 2.住院费用列表-分类 <br/> 
	 * 3.住院费用列表-分类-明细说明 <br/> 
	 * @param msg
	 * @return
	 */
	String QueryInHospitalCostList(InterfaceMessage msg) throws Exception;
	
	/**
	 * API:住院费用列表-详情 <br/> 
	 * 1.查询住院费用列表   <br/> 
	 * 2.住院费用列表-分类★ <br/> 
	 * 3.住院费用列表-分类-明细说明 <br/> 
	 * @param msg
	 * @return
	 */
	String QueryInHospitalCostType(InterfaceMessage msg) throws Exception;
	
	/**
	 * API:住院费用列表-详情-明细说明 <br/> 
	 * 1.查询住院费用列表  <br/> 
	 * 2.住院费用列表-详情 <br/> 
	 * 3.住院费用列表-详情-明细说明 ★ <br/> 
	 * @param msg
	 * @return
	 */
	String QueryInHospitalCostTypeItem(InterfaceMessage msg)throws Exception;
	
	/**
	 * 查询门诊费用列表
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOutpatientCostList(InterfaceMessage msg) throws Exception;
	
	/**
	 *  查询门诊费用列表-详情
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOutpatientCostType(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询门诊费用列表-详情说明
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOutpatientCostTypeItem(InterfaceMessage msg)throws Exception;
	
	/**
	 * API查询住院充值记录，两种实现方案，建议第一种 <br/> 
	 * 1.查询HIS接口，获取患者在医院所有的住院充值记录 <br/> 
	 * 2.查询本地订单的充值记录（待开发） <br/> 
	 * @param msg
	 * @return
	 */
	String QueryInHospitalRechargeList(InterfaceMessage msg) throws Exception;
	
	/**
	 * API查询门诊充值记录，两种实现方案，建议第一种 <br/> 
	 * 1.查询HIS接口，获取患者在医院所有的门诊充值记录 <br/> 
	 * 2.查询本地订单的充值记录（待开发） <br/> 
	 * @param msg
	 * @return
	 */
	String QueryOutpatientRechargeList(InterfaceMessage msg) throws Exception;
	
	/**
	 * API:查询处方订单列表,订单支付用 <br/> 
	 * 1.查询处方订单列表  ★ <br/> 
	 * 2.查询处方订单列表-处方订单信息 <br/> 
	 * @param msg
	 * @return
	 */
	String QueryOrderPrescriptionList(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:处方订单信息,订单支付用 <br/> 
	 * 1.查询处方订单列表  <br/> 
	 * 2.查询处方订单列表-处方订单信息 ★ <br/> 
	 * @param msg
	 * @return
	 */
	String QueryOrderPrescriptionInfo(InterfaceMessage msg)throws Exception;	
	
	/**
	 * API:新增处方的本地订单
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String AddOrderPrescription(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:查询处方结算列表,处方结算用 <br/> 
	 * 1.查询处方结算列表  ★ <br/> 
	 * 2.查询处方结算列表-处方结算信息 <br/> 
	 * @param msg
	 * @return
	 */
	String QueryOrderSettlementList(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:处方结算信息,处方结算用  <br/> 
	 * 1.查询处方结算列表  <br/> 
	 * 2.查询处方结算列表-处方结算信息 ★<br/> 
	 * @param msg
	 * @return
	 */
	String QueryOrderSettlementInfo(InterfaceMessage msg)throws Exception;	
	
	/**
	 * API:结算处方 <br/> 
	 * 只能结算未结算的处方
	 * @param msg
	 * @return
	 */
	String SettleOrderSettlement(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:查询未结算支付的订单/处方/收据,合并支付用 ;不分页<br/> 
	 * @param msg
	 * @return
	 */
	String QueryUnsettledOrderReceiptList(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:合并结算支付订单/处方/收据，合并支付用
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String MergeSettledPayReceipt(InterfaceMessage msg)throws Exception;

}
