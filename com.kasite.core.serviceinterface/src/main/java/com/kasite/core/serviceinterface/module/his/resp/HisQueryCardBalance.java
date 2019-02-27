/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * HIS查询卡余额
 * @author lcy
 * @version 1.0 
 * 2017-6-30下午5:34:44
 */
public class HisQueryCardBalance extends AbsResp {
	private String balance;
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public HisQueryCardBalance(String balance) {
		super();
		this.balance = balance;
	}
	public HisQueryCardBalance() {
		super();
	}
}
