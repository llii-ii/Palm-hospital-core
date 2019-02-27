package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportBankBillCheckVo</p>  
 * <p>Description: 银行勾兑列表数据导出Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportBankBillCheckVo {

	/**
	 * 到账时间
	 */
	private String checkDate;
	
	/**
	 * 开户银行
	 */
	private String bankName;
	
	/**
	 * 银行卡号
	 */
	private String bankNo;
	
	/**
	 * 医院应收金额
	 */
	private String payAbleMoney;
	
	/**
	 * 银行实到金额
	 */
	private String paideMoney;
	
	/**
	 * 是否勾兑
	 */
	private String isCheck;
	
	/**
	 * 勾兑人
	 */
	private String operUserName;
	
	/**
	 * 勾兑状态
	 */
	private String checkState;
	
	/**
	 * 差错金额
	 */
	private String diffMoney;

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
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

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getOperUserName() {
		return operUserName;
	}

	public void setOperUserName(String operUserName) {
		this.operUserName = operUserName;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getDiffMoney() {
		return diffMoney;
	}

	public void setDiffMoney(String diffMoney) {
		this.diffMoney = diffMoney;
	}
	
}
