package com.kasite.core.serviceinterface.module.channel.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQueryChannel extends AbsReq {

	private String channelId;
	
	private String merchId;
	
	private String status;
	
	/**
	 * 是否启用
	 */
	private Integer isEnable;
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public ReqQueryChannel(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.channelId = getDataJs().getString("ChannelId");
			this.merchId = getDataJs().getString("MerchId");
			this.status = getDataJs().getString("Status");
			this.isEnable = getDataJs().getInteger("IsEnable");
		}
	}

}
