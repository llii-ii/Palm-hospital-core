package com.kasite.core.serviceinterface.module.medicalCopy.req;

import java.sql.Timestamp;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqExpressOrder extends AbsReq{

	//数据库表字段
	private String id; //uuid
	private String patientId;//病案号
	private String courierNumber;//单号
	private String courierCompanyCode;//快递公司code
	private String courierCompany;//快递公司
	private String caseIdAll;//病历号
	private String totalMoney;//总额
	private String addressee;//收件人
	private String telephone;//联系电话
	private String province;//省
	private String municipal;//市
	private String county;//区县
	private String address;//详细地址
	private String applyOpenId;
	private String applyName;
	private String applyTime;//申请时间
	private String wxOrderId;//微信支付产生的orderid
	private String orderState;//订单状态1-申请成功  2-已确认  3-已寄送  4-已取消
	private String payState;//支付状态1-待支付  2-支付中  3-已支付  4-退款中  5-已退款
	private String authentication;//身份认证0-未验证  1-已验证
	private String idcardImgName;
	private String receiveType;//1快递2来院自取
	private String receiveCode;//取件编号（快递编号）
	private String applyRelationType;//申请人关系类型：1患者本人2代理成人3代理儿童
	private String reason;//审核原因
	private String proveImgs;//证明材料图片
	private String copyNumber;//复印总数
	private String preMoney;//预付款
	private String hosId;//医院id
	//方便流程使用字段
	private String caseNumberAll;//病历份数
	private String caseMoneyAll;//病历每份的钱
	private String copyContentNames;//复印内容名称（多个以；隔开）
	private String copyUtilitys;//复印用途（多个以逗号隔开）
	private String priceManageId;//收费选项id
	private String mcId;//单病历ID
	private String orgCode;
	private Timestamp updateTime;
	private String msgUrl;
	
	//按需查询字段
	private String name;//名字
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String outHosDate;//出院时间
	private String fuzzyQuery;//模糊查询（支持姓名/病案号/订单id）
	
	//审核使用字段
	private String applyState;//2审核通过3审核不通过


	
	
	public ReqExpressOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.patientId=XMLUtil.getString(ser, "patientId", false);
		this.courierNumber=XMLUtil.getString(ser, "courierNumber", false);
		this.courierCompanyCode=XMLUtil.getString(ser, "courierCompanyCode", false);
		this.courierCompany=XMLUtil.getString(ser, "courierCompany", false);
		this.caseIdAll=XMLUtil.getString(ser, "caseIdAll", false);
		this.caseNumberAll=XMLUtil.getString(ser, "caseNumberAll", false);
		this.caseMoneyAll=XMLUtil.getString(ser, "caseMoneyAll", false);
		this.totalMoney=XMLUtil.getString(ser, "totalMoney", false);
		this.addressee=XMLUtil.getString(ser, "addressee", false);
		this.telephone=XMLUtil.getString(ser, "telephone", false);
		this.province=XMLUtil.getString(ser, "province", false);
		this.municipal=XMLUtil.getString(ser, "municipal", false);
		this.county=XMLUtil.getString(ser, "county", false);
		this.address=XMLUtil.getString(ser, "address", false);
		this.applyTime=XMLUtil.getString(ser, "applyTime", false);
		this.applyOpenId=XMLUtil.getString(ser, "applyOpenId", false);
		this.applyName=XMLUtil.getString(ser, "applyName", false);
		this.wxOrderId=XMLUtil.getString(ser, "wxOrderId", false);
		this.orderState = XMLUtil.getString(ser, "orderState", false);
		this.payState = XMLUtil.getString(ser, "payState", false);
		this.authentication=XMLUtil.getString(ser, "authentication", false);
		this.idcardImgName=XMLUtil.getString(ser, "idcardImgName", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.startTime=XMLUtil.getString(ser, "startTime", false);
		this.endTime=XMLUtil.getString(ser, "endTime", false);
		this.outHosDate=XMLUtil.getString(ser, "outHosDate", false);	
		this.msgUrl=XMLUtil.getString(ser, "msgUrl", false);	
		this.mcId=XMLUtil.getString(ser, "mcId", false);
		this.receiveType=XMLUtil.getString(ser, "receiveType", false);	
		this.receiveCode=XMLUtil.getString(ser, "receiveCode", false);	
		this.applyRelationType=XMLUtil.getString(ser, "applyRelationType", false);
		this.reason=XMLUtil.getString(ser, "reason", false);	
		this.proveImgs=XMLUtil.getString(ser, "proveImgs", false);	
		this.copyNumber=XMLUtil.getString(ser, "copyNumber", false);
		this.preMoney=XMLUtil.getString(ser, "preMoney", false);
		this.applyState=XMLUtil.getString(ser, "applyState", false);
		this.copyContentNames=XMLUtil.getString(ser, "copyContentNames", false);
		this.copyUtilitys=XMLUtil.getString(ser, "copyUtilitys", false);
		this.priceManageId=XMLUtil.getString(ser, "priceManageId", false);
		this.hosId=XMLUtil.getString(ser, "hosId", false);
		this.fuzzyQuery=XMLUtil.getString(ser, "fuzzyQuery", false);
	}

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getOutHosDate() {
		return outHosDate;
	}

	public void setOutHosDate(String outHosDate) {
		this.outHosDate = outHosDate;
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getIdcardImgName() {
		return idcardImgName;
	}

	public void setIdcardImgName(String idcardImgName) {
		this.idcardImgName = idcardImgName;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
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

	public String getCopyContentNames() {
		return copyContentNames;
	}

	public void setCopyContentNames(String copyContentNames) {
		this.copyContentNames = copyContentNames;
	}

	public String getCopyUtilitys() {
		return copyUtilitys;
	}

	public void setCopyUtilitys(String copyUtilitys) {
		this.copyUtilitys = copyUtilitys;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public String getPriceManageId() {
		return priceManageId;
	}

	public void setPriceManageId(String priceManageId) {
		this.priceManageId = priceManageId;
	}

	public String getHosId() {
		if(StringUtil.isBlank(hosId)) {
			try {
				hosId = super.getHosId();
			} catch (AbsHosException e) {
				e.printStackTrace();
			}
		}
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getFuzzyQuery() {
		return fuzzyQuery;
	}

	public void setFuzzyQuery(String fuzzyQuery) {
		this.fuzzyQuery = fuzzyQuery;
	}
	
}
