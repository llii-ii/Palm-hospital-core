package com.yihu.hos.bean;

import java.util.HashMap;
import java.util.Map;

public class ParamSplitVo {
	private Map<String, String> maps = new HashMap<String, String>();
	private String coloms;
	public void set(String key,String value){
		this.coloms = value;
		for (Map.Entry<String, String> entity : maps.entrySet()) {
			String v = entity.getValue();
			String val = "<"+key+">"+value+"</"+key+">";
			String vv = v.replaceAll(val, "");
			maps.put(entity.getKey(), vv);
		}
		maps.put(key, value);
	}
	public Map<String, String> getMaps() {
		return maps;
	}
	public void setColoms(String coloms) {
		this.coloms = coloms;
	}
	public String getValue(String key){
		return maps.get(key);
	}
	public ParamSplitVo() {
		super();
	}
	public String getColoms() {
		return coloms;
	}
}
