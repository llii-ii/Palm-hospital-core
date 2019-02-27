package com.kasite.core.serviceinterface.module.channel.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespChannelList extends AbsResp{

	private String channelId;
	
	private String channelName;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
}
