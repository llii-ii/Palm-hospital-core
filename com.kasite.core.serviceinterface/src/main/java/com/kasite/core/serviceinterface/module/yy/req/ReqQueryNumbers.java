/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**查询号源入参
 * @author lsq
 * version 1.0
 * 2017-6-20下午3:19:08
 * 
 */
public class ReqQueryNumbers extends AbsReq{
	/**科室编码*/
	private String deptCode;
	/**医生编码*/
	private String doctorCode;
	/**查询有出诊日期*/
	private String regDate;
	/**班次:0全天,1上午,2下午*/
	private Integer timeSlice;
	/**排班ID*/
	private String scheduleId;
	/*
	 * @Author Andy
	 * @Description 病人姓名
	 * @Date 16:15 2019/2/20
	 * @Param 
	 * @return 
	 **/
	private String memberName;

	
	public ReqQueryNumbers(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
		this.regDate = XMLUtil.getString(ser, "RegDate", false);
		this.timeSlice = XMLUtil.getInt(ser, "TimeSlice", false);
		this.scheduleId = XMLUtil.getString(ser, "ScheduleId", false);
		this.memberName = XMLUtil.getString(ser, "MemberName", false);
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public Integer getTimeSlice() {
		return timeSlice;
	}

	public void setTimeSlice(Integer timeSlice) {
		this.timeSlice = timeSlice;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
}
