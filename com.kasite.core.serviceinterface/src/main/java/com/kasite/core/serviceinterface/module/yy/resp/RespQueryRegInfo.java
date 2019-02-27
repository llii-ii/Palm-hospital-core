package com.kasite.core.serviceinterface.module.yy.resp;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckEnum;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.his.resp.HisRegFlag;
import com.kasite.core.serviceinterface.module.his.resp.HisSchTimeId;

/**
 * 
 * @className: RespQueryRegInfo
 * @author: lcz
 * @date: 2018年7月24日 下午4:49:32
 */
public class RespQueryRegInfo extends AbsResp{
	
	private String hisOrderId;
	/**保存 本地订单的短号 */
	private String orderNum;
	private String address;
	private String birthDay;
	private String cardNo;
	private Integer cardType;
	private String clinicCard;
	private String deptCode;
	private String deptName;
	private String doctorCode;
	private String doctorName;
	private String idCardNo;
	private String lastModify;
	private String mobile;
	private String name;
	private String operatorId;
	private String operatorName;
	private String orderId;
	private Integer otherFee;
	private Integer payFee;
	private String regDate;
	private Integer regFee;
	@CheckEnum(message="预约状态",groups=AddGroup.class,inf=HisRegFlag.class)
	private Integer regFlag;
	private String regType;
	private String remark;
	private Integer sex;
	private Integer sqNo;
	@CheckEnum(message="预约时段",groups=AddGroup.class,inf=HisSchTimeId.class)
	private Integer timeSlice;
	private Integer treatFee;
	private String commendTime;
	private String takeNoDesc;
	private String takeNoPalce;
	private Integer payState;
	private Integer bizState;
	private Integer overState;
	
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getPayState() {
		return payState;
	}
	public void setPayState(Integer payState) {
		this.payState = payState;
	}
	public Integer getBizState() {
		return bizState;
	}
	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}
	public Integer getOverState() {
		return overState;
	}
	public void setOverState(Integer overState) {
		this.overState = overState;
	}
	public String getHisOrderId() {
		return hisOrderId;
	}
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
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
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Integer otherFee) {
		this.otherFee = otherFee;
	}
	public Integer getPayFee() {
		return payFee;
	}
	public void setPayFee(Integer payFee) {
		this.payFee = payFee;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public Integer getRegFee() {
		return regFee;
	}
	public void setRegFee(Integer regFee) {
		this.regFee = regFee;
	}
	public Integer getRegFlag() {
		return regFlag;
	}
	public void setRegFlag(Integer regFlag) {
		this.regFlag = regFlag;
	}
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getSqNo() {
		return sqNo;
	}
	public void setSqNo(Integer sqNo) {
		this.sqNo = sqNo;
	}
	public Integer getTimeSlice() {
		return timeSlice;
	}
	public void setTimeSlice(Integer timeSlice) {
		this.timeSlice = timeSlice;
	}
	public Integer getTreatFee() {
		return treatFee;
	}
	public void setTreatFee(Integer treatFee) {
		this.treatFee = treatFee;
	}
	public String getCommendTime() {
		return commendTime;
	}
	public void setCommendTime(String commendTime) {
		this.commendTime = commendTime;
	}
	public String getTakeNoDesc() {
		return takeNoDesc;
	}
	public void setTakeNoDesc(String takeNoDesc) {
		this.takeNoDesc = takeNoDesc;
	}
	public String getTakeNoPalce() {
		return takeNoPalce;
	}
	public void setTakeNoPalce(String takeNoPalce) {
		this.takeNoPalce = takeNoPalce;
	}
	
	
	
}
