package com.kasite.core.serviceinterface.module.basic.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新医院信息请求入参
 * @author zhaoy
 *
 */
public class ReqUpdateHospitalLocal extends AbsReq{

	public ReqUpdateHospitalLocal(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.hosId = getDataJs().getString("HosId");
			this.hosName = getDataJs().getString("HosName");
			this.hosLevel = getDataJs().getString("HosLevel");
			this.hosLevelName = getDataJs().getString("HosLevelName");
			this.province = getDataJs().getString("Province");
			this.provinceCode = getDataJs().getString("ProvinceCode");
			this.city = getDataJs().getString("City");
			this.cityCode = getDataJs().getString("CityCode");
			this.postCode = getDataJs().getString("PostCode");
			this.address = getDataJs().getString("Address");
			this.tel = getDataJs().getString("Tel");
			this.photoUrl = getDataJs().getString("PhotoUrl");
			this.isShow = getDataJs().getString("IsShow");
			this.coordinateX = getDataJs().getString("CoordinateX");
			this.coordinateY = getDataJs().getString("CoordinateY");
			this.remark = getDataJs().getString("Remark");
			this.isStore = getDataJs().getString("IsStore");
			this.parentHosId = getDataJs().getString("ParentHosId");
			this.hosRoute = getDataJs().getString("HosRoute");
			this.floorDistriButionPic = getDataJs().getString("FloorDistriButionPic");
			this.hosBrief = getDataJs().getString("HosBrief");
			
			if("update".equals(type)) {
				if(StringUtil.isBlank(this.hosId)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"医院ID参数不能为空!");
				}
			}
		}
	}
	/**医院唯一ID*/
	private String hosId;
	
	/**省份名称*/
	private String province;
	
	/**城市名称*/
	private String city;
	
	/**医院等级*/
	private String hosLevel;
	
	/**城市代码*/
	private String cityCode;
	
	/**邮编*/
	private String postCode;
	
	/**省份代码*/
	private String provinceCode;
	
	/**地址*/
	private String address;
	
	/**电话*/
	private String tel;
	
	/**照片*/
	private String photoUrl;
	
	/**是否显示*/
	private String isShow;
	
	/**X坐标*/
	private String coordinateX;
	
	/**Y坐标*/
	private String coordinateY;
	
	/**备注*/
	private String remark;

	private String isStore;
	
	/**主医院ID*/
	private String parentHosId;
	
	/**交通*/
	private String hosRoute;
	

	private String floorDistriButionPic;
	
	/**医院名称*/
	private String hosName;
	
	/**医院简介*/
	private String hosBrief;
	
	/**医院等级名称*/
	private String hosLevelName;
	
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

	public String getIsStore() {
		return isStore;
	}

	public void setIsStore(String isStore) {
		this.isStore = isStore;
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
