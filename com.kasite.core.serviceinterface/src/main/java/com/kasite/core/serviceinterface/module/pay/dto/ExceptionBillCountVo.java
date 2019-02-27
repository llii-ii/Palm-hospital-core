package com.kasite.core.serviceinterface.module.pay.dto;

/**
 * 异常账单统计
 * 
 * @author zhaoy
 *
 */
public class ExceptionBillCountVo {

	private Integer dealState;
	
	private int count;

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
