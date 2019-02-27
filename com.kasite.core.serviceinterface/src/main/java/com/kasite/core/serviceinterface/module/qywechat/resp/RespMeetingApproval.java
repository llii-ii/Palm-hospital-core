package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 会议-事项审批RESP
 * 
 * @author 無
 *
 */
public class RespMeetingApproval extends AbsResp {
	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 审批编号
	 */
	private String approvalid;

	/**
	 * 成员ID
	 */
	private String memberid;

	/**
	 * 成员名称
	 */
	private String membername;

	/**
	 * 部门名称
	 */
	private String deptname;

	/**
	 * 会议id
	 */
	private String meetingid;

	/**
	 * 会议主题
	 */
	private String meetingtitle;

	/**
	 * 请假原因
	 */
	private String reason;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态 默认0=待审批 1=通过 2=驳回
	 */
	private Integer status;

	/**
	 * 插入时间
	 */
	private String inserttime;

	/**
	 * 修改时间
	 */
	private String updatetime;

	/**
	 * 审批人id
	 */
	private String operatorid;

	/**
	 * 审批人名字
	 */
	private String operatorname;
	

	/**
	 * 是否会议发起人 0-是  1-否
	 */
	private String meetingPublisher;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table QY_MEETING_APPROVAL
	 *
	 * @mbg.generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.ID
	 *
	 * @return the value of QY_MEETING_APPROVAL.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.ID
	 *
	 * @param id the value for QY_MEETING_APPROVAL.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.APPROVALID
	 *
	 * @return the value of QY_MEETING_APPROVAL.APPROVALID
	 *
	 * @mbg.generated
	 */
	public String getApprovalid() {
		return approvalid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.APPROVALID
	 *
	 * @param approvalid the value for QY_MEETING_APPROVAL.APPROVALID
	 *
	 * @mbg.generated
	 */
	public void setApprovalid(String approvalid) {
		this.approvalid = approvalid == null ? null : approvalid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.MEMBERID
	 *
	 * @return the value of QY_MEETING_APPROVAL.MEMBERID
	 *
	 * @mbg.generated
	 */
	public String getMemberid() {
		return memberid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.MEMBERID
	 *
	 * @param memberid the value for QY_MEETING_APPROVAL.MEMBERID
	 *
	 * @mbg.generated
	 */
	public void setMemberid(String memberid) {
		this.memberid = memberid == null ? null : memberid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.MEMBERNAME
	 *
	 * @return the value of QY_MEETING_APPROVAL.MEMBERNAME
	 *
	 * @mbg.generated
	 */
	public String getMembername() {
		return membername;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.MEMBERNAME
	 *
	 * @param membername the value for QY_MEETING_APPROVAL.MEMBERNAME
	 *
	 * @mbg.generated
	 */
	public void setMembername(String membername) {
		this.membername = membername == null ? null : membername.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.DEPTNAME
	 *
	 * @return the value of QY_MEETING_APPROVAL.DEPTNAME
	 *
	 * @mbg.generated
	 */
	public String getDeptname() {
		return deptname;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.DEPTNAME
	 *
	 * @param deptname the value for QY_MEETING_APPROVAL.DEPTNAME
	 *
	 * @mbg.generated
	 */
	public void setDeptname(String deptname) {
		this.deptname = deptname == null ? null : deptname.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.MEETINGID
	 *
	 * @return the value of QY_MEETING_APPROVAL.MEETINGID
	 *
	 * @mbg.generated
	 */
	public String getMeetingid() {
		return meetingid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.MEETINGID
	 *
	 * @param meetingid the value for QY_MEETING_APPROVAL.MEETINGID
	 *
	 * @mbg.generated
	 */
	public void setMeetingid(String meetingid) {
		this.meetingid = meetingid == null ? null : meetingid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.MEETINGTITLE
	 *
	 * @return the value of QY_MEETING_APPROVAL.MEETINGTITLE
	 *
	 * @mbg.generated
	 */
	public String getMeetingtitle() {
		return meetingtitle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.MEETINGTITLE
	 *
	 * @param meetingtitle the value for QY_MEETING_APPROVAL.MEETINGTITLE
	 *
	 * @mbg.generated
	 */
	public void setMeetingtitle(String meetingtitle) {
		this.meetingtitle = meetingtitle == null ? null : meetingtitle.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.REASON
	 *
	 * @return the value of QY_MEETING_APPROVAL.REASON
	 *
	 * @mbg.generated
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.REASON
	 *
	 * @param reason the value for QY_MEETING_APPROVAL.REASON
	 *
	 * @mbg.generated
	 */
	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.REMARK
	 *
	 * @return the value of QY_MEETING_APPROVAL.REMARK
	 *
	 * @mbg.generated
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.REMARK
	 *
	 * @param remark the value for QY_MEETING_APPROVAL.REMARK
	 *
	 * @mbg.generated
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.STATUS
	 *
	 * @return the value of QY_MEETING_APPROVAL.STATUS
	 *
	 * @mbg.generated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.STATUS
	 *
	 * @param status the value for QY_MEETING_APPROVAL.STATUS
	 *
	 * @mbg.generated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.INSERTTIME
	 *
	 * @return the value of QY_MEETING_APPROVAL.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.INSERTTIME
	 *
	 * @param inserttime the value for QY_MEETING_APPROVAL.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_APPROVAL.UPDATETIME
	 *
	 * @return the value of QY_MEETING_APPROVAL.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_APPROVAL.UPDATETIME
	 *
	 * @param updatetime the value for QY_MEETING_APPROVAL.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getMeetingPublisher() {
		return meetingPublisher;
	}

	public void setMeetingPublisher(String meetingPublisher) {
		this.meetingPublisher = meetingPublisher;
	}
	
	

}
