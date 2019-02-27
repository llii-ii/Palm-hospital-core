package com.kasite.client.business.module.backstage.bill.export;

/**
 * <p>Title: ExportBillRFVo</p>  
 * <p>Description: 日账单汇总列表信息导出Excel表格对象</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月28日  
 * @version 1.0
 */
public class ExportBillRFVo {

	/**
	 * 交易时间
	 */
	private String tradeTime;
	
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
	 * 金额不一致笔数
	 */
	private Integer differPirceCount;
	
	/**
	 * his账单金额（分）
	 */
	private String hisBillMoneySum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private String merchBillMoneySum;
	
	/**
	 * 核对状态:1账平 0账不平
	 */
	private String checkState;
	
	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
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

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getHisBillMoneySum() {
		return hisBillMoneySum;
	}

	public void setHisBillMoneySum(String hisBillMoneySum) {
		this.hisBillMoneySum = hisBillMoneySum;
	}

	public String getMerchBillMoneySum() {
		return merchBillMoneySum;
	}

	public void setMerchBillMoneySum(String merchBillMoneySum) {
		this.merchBillMoneySum = merchBillMoneySum;
	}
}
