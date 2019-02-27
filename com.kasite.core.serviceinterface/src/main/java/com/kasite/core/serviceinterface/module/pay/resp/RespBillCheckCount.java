package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespBillCheckCount extends AbsResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 对账总笔数
	 */
	private int totalCount;
	/**
	 * 账平笔数
	 */
	private int checkState0Count;
	/**
	 * 长款笔数
	 */
	private int checkState1Count;
	/**
	 * 短款笔数
	 */
	private int checkStateT1Count;
	
	/**
	 * 已处理笔数
	 */
	private int checkState2Count;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public int getCheckState2Count() {
		return checkState2Count;
	}

	public void setCheckState2Count(int checkState2Count) {
		this.checkState2Count = checkState2Count;
	}
	
}
