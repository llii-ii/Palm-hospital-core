/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**查询历史医生出参
 * @author Administrator
 *
 */
public class HisQueryHistoryDoctor  extends AbsResp {
	/**医生编码*/
	private String doctorCode;
	/**医生姓名*/
	private String doctorName;
	/**医生职称*/
	private String doctorTitle;
	/**医生职称编码*/
	private String doctorTitleCode;
	/**医生出诊状态1:出诊、2:停诊、3:替诊、4:可约、5:可挂、6:申请、 7:已约满*/
	private Integer doctorIsHalt;
	/**医生照片*/
	private String url;
	/**医生简介*/
	private String intro;
	/**医生专长*/
	private String spec;
	/**备注*/
	private String remark;
	/**医师级别*/
	private String levelId;
	/**级别名称(主任,副主任)*/
	private String level;
	/**医生性别*/
	private String sex;
	/**出诊费用：单位（分）*/
	private String price;

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
