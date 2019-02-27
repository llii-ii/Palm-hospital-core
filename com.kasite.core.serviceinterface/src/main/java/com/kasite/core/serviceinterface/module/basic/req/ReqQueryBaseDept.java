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

/**
 * 查询基础科室入参
 * 
 * @author lsq version 1.0 2017-7-31上午11:56:42
 */
public class ReqQueryBaseDept extends AbsReq {
	/**
	 * 医院id
	 */
	private String hosId;
	/**
	 * 科室名称，模糊查询
	 */
	private String deptName;
	/**
	 * 科室代码
	 */
	private String deptCode;
	/**
	 *  科室类型
	 */
	private String deptType;
	
	private Integer isShow;
	
	public ReqQueryBaseDept(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hosId = XMLUtil.getString(ser, "HosId", true);
		this.deptName = XMLUtil.getString(ser, "DeptName", false);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.deptType = XMLUtil.getString(ser, "DeptType", false);
		this.isShow = XMLUtil.getInt(ser, "IsShow", false,null);
	}
	
	public ReqQueryBaseDept(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.hosId = super.getHosId();
			this.deptType = getDataJs().getString("DeptType");
			this.deptCode = getDataJs().getString("DeptCode");
			this.deptName = getDataJs().getString("DeptName");
			this.isShow =  getDataJs().getInteger("IsShow");
		}
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

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

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
}
