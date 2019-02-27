package com.kasite.client.crawler.modules.patient.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.elastic.ElasticQueryResponse;
import com.kasite.core.elastic.ElasticQueryResponseHits_Hits;
import com.kasite.core.elastic.IElasticQueryResponseScrollParse;

public class IElasticQueryResponseScrollParseImpl<T> implements IElasticQueryResponseScrollParse{
	private List<T> resultList;
	private Class<T> clazz;
	public IElasticQueryResponseScrollParseImpl(List<T> resultList,Class<T> clazz) {
		this.resultList = resultList;
		
		
	}
	@Override
	public void parse(ElasticQueryResponse response, ElasticQueryResponseHits_Hits hist, String _source, int index) {
		T entity = JSONObject.toJavaObject(JSONObject.parseObject(_source), clazz);
		resultList.add(entity);
	}

}
