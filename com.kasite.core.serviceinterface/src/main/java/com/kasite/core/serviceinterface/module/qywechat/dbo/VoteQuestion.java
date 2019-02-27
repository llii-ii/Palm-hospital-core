package com.kasite.core.serviceinterface.module.qywechat.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 投票、问卷实体类
 * 
 * @author 無
 *
 */
@Table(name = "QY_VOTE_QUESTION")
public class VoteQuestion {

	/**
	 * 主键ID 自增
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	/**
	 * 32位UUID
	 */
	@Column(name = "UID")
	private String uid;
	/**
	 * 主题
	 */
	@Column(name = "TITLE")
	private String title;
	/**
	 * 首语
	 */
	@Column(name = "BEGININTRO")
	private String beginIntro;
	/**
	 * 尾语
	 */
	@Column(name = "ENDINGINTRO")
	private String endingIntro;
	/**
	 * 开始时间
	 */
	@Column(name = "STARTTIME")
	private String startTime;
	/**
	 * 结束时间
	 */
	@Column(name = "ENDTIME")
	private String endTime;
	/**
	 * 类型 0=投票 1=问卷
	 */
	@Column(name = "THEMETYPE")
	private Integer themeType;
	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	private String remark;
	/**
	 * 状态 0=待发布 1=发布 2=结束 3=删除
	 */
	@Column(name = "STATUS")
	private Integer status;
	/**
	 * 操作人ID
	 */
	@Column(name = "OPERATORID")
	private String operatorId;
	/**
	 * 操作人姓名
	 */
	@Column(name = "OPERATORNAME")
	private String operatorName;
	/**
	 * 插入时间
	 */
	@Column(name = "INSERTTIME")
	private String insertTime;
	/**
	 * 修改时间
	 */
	@Column(name = "UPDATETIME")
	private String updateTime;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
