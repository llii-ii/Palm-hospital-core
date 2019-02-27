package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * Resp用户查询
 * 
 * @author 無
 *
 */
public class RespUserQuery extends AbsResp {
	private String userid;
	private String name;
	private int department;
	private String mobile;
	private String email;
	private String position;
	private String gender;
	// 成员头像的mediaid，通过素材管理接口上传图片获得的mediaid
	private String avatarMediaid;

	public String getAvatarMediaid() {
		return avatarMediaid;
	}

	public void setAvatarMediaid(String avatarMediaid) {
		this.avatarMediaid = avatarMediaid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [userid=" + userid + ", name=" + name + ", department=" + department + ", mobile=" + mobile
				+ ", email=" + email + ", position=" + position + ", gender=" + gender + "]";
	}

	public String toJson() {
		String str = "{\"userid\": %s,\"name\": %s,\"department\": [1],\"mobile\": %s,\"email\": %s,\"gender\": %s}";

		return String.format(str, this.userid, this.name, this.mobile, this.email, this.gender);
	}

}