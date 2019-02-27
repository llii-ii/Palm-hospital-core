package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

/**
 * 智付后台-交易管理-交易明细
 * @author zhaoy
 * TODO
 */
public class RespOrderCountMoney extends AbsResp implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
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
