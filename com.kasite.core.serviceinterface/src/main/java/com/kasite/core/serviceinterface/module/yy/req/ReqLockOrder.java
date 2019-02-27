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

/**锁号入参
 * @author lsq
 * version 1.0
 * 2017-7-4下午2:28:01
 */
public class ReqLockOrder extends AbsReq{
	/**科室编码*/
	private String deptCode;
	/**医生编码*/
	private String doctorCode;
//	/**证件类型*/
//	private String cardType;
	/**证件号*/
	private String cardNo;
	/**号源类型*/
	private String regType;
	/**出诊日期*/
	private String regDate;
	/**班次:0全天,1上午,2下午*/
	private Integer timeSlice;
	/**就诊序号*/
	private String sqNo;
	/**号源唯一id*/
	private String sourceCode;
	/**排班id*/
	private String scheduleId;
	/**号源开始时间*/
	private String commendTime; 
//	/**手机号*/
//	private String mobile;
//	/**就诊卡*/
//	private String clinicCard;
	
	private String hosId;
	
	private String memberId;
	
	
	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public ReqLockOrder(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.deptCode = XMLUtil.getString(ser, "DeptCode", false);
		this.doctorCode = XMLUtil.getString(ser, "DoctorCode", false);
		this.regType = XMLUtil.getString(ser, "RegType", false);
		this.regDate = XMLUtil.getString(ser, "RegDate", false);
		this.timeSlice = XMLUtil.getInt(ser, "TimeSlice", false);
		this.sqNo = XMLUtil.getString(ser, "SqNo", false);
		this.sourceCode = XMLUtil.getString(ser, "SourceCode", false);
		this.scheduleId = XMLUtil.getString(ser, "ScheduleId", false);
		this.commendTime = XMLUtil.getString(ser, "CommendTime", false);
		this.memberId =  XMLUtil.getString(ser, "MemberId", true);
		this.hosId = XMLUtil.getString(ser, "HosId", false,super.getHosId());
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
	}
	
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getClinicCard() {
//		return clinicCard;
//	}
//
//	public void setClinicCard(String clinicCard) {
//		this.clinicCard = clinicCard;
//	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

//	public String getCardType() {
//		return cardType;
//	}
//
//	public void setCardType(String cardType) {
//		this.cardType = cardType;
//	}
//	public String getCardNo() {
//		return cardNo;
//	}
//
//	public void setCardNo(String cardNo) {
//		this.cardNo = cardNo;
//	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
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

	public String getSqNo() {
		return sqNo;
	}

	public void setSqNo(String sqNo) {
		this.sqNo = sqNo;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getCommendTime() {
		return commendTime;
	}

	public void setCommendTime(String commendTime) {
		this.commendTime = commendTime;
	}


}
