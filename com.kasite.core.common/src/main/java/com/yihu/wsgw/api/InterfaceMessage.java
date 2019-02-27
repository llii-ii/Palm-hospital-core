package com.yihu.wsgw.api;

import java.io.Serializable;

public class InterfaceMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String clientIp;
	private String version;
	private String seq;
	private String authInfo;
	private String apiName;
	private String param;
	private int paramType;
	private int outType;

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getClientIp() {
		return this.clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getSeq() {
		return this.seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getAuthInfo() {
		return this.authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public String getApiName() {
		return this.apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getParamType() {
		return this.paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

	public int getOutType() {
		return this.outType;
	}

	public void setOutType(int outType) {
		this.outType = outType;
	}

}
