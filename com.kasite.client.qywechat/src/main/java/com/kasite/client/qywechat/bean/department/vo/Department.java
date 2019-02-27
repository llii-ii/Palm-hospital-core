package com.kasite.client.qywechat.bean.department.vo;

/**
 * 部门
 * 
 * @author 無
 *
 */
public class Department {
	private int id;
	private String name;
	private int parentid;
	private int order;

	public Department(int id, String name, int parentid) {
		super();
		this.id = id;
		this.name = name;
		this.parentid = parentid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}