package com.yihu.hos.web;

import java.util.ArrayList;
import java.util.List;

public class Params {
	
	private List<Params> params;
	
	public List<Params> getParams() {
		return params;
	}

	public void setParams(List<Params> params) {
		this.params = params;
	}

	public Params() {
		super();
		params = new ArrayList<Params>();
	}
	
	public Params add(String key,Object val){
		Params p = new Params();
		p.setKey(key);
		p.setValue(val);
		this.params.add(p);
		return this;
	}
	
	private String key;
	private Object value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
