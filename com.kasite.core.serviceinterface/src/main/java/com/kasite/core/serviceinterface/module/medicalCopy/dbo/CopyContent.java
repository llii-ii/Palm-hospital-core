package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="TB_COPY_CONTENT")
public class CopyContent extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;//复印内容id
	private String name;//复印内容名称
	private String sort;//排序
	private String type;//必选类型:1慢病申报2继续治疗3.报销4.自留(多个以逗号隔开）
	private String isDefault;//是否默认 0否1是
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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
