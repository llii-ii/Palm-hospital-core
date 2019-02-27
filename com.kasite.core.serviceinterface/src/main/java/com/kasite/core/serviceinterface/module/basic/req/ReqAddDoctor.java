package com.kasite.core.serviceinterface.module.basic.req;

import java.util.List;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqAddDoctor
 * @author: lcz
 * @date: 2018年9月27日 下午1:45:50
 */
public class ReqAddDoctor extends AbsReq{

	/**
	 * 批量新增
	 */
	private List<Doctor> doctorList;
	
	private String deptCode;
	private String deptName;
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
	 * @return the doctorList
	 */
	public List<Doctor> getDoctorList() {
		return doctorList;
	}


	/**
	 * @param doctorList the doctorList to set
	 */
	public void setDoctorList(List<Doctor> doctorList) {
		this.doctorList = doctorList;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the titleCode
	 */
	public String getTitleCode() {
		return titleCode;
	}


	/**
	 * @param titleCode the titleCode to set
	 */
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}


	/**
	 * @return the doctorSex
	 */
	public String getDoctorSex() {
		return doctorSex;
	}


	/**
	 * @param doctorSex the doctorSex to set
	 */
	public void setDoctorSex(String doctorSex) {
		this.doctorSex = doctorSex;
	}


	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}


	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}


	/**
	 * @return the levelId
	 */
	public String getLevelId() {
		return levelId;
	}


	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}


	/**
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}


	/**
	 * @param photoUrl the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}


	/**
	 * @return the isShow
	 */
	public Integer getIsShow() {
		return isShow;
	}


	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}


	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}


	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}


	/**
	 * @return the hosId
	 */
	public String getHosId() {
		return hosId;
	}


	/**
	 * @param hosId the hosId to set
	 */
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}


	/**
	 * @return the orderCol
	 */
	public Integer getOrderCol() {
		return orderCol;
	}


	/**
	 * @param orderCol the orderCol to set
	 */
	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}


	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}


	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}


	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}


	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}


	/**
	 * @return the doctoruId
	 */
	public String getDoctoruId() {
		return doctoruId;
	}


	/**
	 * @param doctoruId the doctoruId to set
	 */
	public void setDoctoruId(String doctoruId) {
		this.doctoruId = doctoruId;
	}


	/**
	 * @return the doctorType
	 */
	public Integer getDoctorType() {
		return doctorType;
	}


	/**
	 * @param doctorType the doctorType to set
	 */
	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}


	/**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}


	/**
	 * @Title: ReqAddDoctor
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddDoctor(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.deptCode = getDataJs().getString("DeptCode");
			this.deptName = getDataJs().getString("DeptName");
			this.doctorCode = getDataJs().getString("DoctorCode");
			this.doctorName = getDataJs().getString("DoctorName");
			this.spec = getDataJs().getString("Spec");
			this.title = getDataJs().getString("DoctorTitle");
			this.titleCode = getDataJs().getString("DoctorTitleCode");
			this.doctorSex = getDataJs().getString("DoctorSex");
			this.levelName = getDataJs().getString("LevelName");
			this.levelId = getDataJs().getString("LevelId");
			this.photoUrl = getDataJs().getString("PhotoUrl");
			this.isShow = getDataJs().getInteger("IsShow");
			this.remark = getDataJs().getString("Remark");
			this.hosId = getDataJs().getString("HosId");
			this.orderCol = getDataJs().getInteger("OrderCol");
			this.tel = getDataJs().getString("Tel");
			this.introduction = getDataJs().getString("Intro");
			this.doctoruId = getDataJs().getString("DoctorUid");
			this.doctorType = getDataJs().getInteger("DoctorType");
			this.price = getDataJs().getInteger("Price");
		}else {
			this.deptCode = XMLUtil.getString(getData(), "DeptCode", true);
			this.deptName = XMLUtil.getString(getData(), "DeptName", true);
			this.doctorCode = XMLUtil.getString(getData(), "DoctorCode", true);
			this.doctorName = XMLUtil.getString(getData(), "DoctorName", true);
			this.spec = XMLUtil.getString(getData(), "Spec", false);
			this.title = XMLUtil.getString(getData(), "DoctorTitle", false);
			this.titleCode = XMLUtil.getString(getData(), "DoctorTitleCode", false);
			this.doctorSex = XMLUtil.getString(getData(), "DoctorSex", false);
			this.levelName = XMLUtil.getString(getData(), "LevelName", false);
			this.levelId = XMLUtil.getString(getData(), "LevelId", false);
			this.photoUrl = XMLUtil.getString(getData(), "PhotoUrl", false);
			this.isShow = XMLUtil.getInt(getData(), "IsShow", false);
			this.remark = XMLUtil.getString(getData(), "Remark", false);
			this.hosId = XMLUtil.getString(getData(), "HosId", false);
			this.orderCol = XMLUtil.getInt(getData(), "OrderCol", false);
			this.tel = XMLUtil.getString(getData(), "Tel", false);
			this.introduction = XMLUtil.getString(getData(), "Intro", false);
			this.doctoruId = XMLUtil.getString(getData(), "DoctorUid", false);
			this.doctorType = XMLUtil.getInt(getData(), "DoctorType", false);
			this.price = XMLUtil.getInt(getData(), "Price", false);
		}
	}
	
	

	/**
	 * @Title: ReqAddDoctor
	 * @Description: 
	 * @param msg
	 * @param doctorList
	 * @param deptCode
	 * @param deptName
	 * @param doctorCode
	 * @param doctorName
	 * @param spec
	 * @param title
	 * @param titleCode
	 * @param doctorSex
	 * @param levelName
	 * @param levelId
	 * @param photoUrl
	 * @param isShow
	 * @param remark
	 * @param hosId
	 * @param orderCol
	 * @param tel
	 * @param introduction
	 * @param doctoruId
	 * @param doctorType
	 * @param price
	 * @throws AbsHosException
	 */
	public ReqAddDoctor(InterfaceMessage msg, List<Doctor> doctorList, String deptCode, String deptName, String doctorCode, String doctorName, String spec, String title, String titleCode, String doctorSex, String levelName, String levelId, String photoUrl, Integer isShow, String remark, String hosId, Integer orderCol, String tel, String introduction, String doctoruId, Integer doctorType, Integer price) throws AbsHosException {
		super(msg);
		this.doctorList = doctorList;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.doctorCode = doctorCode;
		this.doctorName = doctorName;
		this.spec = spec;
		this.title = title;
		this.titleCode = titleCode;
		this.doctorSex = doctorSex;
		this.levelName = levelName;
		this.levelId = levelId;
		this.photoUrl = photoUrl;
		this.isShow = isShow;
		this.remark = remark;
		this.hosId = hosId;
		this.orderCol = orderCol;
		this.tel = tel;
		this.introduction = introduction;
		this.doctoruId = doctoruId;
		this.doctorType = doctorType;
		this.price = price;
	}
}
