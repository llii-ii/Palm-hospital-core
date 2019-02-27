package com.kasite.core.serviceinterface.module.channel.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 获取支付方式
 * 
 * @author zhaoy
 *
 */
public class RespPayTypeList extends AbsResp{

	private String payType;
	
	private String payTypeName;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
}
