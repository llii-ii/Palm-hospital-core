package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**排班医生入参
 * @author lsq
 * version 1.0
 * 2017-6-21上午11:15:12
 */
public class ReqQueryClinicDoctor extends AbsReq{
	/**医生名称*/
	private String doctorName;
	/**科室代码*/
	private String deptCode;
	/**科室名称*/
	private String deptName;
	/**医生编码*/
	private String doctorCode;
	/**多个医生编号*/
	private String doctorCodes;
	/**医生职称编码*/
	private String doctorTitleCode;
	/**医生职称*/
	private String doctorTitle;
	/**排班id*/
	private String scheduleId;
	/**查询有出诊日期范围 开始*/
	private String workDateStart;
	/**查询有出诊日期范围 结束*/
	private String workDateEnd;
	/**医院ID*/
	private String hosId;
	private String deptCodes;
	/**是否同步医生  1是**/
	private Integer syncDoctor;
	/**是否查询历史就诊医生 1 是**/
	private Integer isQueryHistory;
	/**接口类型  区分his调用接口**/
	private Integer hisType;
	
	public Integer getHisType() {
		return hisType;
	}

	public void setHisType(Integer hisType) {
		this.hisType = hisType;
	}

	public String getDeptCodes() {
		return deptCodes;
	}

	public void setDeptCodes(String deptCodes) {
		this.deptCodes = deptCodes;
	}

	public Integer getIsQueryHistory() {
		return isQueryHistory;
	}

	public void setIsQueryHistory(Integer isQueryHistory) {
		this.isQueryHistory = isQueryHistory;
	}

	public ReqQueryClinicDoctor(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.hosId = XMLUtil.getString(ser, "HosId", false);
		this.deptCode = XMLUtil.getString(ser, "DeptCode", true);
		this.deptName = XMLUtil.getString(ser, "DeptName", false);
		this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
		this.doctorCodes = XMLUtil.getString(ser, "DoctorCodes", false);
		this.doctorName = XMLUtil.getString(ser, "DoctorName", false);
		this.scheduleId = XMLUtil.getString(ser, "ScheduleId", false);
		this.workDateStart = XMLUtil.getString(ser, "WorkDateStart", false);
		this.workDateEnd = XMLUtil.getString(ser, "WorkDateEnd", false);
		this.doctorTitleCode = XMLUtil.getString(ser, "DoctorTitleCode", false);
		this.doctorTitle = XMLUtil.getString(ser, "DoctorTitle", false);
		this.syncDoctor = XMLUtil.getInt(ser, "SyncDoctor", false);
		this.hisType = XMLUtil.getInt(ser, "HisType", false);
	}
	
	/**
	 * @Title: ReqQueryClinicDoctor
	 * @Description: 
	 * @param msg
	 * @param doctorName
	 * @param deptCode
	 * @param doctorCode
	 * @param doctorCodes
	 * @param doctorTitleCode
	 * @param scheduleId
	 * @param workDateStart
	 * @param workDateEnd
	 * @param hosId
	 * @throws AbsHosException
	 */
	public ReqQueryClinicDoctor(InterfaceMessage msg, String doctorName, String deptCode, String doctorCode, String doctorCodes, String doctorTitleCode, String scheduleId, String workDateStart, String workDateEnd, String hosId,Integer syncDoctor) throws AbsHosException {
		super(msg);
		this.doctorName = doctorName;
		this.deptCode = deptCode;
		this.doctorCode = doctorCode;
		this.doctorCodes = doctorCodes;
		this.doctorTitleCode = doctorTitleCode;
		this.scheduleId = scheduleId;
		this.workDateStart = workDateStart;
		this.workDateEnd = workDateEnd;
		this.hosId = hosId;
		this.syncDoctor = syncDoctor;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the syncDoctor
	 */
	public Integer getSyncDoctor() {
		return syncDoctor;
	}

	/**
	 * @param syncDoctor the syncDoctor to set
	 */
	public void setSyncDoctor(Integer syncDoctor) {
		this.syncDoctor = syncDoctor;
	}

	public String getDoctorTitle() {
		return doctorTitle;
	}

	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
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

	public String getDoctorCodes() {
		return doctorCodes;
	}

	public void setDoctorCodes(String doctorCodes) {
		this.doctorCodes = doctorCodes;
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
	
	public String getDoctorTitleCode() {
		return doctorTitleCode;
	}

	public void setDoctorTitleCode(String doctorTitleCode) {
		this.doctorTitleCode = doctorTitleCode;
	}
}
