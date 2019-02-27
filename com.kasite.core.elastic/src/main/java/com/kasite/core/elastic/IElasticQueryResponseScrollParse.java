package com.kasite.core.elastic;


public interface IElasticQueryResponseScrollParse {

	public void parse(ElasticQueryResponse response,ElasticQueryResponseHits_Hits hist,String _source,int index);
	/**
	 * 解析分页总记录数
	 * @param total
	 */
	void parseTotal(int total);
}
