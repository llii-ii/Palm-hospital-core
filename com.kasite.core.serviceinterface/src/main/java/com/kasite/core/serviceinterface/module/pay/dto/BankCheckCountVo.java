package com.kasite.core.serviceinterface.module.pay.dto;

public class BankCheckCountVo {

	/**
	 * 笔数
	 */
	private int count;
	
	/**
	 * 核对结果
	 */
	private Integer checkState;
	
	/**
	 * 是否勾兑
	 */
	private int isCheck;

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

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	
}
