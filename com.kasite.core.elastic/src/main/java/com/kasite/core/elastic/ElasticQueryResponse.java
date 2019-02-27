package com.kasite.core.elastic;


public class ElasticQueryResponse extends AbsElasticResponse{

	private String _scroll_id;
	private Integer took;
	private Boolean timed_out;
	private ElasticQueryResponseShards _shards;
	private ElasticQueryResponseHits hits;

	
	public String get_scroll_id() {
		return _scroll_id;
	}

	public void set_scroll_id(String _scroll_id) {
		this._scroll_id = _scroll_id;
	}

	public Integer getTook() {
		return took;
	}

	public void setTook(Integer took) {
		this.took = took;
	}

	public Boolean getTimed_out() {
		return timed_out;
	}

	public void setTimed_out(Boolean timed_out) {
		this.timed_out = timed_out;
	}

	public ElasticQueryResponseShards get_shards() {
		return _shards;
	}

	public void set_shards(ElasticQueryResponseShards _shards) {
		this._shards = _shards;
	}

	public ElasticQueryResponseHits getHits() {
		return hits;
	}

	public void setHits(ElasticQueryResponseHits hits) {
		this.hits = hits;
	}
	
	
}
