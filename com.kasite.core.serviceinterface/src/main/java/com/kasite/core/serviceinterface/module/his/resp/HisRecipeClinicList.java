/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * <p>Title : HisRecipeClinicList</p>
 * <p>Description : 查询处方开药信息出参</p>
 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
 * <p>DevelopSystem : windows7</p>
 * <p>Company : com.kst</p>
 * @author : HongHuaYu
 * @date : 2018年12月6日 下午5:37:04
 * @version : 1.0.0
 */
public class HisRecipeClinicList  extends AbsResp{
	
	/**
	 * 病人id
	 */
	private String hisMemberId;
	/**
	 * 就诊卡
	 */
	private String cardNo;
	/**
	 * 身份证
	 */
	private String idCard;
	/**
	 * 收据号
	 */
	private String receiptNo;
	
	/**
	 * 处方（收据）名称
	 */
	private String receiptName;
	
	/**
	 * 收据时间,yyy-MM-dd hh:mm:ss
	 */
	private String receiptTime;
	
	/**
	 * 收据总金额
	 */
	private Integer totalPrice;
	
	/**
	 * 开单科室
	 */
	private String receiptDeptName;
	/**
	 * 开单医生
	 */
	private String receiptDoctorName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 业务类型
	 */
	private String serviceId;
	
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}


	public String getReceiptDeptName() {
		return receiptDeptName;
	}

	public void setReceiptDeptName(String receiptDeptName) {
		this.receiptDeptName = receiptDeptName;
	}

	public String getReceiptDoctorName() {
		return receiptDoctorName;
	}

	public void setReceiptDoctorName(String receiptDoctorName) {
		this.receiptDoctorName = receiptDoctorName;
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	
	
}
