package com.kasite.core.serviceinterface.module.rf.dto;

import java.util.Map;

public class MapVo {

	private String name;
	
	private String value;
	
	private Map<String, String> itemStyle;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, String> getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(Map<String, String> itemStyle) {
		this.itemStyle = itemStyle;
	}
	
}
