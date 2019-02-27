package com.yihu.hos.util;

import java.util.List;

/**
 * 
 * @author zhangzz
 * @company yihu.com
 * 2014-7-30上午11:40:04
 */
public class RouteApiVo{
  
	private String api;
	private String url;
	private String proxyUrl;
	private List<RouteParamVo> list;
	/**
	 * @return the api
	 */
	public String getApi() {
		return api;
	}
	/**
	 * @param api the api to set
	 */
	public void setApi(String api) {
		this.api = api;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the proxyUrl
	 */
	public String getProxyUrl() {
		return proxyUrl;
	}
	/**
	 * @param proxyUrl the proxyUrl to set
	 */
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}
	/**
	 * @return the list
	 */
	public List<RouteParamVo> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<RouteParamVo> list) {
		this.list = list;
	}
    
}
