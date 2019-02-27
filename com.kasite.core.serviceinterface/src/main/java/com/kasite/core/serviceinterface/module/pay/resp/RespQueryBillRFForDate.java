package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * <p>Title: BillRF</p>  
 * <p>Description: 日账单汇总信息对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年09月07日  
 * @version 1.0
 */
public class RespQueryBillRFForDate extends AbsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 对账时间
	 */
	private String billDate;
	
	/**
	 * HIS账单笔数
	 */
	private Integer hisBillCount;
	
	/**
	 * 渠道账单笔数
	 */
	private Integer channelBillCount;
	
	/**
	 * 已勾兑笔数
	 */
	private Integer checkCount;
	
	/**
	 * his单边账笔数
	 */
	private Integer hisSingleBillCount;
	
	/**
	 * 渠道单边账笔数
	 */
	private Integer channelSingleBillCount;
	
	/**
	 * 金额不一致笔
	 */
	private Integer differPirceCount;
	
	/**
	 * his账单金额（分）
	 */
	private Long hisBillMoneySum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private Long merchBillMoneySum;
	
	/**
	 * 核对状态，1账平0账不平
	 */
	private int checkState;
	
	/**
	 * 处理状态:0,未处理1,已处理
	 */
	private int dealState;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Integer getHisBillCount() {
		return hisBillCount;
	}

	public void setHisBillCount(Integer hisBillCount) {
		this.hisBillCount = hisBillCount;
	}

	public Integer getChannelBillCount() {
		return channelBillCount;
	}

	public void setChannelBillCount(Integer channelBillCount) {
		this.channelBillCount = channelBillCount;
	}

	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}

	public Integer getHisSingleBillCount() {
		return hisSingleBillCount;
	}

	public void setHisSingleBillCount(Integer hisSingleBillCount) {
		this.hisSingleBillCount = hisSingleBillCount;
	}

	public Integer getChannelSingleBillCount() {
		return channelSingleBillCount;
	}

	public void setChannelSingleBillCount(Integer channelSingleBillCount) {
		this.channelSingleBillCount = channelSingleBillCount;
	}

	public Integer getDifferPirceCount() {
		return differPirceCount;
	}

	public void setDifferPirceCount(Integer differPirceCount) {
		this.differPirceCount = differPirceCount;
	}

	public Long getHisBillMoneySum() {
		return hisBillMoneySum;
	}

	public void setHisBillMoneySum(Long hisBillMoneySum) {
		this.hisBillMoneySum = hisBillMoneySum;
	}

	public Long getMerchBillMoneySum() {
		return merchBillMoneySum;
	}

	public void setMerchBillMoneySum(Long merchBillMoneySum) {
		this.merchBillMoneySum = merchBillMoneySum;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public int getDealState() {
		return dealState;
	}

	public void setDealState(int dealState) {
		this.dealState = dealState;
	}
	
}
