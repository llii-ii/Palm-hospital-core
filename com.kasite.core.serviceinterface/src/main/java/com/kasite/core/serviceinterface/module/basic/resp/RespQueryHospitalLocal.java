package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 医院返回对象
 * @author mhd
 * @version 1.0 2017-7-17 下午3:43:01
 */
public class RespQueryHospitalLocal extends AbsResp{
	/**
	 * 医院id
	 */
	private String hosId;
	/**
	 * HIS端医院编码
	 */
	private String hospitalCode;
	/**
	 * 医院简介
	 */
	private String hosBrief;
	/**
	 * 医院地址
	 */
	private String address;
	/**
	 * 医院等级
	 */
	private String hosLevel;
	
	/**
	 * 医院等级
	 */
	private String hosLevelName;
	
	/**
	 * 医院编码
	 */
	private String postCode;
	/**
	 * 医院坐标x轴
	 */
	private String coordinateX;
	/**
	 * 医院坐标y轴
	 */
	private String coordinateY;
	/**
	 * 省份行政编码
	 */
	private String provinceCode;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 地区行政编码
	 */
	private String cityCode;
	/**
	 * 地区
	 */
	private String city;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * 医院照片
	 */
	private String photoUrl;
	/**
	 * 交通
	 */
	private String hosRoute;
	/**
	 * 医院名称
	 */
	private String hosName;

	public RespQueryHospitalLocal() {
		super();
	};

	/**
	 * @return the hospitalCode
	 */
	public String getHospitalCode() {
		return hospitalCode;
	}

	/**
	 * @param hospitalCode the hospitalCode to set
	 */
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getHosBrief() {
		return hosBrief;
	}

	public void setHosBrief(String hosBrief) {
		this.hosBrief = hosBrief;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public String getHosLevelName() {
		return hosLevelName;
	}

	public void setHosLevelName(String hosLevelName) {
		this.hosLevelName = hosLevelName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(String coordinateX) {
		this.coordinateX = coordinateX;
	}

	public String getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(String coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getHosRoute() {
		return hosRoute;
	}

	public void setHosRoute(String hosRoute) {
		this.hosRoute = hosRoute;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

}
