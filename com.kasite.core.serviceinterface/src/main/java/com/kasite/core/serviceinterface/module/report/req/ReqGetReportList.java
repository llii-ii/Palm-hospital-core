package com.kasite.core.serviceinterface.module.report.req;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author linjf
 * TODO
 */
public class ReqGetReportList extends AbsReq {
	/**卡类型*/
	private String cardType;
	/**卡号*/
	private String cardNo;
	/**开始时间*/
	private String startDate;
	/**结束时间*/
	private String endDate;
	/**手机号*/
	private String mobile;
	/**用户名字*/
	private String patientName;
	/**报告单类型*/
	private String reportType;
	/**身份证号*/
	private String idCardNo;
	/***
	 * 成员ID
	 */
	private String memberId;
	/**
	 * 个性化参数前端自行拼装
	 * @return
	 */
	private String diyJson;
	
	

	public String getDiyJson() {
		return diyJson;
	}
	public void setDiyJson(String diyJson) {
		this.diyJson = diyJson;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getMobile() {
		return mobile;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getReportType() {
		return reportType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public ReqGetReportList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.startDate = XMLUtil.getString(ser, "StartDate", false);
		this.endDate = XMLUtil.getString(ser, "EndDate", false);
		this.mobile = XMLUtil.getString(ser, "Mobile", false);
		this.patientName = XMLUtil.getString(ser, "PatientName", false);
		this.reportType = XMLUtil.getString(ser, "ReportType", false);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", false);
		this.memberId = XMLUtil.getString(ser, "MemberId", false);
		Element data_1 = ser.element(KstHosConstant.DATA_1);
		if(null != data_1) {
			this.diyJson = XMLUtil.xml2JSON(data_1.asXML());
		}
	}
}
