package com.kasite.core.serviceinterface.module.pay.resp;

import com.yihu.hos.IRetCode;

/**
 * @author linjf
 * 支付网关-统一下单出参
 * 某些渠道的特殊出参字段，请注释清楚
 */
public class PgwRespQueryRefundOrder {

	private IRetCode respCode;
	
	private String respMsg;
	
	/**
	 * 退款金额(分)
	 */
	private Integer refundPrice;
	/**
	 * 订单退费状态
	 */
	private Integer refundStatus;
	/**
	 * 成功退费时间
	 * 微信-yyyyMMddHHmmss
	 * 支付宝-无
	 *   银联-代补充
	 * 威富通-代补充
	 */
	private String refundTime;
	/**
	 * 全流程退款订单号
	 */
	private String refundOrderId;
	/**
	 * 商户订单退款订单号
	 * 支付宝-该参数与RefundOrderId一致
	 * 微信-为微信商户自动生成的数字流水号
	 * 银联-代补充
	 * 威富通-代补充
	 */
	private String refundId;
	
	public IRetCode getRespCode() {
		return respCode;
	}

	public void setRespCode(IRetCode respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}


	
	
	
	
}
