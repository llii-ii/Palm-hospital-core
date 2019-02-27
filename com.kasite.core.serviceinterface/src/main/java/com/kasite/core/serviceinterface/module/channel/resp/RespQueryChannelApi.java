package com.kasite.core.serviceinterface.module.channel.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 渠道API配置
 * 
 * @author zhaoy
 *
 */
public class RespQueryChannelApi extends AbsResp {

	/**被调用的api  例: hos.HosYuYueWSApi.lockOrder*/
	private String api;
	
	/**api 名称*/
	private String apiname;
	
	/**系统模块名称*/
	private String sysname;
	
	/**模块名称*/
	private String modulename;
	
	/**新增时间*/
	private Timestamp createtime;
	
	/**该api是否需要md5加密*/
	private Integer md5;
	
	private Integer status;
	
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

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Integer getMd5() {
		return md5;
	}

	public void setMd5(Integer md5) {
		this.md5 = md5;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
