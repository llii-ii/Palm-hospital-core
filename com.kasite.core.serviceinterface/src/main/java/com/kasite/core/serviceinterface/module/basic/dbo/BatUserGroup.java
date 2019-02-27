package com.kasite.core.serviceinterface.module.basic.dbo;


import java.io.Serializable;

/**
 * 用户分组对象
 * 
 * @author cyh
 * 
 */
public class BatUserGroup implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer groupid;
	private String groupname;
	private Integer count;
	private String accountid;
	private String synctime;
	private Integer state;
	private String sysid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getSynctime() {
		return synctime;
	}
	public void setSynctime(String synctime) {
		this.synctime = synctime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}
	
}
