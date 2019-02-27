package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

/***
 * 异常账单统计
 * 
 * @author zhaoy
 *
 */
public class RespExceptionBillCount extends AbsResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int totalCount;
	
	private int isDealCount;
	
	private int noDealCount;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getIsDealCount() {
		return isDealCount;
	}

	public void setIsDealCount(int isDealCount) {
		this.isDealCount = isDealCount;
	}

	public int getNoDealCount() {
		return noDealCount;
	}

	public void setNoDealCount(int noDealCount) {
		this.noDealCount = noDealCount;
	}

}
