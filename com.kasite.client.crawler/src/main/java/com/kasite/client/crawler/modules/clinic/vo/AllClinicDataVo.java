package com.kasite.client.crawler.modules.clinic.vo;

import java.util.ArrayList;
import java.util.List;

import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.core.httpclient.http.StringUtils;

public class AllClinicDataVo {
	private List<ClinicCaseEntity> clinicCaseList;
	
	private List<ClinicBillEntity> clinicBillList;
	
	private List<ClinicPrescriptionDetailEntity> clinicPrescriptionDetailList;
	
	private List<ClinicFreeSummaryEntity> clinicFreeSummaryList;
	
	private List<ClinicDiagnosisWaterEntity> clinicDiagnosisWaterList;
	
	private List<ClinicRegWaterEntity> clinicRegWaterList;

	public List<ClinicCaseEntity> getClinicCaseList() {
		return clinicCaseList;
	}

	public void setClinicCaseList(List<ClinicCaseEntity> clinicCaseList) {
		this.clinicCaseList = clinicCaseList;
	}

	public List<ClinicBillEntity> getClinicBillList() {
		return clinicBillList;
	}

	public void setClinicBillList(List<ClinicBillEntity> clinicBillList) {
		this.clinicBillList = clinicBillList;
	}

	public List<ClinicPrescriptionDetailEntity> getClinicPrescriptionDetailList() {
		return clinicPrescriptionDetailList;
	}

	public void setClinicPrescriptionDetailList(List<ClinicPrescriptionDetailEntity> clinicPrescriptionDetailList) {
		this.clinicPrescriptionDetailList = clinicPrescriptionDetailList;
	}

	public List<ClinicFreeSummaryEntity> getClinicFreeSummaryList() {
		return clinicFreeSummaryList;
	}

	public void setClinicFreeSummaryList(List<ClinicFreeSummaryEntity> clinicFreeSummaryList) {
		this.clinicFreeSummaryList = clinicFreeSummaryList;
	}

	public List<ClinicDiagnosisWaterEntity> getClinicDiagnosisWaterList() {
		return clinicDiagnosisWaterList;
	}

	public void setClinicDiagnosisWaterList(List<ClinicDiagnosisWaterEntity> clinicDiagnosisWaterList) {
		this.clinicDiagnosisWaterList = clinicDiagnosisWaterList;
	}

	public List<ClinicRegWaterEntity> getClinicRegWaterList() {
		return clinicRegWaterList;
	}

	public void setClinicRegWaterList(List<ClinicRegWaterEntity> clinicRegWaterList) {
		this.clinicRegWaterList = clinicRegWaterList;
	}

	public List<ClinicCaseEntity> getClinicCaseEntityListByClinicNum(String clinicNum){
		List<ClinicCaseEntity> list = new ArrayList<>();
		if(StringUtils.isNotBlank(clinicNum) && null != clinicCaseList) {
			for (ClinicCaseEntity entity : clinicCaseList) {
				if(clinicNum.equalsIgnoreCase(entity.getClinicNum())) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public List<ClinicDiagnosisWaterEntity> getClinicDiagnosisWaterEntityListByClinicNum(String clinicNum){
		List<ClinicDiagnosisWaterEntity> list = new ArrayList<>();
		if(StringUtils.isNotBlank(clinicNum) && null != clinicDiagnosisWaterList) {
			for (ClinicDiagnosisWaterEntity entity : clinicDiagnosisWaterList) {
				if(clinicNum.equalsIgnoreCase(entity.getClinicNum())) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public List<ClinicBillEntity> getClinicBillEntityListByClinicNum(String clinicNum){
		List<ClinicBillEntity> list = new ArrayList<>();
		if(StringUtils.isNotBlank(clinicNum) && null != clinicBillList) {
			for (ClinicBillEntity entity : clinicBillList) {
				if(clinicNum.equalsIgnoreCase(entity.getClinicNum())) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public List<ClinicFreeSummaryEntity> getClinicFreeSummaryEntityListByClinicNum(String clinicNum){
		List<ClinicFreeSummaryEntity> list = new ArrayList<>();
		if(StringUtils.isNotBlank(clinicNum) && null != clinicFreeSummaryList) {
			for (ClinicFreeSummaryEntity entity : clinicFreeSummaryList) {
				if(clinicNum.equalsIgnoreCase(entity.getClinicNum())) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public List<ClinicPrescriptionDetailEntity> getClinicPrescriptionDetailEntityListByClinicNum(String clinicNum){
		List<ClinicPrescriptionDetailEntity> list = new ArrayList<>();
		if(StringUtils.isNotBlank(clinicNum) && null != clinicPrescriptionDetailList) {
			for (ClinicPrescriptionDetailEntity entity : clinicPrescriptionDetailList) {
				if(clinicNum.equalsIgnoreCase(entity.getClinicNum())) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	
}
