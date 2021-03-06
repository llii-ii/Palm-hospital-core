package com.kasite.core.serviceinterface.module.qywechat.dbo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 会议管理 This class was generated by MyBatis Generator. This class corresponds to
 * the database table QY_MEETING
 */
@Table(name = "QY_MEETING")
public class Meeting implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;

	/**
	 * 主键ID
	 */
	@Column(name = "MEETINGID")
	private String meetingid;

	/**
	 * 会议主题
	 */
	@Column(name = "TITLE")
	private String title;

	/**
	 * 会议日期
	 */
	@Column(name = "MEETINGDATE")
	private String meetingdate;

	/**
	 * 会议开始时间
	 */
	@Column(name = "TIMESTART")
	private String timestart;

	/**
	 * 会议结束时间
	 */
	@Column(name = "TIMEEND")
	private String timeend;

	/**
	 * 会议地址
	 */
	@Column(name = "ADDRESS")
	private String address;

	/**
	 * 会议地址ID
	 */
	@Column(name = "ADDRESSID")
	private String addressid;

	/**
	 * 会议学分
	 */
	@Column(name = "CREDITS")
	private Integer credits;

	/**
	 * 会议提醒 默认=0不提醒 5=5分钟 15=15分钟 30=30分钟 45=45分钟 60=1小时 1440=1天
	 */
	@Column(name = "REMIND")
	private String remind;

	/**
	 * 会议是否已经提醒 默认=0无   1=已提醒
	 */
	@Column(name = "ISREMIND")
	private Integer isremind;
	
	/**
	 * 会议签到 默认=0不签到 1=定位签到 2=扫码签到
	 */
	@Column(name = "SIGNIN")
	private Integer signin;

	/**
	 * 消息推送 0=不推送 1=推送
	 */
	@Column(name = "ISPUSH")
	private Integer ispush;

	/**
	 * 状态 0=草稿 1=发布 2=删除
	 */
	@Column(name = "STATUS")
	private Integer status;

	/**
	 * 插入时间
	 */
	@Column(name = "INSERTTIME")
	private String inserttime;

	/**
	 * 修改时间
	 */
	@Column(name = "UPDTETIME")
	private String updtetime;

	/**
	 * 会议议题
	 */
	@Column(name = "CONTENT")
	private String content;

	/**
	 * 参会人数
	 */
	@Column(name = "NUMBER")
	private Integer number;

	/**
	 * 操作人ID
	 */
	@Column(name = "OPERATORID")
	private String operatorid;

	/**
	 * 操作人姓名
	 */
	@Column(name = "OPERATORNAME")
	private String operatorname;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table QY_MEETING
	 *
	 * @mbg.generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.ID
	 *
	 * @return the value of QY_MEETING.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.ID
	 *
	 * @param id the value for QY_MEETING.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.TITLE
	 *
	 * @return the value of QY_MEETING.TITLE
	 *
	 * @mbg.generated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.TITLE
	 *
	 * @param title the value for QY_MEETING.TITLE
	 *
	 * @mbg.generated
	 */
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.MEETINGDATE
	 *
	 * @return the value of QY_MEETING.MEETINGDATE
	 *
	 * @mbg.generated
	 */
	public String getMeetingdate() {
		return meetingdate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.MEETINGDATE
	 *
	 * @param meetingdate the value for QY_MEETING.MEETINGDATE
	 *
	 * @mbg.generated
	 */
	public void setMeetingdate(String meetingdate) {
		this.meetingdate = meetingdate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.TIMESTART
	 *
	 * @return the value of QY_MEETING.TIMESTART
	 *
	 * @mbg.generated
	 */
	public String getTimestart() {
		return timestart;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.TIMESTART
	 *
	 * @param timestart the value for QY_MEETING.TIMESTART
	 *
	 * @mbg.generated
	 */
	public void setTimestart(String timestart) {
		this.timestart = timestart == null ? null : timestart.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.TIMEEND
	 *
	 * @return the value of QY_MEETING.TIMEEND
	 *
	 * @mbg.generated
	 */
	public String getTimeend() {
		return timeend;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.TIMEEND
	 *
	 * @param timeend the value for QY_MEETING.TIMEEND
	 *
	 * @mbg.generated
	 */
	public void setTimeend(String timeend) {
		this.timeend = timeend == null ? null : timeend.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.ADDRESS
	 *
	 * @return the value of QY_MEETING.ADDRESS
	 *
	 * @mbg.generated
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.ADDRESS
	 *
	 * @param address the value for QY_MEETING.ADDRESS
	 *
	 * @mbg.generated
	 */
	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.CREDITS
	 *
	 * @return the value of QY_MEETING.CREDITS
	 *
	 * @mbg.generated
	 */
	public Integer getCredits() {
		return credits;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.CREDITS
	 *
	 * @param credits the value for QY_MEETING.CREDITS
	 *
	 * @mbg.generated
	 */
	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.REMIND
	 *
	 * @return the value of QY_MEETING.REMIND
	 *
	 * @mbg.generated
	 */
	public String getRemind() {
		return remind;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.REMIND
	 *
	 * @param remind the value for QY_MEETING.REMIND
	 *
	 * @mbg.generated
	 */
	public void setRemind(String remind) {
		this.remind = remind == null ? null : remind.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.SIGNIN
	 *
	 * @return the value of QY_MEETING.SIGNIN
	 *
	 * @mbg.generated
	 */
	public Integer getSignin() {
		return signin;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.SIGNIN
	 *
	 * @param signin the value for QY_MEETING.SIGNIN
	 *
	 * @mbg.generated
	 */
	public void setSignin(Integer signin) {
		this.signin = signin;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.ISPUSH
	 *
	 * @return the value of QY_MEETING.ISPUSH
	 *
	 * @mbg.generated
	 */
	public Integer getIspush() {
		return ispush;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.ISPUSH
	 *
	 * @param ispush the value for QY_MEETING.ISPUSH
	 *
	 * @mbg.generated
	 */
	public void setIspush(Integer ispush) {
		this.ispush = ispush;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.STATUS
	 *
	 * @return the value of QY_MEETING.STATUS
	 *
	 * @mbg.generated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.STATUS
	 *
	 * @param status the value for QY_MEETING.STATUS
	 *
	 * @mbg.generated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.INSERTTIME
	 *
	 * @return the value of QY_MEETING.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.INSERTTIME
	 *
	 * @param inserttime the value for QY_MEETING.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.UPDTETIME
	 *
	 * @return the value of QY_MEETING.UPDTETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdtetime() {
		return updtetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.UPDTETIME
	 *
	 * @param updtetime the value for QY_MEETING.UPDTETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdtetime(String updtetime) {
		this.updtetime = updtetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING.CONTENT
	 *
	 * @return the value of QY_MEETING.CONTENT
	 *
	 * @mbg.generated
	 */
	public String getContent() {
		return content;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING.CONTENT
	 *
	 * @param content the value for QY_MEETING.CONTENT
	 *
	 * @mbg.generated
	 */
	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getAddressid() {
		return addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
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

	public String getMeetingid() {
		return meetingid;
	}

	public void setMeetingid(String meetingid) {
		this.meetingid = meetingid;
	}

	public Integer getIsremind() {
		return isremind;
	}

	public void setIsremind(Integer isremind) {
		this.isremind = isremind;
	}

}