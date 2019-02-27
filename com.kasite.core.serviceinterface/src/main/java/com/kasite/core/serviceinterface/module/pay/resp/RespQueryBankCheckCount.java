package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryBankCheckCount extends AbsResp implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 到款总笔数
	 */
	private int totalCount;
	/**
	 * 未勾兑笔数
	 */
	private int isCheck0Count;
	
	/**
	 * 已勾兑笔数
	 */
	private int isCheck1Count;
	
	/**
	 * 已勾兑账平笔数
	 */
	private int checkState0Count;
	/**
	 * 已勾兑长款笔数
	 */
	private int checkState1Count;
	/**
	 * 已勾兑短款笔数
	 */
	private int checkStateT1Count;
	
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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getIsCheck0Count() {
		return isCheck0Count;
	}

	public void setIsCheck0Count(int isCheck0Count) {
		this.isCheck0Count = isCheck0Count;
	}

	public int getIsCheck1Count() {
		return isCheck1Count;
	}

	public void setIsCheck1Count(int isCheck1Count) {
		this.isCheck1Count = isCheck1Count;
	}

	public int getCheckState0Count() {
		return checkState0Count;
	}

	public void setCheckState0Count(int checkState0Count) {
		this.checkState0Count = checkState0Count;
	}

	public int getCheckState1Count() {
		return checkState1Count;
	}

	public void setCheckState1Count(int checkState1Count) {
		this.checkState1Count = checkState1Count;
	}

	public int getCheckStateT1Count() {
		return checkStateT1Count;
	}

	public void setCheckStateT1Count(int checkStateT1Count) {
		this.checkStateT1Count = checkStateT1Count;
	}

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
