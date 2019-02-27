package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;

public class PingAnReqAndRespVo {
	private String orderId;
	private String reqId;
	private String callType;
	private String reqBody;
	private String reqHead;
	private String reqAdditionInfo;
	private String respBody;
	private String respHead;
	private String respAdditionInfo;
	private Integer respCode;
	private String respMessage;
	public Integer getRespCode() {
		return respCode;
	}
	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}
	public String getReqHead() {
		return reqHead;
	}
	public void setReqHead(String reqHead) {
		this.reqHead = reqHead;
	}
	public String getReqAdditionInfo() {
		return reqAdditionInfo;
	}
	public void setReqAdditionInfo(String reqAdditionInfo) {
		this.reqAdditionInfo = reqAdditionInfo;
	}
	public String getRespBody() {
		return respBody;
	}
	public void setRespBody(String respBody) {
		this.respBody = respBody;
	}
	public String getRespHead() {
		return respHead;
	}
	public void setRespHead(String respHead) {
		this.respHead = respHead;
	}
	public String getRespAdditionInfo() {
		return respAdditionInfo;
	}
	public void setRespAdditionInfo(String respAdditionInfo) {
		this.respAdditionInfo = respAdditionInfo;
	}
	public PingAnReqAndRespVo parseReq(PingAnBussId bid,PingAnReqVo req) {
		JSONObject head = req.getReqHead();
		JSONArray body = req.getReqBody();
		JSONObject additionInfo = req.getReqAdditionInfo();
		this.callType = bid.name();
		if(null != body) {
			this.reqBody = body.toJSONString();
		}
		if(null != head) {
			this.reqHead = head.toJSONString();
		}
		if(null != additionInfo) {
			this.reqAdditionInfo = additionInfo.toJSONString();
		}
		
		this.orderId = head.getString("sendTradeNum");
		
		return this;
	}
	public PingAnReqAndRespVo parseResp(PingAnBussId bid,PingAnReqVo req,PingAnRespVo resp) {
		JSONObject head = req.getReqHead();
		JSONArray body = req.getReqBody();
		JSONObject additionInfo = req.getReqAdditionInfo();
		this.callType = bid.name();
		if(null != body) {
			this.reqBody = body.toJSONString();
		}
		if(null != head) {
			this.reqHead = head.toJSONString();
		}
		if(null != additionInfo) {
			this.reqAdditionInfo = additionInfo.toJSONString();
		}
		this.orderId = head.getString("sendTradeNum");
		
		if(null != resp.getAdditionInfo()) {
			this.respAdditionInfo = resp.getAdditionInfo().toJSONString();
			this.respCode =  resp.getAdditionInfo().getInteger("errorCode");
			this.respMessage = resp.getAdditionInfo().getString("errorMsg");
		}
		if(null != resp.getBody()) {
			this.respBody = resp.getBody().toJSONString();
		}
		if(null != resp.getHead()) {
			this.respHead = resp.getHead().toJSONString();
		}
		this.callType = bid.name();
		return this;
	}
	
	public PingAnReqAndRespVo parseResp(PingAnBussId bid,PingAnRespVo resp) {
		if(null != resp.getAdditionInfo()) {
			this.respAdditionInfo = resp.getAdditionInfo().toJSONString();
			this.respCode =  resp.getAdditionInfo().getInteger("errorCode");
			this.respMessage = resp.getAdditionInfo().getString("errorMsg");
		}
		if(null != resp.getBody()) {
			this.respBody = resp.getBody().toJSONString();
		}
		if(null != resp.getHead()) {
			this.respHead = resp.getHead().toJSONString();
		}
		this.callType = bid.name();
		return this;
	}
}
