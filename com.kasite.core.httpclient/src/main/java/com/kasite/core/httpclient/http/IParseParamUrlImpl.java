package com.kasite.core.httpclient.http;



public class IParseParamUrlImpl extends IParseParamUrl{
	private String url;
	private String param;
	private static IParseParamUrlImpl instance;
	public static synchronized IParseParamUrlImpl getInstance(){
		if(null == instance){
			instance = new IParseParamUrlImpl();
		}
		return instance;
	}
	
	public String url() {
		return url;
	}
	public void url(String url) {
		this.url = url;
	}
	public String param() {
		return param;
	}
	public void param(String param) {
		this.param = param;
	}
	
	/**
	 * 解析
	 * @param param 参数报文 非空
	 * @param map 请求对象入参  可以为空
	 * @return 返回url和请求参数
	 */
	public IParseParamUrl parse(String soapXml){
		String retVal = soapXml;
		String url = null;
		String param = soapXml;
		if (StringUtils.isNotBlank(param) && param.indexOf(URL_FLAG_START) >= 0) {
			int index1 = param.indexOf(URL_FLAG_START);
			int index2 = param.indexOf(URL_FLAG_END);
			url = param.substring(index1+URL_FLAG_START.length(),
					index2);
			String repUrl = param.substring(index1,
					index2+URL_FLAG_END.length());
			retVal = soapXml.replace(repUrl, "");
		}
		param(retVal);
		url(url);
		return this;
	}
	
}
