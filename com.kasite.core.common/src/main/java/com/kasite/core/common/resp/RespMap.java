package com.kasite.core.common.resp;

import java.util.HashMap;
import java.util.Map;

public class RespMap extends AbsResp {
	private Map<ApiKey, Object> map = new HashMap<>();

	public static RespMap get() {
		return new RespMap();
	}

	
	public Map<ApiKey, Object> getMap(){
		return this.map;
	}
	
	public String getString(ApiKey key) {
		return this.map.get(key)!=null?this.map.get(key).toString():null;
	}
	public Integer getInteger(ApiKey key) {
		return this.map.get(key)!=null?Integer.valueOf(map.get(key).toString()):null;
	}
	
	public RespMap add(ApiKey key,Object value) {
		this.map.put(key, value);
		return this;
	}
	public RespMap put(ApiKey key,Object value) {
		this.map.put(key, value);
		return this;
	}
	
}
