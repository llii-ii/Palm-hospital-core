package com.kasite.core.serviceinterface.module.yy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryClinicDoctor
 * @author: lcz
 * @date: 2018年7月24日 下午2:45:29
 */
public class RespQueryClinicDoctor extends AbsResp{

	private String deptCode;
	private String deptName;
	private String doctorCode;
	private String doctorName;
	private String doctorTitle;
	private String doctorTitleCode;
	private Integer doctorIsHalt;
	private String url;
	private String intro;
	private String spec;
	private String remark;
	private String levelId;
	private String level;
	private String sex;
	private String price;
	private String doctorDegree;
	
	
	public String getDoctorDegree() {
		return doctorDegree;
	}
	public void setDoctorDegree(String doctorDegree) {
		this.doctorDegree = doctorDegree;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorTitle() {
		return doctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
	}
	public String getDoctorTitleCode() {
		return doctorTitleCode;
	}
	public void setDoctorTitleCode(String doctorTitleCode) {
		this.doctorTitleCode = doctorTitleCode;
	}
	public Integer getDoctorIsHalt() {
		return doctorIsHalt;
	}
	public void setDoctorIsHalt(Integer doctorIsHalt) {
		this.doctorIsHalt = doctorIsHalt;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	
	
}
