package com.kasite.core.common.config;

public class MsgCenterConfigVo {
	private String state;
	private String sendMsgUrl;
	private String queryWxTemplateListUrl;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSendMsgUrl() {
		return sendMsgUrl;
	}
	public void setSendMsgUrl(String sendMsgUrl) {
		this.sendMsgUrl = sendMsgUrl;
	}
	public String getQueryWxTemplateListUrl() {
		return queryWxTemplateListUrl;
	}
	public void setQueryWxTemplateListUrl(String queryWxTemplateListUrl) {
		this.queryWxTemplateListUrl = queryWxTemplateListUrl;
	}
	
}
