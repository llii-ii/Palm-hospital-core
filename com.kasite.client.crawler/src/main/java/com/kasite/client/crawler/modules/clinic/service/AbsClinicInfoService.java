package com.kasite.client.crawler.modules.clinic.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.client.AbsCommonClientCallService;
import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.clinic.vo.AllClinicDataVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;

public abstract class AbsClinicInfoService extends AbsCommonClientCallService implements IClinicService{
	
	public abstract EntityUtils getEntityUtils();
	
	
	@Override
	public List<JSONObject> queryClinicData2DataCloud(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	@Override
	public List<ClinicBillEntity> queryClinicBill(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	
	@Override
	public List<ClinicCaseEntity> queryClinicCase(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	@Override
	public List<ClinicDiagnosisWaterEntity> queryClinicDiagnosis(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	@Override
	public List<ClinicFreeSummaryEntity> queryClinicFreeSummary(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	@Override
	public List<ClinicPrescriptionDetailEntity> queryClinicPrescriptionDetail(AbsQueryClinicReqVo reqVo)
			throws Exception {
		return null;
	}
	@Override
	public List<ClinicRegWaterEntity> queryClinicRegisetrInfo(AbsQueryClinicReqVo reqVo) throws Exception {
		return null;
	}
	

	public AllClinicDataVo queryAll(String pid) throws Exception {
		AllClinicDataVo vo = new AllClinicDataVo();
		List<ClinicCaseEntity> list = queryClinicCase(new AbsQueryClinicReqVo(pid));
		List<ClinicBillEntity> list2 = queryClinicBill(new AbsQueryClinicReqVo(pid));
		List<ClinicPrescriptionDetailEntity> list3 = queryClinicPrescriptionDetail(new AbsQueryClinicReqVo(pid));
		List<ClinicFreeSummaryEntity> list4 = queryClinicFreeSummary(new AbsQueryClinicReqVo(pid));
		List<ClinicDiagnosisWaterEntity> list5 = queryClinicDiagnosis(new AbsQueryClinicReqVo(pid));
		List<ClinicRegWaterEntity> list6 = queryClinicRegisetrInfo(new AbsQueryClinicReqVo(pid));
		
		vo.setClinicCaseList(list);
		vo.setClinicBillList(list2);
		vo.setClinicPrescriptionDetailList(list3);
		vo.setClinicFreeSummaryList(list4);
		vo.setClinicDiagnosisWaterList(list5);
		vo.setClinicRegWaterList(list6);
		
		for (ClinicCaseEntity entity : list) {
			getEntityUtils().save2EsDB(ElasticIndex.cliniccase, entity);
		}
		for (ClinicBillEntity entity : list2) {
			getEntityUtils().save2EsDB(ElasticIndex.clinicbill, entity);
		}
		for (ClinicPrescriptionDetailEntity entity : list3) {
			getEntityUtils().save2EsDB(ElasticIndex.clinicprescriptiondetail, entity);
		}
		for (ClinicFreeSummaryEntity entity : list4) {
			getEntityUtils().save2EsDB(ElasticIndex.clinicfreesummary, entity);
		}
		for (ClinicDiagnosisWaterEntity entity : list5) {
			getEntityUtils().save2EsDB(ElasticIndex.clinicdiagnosis, entity);
		}
		for (ClinicRegWaterEntity entity : list6) {
			getEntityUtils().save2EsDB(ElasticIndex.clinicregisetrinfo, entity);
		}
		
		return vo;
	}
}
