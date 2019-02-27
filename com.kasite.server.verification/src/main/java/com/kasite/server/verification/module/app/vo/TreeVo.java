package com.kasite.server.verification.module.app.vo;

import com.alibaba.fastjson.JSONObject;

public class TreeVo {
	private String name;//节点名
	private String id;//节点ID
	private Boolean isLeaf;//是否为叶子节点
	private JSONObject data;//该节点需要扩展的其它信息
	
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
}
