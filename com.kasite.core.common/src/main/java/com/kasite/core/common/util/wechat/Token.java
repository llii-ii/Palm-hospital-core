package com.kasite.core.common.util.wechat;

import com.alibaba.fastjson.JSON;

/**
 * @author MECHREV
 */
public class Token {

	
	private long time;
	
	private String value;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
