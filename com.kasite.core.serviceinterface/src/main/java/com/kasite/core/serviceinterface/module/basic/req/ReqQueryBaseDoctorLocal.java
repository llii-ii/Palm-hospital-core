package com.kasite.core.serviceinterface.module.basic.req;


import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询基础医生入参
 * 
 * @author lsq version 1.0 2017-7-31下午1:35:27
 */
public class ReqQueryBaseDoctorLocal extends AbsReq {
	/**
	 * 医院id
	 */
	private String hosId;
	/**
	 * 科室代码
	 */
	private String deptCode;
	/**
	 * 医生编码
	 */
	private String doctorCode;
	/**
	 * 医生编码，多个逗号分隔
	 */
	private String doctorCodes;
	/**
	 * 医生姓名
	 */
	private String doctorName;
	/**
	 * 医生姓名：支持模糊检索
	 */
	private String doctorNameLike;
	
	/**
	 * 医生类型
	 */
	private Integer doctorType;
	
	/**
	 * 医生职称编码
	 */
	private String doctorTitleCode;
	/**
	 * 医生职称
	 */
	private String doctorTitle;
	/**
	 * 教学职称 ：0 专家、1 教授、2 副教授、3 讲师、4 未知
	 */
	private Integer teachTitle;

	public ReqQueryBaseDoctorLocal(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()!=0) {
			Element ser = root.element(KstHosConstant.DATA);
			this.hosId = XMLUtil.getString(ser, "HosId", true);
			this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
			this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
			this.doctorName = XMLUtil.getString(ser, "DoctorName", false);
			this.doctorType = XMLUtil.getInt(ser, "DoctorType", false);
			this.doctorNameLike = XMLUtil.getString(ser, "DoctorNameLike", false);
			this.doctorCodes = XMLUtil.getString(ser, "DoctorCodes", false);
			this.doctorTitleCode = XMLUtil.getString(ser, "DoctorTitleCode", false);
			this.doctorTitle = XMLUtil.getString(ser, "DoctorTitle", false);
			this.teachTitle = XMLUtil.getInt(ser, "TeachTitle", false, null);
		}else {
			this.hosId = getDataJs().getString("HosId");
			this.deptCode = getDataJs().getString("DeptCode");
			this.doctorCode = getDataJs().getString("DoctorCode");
			this.doctorName = getDataJs().getString("DoctorName");
			this.doctorType = getDataJs().getInteger("DoctorType");
			this.doctorNameLike = getDataJs().getString("DoctorNameLike");
			this.doctorCodes = getDataJs().getString("DoctorCodes");
			this.doctorTitleCode = getDataJs().getString("DoctorTitleCode");
			this.doctorTitle = getDataJs().getString("DoctorTitle");
			this.teachTitle = getDataJs().getInteger("TeachTitle");
		}
		
	}
	

	/**
	 * @Title: ReqQueryBaseDoctorLocal
	 * @Description: 
	 * @param msg
	 * @param hosId
	 * @param deptCode
	 * @param doctorCode
	 * @param doctorName
	 * @param doctorType
	 * @throws AbsHosException
	 */
	public ReqQueryBaseDoctorLocal(InterfaceMessage msg, String hosId, String deptCode, String doctorCode, String doctorName, Integer doctorType,String doctorNameLike,String doctorCodes,String doctorTitleCode) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		this.deptCode = deptCode;
		this.doctorCode = doctorCode;
		this.doctorName = doctorName;
		this.doctorType = doctorType;
		this.doctorNameLike = doctorNameLike;
		this.doctorCodes = doctorCodes;
		this.doctorTitleCode = doctorTitleCode;
	}
	
	/**
	 * @param msg
	 * @param hosId
	 * @param deptCode
	 * @param doctorCode
	 * @param doctorCodes
	 * @param doctorName
	 * @param doctorNameLike
	 * @param doctorType
	 * @param doctorTitleCode
	 * @param doctorTitle
	 * @param teachTitle
	 * @throws AbsHosException
	 */
	public ReqQueryBaseDoctorLocal(InterfaceMessage msg, String hosId, String deptCode, String doctorCode,
			String doctorCodes, String doctorName, String doctorNameLike, Integer doctorType, String doctorTitleCode,
			String doctorTitle, Integer teachTitle) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		this.deptCode = deptCode;
		this.doctorCode = doctorCode;
		this.doctorCodes = doctorCodes;
		this.doctorName = doctorName;
		this.doctorNameLike = doctorNameLike;
		this.doctorType = doctorType;
		this.doctorTitleCode = doctorTitleCode;
		this.doctorTitle = doctorTitle;
		this.teachTitle = teachTitle;
	}


	/**
	 * @return the doctorTitleCode
	 */
	public String getDoctorTitleCode() {
		return doctorTitleCode;
	}


	public String getDoctorTitle() {
		return doctorTitle;
	}


	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
	}


	/**
	 * @param doctorTitleCode the doctorTitleCode to set
	 */
	public void setDoctorTitleCode(String doctorTitleCode) {
		this.doctorTitleCode = doctorTitleCode;
	}


	/**
	 * @return the doctorCodes
	 */
	public String getDoctorCodes() {
		return doctorCodes;
	}


	public Integer getTeachTitle() {
		return teachTitle;
	}


	public void setTeachTitle(Integer teachTitle) {
		this.teachTitle = teachTitle;
	}


	/**
	 * @param doctorCodes the doctorCodes to set
	 */
	public void setDoctorCodes(String doctorCodes) {
		this.doctorCodes = doctorCodes;
	}


	/**
	 * @return the doctorNameLike
	 */
	public String getDoctorNameLike() {
		return doctorNameLike;
	}


	/**
	 * @param doctorNameLike the doctorNameLike to set
	 */
	public void setDoctorNameLike(String doctorNameLike) {
		this.doctorNameLike = doctorNameLike;
	}
	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

}
