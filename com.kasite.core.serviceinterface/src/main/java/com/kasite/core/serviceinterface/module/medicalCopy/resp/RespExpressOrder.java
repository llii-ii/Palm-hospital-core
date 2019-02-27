package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.OrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.PriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.CopyCaseVo;

public class RespExpressOrder extends AbsResp{
	
	
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
	private String orderState;//订单状态1-申请成功  2-已确认  3-已寄送  4-已取消
	private String payState;//支付状态1-待支付  2-支付中  3-已支付  4-退款中  5-已退款
	private String authentication;//身份认证0-未验证  1-已验证
	private String idcardImgName;
	private String name;//病人姓名
	private String outHosDate;//出院日期
	private String inHosDate;//住院日期
	private String hospitalization;//住院天数
	private String operationName;//手术名称
	private int caseNumber;//复印份数
	private String outDeptName;//出院科室
	private String isoperation;//是否手术
	private String money;
	private String mcId;//单个病历ID
	private String preMoney;//预付款
	private String state;//1待支付2待审核3.待发件4.待收件5审核不通过6待补缴7已失效8已取消9 已签收
	private String receiveType;//1快递2来院自取
	private String receiveCode;//取件编号（快递编号）
	private String applyRelationType;//申请人关系类型：1患者本人2代理成人3代理儿童
	private String reason;//审核原因
	private String proveImgs;//证明材料图片
	private String copyNumber;//复印总数
	private String idCard;//身份证
	private List<CopyCaseVo> orderCases;
	private PriceManage priceManage;//缴费信息
	private String refundMoney;//退款金额
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

	public String getCourierNumber() {
		return courierNumber;
	}

	public void setCourierNumber(String courierNumber) {
		this.courierNumber = courierNumber;
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

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
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

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutHosDate() {
		return outHosDate;
	}

	public void setOutHosDate(String outHosDate) {
		this.outHosDate = outHosDate;
	}

	public String getHospitalization() {
		return hospitalization;
	}

	public void setHospitalization(String hospitalization) {
		this.hospitalization = hospitalization;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(int caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getOutDeptName() {
		return outDeptName;
	}

	public void setOutDeptName(String outDeptName) {
		this.outDeptName = outDeptName;
	}

	public String getIsoperation() {
		return isoperation;
	}

	public void setIsoperation(String isoperation) {
		this.isoperation = isoperation;
	}

	public String getInHosDate() {
		return inHosDate;
	}

	public void setInHosDate(String inHosDate) {
		this.inHosDate = inHosDate;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getMcId() {
		return mcId;
	}

	public void setMcId(String mcId) {
		this.mcId = mcId;
	}

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

	public String getIdcardImgName() {
		return idcardImgName;
	}

	public void setIdcardImgName(String idcardImgName) {
		this.idcardImgName = idcardImgName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public List<CopyCaseVo> getOrderCases() {
		return orderCases;
	}

	public void setOrderCases(List<CopyCaseVo> orderCases) {
		this.orderCases = orderCases;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public PriceManage getPriceManage() {
		return priceManage;
	}

	public void setPriceManage(PriceManage priceManage) {
		this.priceManage = priceManage;
	}

	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	
}
