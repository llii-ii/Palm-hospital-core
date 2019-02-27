package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * Resp参与人员
 * 
 * @author 無
 *
 */
public class RespToMember extends AbsResp {
	private Long id;

	/**
	 * 成员对应的订单ID、会议ID等
	 */
	private String uid;

	/**
	 * 人员ID、或者部门ID
	 */
	private String memberid;

	/**
	 * 人员名称、或者部门名称
	 */
	private String membername;

	/**
	 * 是否部门 默认0=否 1=是
	 */
	private int isDept;

	/**
	 * 类型 0=参与人 1=会议记录人 2=参加（报名） 3=请假 4=维修人 5=可见人员
	 */
	private Integer membertype;

//	/**
//	 * 状态 默认=0无状态\待定 1=参加（报名） 2=请假
//	 */
//	private Integer status;

	/**
	 * 创建时间
	 */
	private String inserttime;

	/**
	 * 部门名称
	 */
	private String deptname;

	/**
	 * 成员头像
	 */
	private String memberAvatar;

	public int getIsDept() {
		return isDept;
	}

	public void setIsDept(int isDept) {
		this.isDept = isDept;
	}

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
		this.uid = uid;
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

	public Integer getMembertype() {
		return membertype;
	}

	public void setMembertype(Integer membertype) {
		this.membertype = membertype;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getMemberAvatar() {
		return memberAvatar;
	}

	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}

}