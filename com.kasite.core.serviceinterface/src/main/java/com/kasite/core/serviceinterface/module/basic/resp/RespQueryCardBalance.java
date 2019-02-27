package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryCardBalance
 * @author: lcz
 * @date: 2018年7月23日 下午5:57:48
 */
public class RespQueryCardBalance extends AbsResp{
	
	private Integer balance;
	
	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
}
