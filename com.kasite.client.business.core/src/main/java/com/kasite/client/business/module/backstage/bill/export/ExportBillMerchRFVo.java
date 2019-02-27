package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportBillMerchRFVo</p>  
 * <p>Description: 支付方式-资金对账报表导出Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportBillMerchRFVo {

	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	/**
	 * 商户号
	 */
	private String configkey;
	
	/**
	 * 所属银行
	 */
	private String bank;
	
	/**
	 * 银行账号
	 */
	private String bankNo;

	/**
	 * his账单金额（分）
	 */
	private String hisBillSum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private String merchBillSum;
	
	/**
	 * 对账结果
	 */
	private String checkState;

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
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

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

}
