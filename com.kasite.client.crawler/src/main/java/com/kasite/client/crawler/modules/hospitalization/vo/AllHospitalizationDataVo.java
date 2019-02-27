package com.kasite.client.crawler.modules.hospitalization.vo;

import java.util.List;

import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;

public class AllHospitalizationDataVo {

	private List<HospitalizationEntity> hospitalizationList;
	private List<HospitalizationBillEntity> hospitalizationBillList;
	private List<HospitalizationDiagnosisEntity> hospitalizationDiagnosisList;
	private List<HospitalizationFreeSummaryEntity> hospitalizationFreeSummaryList;
	private List<HospitalizationPerscriptionDetailEntity> hospitalizationPerscriptionDetailList;
	private List<HospitalizationSummaryEntity> hospitalizationSummaryList;
	public List<HospitalizationEntity> getHospitalizationList() {
		return hospitalizationList;
	}
	public void setHospitalizationList(List<HospitalizationEntity> hospitalizationList) {
		this.hospitalizationList = hospitalizationList;
	}
	public List<HospitalizationBillEntity> getHospitalizationBillList() {
		return hospitalizationBillList;
	}
	public void setHospitalizationBillList(List<HospitalizationBillEntity> hospitalizationBillList) {
		this.hospitalizationBillList = hospitalizationBillList;
	}
	public List<HospitalizationDiagnosisEntity> getHospitalizationDiagnosisList() {
		return hospitalizationDiagnosisList;
	}
	public void setHospitalizationDiagnosisList(List<HospitalizationDiagnosisEntity> hospitalizationDiagnosisList) {
		this.hospitalizationDiagnosisList = hospitalizationDiagnosisList;
	}
	public List<HospitalizationFreeSummaryEntity> getHospitalizationFreeSummaryList() {
		return hospitalizationFreeSummaryList;
	}
	public void setHospitalizationFreeSummaryList(List<HospitalizationFreeSummaryEntity> hospitalizationFreeSummaryList) {
		this.hospitalizationFreeSummaryList = hospitalizationFreeSummaryList;
	}
	public List<HospitalizationPerscriptionDetailEntity> getHospitalizationPerscriptionDetailList() {
		return hospitalizationPerscriptionDetailList;
	}
	public void setHospitalizationPerscriptionDetailList(
			List<HospitalizationPerscriptionDetailEntity> hospitalizationPerscriptionDetailList) {
		this.hospitalizationPerscriptionDetailList = hospitalizationPerscriptionDetailList;
	}
	public List<HospitalizationSummaryEntity> getHospitalizationSummaryList() {
		return hospitalizationSummaryList;
	}
	public void setHospitalizationSummaryList(List<HospitalizationSummaryEntity> hospitalizationSummaryList) {
		this.hospitalizationSummaryList = hospitalizationSummaryList;
	}
	
}
