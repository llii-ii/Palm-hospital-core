package com.kasite.client.yy.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;


/**预约流水
 * @author lsq
 * version 1.0
 * 2017-7-7上午9:46:45
 */
@Table(name="YY_WATER")
public class YyWater extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private String orderId;
	private String idCardNo;
	private String birthday;
	private String userMobile;
	private String userName;
	private Integer sex;
	private String address;
	private String clinicCard;
	private String hosId;
	private String hosName;
	private String deptCode;
	private String deptName;
	private String clinicCode;
	private String clinicName;
	private String doctorCode;
	private String doctorName;
	private Integer registrationFee;
	private Integer clinicFee;
	private Integer otherFee;
	private Integer serviceFee;
	private Integer fee;
	private Integer state;
	private String scheduleId;
	private String registerDate;
	private Integer timeId;
	private Integer queueNo;
	private String commendTime;
	private String sourceCode;
	private String operator;
	private String ssn;
	private String insuranceCard;
	private Integer cState;
	private String workPlace;
	private String drawPoint;
	private Integer cType;
	private String cNo;
	private String hisOrderId;
	private String remark;
	private Integer pushState;
	private String pushRemark;
	private Integer num;
	private String memberId;
	private String store;
	
	
	
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
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
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
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
	public Integer getRegistrationFee() {
		return registrationFee;
	}
	public void setRegistrationFee(Integer registrationFee) {
		this.registrationFee = registrationFee;
	}
	public Integer getClinicFee() {
		return clinicFee;
	}
	public void setClinicFee(Integer clinicFee) {
		this.clinicFee = clinicFee;
	}
	public Integer getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Integer otherFee) {
		this.otherFee = otherFee;
	}
	public Integer getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(Integer serviceFee) {
		this.serviceFee = serviceFee;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public Integer getTimeId() {
		return timeId;
	}
	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}
	public Integer getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(Integer queueNo) {
		this.queueNo = queueNo;
	}
	public String getCommendTime() {
		return commendTime;
	}
	public void setCommendTime(String commendTime) {
		this.commendTime = commendTime;
	}
	
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getInsuranceCard() {
		return insuranceCard;
	}
	public void setInsuranceCard(String insuranceCard) {
		this.insuranceCard = insuranceCard;
	}
	
	public Integer getcState() {
		return cState;
	}
	public void setcState(Integer cState) {
		this.cState = cState;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getDrawPoint() {
		return drawPoint;
	}
	public void setDrawPoint(String drawPoint) {
		this.drawPoint = drawPoint;
	}
	public Integer getcType() {
		return cType;
	}
	public void setcType(Integer cType) {
		this.cType = cType;
	}
	public String getcNo() {
		return cNo;
	}
	public void setcNo(String cNo) {
		this.cNo = cNo;
	}
	public String getHisOrderId() {
		return hisOrderId;
	}
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getPushState() {
		return pushState;
	}
	public void setPushState(Integer pushState) {
		this.pushState = pushState;
	}
	public String getPushRemark() {
		return pushRemark;
	}
	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
	
	
}
