package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 基础医生实体类
 * 
 * @author lq version 1.0 2017-7-18下午18:55:24
 */
@Table(name="B_DOCTOR")
public class Doctor  extends BaseDbo{
	@Id
	private String deptCode;
	private String deptName;
	@Id
	@KeySql(useGeneratedKeys = true)
	private String doctorCode;
	private String doctorName;
	private String spec;
	private String title;
	private String titleCode;
	private String doctorSex;
	private String levelName;
	private String levelId;
	private String photoUrl;
	private Integer isShow;
	private String remark;
	private String hosId;
	private Integer orderCol;
	private String tel;
	private String introduction;
	private String doctoruId;
	private Integer doctorType;
	private Integer price;
	/**
	 * 教学职称 ：0 专家、1 教授、2 副教授、3 讲师、4 未知
	 */
	private Integer teachTitle;
	/**
	 * 医生学位
	 */
	private String doctorDegree;
	
	
	/**
	 * @return the doctorDegree
	 */
	public String getDoctorDegree() {
		return doctorDegree;
	}
	/**
	 * @param doctorDegree the doctorDegree to set
	 */
	public void setDoctorDegree(String doctorDegree) {
		this.doctorDegree = doctorDegree;
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
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleCode() {
		return titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	public String getDoctorSex() {
		return doctorSex;
	}
	public void setDoctorSex(String doctorSex) {
		this.doctorSex = doctorSex;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public Integer getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getDoctoruId() {
		return doctoruId;
	}
	public void setDoctoruId(String doctoruId) {
		this.doctoruId = doctoruId;
	}
	public Integer getDoctorType() {
		return doctorType;
	}
	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Doctor [deptCode=" + deptCode + ", deptName=" + deptName + ", doctorCode=" + doctorCode
				+ ", doctorName=" + doctorName + ", spec=" + spec + ", title=" + title + ", titleCode=" + titleCode
				+ ", doctorSex=" + doctorSex + ", levelName=" + levelName + ", levelId=" + levelId + ", photoUrl="
				+ photoUrl + ", isShow=" + isShow + ", remark=" + remark + ", hosId=" + hosId + ", orderCol=" + orderCol
				+ ", tel=" + tel + ", introduction=" + introduction + ", doctoruId=" + doctoruId + ", doctorType="
				+ doctorType + ", price=" + price + "]";
	}
}
