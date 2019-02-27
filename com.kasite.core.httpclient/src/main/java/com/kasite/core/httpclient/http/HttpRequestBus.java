package com.kasite.core.httpclient.http;




public class HttpRequestBus {
	
	static HttpRequestConfigVo config;
	static YYHisMethodInf method;
	static String url;
	static RequestType type;
	
	private static boolean inited;
	public static synchronized void init(HttpRequestConfigVo config) {
		if (inited) {
			return;
		}
		System.out.println("HttpRequstBus init");
		notNull(config);
		HttpRequestBus.config=config;
		inited=true;
		
	}
	/**
	 * 创建一个httpclient 连接 并设置初始属性，默认连接为长连接。
	 * @param method
	 * @param url
	 * @param type
	 * @return
	 */
	public static HttpRequstBusSender create(String url,RequestType type){
		HttpRequstBusSender sbs = new HttpRequstBusSender();
		if(null != config){
			sbs.httpRequestConfigVo(config);
		}
		return sbs.requestType(type).url(url).clientKey(url).isLongConnection(true);
	}
	static void notNull(Object obj){
		if(obj==null){
			throw new IllegalArgumentException("object cannot be null");
		}
	}
}
