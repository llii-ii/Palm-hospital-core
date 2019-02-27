package com.kasite.core.serviceinterface.module.pay.dto;

/**
 * 对账账单统计
 * 
 * @author zhaoy
 *
 */
public class BillCheckCountVo {
	
	/**
	 * 笔数
	 */
	private int count;
	
	/**
	 * 核对结果
	 */
	private Integer checkState;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	
}
