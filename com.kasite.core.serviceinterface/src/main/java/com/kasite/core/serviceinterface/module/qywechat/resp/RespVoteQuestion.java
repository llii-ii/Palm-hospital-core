package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * RESP投票、问卷
 * 
 * @author 無
 *
 */
public class RespVoteQuestion extends AbsResp {
	/**
	 * 主键ID 自增
	 */
	private Long id;
	
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
	 * 类型 0=投票 1=问卷
	 */
	private Integer themeType;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态 0=待发布 1=发布 2=结束 3=删除
	 */
	private Integer status;
	/**
	 * 操作人ID
	 */
	private String operatorId;
	/**
	 * 操作人姓名
	 */
	private String operatorName;
	/**
	 * 插入时间
	 */
	private String insertTime;
	/**
	 * 修改时间
	 */
	private String updateTime;
	/**
	 * 成员
	 */
	private String memberIds;
	private String memberNames;
	/**
	 * 附件
	 */
	private String attachmentUrl;
	private String attachmentName;
	/**
	 * 参与人数
	 * 
	 */
	private int count; 
	/**
	 * 用来判断是否已投票 >0
	 */
	private String voted;
	
	public String getVoted() {
		return voted;
	}

	public void setVoted(String voted) {
		this.voted = voted;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public Integer getThemeType() {
		return themeType;
	}

	public void setThemeType(Integer themeType) {
		this.themeType = themeType;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
