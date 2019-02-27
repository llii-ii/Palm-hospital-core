package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSONObject;

/*
"busseID": "1800",
"sendTradeNum": "20150701083030-10011001-0001", 
"senderCode": "001",
"senderName": "消息发送者名称",
"receiverCode": "002",
"receiverName": "消息接受者的名称", 
"intermediaryCode": "003", 
"intermediaryName": "第三方的名称",
"hosorgNum": "001",
"hosorgName": "操作员姓名",
"systemType": "1",
"busenissType": "2", 
"standardVersionCode": 
"version:1.0.0", 
"clientmacAddress ": "30BB7E0A5E2D ", 
"recordCount ": "1"
 */
public class HeadReqVo {
	
	private JSONObject json;
	
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	public String getBusseID() {
		return json.getString("busseID");
	}
	public String getSendTradeNum() {
		return json.getString("sendTradeNum");
	}
	public String getSenderCode() {
		return json.getString("senderCode");
	}
	public String getSenderName() {
		return json.getString("senderName");
	}
	public String getReceiverCode() {
		return json.getString("receiverCode");
	}
	public String getReceiverName() {
		return json.getString("receiverName");
	}
	public String getIntermediaryCode() {
		return json.getString("intermediaryCode");
	}
	public String getIntermediaryName() {
		return json.getString("intermediaryName");
	}
	public String getHosorgNum() {
		return json.getString("hosorgNum");
	}
	public String getHosorgName() {
		return json.getString("hosorgName");
	}
	public String getSystemType() {
		return json.getString("systemType");
	}
	public String getBusenissType() {
		return json.getString("busenissType");
	}
	public String getStandardVersionCode() {
		return json.getString("standardVersionCode");
	}
	public String getVersion() {
		return json.getString("version");
	}
	public String getClientmacAddress() {
		return json.getString("clientmacAddress");
	}
	public String getRecordCount() {
		return json.getString("recordCount");
	}
	
}
