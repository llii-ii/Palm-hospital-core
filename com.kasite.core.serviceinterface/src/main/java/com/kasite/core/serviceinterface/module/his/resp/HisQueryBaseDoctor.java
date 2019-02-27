package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: HisQueryBaseDoctor
 * @author: lcz
 * @date: 2018年5月31日 下午2:52:14
 */
public class HisQueryBaseDoctor  extends AbsResp{
	@NotBlank(message="科室代码 deptCode 不能为空", groups = {AddGroup.class})
	private String deptCode;
	@NotBlank(message="科室名称 deptName 不能为空", groups = {AddGroup.class})
	private String deptName;
	@NotBlank(message="医生代码 doctorCode 不能为空", groups = {AddGroup.class})
	private String doctorCode;
	@NotBlank(message="医生名称 doctorName 不能为空", groups = {AddGroup.class})
	private String doctorName;
	private String spec;
	private String title;
	private String titleCode;
	private String doctorLevel;
	private String doctorSex;
	private String levelName;
	private String levelId;
	private String photoUrl;
	private Integer isShow;
	private String remark;

	private String hosId;
	private String relativeDeptid;
	private String relativeDetname;
	private Integer orderCol;
	private String tel;
	private String introduction;
	private String doctoruId;
	private String qrUrl;
	private String qrPicurl;
	private String bossUrl;
	private String qrTime;
	private Integer qrState;
	private Integer doctorType;
	private Integer price;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
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

	public String getDoctorLevel() {
		return doctorLevel;
	}

	public void setDoctorLevel(String doctorLevel) {
		this.doctorLevel = doctorLevel;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getRelativeDeptid() {
		return relativeDeptid;
	}

	public void setRelativeDeptid(String relativeDeptid) {
		this.relativeDeptid = relativeDeptid;
	}

	public String getRelativeDetname() {
		return relativeDetname;
	}

	public void setRelativeDetname(String relativeDetname) {
		this.relativeDetname = relativeDetname;
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

	public String getQrPicurl() {
		return qrPicurl;
	}

	public void setQrPicurl(String qrPicurl) {
		this.qrPicurl = qrPicurl;
	}

	public String getBossUrl() {
		return bossUrl;
	}

	public void setBossUrl(String bossUrl) {
		this.bossUrl = bossUrl;
	}

	public String getQrTime() {
		return qrTime;
	}

	public void setQrTime(String qrTime) {
		this.qrTime = qrTime;
	}

	public Integer getQrState() {
		return qrState;
	}

	public void setQrState(Integer qrState) {
		this.qrState = qrState;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}
}
