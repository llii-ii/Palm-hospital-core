package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**排班科室入参
 * @author lsq
 * version 1.0
 * 2017-6-21上午11:17:48
 */
public class ReqQueryClinicDept extends AbsReq{
	/**医院id*/
	private String hosId;
	/**科室名称*/
	private String deptName;
	/**科室代码*/
	private String deptCode;
	/**查询有出诊日期范围 开始*/
	private String workDateStart;
	/**查询有出诊日期范围 结束*/
	private String workDateEnd;
	/**查询有出诊日期范围 时段0-全天 1-上午 2-下午*/
	private Integer workTime;
	/**获取的号源类别（是否有分时段号源  0：无 ，1：有）*/
	private Integer reqType;

	
	public ReqQueryClinicDept(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.hosId = XMLUtil.getString(ser, "HosId", false);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.deptName = XMLUtil.getString(ser, "DeptName", false);
		this.workDateStart = XMLUtil.getString(ser, "WorkDateStart", false);
		this.workDateEnd = XMLUtil.getString(ser, "WorkDateEnd", false);
		this.workTime = XMLUtil.getInt(ser, "WorkTime", false);
		this.reqType = XMLUtil.getInt(ser, "RegType", false);
	}
	
	
	/**
	 * @Title: ReqQueryClinicDept
	 * @Description: 
	 * @param msg
	 * @param hosId
	 * @param deptName
	 * @param deptCode
	 * @param workDateStart
	 * @param workDateEnd
	 * @param workTime
	 * @param reqType
	 * @throws AbsHosException
	 */
	public ReqQueryClinicDept(InterfaceMessage msg, String hosId, String deptName, String deptCode, String workDateStart, String workDateEnd, Integer workTime, Integer reqType) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		this.deptName = deptName;
		this.deptCode = deptCode;
		this.workDateStart = workDateStart;
		this.workDateEnd = workDateEnd;
		this.workTime = workTime;
		this.reqType = reqType;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getWorkDateStart() {
		return workDateStart;
	}

	public void setWorkDateStart(String workDateStart) {
		this.workDateStart = workDateStart;
	}

	public String getWorkDateEnd() {
		return workDateEnd;
	}

	public void setWorkDateEnd(String workDateEnd) {
		this.workDateEnd = workDateEnd;
	}

	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}

	public Integer getReqType() {
		return reqType;
	}

	public void setReqType(Integer reqType) {
		this.reqType = reqType;
	}
}
