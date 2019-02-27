package com.kasite.core.elastic;

import java.util.List;

public class ElasticQueryResponseHits extends AbsElasticResponse{

	private Integer total;
	private String max_score;
	private List<ElasticQueryResponseHits_Hits> hits;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getMax_score() {
		return max_score;
	}
	public void setMax_score(String max_score) {
		this.max_score = max_score;
	}
	public List<ElasticQueryResponseHits_Hits> getHits() {
		return hits;
	}
	public void setHits(List<ElasticQueryResponseHits_Hits> hits) {
		this.hits = hits;
	}
	
	
	
	
}
