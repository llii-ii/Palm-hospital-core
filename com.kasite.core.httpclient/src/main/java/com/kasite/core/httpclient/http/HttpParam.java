package com.kasite.core.httpclient.http;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class HttpParam {
	private StringBuffer sb;
	private Map<String, String> headerParams;
	private String param;
	private Map<String, String> paramMap;
	public HttpParam() {
		sb = new StringBuffer();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sb.append("&SequenceNo=").append(format.format(date));
		headerParams = new ConcurrentHashMap<String, String>();
		paramMap = new ConcurrentHashMap<String,String>();
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public HttpParam addHeaderParam(String name, String value){
		headerParams.put(name, value);
		paramMap.put(name, value);
		return this;
	}
	public HttpParam addParam(String name, Object value) {
		sb.append("&");
		sb.append(name).append("=").append(value);
		if(null!=value) {
			paramMap.put(name, value.toString());
		}
		return this;
	}
	public HttpParam setParam(String param) {
		this.param = param;
		return this;
	}

	/**
	 * 将参数编码后加入到url字符串中
	 * @param name
	 * @param value
	 * @param enc URLEncoder 编码  utf-8/gbk／gb2312/等
	 * @return
	 */
	public HttpParam addParam(String name, Object value, String enc) {
		sb.append("&");
		if ((value instanceof String))
			try {
				value = URLEncoder.encode((String) value, enc);
				paramMap.put(name, value.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		sb.append(name).append("=").append(value);
		return this;
	}

	public String toString() {
		return sb.toString();
	}
	public String toGetString() {
		return sb.toString();
	}
	public String toBodyString() {
		if(null != param){
			return param;
		}
		return sb.substring(1, sb.length());
	}
	public Map<String, String> getHeaderParams() {
		return headerParams;
	}
	
}
