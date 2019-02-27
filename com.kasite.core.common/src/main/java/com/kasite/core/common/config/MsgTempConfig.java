package com.kasite.core.common.config;

/**
 * 消息模版
 * @author daiyanshui
 */
public class MsgTempConfig {
	private ChannelTypeEnum channelType;
	private String content;
	private MsgTempTypeEnum msgType;
	public ChannelTypeEnum getChannelType() {
		return channelType;
	}
	public void setChannelType(ChannelTypeEnum channelType) {
		this.channelType = channelType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MsgTempTypeEnum getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgTempTypeEnum msgType) {
		this.msgType = msgType;
	}
	
	
	
}
