package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 会议req
 * 
 * @author 無
 *
 */
public class ReqMeeting extends AbsReq {
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
	private String updatetime;

	/**
	 * 会议议题
	 */
	private String content;

	/**
	 * 参会人数
	 */
	private Integer number;

	/**
	 * 附件url
	 */
	private String url;

	/**
	 * 附件名
	 */
	private String fileName;

	/**
	 * 附件类型
	 */
	private Integer attachmenttype;

	/**
	 * 部门ids
	 */
	private String deptIds;

	/**
	 * 操作人id
	 */
	private String operatorId;

	/**
	 * 查询类型 1-将参加 2-已结束 3-自己发起
	 */
	private String type;

	private String qyClientId;
	private String qyConfigKey;

	public ReqMeeting(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(ser, "id", false);
		this.meetingid = XMLUtil.getString(ser, "meetingId", false);
		this.title = XMLUtil.getString(ser, "title", false);
		this.meetingdate = XMLUtil.getString(ser, "meetingDate", false);
		this.content = XMLUtil.getString(ser, "content", false);
		this.timestart = XMLUtil.getString(ser, "timeStart", false);
		this.timeend = XMLUtil.getString(ser, "timeEnd", false);
		this.address = XMLUtil.getString(ser, "address", false);
		this.addressid = XMLUtil.getString(ser, "addressId", false);
		this.remind = XMLUtil.getString(ser, "remind", false);
		this.credits = XMLUtil.getInt(ser, "credits", false);
		this.signin = XMLUtil.getInt(ser, "signin", false);
		this.ispush = XMLUtil.getInt(ser, "ispush", false);
		this.status = XMLUtil.getInt(ser, "status", false);
		this.inserttime = XMLUtil.getString(ser, "insertTime", false);
		this.updatetime = XMLUtil.getString(ser, "updateTime", false);
		this.number = XMLUtil.getInt(ser, "number", false);
		this.url = XMLUtil.getString(ser, "url", false);
		this.fileName = XMLUtil.getString(ser, "fileName", false);
		this.attachmenttype = XMLUtil.getInt(ser, "attachmentType", false);
		this.operatorId = XMLUtil.getString(ser, "operatorId", false);
		this.type = XMLUtil.getString(ser, "type", false);
		// 企微渠道id、CONFIGKEY
		this.qyClientId = XMLUtil.getString(ser, "qyClientId", false);
		this.qyConfigKey = XMLUtil.getString(ser, "qyConfigKey", false);
	}

	public String getQyClientId() {
		return qyClientId;
	}

	public void setQyClientId(String qyClientId) {
		this.qyClientId = qyClientId;
	}

	public String getQyConfigKey() {
		return qyConfigKey;
	}

	public void setQyConfigKey(String qyConfigKey) {
		this.qyConfigKey = qyConfigKey;
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

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getMeetingid() {
		return meetingid;
	}

	public void setMeetingid(String meetingid) {
		this.meetingid = meetingid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAttachmenttype() {
		return attachmenttype;
	}

	public void setAttachmenttype(Integer attachmenttype) {
		this.attachmenttype = attachmenttype;
	}

}
