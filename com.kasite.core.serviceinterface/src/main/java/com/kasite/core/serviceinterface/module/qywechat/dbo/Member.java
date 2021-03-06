package com.kasite.core.serviceinterface.module.qywechat.dbo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 成员 This class was generated by MyBatis Generator. This class corresponds to
 * the database table QY_MEMBER_CREDITS
 */
@Table(name = "QY_MEMBER")
public class Member implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;

	/**
	 * 成员ID
	 */
	@Column(name = "MEMBERID")
	private String memberid;

	/**
	 * 成员名称
	 */
	@Column(name = "MEMBERNAME")
	private String membername;

	/**
	 * 部门ID
	 */
	@Column(name = "DEPTID")
	private String deptid;

	/**
	 * 部门名称
	 */
	@Column(name = "DEPTNAME")
	private String deptname;

	/**
	 * 头像
	 */
	@Column(name = "MEMBERAVATAR")
	private String memberavatar;

	/**
	 * 性别
	 */
	@Column(name = "SEX")
	private String sex;

	/**
	 * 职务
	 */
	@Column(name = "POSITION")
	private String position;

	/**
	 * 手机
	 */
	@Column(name = "MOBILE")
	private String mobile;

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
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table QY_MEMBER_CREDITS
	 *
	 * @mbg.generated
	 */
	private static final long serialVersionUID = 1L;

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS.ID
	 *
	 * @return the value of QY_MEMBER_CREDITS.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS.ID
	 *
	 * @param id the value for QY_MEMBER_CREDITS.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS.MEMBERID
	 *
	 * @return the value of QY_MEMBER_CREDITS.MEMBERID
	 *
	 * @mbg.generated
	 */
	public String getMemberid() {
		return memberid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS.MEMBERID
	 *
	 * @param memberid the value for QY_MEMBER_CREDITS.MEMBERID
	 *
	 * @mbg.generated
	 */
	public void setMemberid(String memberid) {
		this.memberid = memberid == null ? null : memberid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS.MEMBERNAME
	 *
	 * @return the value of QY_MEMBER_CREDITS.MEMBERNAME
	 *
	 * @mbg.generated
	 */
	public String getMembername() {
		return membername;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS.MEMBERNAME
	 *
	 * @param membername the value for QY_MEMBER_CREDITS.MEMBERNAME
	 *
	 * @mbg.generated
	 */
	public void setMembername(String membername) {
		this.membername = membername == null ? null : membername.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS.INSERTTIME
	 *
	 * @return the value of QY_MEMBER_CREDITS.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS.INSERTTIME
	 *
	 * @param inserttime the value for QY_MEMBER_CREDITS.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEMBER_CREDITS.UPDATETIME
	 *
	 * @return the value of QY_MEMBER_CREDITS.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getMemberavatar() {
		return memberavatar;
	}

	public void setMemberavatar(String memberavatar) {
		this.memberavatar = memberavatar;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEMBER_CREDITS.UPDATETIME
	 *
	 * @param updatetime the value for QY_MEMBER_CREDITS.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table QY_MEMBER_CREDITS
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
		sb.append(", memberid=").append(memberid);
		sb.append(", membername=").append(membername);
		sb.append(", inserttime=").append(inserttime);
		sb.append(", updatetime=").append(updatetime);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}