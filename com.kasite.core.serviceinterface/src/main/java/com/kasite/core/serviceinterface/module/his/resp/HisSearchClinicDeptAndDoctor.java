package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: HisSearchClinicDeptAndDoctor
 * @author: lcz
 * @date: 2018年9月18日 下午4:28:49
 */
public class HisSearchClinicDeptAndDoctor extends AbsResp{
	private Integer searchType;
	private String deptCode;
	private String deptName;
	private String doctorCode;
	private String doctorName;
	private String doctorTitle;
	private String doctorTitleCode;
	private String spec;
	private Integer doctorIsHalt;
	private String url;
	
	
	/**
	 * @return the doctorTitleCode
	 */
	public String getDoctorTitleCode() {
		return doctorTitleCode;
	}
	/**
	 * @param doctorTitleCode the doctorTitleCode to set
	 */
	public void setDoctorTitleCode(String doctorTitleCode) {
		this.doctorTitleCode = doctorTitleCode;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the searchType
	 */
	public Integer getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the doctorCode
	 */
	public String getDoctorCode() {
		return doctorCode;
	}
	/**
	 * @param doctorCode the doctorCode to set
	 */
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	/**
	 * @return the doctorName
	 */
	public String getDoctorName() {
		return doctorName;
	}
	/**
	 * @param doctorName the doctorName to set
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	/**
	 * @return the doctorTitle
	 */
	public String getDoctorTitle() {
		return doctorTitle;
	}
	/**
	 * @param doctorTitle the doctorTitle to set
	 */
	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
	}
	/**
	 * @return the spec
	 */
	public String getSpec() {
		return spec;
	}
	/**
	 * @param spec the spec to set
	 */
	public void setSpec(String spec) {
		this.spec = spec;
	}
	/**
	 * @return the doctorIsHalt
	 */
	public Integer getDoctorIsHalt() {
		return doctorIsHalt;
	}
	/**
	 * @param doctorIsHalt the doctorIsHalt to set
	 */
	public void setDoctorIsHalt(Integer doctorIsHalt) {
		this.doctorIsHalt = doctorIsHalt;
	}
	
	
	
}
