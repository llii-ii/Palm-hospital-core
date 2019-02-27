package com.kasite.client.business.module.sys.check;

public class InterfaceStatusVo {
	/**
	 * 是否是http链接／如果是的华通过http判断 
	 * 如果是 socket 通过ping的方式判断
	 */
	private String urlType;
	/**
	 * 接口地址
	 */
	private String url;
	/**
	 * 接口状态
	 */
	private boolean status;
	/**
	 * 上次判断接口状态的时间
	 */
	private long updatetime;
	/**
	 * 异常后通知回调地址
	 */
	private CheckInterfaceHandler handler;
	
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	public void notifyHandler() {
		if(null != handler) {
			handler.notify(this);
		}
	}
	public void setHandler(CheckInterfaceHandler handler) {
		this.handler = handler;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	
	
	
}
