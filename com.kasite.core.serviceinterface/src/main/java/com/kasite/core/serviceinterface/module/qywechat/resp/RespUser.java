package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.Resp;

/**
 * 人员RESP
 * 
 * @author 無
 *
 */
public class RespUser extends Resp {
	// 成员id
	private String userId;
	// 名字
	private String name;
	// 部门ids
	private String[] department;
	// 手机号码
	private String mobile;
	// 职务信息
	private String position;
	// 性别
	private Integer gender;
	// 邮箱
	private String email;
	// 头像url
	private String avatar;
	// 别名
	private String alias;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getDepartment() {
		return department;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
