package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 医院实体类
 * 
 * @author mhd
 * @version 1.0 2017-7-18 下午4:14:04
 */
@Table(name="B_HOSPITAL")
public class Hospital extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys = true)
	private String hosId;
	private String hospitalCode;
	private String province;
	private String city;
	private String hosLevel;
	private String cityCode;
	private String postCode;
	private String provinceCode;
	private String address;
	private String tel;
	private String photoUrl;
	private String isShow;
	private String coordinateX;
	private String coordinateY;
	private String remark;
	private String parentHosId;
	private String hosRoute;
	private String floorDistriButionPic;
	private String hosName;
	private String hosBrief;
	private String hosLevelName;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getParentHosId() {
		return parentHosId;
	}

	public void setParentHosId(String parentHosId) {
		this.parentHosId = parentHosId;
	}

	public String getHosRoute() {
		return hosRoute;
	}

	public void setHosRoute(String hosRoute) {
		this.hosRoute = hosRoute;
	}

	public String getFloorDistriButionPic() {
		return floorDistriButionPic;
	}

	public void setFloorDistriButionPic(String floorDistriButionPic) {
		this.floorDistriButionPic = floorDistriButionPic;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getHosBrief() {
		return hosBrief;
	}

	public void setHosBrief(String hosBrief) {
		this.hosBrief = hosBrief;
	}

	public String getHosLevelName() {
		return hosLevelName;
	}

	public void setHosLevelName(String hosLevelName) {
		this.hosLevelName = hosLevelName;
	}
}
