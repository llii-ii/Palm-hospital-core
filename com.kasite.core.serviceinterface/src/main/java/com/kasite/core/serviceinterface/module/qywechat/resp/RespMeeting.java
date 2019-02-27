package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 会议RESP
 * 
 * @author 無
 *
 */
public class RespMeeting extends AbsResp {
	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 会议主题
	 */
	private String meetingid;

	/**
	 * 会议主题
	 */
	private String title;

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
	 * 会议地址
	 */
	private String address;

	/**
	 * 会议地址ID
	 */
	private String addressid;

	/**
	 * 会议学分
	 */
	private Integer credits;

	/**
	 * 会议提醒 默认=0不提醒 5=5分钟 15=15分钟 30=30分钟 45=45分钟 60=1小时 1440=1天
	 */
	private String remind;

	/**
	 * 会议签到 默认=0不签到 1=定位签到 2=扫码签到
	 */
	private Integer signin;

	/**
	 * 消息推送 0=不推送 1=推送
	 */
	private Integer ispush;

	/**
	 * 状态 0=草稿 1=发布 2=删除
	 */
	private Integer status;

	/**
	 * 插入时间
	 */
	private String inserttime;

	/**
	 * 修改时间
	 */
	private String updtetime;

	/**
	 * 会议议题
	 */
	private String content;

	/**
	 * 参会人数
	 */
	private Integer number;

	/**
	 * 操作人ID
	 */
	private String operatorid;

	/**
	 * 操作人姓名
	 */
	private String operatorname;

	/**
	 * 附件路径
	 */
	private String fileUrl;

	/**
	 * 附件名
	 */
	private String fileName;

	/**
	 * 附件类型
	 */
	private Integer attachmenttype;

	/**
	 * 二维码签到图片地址
	 */
	private String qrPicSignInUrl;

	/**
	 * 二维码签退图片地址
	 */
	private String qrPicSignOutUrl;

	/**
	 * 当前登录人姓名
	 */
	private String loginMemberName;

	public String getQrPicSignInUrl() {
		return qrPicSignInUrl;
	}

	public void setQrPicSignInUrl(String qrPicSignInUrl) {
		this.qrPicSignInUrl = qrPicSignInUrl;
	}

	public String getQrPicSignOutUrl() {
		return qrPicSignOutUrl;
	}

	public void setQrPicSignOutUrl(String qrPicSignOutUrl) {
		this.qrPicSignOutUrl = qrPicSignOutUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	public Integer getSignin() {
		return signin;
	}

	public void setSignin(Integer signin) {
		this.signin = signin;
	}

	public Integer getIspush() {
		return ispush;
	}

	public void setIspush(Integer ispush) {
		this.ispush = ispush;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getAddressid() {
		return addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public String getUpdtetime() {
		return updtetime;
	}

	public void setUpdtetime(String updtetime) {
		this.updtetime = updtetime;
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

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getAttachmenttype() {
		return attachmenttype;
	}

	public void setAttachmenttype(Integer attachmenttype) {
		this.attachmenttype = attachmenttype;
	}

	public String getLoginMemberName() {
		return loginMemberName;
	}

	public void setLoginMemberName(String loginMemberName) {
		this.loginMemberName = loginMemberName;
	}

}
