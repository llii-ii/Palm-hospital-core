package com.kasite.core.httpclient.http;



/**
 * 解析参数中对应的url字符串结点
 * @author daiyanshui
 *
 */
public abstract class IParseParamUrl {
	static String URL_FLAG_START = "<soapenvaddurl>";
	static String URL_FLAG_END = "</soapenvaddurl>";
	public abstract String url();
	public abstract void url(String url);
	public abstract String param();
	public abstract void param(String param);
	
}
