package com.kasite.client.crawler.modules.patient.vo;

import com.alibaba.fastjson.JSONObject;

public class ReqQueryPatientListVo {

	private String getHosKey;
	private String idCardId;
	private String patientId;
	private String clinicCard;
	private String certType;
	private String certNum;
	private String ssNum;
	private String name;
	private String inHospitalNum;
	private Integer pageIndex;
	private Integer pageSize;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInHospitalNum() {
		return inHospitalNum;
	}

	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNum() {
		return certNum;
	}

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}

	public String getSsNum() {
		return ssNum;
	}

	public void setSsNum(String ssNum) {
		this.ssNum = ssNum;
	}

	public String getIdCardId() {
		return idCardId;
	}
	
	public String getGetHosKey() {
		return getHosKey;
	}

	public void setGetHosKey(String getHosKey) {
		this.getHosKey = getHosKey;
	}

	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public ReqQueryPatientListVo parse(JSONObject json) {
		this.clinicCard = json.getString("clinicCard");
		this.idCardId = json.getString("idCardId");
		this.patientId = json.getString("patientId");
		this.pageIndex = json.getIntValue("pageIndex");
		this.pageSize = json.getInteger("pageSize");
		this.certType = json.getString("certType");
		this.certNum = json.getString("certNum");
		this.ssNum = json.getString("ssNum");
		return this;
	}
}
