package com.kasite.core.serviceinterface.module.order.dto;

public class OrderCountMoneyVo {

	/**
	 * 订单金额合计
	 */
	private Long totalOrderMoney;
	
	/**
	 * 支付金额合计
	 */
	private Long totalPayMoney;
	
	/**
	 * 退款金额合计
	 */
	private Long totalRefundMoney;

	public Long getTotalOrderMoney() {
		return totalOrderMoney;
	}

	public void setTotalOrderMoney(Long totalOrderMoney) {
		this.totalOrderMoney = totalOrderMoney;
	}

	public Long getTotalPayMoney() {
		return totalPayMoney;
	}

	public void setTotalPayMoney(Long totalPayMoney) {
		this.totalPayMoney = totalPayMoney;
	}

	public Long getTotalRefundMoney() {
		return totalRefundMoney;
	}

	public void setTotalRefundMoney(Long totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	
}
