/**
 * 
 */
package com.kasite.core.serviceinterface.module.queue.req;

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
public class ReqSetReMindNo extends AbsReq {

	private String registerDate;
	private String timeId;
	private String location;
	/**卡类型*/
	private String cardType;
	/**卡号*/
	private String cardNo;

	private String patientName;
	private String phoneNo;
	private String deptName;
	private String doctorName;
	private String no;
	private String maxNo;
	private String nextNo;
	private String queryId;
	private String ifMsg;
	private String reMindNo;
	private String channelId;

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getTimeId() {
		return timeId;
	}

	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getMaxNo() {
		return maxNo;
	}

	public void setMaxNo(String maxNo) {
		this.maxNo = maxNo;
	}

	public String getNextNo() {
		return nextNo;
	}

	public void setNextNo(String nextNo) {
		this.nextNo = nextNo;
	}


	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getIfMsg() {
		return ifMsg;
	}

	public void setIfMsg(String ifMsg) {
		this.ifMsg = ifMsg;
	}

	public String getReMindNo() {
		return reMindNo;
	}

	public void setReMindNo(String reMindNo) {
		this.reMindNo = reMindNo;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public ReqSetReMindNo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);

		if (ser == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.registerDate = XMLUtil.getString(ser, "RegisterDate", false);
		this.timeId = XMLUtil.getString(ser, "TimeId", false);
		this.location = XMLUtil.getString(ser, "Location", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.phoneNo = XMLUtil.getString(ser, "PhoneNo", false);
		this.patientName = XMLUtil.getString(ser, "PatientName", false);
		this.deptName = XMLUtil.getString(ser, "DeptName", false);
		this.doctorName = XMLUtil.getString(ser, "DoctorName", false);
		this.no = XMLUtil.getString(ser, "No", false);
		this.maxNo = XMLUtil.getString(ser, "MaxNo", false);
		this.nextNo = XMLUtil.getString(ser, "NextNo", false);
		this.queryId = XMLUtil.getString(ser, "QueryId", false);
		this.ifMsg = XMLUtil.getString(ser, "IfMsg", false);
		this.reMindNo = XMLUtil.getString(ser, "ReMindNo", false);
		this.channelId = XMLUtil.getString(ser, "ChannelId", false,super.getClientId());

	}

}
