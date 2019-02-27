package com.kasite.core.common.config;

public enum MsgTempTypeEnum {

	/**
	 * 发送短信消息
	 */
	smsMessage("3"),
	
	/**
	 * 客服消息
	 */
	CustomMessage("3.1.1"),
	
	/**
	 * 群发消息  （目前未实现群推功能，后续有群推消息模版的时候可以通过扩展 2类型来加
	 */
	
	/**模版消息*/
	TemplateMessage("3.3"),
	;
	private String msgType;
	MsgTempTypeEnum(String msgType){
		this.msgType = msgType;
	}
	public String getMsgType() {
		return msgType;
	}
}
