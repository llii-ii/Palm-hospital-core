package com.kasite.core.httpclient.http;


public class IParseParamHeaderImpl extends IParseParamHeader {
	private static IParseParamHeaderImpl instance;
	
	public static synchronized IParseParamHeader getInstance(){
		if(null == instance){
			instance = new IParseParamHeaderImpl();
		}
		return instance;
	}
	
	private String respXMl;
	public String respXMl() {
		return respXMl;
	}
	public void respXMl(String respXMl) {
		this.respXMl = respXMl;
	}
	
}
