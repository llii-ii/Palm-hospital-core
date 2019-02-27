package com.kasite.core.serviceinterface.module.order.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryOrderSettlementInfo extends AbsResp{

private String memberName;
	
	private String cardNo;
	
	private Integer cardType;
	
	/**
	 * 处方金额（分）
	 */
	private Integer price;
	
	/**
	 * 是否结算0未结算1已结算
	 */
	private Integer isSettlement;
	
	/**
	 * His处方订单号
	 */
	private String hisOrderId;
	
	/**
	 * 处方号
	 */
	private String prescNo;
	
	/**
	 * 处方类型名称
	 */
	private String prescType;
	
	/**
	 * 处方医生
	 */
	private String doctorName;
	
	/**
	 * 处方医生所属科室
	 */
	private String  deptName;
	
	/**
	 * 处方时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String prescTime;
	
	/**
	 * 处方明细项
	 */
	private List<CommonPrescriptionItem> data_1;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Integer isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getPrescType() {
		return prescType;
	}

	public void setPrescType(String prescType) {
		this.prescType = prescType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPrescTime() {
		return prescTime;
	}

	public void setPrescTime(String prescTime) {
		this.prescTime = prescTime;
	}

	public List<CommonPrescriptionItem> getData_1() {
		return data_1;
	}

	public void setData_1(List<CommonPrescriptionItem> data_1) {
		this.data_1 = data_1;
	}
	
	
}
