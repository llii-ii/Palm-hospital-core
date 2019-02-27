/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**查询基础医生入参
 * @author lsq
 * version 1.0
 * 2017-7-31下午1:35:27
 */
public class ReqQueryBaseDoctor extends AbsReq{
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
	 * 医生姓名：支持模糊检索
	 */
	private String doctorName;
	
	 /**
	 * 医生类型
	 */
	private Integer doctorType;
	 
	public ReqQueryBaseDoctor(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hosId = XMLUtil.getString(ser, "HosId", true);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
		this.doctorName = XMLUtil.getString(ser, "DoctorName", false);
		this.doctorType = XMLUtil.getInt(ser, "DoctorType", false);
	}
	
	public ReqQueryBaseDoctor(InterfaceMessage msg, String source)throws AbsHosException {
		super(msg);
		this.hosId = super.getHosId();
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
