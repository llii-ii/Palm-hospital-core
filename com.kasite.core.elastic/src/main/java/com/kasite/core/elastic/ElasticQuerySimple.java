package com.kasite.core.elastic;

import java.util.HashMap;
import java.util.Map;

public class ElasticQuerySimple {
	public static final String QUERY = "query";
	public static final String WILDCARD = "wildcard";
	private Map<String, Object> query;
	private Map<String, Object> _search;
	public ElasticQuerySimple(Map<String, Object> _search) {
		this._search = _search;
		query = new HashMap<String, Object>();
		_search.put(QUERY, query);
	}
	public ElasticQuerySimpleBool createElasticQuerySimpleBool(){
		ElasticQuerySimpleBool bool = new ElasticQuerySimpleBool(_search, query);
		return bool;
	}
	public ElasticQuerySimple addWildcard(String key,Object value){
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		query.put(WILDCARD, p);
		return this;
	}
}
