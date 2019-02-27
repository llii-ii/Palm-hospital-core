package com.kasite.core.serviceinterface.module.msg.resp;


import java.sql.Timestamp;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;
import com.kasite.core.common.resp.AbsResp;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespQueryOpenId extends AbsResp{
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	private String channelId;
	private String openId;
	
	
}
