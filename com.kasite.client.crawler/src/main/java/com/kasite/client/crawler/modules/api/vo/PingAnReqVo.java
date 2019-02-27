package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.httpclient.http.StringUtils;

public class PingAnReqVo {
	private String orderId;
	private String reqId;
	private String callType;
	private JSONArray reqBody;
	private JSONObject reqHead;
	private JSONObject reqAdditionInfo;
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
	
	public JSONArray getReqBody() {
		return reqBody;
	}
	public void setReqBody(JSONArray reqBody) {
		this.reqBody = reqBody;
	}
	public JSONObject getReqHead() {
		return reqHead;
	}
	public void setReqHead(JSONObject reqHead) {
		this.reqHead = reqHead;
	}
	public JSONObject getReqAdditionInfo() {
		return reqAdditionInfo;
	}
	public void setReqAdditionInfo(JSONObject reqAdditionInfo) {
		this.reqAdditionInfo = reqAdditionInfo;
	}
	
	public HeadReqVo getHead() {
		HeadReqVo head = new HeadReqVo();
		head.setJson(reqHead);
		return head;
	}
	public PingAnReqVo parseReq(PingAnBussId bid,JSONObject req) {
		JSONObject pkg = req.getJSONObject("package");
		JSONObject head = pkg.getJSONObject("head");
		JSONArray body = pkg.getJSONArray("body");
		JSONObject additionInfo = pkg.getJSONObject("additionInfo");
		String busId = head.getString("busseID");
		if(StringUtils.isBlank(busId)) {
			throw new RRException("head 参数中 ： busseID 不能为空。");
		}
		PingAnBussId bussId = PingAnBussId.valueOf(busId);
		if(null == bussId) {
			throw new RRException("未定义实现此类型的接口调用，请联系管理员核实。");
		}
		this.callType = busId;
		this.reqHead = head;
		this.reqBody = body;
		this.reqAdditionInfo = additionInfo;
		return this;
	}
	
}
