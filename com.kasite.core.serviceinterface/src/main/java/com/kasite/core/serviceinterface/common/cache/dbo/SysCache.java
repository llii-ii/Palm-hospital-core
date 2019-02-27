package com.kasite.core.serviceinterface.common.cache.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="SYS_CACHE")
public class SysCache implements Serializable{
	private static final long serialVersionUID = -7199464170105922084L;
	@Id
	@KeySql(useGeneratedKeys = true)
	private long id;
	/**
	 * 科室代码
	 */
	private String sid;
	
	private String value;
	
	private Timestamp invalidTime;
 
	public Timestamp getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Timestamp invalidTime) {
		this.invalidTime = invalidTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
