package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportBillChannelRFVo</p>  
 * <p>Description: 交易场景-订单对账报表导出Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportBillChannelRfVo {

	/**
	 * 交易渠道
	 */
	private String channelName;
	
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
	 * his账单金额（元）
	 */
	private String hisBillSum;
	
	/**
	 * 渠道账单金额（元）
	 */
	private String merchBillSum;
	
	/**
	 * 差额(元)
	 */
	private String diffMoney;
	
	/**
	 * 核对状态，1账平0账不平
	 */
	private String checkState;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public String getHisBillSum() {
		return hisBillSum;
	}

	public void setHisBillSum(String hisBillSum) {
		this.hisBillSum = hisBillSum;
	}

	public String getMerchBillSum() {
		return merchBillSum;
	}

	public void setMerchBillSum(String merchBillSum) {
		this.merchBillSum = merchBillSum;
	}

	public String getDiffMoney() {
		return diffMoney;
	}

	public void setDiffMoney(String diffMoney) {
		this.diffMoney = diffMoney;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
}
