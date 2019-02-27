package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: ExpressoOrder
 * @author: cjy
 * @date: 2018年9月13日 下午2:53:04
 */
@Table(name="TB_EXPRESS_ORDER")
public class ExpressOrder extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id; //uuid
	private String patientId;//病案号
	private String courierNumber;//单号
	private String courierCompanyCode;//快递公司code
	private String courierCompany;//快递公司
	private String caseIdAll;//病历号
	private String caseNumberAll;//病历份数
	private String caseMoneyAll;
	private String totalMoney;//总额
	private String addressee;//收件人
	private String telephone;//联系电话
	private String province;//省
	private String municipal;//市
	private String county;//区县
	private String address;//详细地址
	private String applyTime;//申请时间
	private String applyOpenId;
	private String applyName;
	private String wxOrderId;//微信支付产生的orderid
	private String orderState;//订单状态订单状态1-申请成功  2-已确认  3-已寄送  4-已取消 5-审核不通过 6-审核通过 7-待补缴 8-已补缴
	private String payState;//支付状态1-待支付  2-支付中  3-已支付  4-退款中  5-已退款
	private String authentication;//身份认证0-未验证  1-已验证
	private String idcardImgName;//身份证照片名
	private String receiveType;//1快递2来院自取
	private String receiveCode;//取件编号（快递编号）
	private String applyRelationType;//申请人关系类型：1患者本人2代理成人3代理儿童
	private String reason;//审核原因
	private String proveImgs;//证明材料图片
	private String copyNumber;//复印总数
	private String preMoney;//预付款
	private String hosId;//医院id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getMunicipal() {
		return municipal;
	}

	public void setMunicipal(String municipal) {
		this.municipal = municipal;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getCourierNumber() {
		return courierNumber;
	}

	public void setCourierNumber(String courierNumber) {
		this.courierNumber = courierNumber;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

//	public String getPayState() {
//		return payState;
//	}
//
//	public void setPayState(String payState) {
//		this.payState = payState;
//	}

	public String getCourierCompany() {
		return courierCompany;
	}

	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}

	public String getCourierCompanyCode() {
		return courierCompanyCode;
	}

	public void setCourierCompanyCode(String courierCompanyCode) {
		this.courierCompanyCode = courierCompanyCode;
	}

	public String getCaseIdAll() {
		return caseIdAll;
	}

	public void setCaseIdAll(String caseIdAll) {
		this.caseIdAll = caseIdAll;
	}

	public String getCaseNumberAll() {
		return caseNumberAll;
	}

	public void setCaseNumberAll(String caseNumberAll) {
		this.caseNumberAll = caseNumberAll;
	}

	public String getCaseMoneyAll() {
		return caseMoneyAll;
	}

	public void setCaseMoneyAll(String caseMoneyAll) {
		this.caseMoneyAll = caseMoneyAll;
	}

	public String getWxOrderId() {
		return wxOrderId;
	}

	public void setWxOrderId(String wxOrderId) {
		this.wxOrderId = wxOrderId;
	}

	public String getApplyOpenId() {
		return applyOpenId;
	}

	public void setApplyOpenId(String applyOpenId) {
		this.applyOpenId = applyOpenId;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getIdcardImgName() {
		return idcardImgName;
	}

	public void setIdcardImgName(String idcardImgName) {
		this.idcardImgName = idcardImgName;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public String getApplyRelationType() {
		return applyRelationType;
	}

	public void setApplyRelationType(String applyRelationType) {
		this.applyRelationType = applyRelationType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProveImgs() {
		return proveImgs;
	}

	public void setProveImgs(String proveImgs) {
		this.proveImgs = proveImgs;
	}

	public String getCopyNumber() {
		return copyNumber;
	}

	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}

	public String getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	
}
