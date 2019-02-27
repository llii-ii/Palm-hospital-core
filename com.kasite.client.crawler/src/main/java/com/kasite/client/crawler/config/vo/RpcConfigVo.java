package com.kasite.client.crawler.config.vo;

public class RpcConfigVo {

	private String appId;
	private String appName;
	private Integer port;
	private boolean startRpc;
	
	public boolean isStartRpc() {
		return startRpc;
	}
	public void setStartRpc(boolean startRpc) {
		this.startRpc = startRpc;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
}
