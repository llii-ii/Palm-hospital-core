package com.kasite.client.crawler.config;

import com.kasite.core.common.config.ClientType;
import com.kasite.core.common.config.DBType;
import com.kasite.core.httpclient.http.RequestType;

public class HosClientConfig {
	private ClientType type;
	private String wsUrl;
	private String dbIp;
	private int dbPort;
	private String file;
	
	/**
	 * Oracle , MySql ,SqlServer
	 * */
	private DBType dbType;
	
	private RequestType reqType;
	private String key;
	private String senderCode;
	private String intermediaryCode;
	private String intermediaryName;
	
	
	public String getIntermediaryCode() {
		return intermediaryCode;
	}
	public void setIntermediaryCode(String intermediaryCode) {
		this.intermediaryCode = intermediaryCode;
	}
	public String getIntermediaryName() {
		return intermediaryName;
	}
	public void setIntermediaryName(String intermediaryName) {
		this.intermediaryName = intermediaryName;
	}
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public RequestType getReqType() {
		return reqType;
	}
	public void setReqType(RequestType reqType) {
		this.reqType = reqType;
	}
	public ClientType getType() {
		return type;
	}
	public void setType(ClientType type) {
		this.type = type;
	}
	public String getWsUrl() {
		return wsUrl;
	}
	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}
	public String getDbIp() {
		return dbIp;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}
	
	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}
	public DBType getDbType() {
		return dbType;
	}
}
