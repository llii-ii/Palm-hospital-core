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

/**查询门诊排班入参
 * @author lsq
 * @version 1.0 
 * 2017-6-29下午3:38:34
 */
public class ReqQueryClinicSchedule extends AbsReq{
	/**医院唯一编号*/
	private String hosId;
	/**科室代码*/
	private String deptCode;
	/**医生编码*/
	private String doctorCode;
	/**排班id*/
	private String scheduleId;
	/**查询有出诊日期范围 开始*/
	private String workDateStart;
	/**查询有出诊日期范围 结束*/
	private String workDateEnd;

	
	public ReqQueryClinicSchedule(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.hosId = XMLUtil.getString(ser, "HosId", false);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
		this.scheduleId = XMLUtil.getString(ser, "ScheduleId", false);
		this.workDateStart = XMLUtil.getString(ser, "WorkDateStart", false);
		this.workDateEnd = XMLUtil.getString(ser, "WorkDateEnd", false);
	}
	
	public ReqQueryClinicSchedule(InterfaceMessage msg, String hosId, String deptCode, String doctorCode,
			String scheduleId, String workDateStart, String workDateEnd) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		this.deptCode = deptCode;
		this.doctorCode = doctorCode;
		this.scheduleId = scheduleId;
		this.workDateStart = workDateStart;
		this.workDateEnd = workDateEnd;
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
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
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
}
