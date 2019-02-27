//package test.com.kasite.client.crawler.checkdata;
//
//import java.util.List;
//
//import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
//import com.kasite.client.crawler.modules.ModuleManage;
//import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.QZZGHosClientConfig;
//import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.hospitalization.service.HospitalizationServiceImpl;
//import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
//import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;
//import com.kasite.client.crawler.modules.hospitalization.service.IHospitalizationService;
//import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
//import com.kasite.client.crawler.modules.utils.EntityUtils;
//
//public class HospitalitalDataCheck {
//
//	public static void main(String[] args) throws Exception {
////		QueryHospitalization();
//		QueryHospitalizationBill();
//		System.exit(-1);
//	}
//
//	public static void QueryHospitalizationPerscriptionDetail() throws Exception {
//		String pid = "19939";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalizationperscriptiondetail;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationPerscriptionDetailEntity> list = service.queryHospitalizationPerscriptionDetail(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationPerscriptionDetailEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	public static void QueryHospitalizationBill() throws Exception {
//		String pid = "34221";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalizationbill;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationBillEntity> list = service.queryHospitalizationBill(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationBillEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	public static void QueryHospitalizationSummary() throws Exception {
//		String pid = "348894";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalizationsummary;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationSummaryEntity> list = service.queryHospitalizationSummary(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationSummaryEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	public static void QueryHospitalizationFreeSummary() throws Exception {
//		String pid = "34221";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalizationfreesummary;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationFreeSummaryEntity> list = service.queryHospitalizationFreeSummary(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationFreeSummaryEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	
//	public static void QueryHospitalization() throws Exception {
//		String pid = "336132";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalization;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationEntity> list = service.queryHospitalization(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	public static void QueryHospitalizationDiagnosis() throws Exception {
//		String pid = "336132";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.hospitalizationdiagnosis;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IHospitalizationService<?> service = new HospitalizationServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<HospitalizationDiagnosisEntity> list = service.queryHospitalizationDiagnosis(new AbsQueryClinicReqVo(pid));
//		for (HospitalizationDiagnosisEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//}
