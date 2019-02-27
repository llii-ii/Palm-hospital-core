package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="TB_COPY_PURPOSE")
public class CopyPurpose extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;//复印内容id
	private String name;//复印内容名称
	private String sort;//排序
	private String state;//状态0删除1正常
	private String contentIds;//内容id多个以逗号隔开
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
	
	
}
