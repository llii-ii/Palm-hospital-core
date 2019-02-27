package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryBaseDoctorLocal
 * @author: lcz
 * @date: 2018年7月23日 上午11:26:24
 */
public class RespQueryBaseDoctorLocal extends AbsResp{
	
	private String deptCode;
	private String deptName;
	private String doctorCode;
	private String doctorName;
	private String doctorTitle;
	private String doctorTitleCode;
	private String photoUrl;
	private String intro;
	private String spec;
	private String remark;
	private String levelId;
	private String level;
	private String sex;
	private Integer price;
	private String doctorTel;
	private Integer orderCol;
	/**归属科室*/
	public String relativeDeptName;
	/**归属科室ID*/
	public String relativeDeptId;
	
	/**
	 * 教学职称 ：0 专家、1 教授、2 副教授、3 讲师、4 未知
	 */
	private Integer teachTitle;
	/**
	 * 是否显示
	 */
	private Integer isShow;
	
	/**
	 * 医生学位
	 */
	private String doctorDegree;
	
	
	public String getDoctorDegree() {
		return doctorDegree;
	}
	public void setDoctorDegree(String doctorDegree) {
		this.doctorDegree = doctorDegree;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public Integer getTeachTitle() {
		return teachTitle;
	}
	public void setTeachTitle(Integer teachTitle) {
		this.teachTitle = teachTitle;
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
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getDoctorTel() {
		return doctorTel;
	}
	public void setDoctorTel(String doctorTel) {
		this.doctorTel = doctorTel;
	}
	public Integer getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}
	public String getRelativeDeptName() {
		return relativeDeptName;
	}
	public void setRelativeDeptName(String relativeDeptName) {
		this.relativeDeptName = relativeDeptName;
	}
	public String getRelativeDeptId() {
		return relativeDeptId;
	}
	public void setRelativeDeptId(String relativeDeptId) {
		this.relativeDeptId = relativeDeptId;
	}
	
}
