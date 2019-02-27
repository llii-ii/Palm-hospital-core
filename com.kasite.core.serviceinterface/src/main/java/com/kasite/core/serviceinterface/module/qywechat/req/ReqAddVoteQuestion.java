package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ增加投票、问卷
 * 
 * @author 無
 *
 */
public class ReqAddVoteQuestion extends AbsReq {
	public ReqAddVoteQuestion(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getInt(dataEl, "id", false);
		this.uid = XMLUtil.getString(dataEl, "uid", false);
		this.title = XMLUtil.getString(dataEl, "title", false);
		this.beginIntro = XMLUtil.getString(dataEl, "beginIntro", false);
		this.endingIntro = XMLUtil.getString(dataEl, "endingIntro", false);
		this.startTime = XMLUtil.getString(dataEl, "startTime", false);
		this.endTime = XMLUtil.getString(dataEl, "endTime", false);
		this.remark = XMLUtil.getString(dataEl, "remark", false);
		this.status = XMLUtil.getInt(dataEl, "status", false);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", false);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", false);
		this.themeType = XMLUtil.getInt(dataEl, "themeType", false);
		// 附件
		this.attachmentUrl = XMLUtil.getString(dataEl, "attachmentUrl", false);
		this.attachmentName = XMLUtil.getString(dataEl, "attachmentName", false);
		// 人员
		this.memberIds = XMLUtil.getString(dataEl, "memberIds", false);
		this.memberNames = XMLUtil.getString(dataEl, "memberNames", false);
		// 企微渠道id、CONFIGKEY
		this.qyClientId = XMLUtil.getString(dataEl, "qyClientId", false);
		this.qyConfigKey = XMLUtil.getString(dataEl, "qyConfigKey", false);
	}

	private String qyClientId;
	private String qyConfigKey;
	private int id;
	private String uid;
	/**
	 * 主题
	 */
	private String title;
	/**
	 * 首语
	 */
	private String beginIntro;
	/**
	 * 尾语
	 */
	private String endingIntro;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态 0=待发布 1=发布 2=结束 3=删除
	 */
	private Integer status;
	/**
	 * 类型 0=投票 1=问卷
	 */
	private Integer themeType;
	/**
	 * 操作人ID
	 */
	private String operatorId;
	/**
	 * 操作人姓名
	 */
	private String operatorName;
	/**
	 * 附件url
	 */
	private String attachmentUrl;
	/**
	 * 附件名称
	 */
	private String attachmentName;
	/**
	 * 成员IDs
	 */
	private String memberIds;
	/**
	 * 成员姓名D
	 */
	private String memberNames;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	public String getMemberNames() {
		return memberNames;
	}

	public void setMemberNames(String memberNames) {
		this.memberNames = memberNames;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBeginIntro() {
		return beginIntro;
	}

	public void setBeginIntro(String beginIntro) {
		this.beginIntro = beginIntro;
	}

	public String getEndingIntro() {
		return endingIntro;
	}

	public void setEndingIntro(String endingIntro) {
		this.endingIntro = endingIntro;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Override
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Integer getThemeType() {
		return themeType;
	}

	public void setThemeType(Integer themeType) {
		this.themeType = themeType;
	}

}
