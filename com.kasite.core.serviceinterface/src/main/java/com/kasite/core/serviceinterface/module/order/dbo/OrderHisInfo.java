package com.kasite.core.serviceinterface.module.order.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="O_ORDER_HISINFO")
public class OrderHisInfo {
	@Id
	@KeySql(useGeneratedKeys = true)
	private long id;
	/**调用类型名*/
	private String name;
	/**对应的订单唯一ID*/
	private String sid;
	/**对应订单结果内容*/
	private String content;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
