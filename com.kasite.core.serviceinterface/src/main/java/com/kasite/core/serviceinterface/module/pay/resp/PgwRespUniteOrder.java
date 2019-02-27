package com.kasite.core.serviceinterface.module.pay.resp;

import com.yihu.hos.IRetCode;

/**
 * @author linjf
 * 支付网关-统一下单出参
 * 某些渠道的特殊出参字段，请注释清楚
 */
public class PgwRespUniteOrder {

	private IRetCode respCode;
	
	private String respMsg;
	

	/**
	 * 支付信息
	 * 微信：用于调用微信js原生支付控件的json
	 * 支付宝：发起支付宝支付的html
	 */
	private String payInfo;

	
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

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	
	
	
}
