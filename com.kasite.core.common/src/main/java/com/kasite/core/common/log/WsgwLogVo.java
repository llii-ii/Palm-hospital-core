package com.kasite.core.common.log;

public class WsgwLogVo {

	private String authInfo;
	private String userAgent;
	private String uniqueReqId;
	private String api;
	private String param;
	private Integer paramType;
	private Integer outType;
	private String v;
	private String result;
	private String className;
	private String methodName;
	private Integer mills;
	private String clientIp;
	private String orderId;
	private String callType;
	private Boolean isSuccess;
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	private String referer;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getUniqueReqId() {
		return uniqueReqId;
	}
	public void setUniqueReqId(String uniqueReqId) {
		this.uniqueReqId = uniqueReqId;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public Integer getParamType() {
		return paramType;
	}
	public void setParamType(Integer paramType) {
		this.paramType = paramType;
	}
	public Integer getOutType() {
		return outType;
	}
	public void setOutType(Integer outType) {
		this.outType = outType;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Integer getMills() {
		return mills;
	}
	public void setMills(Integer mills) {
		this.mills = mills;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
}
