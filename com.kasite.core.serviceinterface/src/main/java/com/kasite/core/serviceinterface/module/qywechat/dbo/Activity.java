package com.kasite.core.serviceinterface.module.qywechat.dbo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 活动邀请 This class was generated by MyBatis Generator. This class corresponds to
 * the database table QY_ACTIVITY
 */
@Table(name = "QY_ACTIVITY")
public class Activity implements Serializable {
	/**
	 * 主键
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;

	/**
	 * uuid
	 */
	@Column(name = "ACTIVITYID")
	private String activityid;
	/**
	 * 主题
	 */
	@Column(name = "TITLE")
	private String title;

	/**
	 * 报名截止时间
	 */
	@Column(name = "EXPIRETIME")
	private String expiretime;

	/**
	 * 活动开始时间
	 */
	@Column(name = "STARTTIME")
	private String starttime;

	/**
	 * 活动结束时间
	 */
	@Column(name = "ENDTIME")
	private String endtime;

	/**
	 * 报名人数 默认0=不限制人数 N=N个人
	 */
	@Column(name = "TOTALNUMBER")
	private Integer totalnumber;

	/**
	 * 活动地点
	 */
	@Column(name = "PLACE")
	private String place;

	/**
	 * 状态 0=未开始 1=报名中 2=已结束
	 */
	@Column(name = "STATUS")
	private Integer status;

	/**
	 * 发起人ID
	 */
	@Column(name = "OPERATORID")
	private String operatorid;

	/**
	 * 发起人姓名
	 */
	@Column(name = "OPERATORNAME")
	private String operatorname;

	/**
	 * 插入时间
	 */
	@Column(name = "INSERTTIME")
	private String inserttime;

	/**
	 * 修改时间
	 */
	@Column(name = "UPDATETIME")
	private String updatetime;

	/**
	 * 简介
	 */
	@Column(name = "INTRO")
	private String intro;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table QY_ACTIVITY
	 *
	 * @mbg.generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.ID
	 *
	 * @return the value of QY_ACTIVITY.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.ID
	 *
	 * @param id the value for QY_ACTIVITY.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.TITLE
	 *
	 * @return the value of QY_ACTIVITY.TITLE
	 *
	 * @mbg.generated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.TITLE
	 *
	 * @param title the value for QY_ACTIVITY.TITLE
	 *
	 * @mbg.generated
	 */
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.EXPIRETIME
	 *
	 * @return the value of QY_ACTIVITY.EXPIRETIME
	 *
	 * @mbg.generated
	 */
	public String getExpiretime() {
		return expiretime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.EXPIRETIME
	 *
	 * @param expiretime the value for QY_ACTIVITY.EXPIRETIME
	 *
	 * @mbg.generated
	 */
	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.STARTTIME
	 *
	 * @return the value of QY_ACTIVITY.STARTTIME
	 *
	 * @mbg.generated
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.STARTTIME
	 *
	 * @param starttime the value for QY_ACTIVITY.STARTTIME
	 *
	 * @mbg.generated
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.ENDTIME
	 *
	 * @return the value of QY_ACTIVITY.ENDTIME
	 *
	 * @mbg.generated
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.ENDTIME
	 *
	 * @param endtime the value for QY_ACTIVITY.ENDTIME
	 *
	 * @mbg.generated
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.TOTALNUMBER
	 *
	 * @return the value of QY_ACTIVITY.TOTALNUMBER
	 *
	 * @mbg.generated
	 */
	public Integer getTotalnumber() {
		return totalnumber;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.TOTALNUMBER
	 *
	 * @param totalnumber the value for QY_ACTIVITY.TOTALNUMBER
	 *
	 * @mbg.generated
	 */
	public void setTotalnumber(Integer totalnumber) {
		this.totalnumber = totalnumber;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.PLACE
	 *
	 * @return the value of QY_ACTIVITY.PLACE
	 *
	 * @mbg.generated
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.PLACE
	 *
	 * @param place the value for QY_ACTIVITY.PLACE
	 *
	 * @mbg.generated
	 */
	public void setPlace(String place) {
		this.place = place == null ? null : place.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.STATUS
	 *
	 * @return the value of QY_ACTIVITY.STATUS
	 *
	 * @mbg.generated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.STATUS
	 *
	 * @param status the value for QY_ACTIVITY.STATUS
	 *
	 * @mbg.generated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.OPERATORID
	 *
	 * @return the value of QY_ACTIVITY.OPERATORID
	 *
	 * @mbg.generated
	 */
	public String getOperatorid() {
		return operatorid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.OPERATORID
	 *
	 * @param operatorid the value for QY_ACTIVITY.OPERATORID
	 *
	 * @mbg.generated
	 */
	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid == null ? null : operatorid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.OPERATORNAME
	 *
	 * @return the value of QY_ACTIVITY.OPERATORNAME
	 *
	 * @mbg.generated
	 */
	public String getOperatorname() {
		return operatorname;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.OPERATORNAME
	 *
	 * @param operatorname the value for QY_ACTIVITY.OPERATORNAME
	 *
	 * @mbg.generated
	 */
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname == null ? null : operatorname.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.INSERTTIME
	 *
	 * @return the value of QY_ACTIVITY.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.INSERTTIME
	 *
	 * @param inserttime the value for QY_ACTIVITY.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.UPDATETIME
	 *
	 * @return the value of QY_ACTIVITY.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.UPDATETIME
	 *
	 * @param updatetime the value for QY_ACTIVITY.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_ACTIVITY.INTRO
	 *
	 * @return the value of QY_ACTIVITY.INTRO
	 *
	 * @mbg.generated
	 */
	public String getIntro() {
		return intro;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_ACTIVITY.INTRO
	 *
	 * @param intro the value for QY_ACTIVITY.INTRO
	 *
	 * @mbg.generated
	 */
	public void setIntro(String intro) {
		this.intro = intro == null ? null : intro.trim();
	}

	public String getActivityid() {
		return activityid;
	}

	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table QY_ACTIVITY
	 *
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", expiretime=").append(expiretime);
		sb.append(", starttime=").append(starttime);
		sb.append(", endtime=").append(endtime);
		sb.append(", totalnumber=").append(totalnumber);
		sb.append(", place=").append(place);
		sb.append(", status=").append(status);
		sb.append(", operatorid=").append(operatorid);
		sb.append(", operatorname=").append(operatorname);
		sb.append(", inserttime=").append(inserttime);
		sb.append(", updatetime=").append(updatetime);
		sb.append(", intro=").append(intro);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}