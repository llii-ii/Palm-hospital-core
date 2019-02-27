package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespConsigneeAddress
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class RespSetting extends AbsResp{
	
	private String id;
	private String description;//描述
	private String types;//类型（多条以逗号隔开）
	private String defaultType;//默认类型
	private String remark;//备注
	private String name;//设置名称
	private String settingSwitch;//0关闭1开启(认证设置为人脸识别，病历设置为是否
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getDefaultType() {
		return defaultType;
	}
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSettingSwitch() {
		return settingSwitch;
	}
	public void setSettingSwitch(String settingSwitch) {
		this.settingSwitch = settingSwitch;
	}	
	
}