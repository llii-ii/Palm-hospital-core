package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjianfa
 * @Description: TODO 基础科室实体类
 * @version: V1.0 2017年10月30日 下午4:26:07
 */
@Table(name="B_DEPT")
public class Dept extends BaseDbo{

	/**
	 * 医院ID
	 */
	private String hosId;

	/**
	 * 科室名称
	 */
	private String deptName;

	/**
	 * 科室代码
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private String deptCode;

	/**
	 * 父科室代码
	 */
	private String parentDeptCode;

	/**
	 * 科室地址
	 */
	private String deptAddr;

	/**
	 * 归属楼
	 */
	private String attachBuilding;

	/**
	 * 楼层
	 */
	private String floorNum;

	/**
	 * 图片地址
	 */
	private String photoUrl;

	/**
	 * 是否显示
	 */
	private Integer isShow;

	/**
	 * X坐标
	 */
	private String coordinateX;

	/**
	 * Y坐标
	 */
	private String coordinateY;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 科室唯一ID
	 */
	private String deptId;

	/**
	 * 排序
	 */
	private Integer orderCol;

	/**
	 * 科室类型1行政科室，2门诊科室
	 */
	private Integer deptType;

	/**
	 * 科室简介
	 */
	private String deptBrief;

	/**
	 * BOSS中的科室ID
	 */
	private String deptUid;

	/**
	 * 科室电话
	 */
	private String deptTel;

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getParentDeptCode() {
		return parentDeptCode;
	}

	public void setParentDeptCode(String parentDeptCode) {
		this.parentDeptCode = parentDeptCode;
	}

	public String getDeptAddr() {
		return deptAddr;
	}

	public void setDeptAddr(String deptAddr) {
		this.deptAddr = deptAddr;
	}

	public String getAttachBuilding() {
		return attachBuilding;
	}

	public void setAttachBuilding(String attachBuilding) {
		this.attachBuilding = attachBuilding;
	}

	public String getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Integer getOrderCol() {
		return orderCol;
	}

	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}

	public Integer getDeptType() {
		return deptType;
	}

	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}

	public String getDeptBrief() {
		return deptBrief;
	}

	public void setDeptBrief(String deptBrief) {
		this.deptBrief = deptBrief;
	}

	public String getDeptUid() {
		return deptUid;
	}

	public void setDeptUid(String deptUid) {
		this.deptUid = deptUid;
	}

	public String getDeptTel() {
		return deptTel;
	}

	public void setDeptTel(String deptTel) {
		this.deptTel = deptTel;
	}

	@Override
	public String toString() {
		return "Dept [hosId=" + hosId + ", deptName=" + deptName + ", deptCode=" + deptCode + ", parentDeptCode="
				+ parentDeptCode + ", deptAddr=" + deptAddr + ", attachBuilding=" + attachBuilding + ", floorNum="
				+ floorNum + ", photoUrl=" + photoUrl + ", isShow=" + isShow + ", coordinateX=" + coordinateX
				+ ", coordinateY=" + coordinateY + ", remark=" + remark + ", deptId=" + deptId + ", orderCol="
				+ orderCol + ", deptType=" + deptType + ", deptBrief=" + deptBrief + ", deptUid=" + deptUid
				+ ", deptTel=" + deptTel + "]";
	}

}
