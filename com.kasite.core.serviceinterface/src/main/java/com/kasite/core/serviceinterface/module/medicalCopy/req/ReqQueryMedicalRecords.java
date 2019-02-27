package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * 
 * @className: ReqQueryMedicalRecords
 * @author: cjy
 * @date: 2018年9月18日 下午4:20:19
 */
public class ReqQueryMedicalRecords extends AbsReq{

	private String patientId; //病案号
	private String idCard; //身份证
	private String name; //患者名称
	private String orgCode; //机构ID
	
	private String caseId;
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReqQueryMedicalRecords(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.patientId=XMLUtil.getString(ser, "patientId", false);
		this.orgCode=XMLUtil.getString(ser, "orgCode", false);
		this.caseId=XMLUtil.getString(ser, "caseId", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.idCard=XMLUtil.getString(ser, "idCard", false);
	}

}
