package com.kasite.client.business.module.backstage.bill.export;

/**
 * <p>Title: ExportBillCheckVo</p>  
 * <p>Description: 对账明细列表导出EXCEL对象</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月28日  
 * @version 1.0
 */
public class ExportBillCheckVo {

	/**
	 * 交易时间
	 */
	private String transDate;
	
	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 全流程下单号
	 */
	private String orderId;

	/**
	 * 病人姓名
	 */
	private String nickName;
	
	/**
	 * 住院号/病历号
	 */
	private String caseHistory;
	
	/**
	 * HIS支付/退费订单号
	 */
	private String hisOrderNo;
	
	/**
	 * 商户流水号
	 */
	private String MerchNo;
	
	/**
	 * 应收/退
	 */
	private String billType1;
	
	/**
	 * his应收/应退金额（分）
	 */
	private String hisPrice;
	
	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	/**
	 * 实收/退
	 */
	private String billType2;
	
	/**
	 * 商户实收/实退金额（分）
	 */
	private String merchPrice;
	
	/**
	 * 差错金额
	 */
	private String diffPrice;
	
	/**
	 * 核对状态0账平1长款-1短款2处理
	 */
	private String checkState;
	
	/**
	 * 对账时间
	 */
	private String checkDate;

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCaseHistory() {
		return caseHistory;
	}

	public void setCaseHistory(String caseHistory) {
		this.caseHistory = caseHistory;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getMerchNo() {
		return MerchNo;
	}

	public void setMerchNo(String merchNo) {
		MerchNo = merchNo;
	}

	public String getBillType1() {
		return billType1;
	}

	public void setBillType1(String billType1) {
		this.billType1 = billType1;
	}

	public String getHisPrice() {
		return hisPrice;
	}

	public void setHisPrice(String hisPrice) {
		this.hisPrice = hisPrice;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getBillType2() {
		return billType2;
	}

	public void setBillType2(String billType2) {
		this.billType2 = billType2;
	}

	public String getMerchPrice() {
		return merchPrice;
	}

	public void setMerchPrice(String merchPrice) {
		this.merchPrice = merchPrice;
	}

	public String getDiffPrice() {
		return diffPrice;
	}

	public void setDiffPrice(String diffPrice) {
		this.diffPrice = diffPrice;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

}
