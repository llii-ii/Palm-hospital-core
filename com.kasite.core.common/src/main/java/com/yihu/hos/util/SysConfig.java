package com.yihu.hos.util;
/**
 * 
	   ID                   VARCHAR2(36)         not null,
	   HosID                varchar2(20),
	   HosName              varchar2(50),
	   NodeType             Varchar2(30),
	   Info                 varchar2(100),
	   State                Number
 * @author daiys
 * @date 2015-5-27
 */
public class SysConfig {

	private String id;
	private String hosid;
	private String hosname;
	private String api;
	private String apiname;
	private String isout;
	private String info;
	private Integer state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHosid() {
		return hosid;
	}
	public void setHosid(String hosid) {
		this.hosid = hosid;
	}
	public String getHosname() {
		return hosname;
	}
	public void setHosname(String hosname) {
		this.hosname = hosname;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getApiname() {
		return apiname;
	}
	public void setApiname(String apiname) {
		this.apiname = apiname;
	}
	public String getIsout() {
		return isout;
	}
	public void setIsout(String isout) {
		this.isout = isout;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
