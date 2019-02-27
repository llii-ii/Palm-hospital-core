package com.kasite.core.httpclient.http;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpPostParam {
	private Map<String, Object> params;
	private Map<String, String> headerParams;
	public HttpPostParam(){
		params = new ConcurrentHashMap<String, Object>();
		headerParams = new ConcurrentHashMap<String, String>();
	}
	public HttpPostParam addHeaderParam(String name, String value){
		headerParams.put(name, value);
		return this;
	}
	public HttpPostParam addParam(String name, Object value){
		params.put(name, value);
		return this;
	}
	public Map<String, String> getHeaderParams() {
		return headerParams;
	}
	public Map<String, Object> getParams() {
		return params;
	}
}
