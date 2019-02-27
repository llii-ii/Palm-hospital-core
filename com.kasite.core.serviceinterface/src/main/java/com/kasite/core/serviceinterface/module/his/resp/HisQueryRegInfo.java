package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 预约信息查询出参
 * 
 * @author lsq version 1.0 2017-7-10下午2:23:11
 */
public class HisQueryRegInfo  extends AbsResp{
	/** 订单号Uuid */
	private String hisOrderId;
	/** 证件类型 */
	private Integer cardType;
	/** 证件号 */
	private String cardNo;
	/** 身份证号 */
	private String idCardNo;
	/** 出生日期YYYY-MM-DD */
	private String birthDay;
	/** 联系电话 */
	private String mobile;
	/** 患者姓名 */
	private String name;
	/** 患者性别 */
	private Integer sex;
	/** 患者家庭住址 */
	private String address;
	/** 门诊卡 */
	private String clinicCard;
	/** 挂号类别 */
	private String regType;
	/** 医生姓名 */
	private String doctorName;
	/** 医生工号 */
	private String doctorCode;
	/** 科室代码 */
	private String deptCode;
	/** 科室名称 */
	private String deptName;
	/** 是否付费 */
	private Integer payFee;
	/** 挂号费（单位：分） */
	private Integer regFee;
	/** 诊疗费（单位：分） */
	private Integer treatFee;
	/** 其他费用（单位：分） */
	private Integer otherFee;
	/** 操作人id */
	private String operatorId;
	/** 操作人名称 */
	private String operatorName;
	/** 就诊日期YYYY-MM-DD */
	private String regDate;
	/** 时段id 1上午2下午3晚上 */
	private Integer timeSlice;
	/** 预约序号 */
	private Integer sqNo;
	/** 备注 */
	private String remark;
	/** 预约状态：1正常，2退号，3停诊，4替诊 */
	private Integer regFlag;
	/** 最后修改时间 */
	private String lastModify;
	/** 号源开始时间 */
	private String commendTime;
	/** 取号说明 */
	private String takeNoDesc;
	/** 取号地点 */
	private String takeNoPalce;

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
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

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
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

	public Integer getPayFee() {
		return payFee;
	}

	public void setPayFee(Integer payFee) {
		this.payFee = payFee;
	}

	public Integer getRegFee() {
		return regFee;
	}

	public void setRegFee(Integer regFee) {
		this.regFee = regFee;
	}

	public Integer getTreatFee() {
		return treatFee;
	}

	public void setTreatFee(Integer treatFee) {
		this.treatFee = treatFee;
	}

	public Integer getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(Integer otherFee) {
		this.otherFee = otherFee;
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

	public Integer getSqNo() {
		return sqNo;
	}

	public void setSqNo(Integer sqNo) {
		this.sqNo = sqNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRegFlag() {
		return regFlag;
	}

	public void setRegFlag(Integer regFlag) {
		this.regFlag = regFlag;
	}

	public String getLastModify() {
		return lastModify;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
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

//	public HisQueryRegInfo yyWaterToResp(YyWater yyWater) {
//		this.orderId = yyWater.getYyOrderId();
//		this.cardNo = yyWater.getCNo();
//		this.idCardNo = yyWater.getIdCardNo();
//		this.birthDay = yyWater.getBirthday();
//		this.mobile = yyWater.getUserMobile();
//		this.name = yyWater.getUserName();
//		this.sex = yyWater.getSex();
//		this.address = yyWater.getAddress();
//		this.clinicCard = yyWater.getClinicCard();
//		this.doctorName = yyWater.getDoctorName();
//		this.doctorCode = yyWater.getDoctorCode();
//		this.deptCode = yyWater.getDeptCode();
//		this.deptName = yyWater.getDeptName();
//		this.payFee = yyWater.getFee();
//		this.regFee = yyWater.getRegistrationFee();
//		this.treatFee = yyWater.getServiceFee();
//		this.otherFee = yyWater.getOtherFee();
//		this.operatorId = yyWater.getOperatorId();
//		this.operatorName = yyWater.getOperatorName();
//		this.regDate = yyWater.getRegisterDate();
//		this.timeSlice = yyWater.getTimeId();
//		this.sqNo = yyWater.getQueueNo();
//		this.remark = yyWater.getRemark();
//		this.regFlag = yyWater.getState();
//		this.lastModify = String.valueOf(yyWater.getLastModify());
//		this.commendTime = yyWater.getCommendTime();
//		return this;
//	}
//
//	public HisQueryRegInfo yyOrderToResp(OrderView orderView, YyLock qlock) {
//		String scheduleIdSplit = "_";
//		int scheduleArrlenth = 4;
//		if (orderView == null || qlock == null) {
//			return this;
//		}
//		this.orderId = qlock.getYyOrderId();
//		this.cardNo = qlock.getCardNo();
//		this.name = orderView.getOperatorName();
//		this.doctorName = qlock.getDoctorName();
//		this.doctorCode = qlock.getDoctorCode();
//		this.deptCode = orderView.getDeptCode();
//		this.deptName = orderView.getDeptName();
//		this.regFee = Integer.valueOf(orderView.getTotalPrice());
//		this.operatorId = orderView.getOperatorId();
//		this.operatorName = orderView.getOperatorName();
//		String scheduleId = qlock.getScheduleId();
//		if (scheduleId != null && scheduleId.split(scheduleIdSplit).length == scheduleArrlenth) {
//			this.regDate = scheduleId.split(scheduleIdSplit)[2];
//			this.timeSlice = Integer.valueOf(scheduleId.split(scheduleIdSplit)[3]);
//		}
//		this.sqNo = qlock.getSqNo();
//		if (Constant.ORDERBIZSTATE_2.equals(orderView.getBizState())
//				&& Constant.ORDERPAY_0.equals(orderView.getPayState())) {
//			this.regFlag = Integer.valueOf(Constant.BOOK_3);
//		} else if (KstHosConstant.ORDEROVER_5.equals(orderView.getOverState())
//				|| KstHosConstant.ORDEROVER_6.equals(orderView.getOverState())) {
//			this.regFlag = Integer.valueOf(Constant.BOOK_3);
//			/** 请不要在条件中使用复杂的表达式 ╮(╯▽╰)╭ */
//		} else if (Constant.ORDERBIZSTATE_0.equals(orderView.getBizState())
//				&& (Constant.ORDERPAY_3.equals(orderView.getPayState())
//						|| Constant.ORDERPAY_4.equals(orderView.getPayState()))
//				&& KstHosConstant.ORDEROVER_0.equals(orderView.getOverState())) {
//			// 挂号的时候号源已经被解锁则退回，该状态视为已取消
//			this.regFlag = Integer.valueOf(Constant.BOOK_3);
//		} else if (Constant.ORDERBIZSTATE_0.equals(orderView.getBizState())
//				&& Constant.ORDERPAY_0.equals(orderView.getPayState())) {
//			this.regFlag = Integer.valueOf(Constant.BOOK_0);
//			/** 请不要在条件中使用复杂的表达式 ╮(╯▽╰)╭ */
//		} else if (KstHosConstant.ORDEROVER_0.equals(String.valueOf(orderView.getOverState()))
//				&& KstHosConstant.ORDERBIZSTATE_0.equals(orderView.getBizState())
//				&& (Constant.ORDERPAY_1.equals(orderView.getPayState())
//						|| Constant.ORDERPAY_2.equals(orderView.getPayState()))) {
//			// 正在支付或者支付完成的情况
//			this.regFlag = Integer.valueOf(Constant.BOOK_4);
//		}
//		return this;
//	}

}
