package com.kasite.core.common.config;

import com.alibaba.fastjson.JSONObject;

public class PayRule {
	/***当笔限额最小值*/
	private Integer singleLimitStart;
	/***当笔限额最大值*/
	private Integer singleLimitEnd;
	/***允许支付开始时间*/
	private String payTimeStart;
	/***允许支付结束时间*/
	private String payTimeEnd;
	/***是否允许使用信用卡： true／false*/
	private String creditCardsAccepted;
	/***允许退款开始时间*/
	private String refundTimeStart;
	/***允许退款结束时间*/
	private String refundTimeEnd;
	/**配置的个性化Json参数*/
	private JSONObject json;
	
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	public Integer getSingleLimitStart() {
		return singleLimitStart;
	}
	public void setSingleLimitStart(Integer singleLimitStart) {
		this.singleLimitStart = singleLimitStart;
	}
	public Integer getSingleLimitEnd() {
		return singleLimitEnd;
	}
	public void setSingleLimitEnd(Integer singleLimitEnd) {
		this.singleLimitEnd = singleLimitEnd;
	}
	public String getPayTimeStart() {
		return payTimeStart;
	}
	public void setPayTimeStart(String payTimeStart) {
		this.payTimeStart = payTimeStart;
	}
	public String getPayTimeEnd() {
		return payTimeEnd;
	}
	public void setPayTimeEnd(String payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}
	public String getCreditCardsAccepted() {
		return creditCardsAccepted;
	}
	public void setCreditCardsAccepted(String creditCardsAccepted) {
		this.creditCardsAccepted = creditCardsAccepted;
	}
	public String getRefundTimeStart() {
		return refundTimeStart;
	}
	public void setRefundTimeStart(String refundTimeStart) {
		this.refundTimeStart = refundTimeStart;
	}
	public String getRefundTimeEnd() {
		return refundTimeEnd;
	}
	public void setRefundTimeEnd(String refundTimeEnd) {
		this.refundTimeEnd = refundTimeEnd;
	}
	public String getCreditCardsAcceptedByClientId(String clientId) {
		String key = clientId+"_"+"creditCardsAccepted";
		return json.getString(key);
	}
}
