package com.kasite.client.qywechat.bean.user.vo;

/**
 * 用户
 * 
 * @author 無
 *
 */
public class User {
	private String userid;
	private String name;
	private String[] department;
	private String mobile;
	private String email;
	private String position;
	private String gender;
	// 成员头像url
	private String avatar;

	public User() {
	};

	public User(String userid, String name, String[] department, String mobile, String email, String position,
			String gender, String avatar) {
		super();
		this.userid = userid;
		this.name = name;
		this.department = department;
		this.mobile = mobile;
		this.email = email;
		this.position = position;
		this.gender = gender;
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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