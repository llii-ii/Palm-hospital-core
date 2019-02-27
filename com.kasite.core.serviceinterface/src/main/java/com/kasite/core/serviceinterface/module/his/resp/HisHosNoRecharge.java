/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * His就诊卡充值对象
 * @author lcy
 * @version 1.0 
 * 2017-7-19上午9:26:01
 */
public class HisHosNoRecharge extends AbsResp {
 	/**订单号*/
 	public String orderId;
	/**His返回的订单号*/
	public String hisFlowNo;
	/**His返回缴费时间*/
	public String hisDate;
	/**当前余额*/
	public String balance;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getHisFlowNo() {
		return hisFlowNo;
	}
	public void setHisFlowNo(String hisFlowNo) {
		this.hisFlowNo = hisFlowNo;
	}
	public String getHisDate() {
		return hisDate;
	}
	public void setHisDate(String hisDate) {
		this.hisDate = hisDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
}
