package com.kasite.core.elastic;

public class ElasticRestClientBus {
	/**
	 * 创建一个httpclient 连接 并设置初始属性，默认连接为长连接。
	 * @param method
	 * @param url
	 * @param type
	 * @return
	 */
	public static ElasticRestClientBusSender create(String ips,ElasticRestTypeEnum type){
		ElasticRestClientBusSender bus = new ElasticRestClientBusSender();
		bus.setMethod(type);
		bus.setIps(ips);
		return bus;
	}
}
