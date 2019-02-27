package com.kasite.core.serviceinterface.module.basic.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新医生信息的请求入参
 * @author zhaoy
 *
 */
public class ReqUpdateDoctorLocal extends AbsReq {

	
	
	
	public ReqUpdateDoctorLocal(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.hosId = super.getHosId();
			this.deptCode = getDataJs().getString("DeptCode");
			this.deptName = getDataJs().getString("DeptName");
			this.doctorCode = getDataJs().getString("DoctorCode");
			this.spec = getDataJs().getString("Spec");
			this.doctorName = getDataJs().getString("DoctorName");
			this.title = getDataJs().getString("Title");
			this.titleCode = getDataJs().getString("TitleCode");
			this.doctorSex = getDataJs().getString("DocotrSex");
			this.departmentId = getDataJs().getString("DepartmentId");
			this.introduction = getDataJs().getString("Introduction");
			this.doctorLevel = getDataJs().getString("DoctorLevel");
			this.levelName = getDataJs().getString("LevelName");
			this.orderCol = getDataJs().getInteger("OrderCol");
			this.teachTitle = getDataJs().getInteger("TeachTitle");
			this.photoUrl = getDataJs().getString("PhotoUrl");
			this.isShow = getDataJs().getInteger("IsShow");
			this.remark = getDataJs().getString("Remark");
			this.hosRoute = getDataJs().getString("HosRoute");
			this.relativeDeptName = getDataJs().getString("RelativeDeptName");
			this.relativeDeptId = getDataJs().getString("RelativeDeptId");
			this.tel = getDataJs().getString("Tel");
			this.doctorDegree = getDataJs().getString("DoctorDegree");
			
			if("update".equals(type)) {
				if(StringUtil.isBlank(this.doctorCode)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"医生CODE参数不能为空!");
				}
			}
		}
	}
/**
	 * @Title: ReqUpdateDoctorLocal
	 * @Description: 
	 * @param msg
	 * @param deptCode
	 * @param doctorCode
	 * @param spec
	 * @param doctorName
	 * @param title
	 * @param titleCode
	 * @param doctorSex
	 * @param departmentId
	 * @param introduction
	 * @param doctorLevel
	 * @param levelName
	 * @param orderCol
	 * @param photoUrl
	 * @param isShow
	 * @param remark
	 * @param hosId
	 * @param hosRoute
	 * @param relativeDeptName
	 * @param relativeDeptId
	 * @param deptName
	 * @param tel
	 * @throws AbsHosException
	 */
	public ReqUpdateDoctorLocal(InterfaceMessage msg, String deptCode, String doctorCode, String spec, String doctorName, String title, String titleCode, String doctorSex, String departmentId, String introduction, String doctorLevel, String levelName, Integer orderCol, String photoUrl, Integer isShow, String remark, String hosId, String hosRoute, String relativeDeptName, String relativeDeptId, String deptName, String tel) throws AbsHosException {
		super(msg);
		this.deptCode = deptCode;
		this.doctorCode = doctorCode;
		this.spec = spec;
		this.doctorName = doctorName;
		this.title = title;
		this.titleCode = titleCode;
		this.doctorSex = doctorSex;
		this.departmentId = departmentId;
		this.introduction = introduction;
		this.doctorLevel = doctorLevel;
		this.levelName = levelName;
		this.orderCol = orderCol;
		this.photoUrl = photoUrl;
		this.isShow = isShow;
		this.remark = remark;
		this.hosId = hosId;
		this.hosRoute = hosRoute;
		this.relativeDeptName = relativeDeptName;
		this.relativeDeptId = relativeDeptId;
		this.deptName = deptName;
		this.tel = tel;
	}

	/**科室代码*/
	public String deptCode; 
	
	/**医生编码*/
	public String doctorCode;
	
	/**专长*/
	public String spec;
	
	public String doctorName;
	
	/**医生职称*/
	public String title;
	
	/**职称编码*/
	public String titleCode;

	public String doctorSex;
	
	/**归属科室编码*/
	public String departmentId;
	
	/**医生简介*/
	public String introduction;
	
	/**医师级别*/
	public String doctorLevel;
	
	/**级别名称*/
	public String levelName;

	public Integer orderCol;
	
	public String photoUrl;
	
	/**是否显示*/
	public Integer isShow;
	
	/**备注*/
	public String remark; 
	
	/**医院ID*/
	public String hosId;
	
	/**来院路线*/
	public String hosRoute;
	
	/**归属科室*/
	public String relativeDeptName;
	
	/**归属科室ID*/
	public String relativeDeptId;
	
	/**科室名称*/
	public String deptName;

	public String tel;

	/**
	 * 教学职称 ：0 专家、1 教授、2 副教授、3 讲师、4 未知
	 */
	private Integer teachTitle;
	
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDoctorLevel() {
		return doctorLevel;
	}

	public void setDoctorLevel(String doctorLevel) {
		this.doctorLevel = doctorLevel;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getOrderCol() {
		return orderCol;
	}

	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
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

	public String getHosRoute() {
		return hosRoute;
	}

	public void setHosRoute(String hosRoute) {
		this.hosRoute = hosRoute;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
