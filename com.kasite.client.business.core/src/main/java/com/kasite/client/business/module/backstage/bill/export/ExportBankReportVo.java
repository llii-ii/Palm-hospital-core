package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportChannelCheckReportVo</p>  
 * <p>Description: 渠道金额统计数据导出Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportBankReportVo {

	/** 日期 ***/
	private String transdate;
	
	/** 所属银行 ***/
	private String bankName;
	
	/** 银行卡号 ***/
	private String bankNo;
	
	/** 交易场景 ***/
	private String channel;
	
	/** 服务类型 ***/
	private String transType;
	
	/** 支付方式 ***/
	private String payMethod;
	
	/** 医院应收金额 ***/
	private String payAbleMoney;
	
	/** 银行实到金额  ***/
	private String paideMoney;
	
	/** 差错金额  ***/
	private String diffMoney;
	
	/** 对账结果 ***/
	private String checkState;

	public String getTransdate() {
		return transdate;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getPayAbleMoney() {
		return payAbleMoney;
	}

	public void setPayAbleMoney(String payAbleMoney) {
		this.payAbleMoney = payAbleMoney;
	}

	public String getPaideMoney() {
		return paideMoney;
	}

	public void setPaideMoney(String paideMoney) {
		this.paideMoney = paideMoney;
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
