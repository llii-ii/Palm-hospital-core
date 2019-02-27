package com.kasite.core.elastic;

import java.util.HashMap;
import java.util.Map;

public class ElasticQuery {
	public static final String SIZE = "size";
	public static final String FROM = "from";
	public static final String SORT = "sort";
	public static final String AGGS = "aggs";
	private Map<String, Object> _search;
	public ElasticQuery(){
		_search = new HashMap<String, Object>();
	}
	public ElasticQuery setSize(int size){
		_search.put(SIZE, size);
		return this;
	}
	public ElasticQuery setFrom(int from){
		_search.put(FROM, from);
		return this;
	}
	public ElasticQuery setSort(Object...strings ){
		_search.put(SORT, strings);
		return this;
	}
	public ElasticQuery setAggs(Map<String, Object> aggs ){
		_search.put(AGGS, aggs);
		return this;
	}
	
	
	public ElasticQuerySimple createSimpleQuery(){
		ElasticQuerySimple simpleQuery = new ElasticQuerySimple(_search);
		return simpleQuery;
	}
	
	
	
}
