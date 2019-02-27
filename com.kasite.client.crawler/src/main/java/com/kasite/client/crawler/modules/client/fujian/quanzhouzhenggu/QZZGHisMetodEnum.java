package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu;

import com.kasite.core.common.service.CommonMethodEnum;

public enum QZZGHisMetodEnum implements CommonMethodEnum {
	QueryPersion,
	QueryClinicRegisetrInfo,
	QueryClinicDiagnosis,
	QueryClinicFreeSummary,
	QueryClinicPrescriptionDetail,
	QueryClinicBill,
	QueryClinicCase,
	
	//住院相关
	QueryHospitalization,
	QueryHospitalizationDiagnosis,
	QueryHospitalizationFreeSummary,
	QueryHospitalizationPerscriptionDetail,
	QueryHospitalizationBill,
	QueryHospitalizationSummary,
	
	//电子病例
	QueryHospitalRecordList,
	QueryDiseaseList,
	QueryOperationList,
	QueryReportList,
	QueryCheckList,
	QueryCheckItemList,
	;
	public String getName() {
		return this.name();
	}
}
