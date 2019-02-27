package com.kasite.core.serviceinterface.module.pay.dto;

public class BillByMonthVo {

	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	/**
	 * his交易笔数
	 */
	private String hisCount;
	
	/**
	 * 商户交易笔数
	 */
	private String merchCount;
	
	/**
	 * 对账结果
	 */
	private Integer checkState;
	
	/**
	 * 交易时间
	 */
	private String transDate;
	
	/**
	 * 交易月份
	 */
	private String transMonth;
	
	/**
	 * 订单类型(1,智付 2,退款)
	 */
	private Integer billType;
	
	/**
	 * his交易金额
	 */
	private Long hisMoney;
	
	/**
	 * 商户交易金额
	 */
	private Long merchMoney;

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getHisCount() {
		return hisCount;
	}

	public void setHisCount(String hisCount) {
		this.hisCount = hisCount;
	}

	public String getMerchCount() {
		return merchCount;
	}

	public void setMerchCount(String merchCount) {
		this.merchCount = merchCount;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransMonth() {
		return transMonth;
	}

	public void setTransMonth(String transMonth) {
		this.transMonth = transMonth;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Long getHisMoney() {
		return hisMoney;
	}

	public void setHisMoney(Long hisMoney) {
		this.hisMoney = hisMoney;
	}

	public Long getMerchMoney() {
		return merchMoney;
	}

	public void setMerchMoney(Long merchMoney) {
		this.merchMoney = merchMoney;
	}
	
}
