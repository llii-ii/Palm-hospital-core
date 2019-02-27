package com.kasite.core.elastic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticQuerySimpleBool {
	public static final String BOOL="bool";
	public static final String TERM="term";
	public static final String MATCH="match";
	public static final String MUST_NOT="must_not";
	public static final String MUST="must";
	public static final String RANGE = "range";
	public static final String SHOULD = "should";
	public static final String WILDCARD = "wildcard";
	private Map<String, Object> bool;
	private Map<String, Object> _search;
	private List<Map<String, Object>> must;
	private List<Map<String, Object>> mustNot;
	private List<Map<String, Object>> should;
	
	public ElasticQuerySimpleBool(Map<String, Object> _search,Map<String, Object> query) {
		bool = new HashMap<String, Object>();
		this._search = _search;
		this.must = new ArrayList<Map<String,Object>>();
		this.mustNot = new ArrayList<Map<String,Object>>();
		this.should = new ArrayList<Map<String,Object>>();
		buildShouldMustBool();
		bool.put(MUST, must);
		bool.put(MUST_NOT, mustNot);
		query.put(BOOL, bool);
	}
	
	public ElasticQuerySimpleBool buildShouldMustBool(){
		Map<String, Object> shouldQuery = new HashMap<String, Object>();
		Map<String, Object> shouldBool = new HashMap<String, Object>();
		shouldBool.put(SHOULD, should);
		shouldQuery.put(BOOL, shouldBool);
		must.add(shouldQuery);
		return this;
	}
	
	/**
	 * Should wildcard
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addShouldWildcard(String key,Object value){
		Map<String, Object> wildcard = new HashMap<String, Object>(1);
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		wildcard.put(WILDCARD, p);
		should.add(wildcard);
		return this;
	}
	
	/**
	 * 文档应该匹配should子句查询的一个或多个
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addShouldTerm(String key,Object value){
		add2TermList(key, value, should);
		return this;
	}
	/**
	 * 文档应该匹配should子句查询的一个或多个
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addShouldMatch(String key,Object value){
		add2MatchList(key, value, should);
		return this;
	}
	/**
	 * match查询会先对搜索词进行分词,分词完毕后再逐个对分词结果进行匹配，因此相比于term的精确搜索，match是分词匹配搜索
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustMatch(String key,Object value){
		add2MatchList(key, value, must);
		return this;
	}
	/**
	 * 精确查找 全匹配
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustTerm(String key,Object value){
		add2TermList(key, value, must);
		return this;
	}
	/**
	 * Match 模糊匹配  MustNot
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustNotMatch(String key,Object value){
		add2MatchList(key, value, mustNot);
		return this;
	}
	/**
	 * Term 精确查找 MustNot
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustNotTerm(String key,Object value){
		add2TermList(key, value, mustNot);
		return this;
	}
	/**
	 * MustR 查询范围 
	 * @param type
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustRange(ElasticRangeEnum type,String key, Object value){
		add2RangeList(type, key, value, must);
		return this;
	}
	/**
	 * MustNot 查询范围  
	 * @param type
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustNotRange(ElasticRangeEnum type,String key, Object value){
		add2RangeList(type, key, value, mustNot);
		return this;
	}

	/**
	 * Must wildcard
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustWildcard(String key,Object value){
		Map<String, Object> wildcard = new HashMap<String, Object>(1);
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		wildcard.put(WILDCARD, p);
		must.add(wildcard);
		return this;
	}
	
	/**
	 * MustNot wildcard
	 * @param key
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMustNotWildcard(String key,Object value){
		Map<String, Object> wildcard = new HashMap<String, Object>(1);
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		wildcard.put(WILDCARD, p);
		mustNot.add(wildcard);
		return this;
	}
	
	/**
	 * add minimum_should_match
	 * @param value
	 * @return
	 */
	public ElasticQuerySimpleBool addMinimumShouldMatch( String value) {
		bool.put("minimum_should_match", value);
		return this;
	}
	
	private void add2MatchList(String key,Object value,List<Map<String, Object> > list){
		Map<String, Object> term = new HashMap<String, Object>(1);
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		term.put(MATCH, p);
		list.add(term);
	}
	private void add2TermList(String key,Object value,List<Map<String, Object> > list){
		Map<String, Object> term = new HashMap<String, Object>(1);
		Map<String, Object> p = new HashMap<String, Object>(1);
		p.put(key, value);
		term.put(TERM, p);
		list.add(term);
	}
	/**
	 * {
			"range": {
				"reqtime": {
					"gte": "2018-01-02 00:00:00",
					"lt": "2018-01-03 00:00:00"
				}
			}
		}
	 * @param type
	 * @param key
	 * @param value
	 * @param list
	 */
	private void add2RangeList(ElasticRangeEnum type,String key, Object value,List<Map<String, Object> > list){
		boolean is = false;
		for (Map<String, Object> map : list) {
			for (Map.Entry<String, Object> qp : map.entrySet()) {
				String qk = qp.getKey();
				if(RANGE.equals(qk)){
					if(null != qp.getValue()){
						Map<String, Object> qk2 = (Map<String, Object>) qp.getValue();
						if(qk2.containsKey(key)){
							Map<String, Object> qk3 = (Map<String, Object>) qk2.get(key);
							qk3.put(type.name(), value);
							is = true;
						}
					}
				}
			}
		}
		if(!is){
			Map<String, Object> f = new HashMap<String, Object>(2);
			Map<String, Object> p = new HashMap<String, Object>(2);
			p.put(type.name(), value);
			f.put(key, p);
			Map<String, Object> range = new HashMap<String, Object>(2);
			range.put(RANGE, f);
			list.add(range);
		}
	}
	
	public String toParam(){
		return com.alibaba.fastjson.JSONObject.toJSONString(_search);
	}
	
	public Map<String, Object> getSearch(){
		return _search;
	}

}
