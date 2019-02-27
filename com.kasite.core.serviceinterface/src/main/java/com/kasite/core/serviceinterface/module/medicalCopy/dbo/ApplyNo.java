package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="TB_APPLY_NO")
public class ApplyNo extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;
	private String titles;//字段标题（多个以逗号隔开）
	private String sort;//排序
	private String mandatory;//是否必填
	private String enableShow;//是否显示
	private String type;//字段类型（1=文本 2=单选 3=多选）
	private String replyOptionNames;//选项名称（以逗号隔开）
	private String replyOptions;//回复选项（多个以逗号隔开）
	private String state;//状态：0删除1启用
	private String description;//描述
	private String enableDel;//是否能删除1是0否
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getEnableShow() {
		return enableShow;
	}
	public void setEnableShow(String enableShow) {
		this.enableShow = enableShow;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReplyOptionNames() {
		return replyOptionNames;
	}
	public void setReplyOptionNames(String replyOptionNames) {
		this.replyOptionNames = replyOptionNames;
	}
	public String getReplyOptions() {
		return replyOptions;
	}
	public void setReplyOptions(String replyOptions) {
		this.replyOptions = replyOptions;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnableDel() {
		return enableDel;
	}
	public void setEnableDel(String enableDel) {
		this.enableDel = enableDel;
	}
	
	

}
