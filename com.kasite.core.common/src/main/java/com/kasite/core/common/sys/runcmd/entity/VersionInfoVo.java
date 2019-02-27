package com.kasite.core.common.sys.runcmd.entity;


public class VersionInfoVo {
	/**名称*/
	private String name;
	/**别称*/
	private String abs_name;
	/**备注*/
	private String remark;
	/**注册码*/
	private String pid;
	/**详细当前版本信息*/
	private VersionEntity version;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbs_name() {
		return abs_name;
	}
	public void setAbs_name(String abs_name) {
		this.abs_name = abs_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public VersionEntity getVersion() {
		return version;
	}
	public void setVersion(VersionEntity version) {
		this.version = version;
	}
	
}
