package com.kasite.core.serviceinterface.module.pay.resp;

import com.yihu.hos.IRetCode;

/**
 * @author linjf
 * 支付网关-统一下单出参
 * 某些渠道的特殊出参字段，请注释清楚
 */
public class PgwRespRefund {

	private IRetCode respCode;
	
	private String respMsg;
	
	/**
	 * 商户的退款订单号
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

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
}
