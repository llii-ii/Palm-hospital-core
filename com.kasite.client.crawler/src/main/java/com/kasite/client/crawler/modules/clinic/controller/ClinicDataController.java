package com.kasite.client.crawler.modules.clinic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.client.crawler.common.controller.AbstractController;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.ModuleManage;
import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.service.IClinicService;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.core.common.util.R;

@RestController("/api/clinic")
public class ClinicDataController extends AbstractController{

	@Autowired
	private EntityUtils entityUtils;
	/**注入机构代码*/
	public ClinicDataController() {
		
	}
	
	public IClinicService getService(String orgCode) {
		return ModuleManage.getInstance().getClinicService(orgCode);
	}
	@PostMapping("/test")
	R test(String orgCode) throws Exception {
		
		for (int i = 10000; i < 100000; i++) {
			String pid = i+"";
			clinicbill(pid, orgCode);
			cliniccase(pid, orgCode);
			clinicdiagnosis(pid, orgCode);
			clinicfreesummary(pid, orgCode);
			clinicprescriptiondetail(pid, orgCode);
			clinicregisetrinfo(pid, orgCode);
		}
		
		return R.ok();
	}
	
	@PostMapping("/cliniccase")
	R cliniccase(String pid,String orgCode) throws Exception {
		List<ClinicCaseEntity> list = getService(orgCode).queryClinicCase(new AbsQueryClinicReqVo(pid));
		ElasticIndex index = ElasticIndex.cliniccase;
		for (ClinicCaseEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	

	@PostMapping("/clinicbill")
	R clinicbill(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.clinicbill;
		List<ClinicBillEntity> list = getService(orgCode).queryClinicBill(new AbsQueryClinicReqVo(pid));
		for (ClinicBillEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/clinicprescriptiondetail")
	R clinicprescriptiondetail(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.clinicprescriptiondetail;
		List<ClinicPrescriptionDetailEntity> list = getService(orgCode).queryClinicPrescriptionDetail(new AbsQueryClinicReqVo(pid));
		for (ClinicPrescriptionDetailEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/clinicfreesummary")
	R clinicfreesummary(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.clinicfreesummary;
		List<ClinicFreeSummaryEntity> list = getService(orgCode).queryClinicFreeSummary(new AbsQueryClinicReqVo(pid));
		for (ClinicFreeSummaryEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}

	@PostMapping("/clinicdiagnosis")
	R clinicdiagnosis(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.clinicdiagnosis;
		List<ClinicDiagnosisWaterEntity> list = getService(orgCode).queryClinicDiagnosis(new AbsQueryClinicReqVo(pid));
		for (ClinicDiagnosisWaterEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/clinicregisetrinfo")
	R clinicregisetrinfo(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.clinicregisetrinfo;
		List<ClinicRegWaterEntity> list = getService(orgCode).queryClinicRegisetrInfo(new AbsQueryClinicReqVo(pid));
		for (ClinicRegWaterEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	
	
}



//@RestController
//@PostMapping("/api/init")
//public class TestInit {
//	@PostMapping("/test")
//    @ResponseBody
//    R init() {
//		R r = new R();
//		r.put("msg", "测试成功。");
//		return r; 
//    }
//}
