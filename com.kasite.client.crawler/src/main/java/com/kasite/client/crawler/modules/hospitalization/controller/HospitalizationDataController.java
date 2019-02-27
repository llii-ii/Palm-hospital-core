package com.kasite.client.crawler.modules.hospitalization.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.client.crawler.common.controller.AbstractController;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.ModuleManage;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.service.IHospitalizationService;
import com.kasite.client.crawler.modules.hospitalization.vo.QueryHospitalizationReqVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.core.common.util.R;

@RestController("/api/hospitalization")
public class HospitalizationDataController extends AbstractController{

	@Autowired
	private HosClientConfig hosClientConfig;
	@Autowired
	private EntityUtils entityUtils;
	
	public IHospitalizationService getService() {
		return ModuleManage.getInstance().getHospitalizationService(hosClientConfig);
	}
	
	@PostMapping("/hospitalization")
	R hospitalization(String pid,String orgCode) throws Exception {
		List<HospitalizationEntity> list = getService().queryHospitalization(new QueryHospitalizationReqVo(pid));
		ElasticIndex index = ElasticIndex.hospitalization;
		for (HospitalizationEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	

	@PostMapping("/hospitalizationbill")
	R hospitalizationbill(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.hospitalizationbill;
		List<HospitalizationBillEntity> list = getService().queryHospitalizationBill(new QueryHospitalizationReqVo(pid));
		for (HospitalizationBillEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/hospitalizationdiagnosis")
	R hospitalizationdiagnosis(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.hospitalizationdiagnosis;
		List<HospitalizationDiagnosisEntity> list = getService().queryHospitalizationDiagnosis(new QueryHospitalizationReqVo(pid));
		for (HospitalizationDiagnosisEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/hospitalizationfreesummary")
	R hospitalizationfreesummary(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.hospitalizationfreesummary;
		List<HospitalizationFreeSummaryEntity> list = getService().queryHospitalizationFreeSummary(new QueryHospitalizationReqVo(pid));
		for (HospitalizationFreeSummaryEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}

	@PostMapping("/hospitalizationperscriptiondetail")
	R hospitalizationperscriptiondetail(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.hospitalizationperscriptiondetail;
		List<HospitalizationPerscriptionDetailEntity> list = getService().queryHospitalizationPerscriptionDetail(new QueryHospitalizationReqVo(pid));
		for (HospitalizationPerscriptionDetailEntity entity : list) {
			entityUtils.save2EsDB(index, entity);
		}
		return R.ok(list);
	}
	
	@PostMapping("/hospitalizationsummary")
	R hospitalizationsummary(String pid,String orgCode) throws Exception {
		ElasticIndex index = ElasticIndex.hospitalizationsummary;
		List<HospitalizationSummaryEntity> list = getService().queryHospitalizationSummary(new QueryHospitalizationReqVo(pid));
		for (HospitalizationSummaryEntity entity : list) {
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
