package com.kasite.core.serviceinterface.module.qywechat.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 危急值实体类
 * 
 * @author 無
 *
 */
@Table(name = "QY_WARN_VALUE")
public class WarnValue {
	/**
	 * 主键ID 自增
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	/**
	 * lis系统报告单号
	 */
	@Column(name = "LISORDERID")
	private String lisOrderid;
	/**
	 * 病区
	 */
	@Column(name = "WARD")
	private String ward;
	/**
	 * 病床
	 */
	@Column(name = "BEDNO")
	private String bedNo;
	/**
	 * 住院号
	 */
	@Column(name = "INHOSPITALNO")
	private int inHospitalNo;
	/**
	 * 患者姓名
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 检查项目
	 */
	@Column(name = "CHECKITEAM")
	private String checkIteam;
	/**
	 * 结果
	 */
	@Column(name = "RESULT")
	private String result;
	/**
	 * 参考值
	 */
	@Column(name = "REFERENCE")
	private String reference;
	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	private String remark;
	/**
	 * 主治医生工号 多个逗号分隔
	 */
	@Column(name = "DOCTORCODE")
	private String doctorCode;
	/**
	 * 状态 0=未读 1=已读
	 */
	@Column(name = "STATUS")
	private String status;
	/**
	 * 插入时间
	 */
	@Column(name = "INSERTTIME")
	private String insertTime;
	/**
	 * 修改时间
	 */
	@Column(name = "UPDTETIME")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLisOrderid() {
		return lisOrderid;
	}

	public void setLisOrderid(String lisOrderid) {
		this.lisOrderid = lisOrderid;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public int getInHospitalNo() {
		return inHospitalNo;
	}

	public void setInHospitalNo(int inHospitalNo) {
		this.inHospitalNo = inHospitalNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheckIteam() {
		return checkIteam;
	}

	public void setCheckIteam(String checkIteam) {
		this.checkIteam = checkIteam;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
