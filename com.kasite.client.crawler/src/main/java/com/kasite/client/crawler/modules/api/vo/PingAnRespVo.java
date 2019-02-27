package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;

public class PingAnRespVo {
	private JSONArray body;
	private JSONObject head;
	private JSONObject additionInfo;
	public JSONArray getBody() {
		return body;
	}
	public void setBody(JSONArray body) {
		this.body = body;
	}

	public JSONObject getHead() {
		return head;
	}

	public void setHead(JSONObject head) {
		this.head = head;
	}

	public JSONObject getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(JSONObject additionInfo) {
		this.additionInfo = additionInfo;
	}
	
	public PingAnRespVo parseHead(PingAnBussId busseID,HeadRespVo vo) {
		this.head = (JSONObject) JSON.toJSON(vo);
		return this;
	}
	public PingAnRespVo parseAdditionInfo(PingAnBussId busseID,AdditionInfoVo vo) {
		this.additionInfo = (JSONObject) JSON.toJSON(vo);
		return this;
	}
	
/*
 "head": {
"busseID": " C100",
"senderCode": "发送方编码",
"senderName": "发送方名称",
"receiverCode": "接收方编码",
"receiverName": "接收方名称", "intermediaryCode": "医保编码", "intermediaryName": "医保名称",
"hosorgNum": "操作员编号",
"hosorgName": "操作员姓名",
"systemType": "1",
"busenissType": "2",
"sendTradeNum": "20171201144523-400000981-9134", "standardVersionCode": "version:1.0.0", "clientmacAddress": "00:00:00:00:00:00", "recordCount": "1"
}
 */
	
	/*
	 *
additionInfo :
{
"asyncAsk": "0",
 "callback": "",
"correlationId": "",
"curDllAddr": "",
"errorCode": "0",
"errorMsg": "处理成功!",
"receiverTradeNum": "20171201144523-200000241-6238"
}
	 */
	
}
