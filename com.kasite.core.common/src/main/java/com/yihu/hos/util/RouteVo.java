/**
 * 
 */
package com.yihu.hos.util;

import java.util.Map;

/**
 * @author zhangzz
 * @company yihu.com
 * 2014-7-21上午11:25:39
 */
public class RouteVo {
	private static final long serialVersionUID = 1L;
	
	private String moduleName;
	private String remark;
	private String url;
	private String proxyUrl;
	private Map<String,RouteModuleVo>  moduleMap;//key为a.b
	private Map<String,RouteApiVo>  apiMap; //key为a.b.c
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * @return the moduleMap
	 */
	public Map<String, RouteModuleVo> getModuleMap() {
		return moduleMap;
	}
	/**
	 * @param moduleMap the moduleMap to set
	 */
	public void setModuleMap(Map<String, RouteModuleVo> moduleMap) {
		this.moduleMap = moduleMap;
	}
	/**
	 * @return the apiMap
	 */
	public Map<String, RouteApiVo> getApiMap() {
		return apiMap;
	}
	/**
	 * @param apiMap the apiMap to set
	 */
	public void setApiMap(Map<String, RouteApiVo> apiMap) {
		this.apiMap = apiMap;
	}
	
	
	
}
