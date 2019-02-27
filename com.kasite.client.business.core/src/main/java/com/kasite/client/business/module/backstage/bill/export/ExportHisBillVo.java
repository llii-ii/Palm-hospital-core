package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportHisBillVo</p>  
 * <p>Description: 导出医院His的账单信息的Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportHisBillVo {

	/**
	 * 全流程订单ID
	 */
	private String orderId;
	
	/**
	 * 全流程退款订单ID
	 */
	private String refundOrderId;
	
	/**
	 * 商户支付订单ID
	 */
	private String merchOrderNo;
	
	/**
	 * HIS订单号
	 */
	private String hisOrderId;
	
	/**
	 * 支付金额/退费金额
	 */
	private String payMoney;
	
	/**
	 * 全部金额
	 */
	private String totalMoney;
	
	/**
	 * 退款金额
	 */
	private String refundMoney;
	
	/**
	 * 订单时间
	 */
	private String transDate;
	
	/**
	 * 支付渠道
	 */
	private String channelName;
	
	/**
	 * 价格名称
	 */
	private String priceName;
	
	/**
	 * 订单说明
	 */
	private String orderMemo;
	
	/**
	 * HIS订单类型1支付2退费
	 */
	private String hisOrderType;
	
	/**
	 * HIS业务状态
	 */
	private String hisBizState;
	
	/**
	 * 插入时间
	 */
	private String createDate;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public String getHisOrderType() {
		return hisOrderType;
	}

	public void setHisOrderType(String hisOrderType) {
		this.hisOrderType = hisOrderType;
	}

	public String getHisBizState() {
		return hisBizState;
	}

	public void setHisBizState(String hisBizState) {
		this.hisBizState = hisBizState;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
