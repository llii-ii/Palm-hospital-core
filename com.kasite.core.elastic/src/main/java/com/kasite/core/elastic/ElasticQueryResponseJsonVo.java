package com.kasite.core.elastic;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class ElasticQueryResponseJsonVo {

	private int total;
	private List<JSONObject> _sources = new ArrayList<JSONObject>();
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<JSONObject> get_sources() {
		if(null == _sources) {
			_sources =  new ArrayList<JSONObject>();
		}
		return _sources;
	}
	public void set_sources(List<JSONObject> _sources) {
		this._sources = _sources;
	}
	
	
}
