package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespConsigneeAddress
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class RespCopyPurpose extends AbsResp{
	
	private String id;//复印内容id
	private String name;//复印内容名称
	private String sort;//排序
	private String state;//状态0删除1正常
	private String contentIds;//内容id多个以逗号隔开
	private String contentNames;//内容名称多个以逗号隔开
	private String enableDel;//能否删除0-否1-能
	private String enableShow;//是否显示0-否1-是
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getContentIds() {
		return contentIds;
	}
	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}
	public String getEnableDel() {
		return enableDel;
	}
	public void setEnableDel(String enableDel) {
		this.enableDel = enableDel;
	}
	public String getEnableShow() {
		return enableShow;
	}
	public void setEnableShow(String enableShow) {
		this.enableShow = enableShow;
	}
	public String getContentNames() {
		return contentNames;
	}
	public void setContentNames(String contentNames) {
		this.contentNames = contentNames;
	}
	
}
