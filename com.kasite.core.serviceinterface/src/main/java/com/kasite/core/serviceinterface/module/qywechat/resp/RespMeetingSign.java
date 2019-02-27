package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 会议签到、签退RESP
 * 
 * @author 無
 *
 */
public class RespMeetingSign extends AbsResp {
	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 附件对应的订单ID、会议ID等
	 */
	private String uid;

	/**
	 * 纬度
	 */
	private String latitude;

	/**
	 * 维度
	 */
	private String longitude;

	/**
	 * 位置精度
	 */
	private String accuracy;

	/**
	 * 签到、签退地点名称
	 */
	private String place;

	/**
	 * 签到人ID
	 */
	private String memberid;

	/**
	 * 签到人姓名
	 */
	private String membername;

	/**
	 * 签到、签退时间
	 */
	private String inserttime;

	/**
	 * 类型 0=签到 1=签退
	 */
	private Integer signtype;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 参与人名称
	 */
	private String gotoMember;

	/**
	 * 参会情况
	 */
	private String memberType;

	/**
	 * 请假理由
	 */
	private String reason;

	/**
	 * 签到
	 */
	private String signInType;

	/**
	 * 签到地点
	 */
	private String signInAddress;

	/**
	 * 签到时间
	 */
	private String signInTime;

	/**
	 * 签退
	 */
	private String signBackType;

	/**
	 * 签退地点
	 */
	private String signBackAddress;

	/**
	 * 签退时间
	 */
	private String signBackTime;

	/**
	 * 签到人数
	 */
	private Integer signInNum;

	/**
	 * 签退人数
	 */
	private Integer signBackNum;

	/**
	 * 请假人数
	 */
	private Integer leaveNum;

	/**
	 * 成员头像
	 */
	private String memberAvatar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid == null ? null : uid.trim();
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude == null ? null : latitude.trim();
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude == null ? null : longitude.trim();
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy == null ? null : accuracy.trim();
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place == null ? null : place.trim();
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
		this.membername = membername == null ? null : membername.trim();
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public Integer getSigntype() {
		return signtype;
	}

	public void setSigntype(Integer signtype) {
		this.signtype = signtype;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getGotoMember() {
		return gotoMember;
	}

	public void setGotoMember(String gotoMember) {
		this.gotoMember = gotoMember;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSignInType() {
		return signInType;
	}

	public void setSignInType(String signInType) {
		this.signInType = signInType;
	}

	public String getSignInAddress() {
		return signInAddress;
	}

	public void setSignInAddress(String signInAddress) {
		this.signInAddress = signInAddress;
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

	public String getSignBackAddress() {
		return signBackAddress;
	}

	public void setSignBackAddress(String signBackAddress) {
		this.signBackAddress = signBackAddress;
	}

	public String getSignBackTime() {
		return signBackTime;
	}

	public void setSignBackTime(String signBackTime) {
		this.signBackTime = signBackTime;
	}

	public Integer getSignInNum() {
		return signInNum;
	}

	public void setSignInNum(Integer signInNum) {
		this.signInNum = signInNum;
	}

	public Integer getSignBackNum() {
		return signBackNum;
	}

	public void setSignBackNum(Integer signBackNum) {
		this.signBackNum = signBackNum;
	}

	public Integer getLeaveNum() {
		return leaveNum;
	}

	public void setLeaveNum(Integer leaveNum) {
		this.leaveNum = leaveNum;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getMemberAvatar() {
		return memberAvatar;
	}

	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}

}
