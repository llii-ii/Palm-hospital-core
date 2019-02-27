package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 成员积分RESP
 * 
 * @author 無
 *
 */
public class RespMemberCreditsInfo extends AbsResp {
	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 成员ID
	 */
	private String memberid;

	/**
	 * 会议id
	 */
	private String meetingid;

	/**
	 * 会议主题
	 */
	private String meetingtitle;

	/**
	 * 学分
	 */
	private Integer credits;

	/**
	 * 插入时间
	 */
	private String inserttime;

	/**
	 * 修改时间
	 */
	private String updatetime;

	/**
	 * 会议日期
	 */
	private String meetingdate;

	/**
	 * 会议开始时间
	 */
	private String timestart;

	/**
	 * 会议结束时间
	 */
	private String timeend;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.ID
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.ID
	 *
	 * @param id the value for QY_MEMBER_CREDITS_INFO.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.MEMBERID
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.MEMBERID
	 *
	 * @mbg.generated
	 */
	public String getMemberid() {
		return memberid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.MEMBERID
	 *
	 * @param memberid the value for QY_MEMBER_CREDITS_INFO.MEMBERID
	 *
	 * @mbg.generated
	 */
	public void setMemberid(String memberid) {
		this.memberid = memberid == null ? null : memberid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.MEETINGID
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.MEETINGID
	 *
	 * @mbg.generated
	 */
	public String getMeetingid() {
		return meetingid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.MEETINGID
	 *
	 * @param meetingid the value for QY_MEMBER_CREDITS_INFO.MEETINGID
	 *
	 * @mbg.generated
	 */
	public void setMeetingid(String meetingid) {
		this.meetingid = meetingid == null ? null : meetingid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.MEETINGTITLE
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.MEETINGTITLE
	 *
	 * @mbg.generated
	 */
	public String getMeetingtitle() {
		return meetingtitle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.MEETINGTITLE
	 *
	 * @param meetingtitle the value for QY_MEMBER_CREDITS_INFO.MEETINGTITLE
	 *
	 * @mbg.generated
	 */
	public void setMeetingtitle(String meetingtitle) {
		this.meetingtitle = meetingtitle == null ? null : meetingtitle.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.CREDITS
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.CREDITS
	 *
	 * @mbg.generated
	 */
	public Integer getCredits() {
		return credits;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.CREDITS
	 *
	 * @param credits the value for QY_MEMBER_CREDITS_INFO.CREDITS
	 *
	 * @mbg.generated
	 */
	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.INSERTTIME
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.INSERTTIME
	 *
	 * @param inserttime the value for QY_MEMBER_CREDITS_INFO.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS_INFO.UPDATETIME
	 *
	 * @return the value of QY_MEMBER_CREDITS_INFO.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS_INFO.UPDATETIME
	 *
	 * @param updatetime the value for QY_MEMBER_CREDITS_INFO.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getMeetingdate() {
		return meetingdate;
	}

	public void setMeetingdate(String meetingdate) {
		this.meetingdate = meetingdate;
	}

	public String getTimestart() {
		return timestart;
	}

	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

}
