package com.kasite.core.elastic;


public class ElasticBus {
	/***
	 * 默认分页参数 每页 10条 
	 * @return
	 */
	public static synchronized ElasticQuery create(){
		ElasticQuery query = new ElasticQuery();
		query.setSize(10);
		query.setFrom(0);
		return query;
	}
	
	
}
