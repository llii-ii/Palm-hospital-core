package com.kasite.core.serviceinterface.module.report.req;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author linjf
 * TODO
 */
public class ReqGetReportInfo extends AbsReq {
	private String reportId;
	private String reportType;
	private String eventNo;
	private String sampleNo;
	/*
	*
	 * @Author Andy
	 * @Description 报告提交时间
	 * @Date 15:33 2019/2/26
	 * @Param
	 * @return
	 **/
	private String submissionTime;

	/**
	 * 个性化参数前端自行拼装
	 * @return
	 */
	private String diyJson;


	public String getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}

	public String getDiyJson() {
		return diyJson;
	}
	public void setDiyJson(String diyJson) {
		this.diyJson = diyJson;
	}
	
	/**
	 * @return the reportId
	 */
	public String getReportId() {
		return reportId;
	}


	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}


	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}


	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


	/**
	 * @return the eventNo
	 */
	public String getEventNo() {
		return eventNo;
	}


	/**
	 * @param eventNo the eventNo to set
	 */
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}


	/**
	 * @return the sampleNo
	 */
	public String getSampleNo() {
		return sampleNo;
	}


	/**
	 * @param sampleNo the sampleNo to set
	 */
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}


	public ReqGetReportInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.reportId = XMLUtil.getString(ser, "ReportId", false);
		this.reportType = XMLUtil.getString(ser, "ReportType", false);
		this.eventNo = XMLUtil.getString(ser, "EventNo", false);
		this.sampleNo = XMLUtil.getString(ser, "SampleNo", false);
		this.submissionTime = XMLUtil.getString(ser, "SubmissionTime", false);
		Element data_1 = ser.element(KstHosConstant.DATA_1);
		if(null != data_1) {
			this.diyJson = XMLUtil.xml2JSON(data_1.asXML());
		}
	}
}
