package com.kasite.core.serviceinterface.module.qywechat.dto;

/**
 * 会议签到、签退 vo
 * 
 * @author 無
 *
 */
public class MeetingSignVo {

	/**
	 * UID
	 */
	private String uid;

	/**
	 * 成员id
	 */
	private String memberid;

	/**
	 * 成员名称
	 */
	private String membername;

	/**
	 * 科室名称
	 */
	private String deptName;

	/**
	 * 类型
	 */
	private String memberType;

	/**
	 * 签到
	 */
	private String signInType;

	/**
	 * 签到/签退地点
	 */
	private String place;

	/**
	 * 签到时间
	 */
	private String signInTime;

	/**
	 * 签退
	 */
	private String signBackType;

	/**
	 * 签退时间
	 */
	private String signBackTime;

	/**
	 * 请假原因
	 */
	private String reason;

	/**
	 * 是否部门
	 */
	private String isDept;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getSignInType() {
		return signInType;
	}

	public void setSignInType(String signInType) {
		this.signInType = signInType;
	}

	public String getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(String signInTime) {
		this.signInTime = signInTime;
	}

	public String getSignBackType() {
		return signBackType;
	}

	public void setSignBackType(String signBackType) {
		this.signBackType = signBackType;
	}

	public String getSignBackTime() {
		return signBackTime;
	}

	public void setSignBackTime(String signBackTime) {
		this.signBackTime = signBackTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getIsDept() {
		return isDept;
	}

	public void setIsDept(String isDept) {
		this.isDept = isDept;
	}

}
