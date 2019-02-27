package test.com.kasite.client.crawler.checkdata;

import java.util.Map;

import com.coreframework.util.AppConfig;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.core.elastic.ElasticBus;
import com.kasite.core.elastic.ElasticQueryResponse;
import com.kasite.core.elastic.ElasticQueryResponseHits_Hits;
import com.kasite.core.elastic.ElasticRestClientBus;
import com.kasite.core.elastic.ElasticRestClientBusSender;
import com.kasite.core.elastic.ElasticRestResponse;
import com.kasite.core.elastic.ElasticRestTypeEnum;
import com.kasite.core.elastic.IElasticQueryResponseScrollParse;

public class ClinicDataCheck {

	public static void main(String[] args) throws Exception {
//		checkClinicCase();
//		System.exit(-1);
//	}
//
//	
//	
//	public static void checkClinicCase() throws Exception {
//		String pid = "936412";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.cliniccase;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicCaseEntity> list = service.queryClinicCase(new AbsQueryClinicReqVo(pid));
//		for (ClinicCaseEntity entity : list) {
//			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	
//	public static void checkClinicBill() throws Exception {
//		String pid = "932621";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.clinicbill;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicBillEntity> list = service.queryClinicBill(new AbsQueryClinicReqVo(pid));
//		for (ClinicBillEntity entity : list) {
////			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	public static void checkClinicPrescriptionDetail() throws Exception {
//		String pid = "710955";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.clinicprescriptiondetail;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicPrescriptionDetailEntity> list = service.queryClinicPrescriptionDetail(new AbsQueryClinicReqVo(pid));
//		for (ClinicPrescriptionDetailEntity entity : list) {
////			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	public static void checkClinicFreeSummary() throws Exception {
//		String pid = "301592";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.clinicfreesummary;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicFreeSummaryEntity> list = service.queryClinicFreeSummary(new AbsQueryClinicReqVo(pid));
//		for (ClinicFreeSummaryEntity entity : list) {
////			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	
//	public static void checkClinicDiagnosis() throws Exception {
//		
//		String pid = "710955";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.clinicdiagnosis;
//		
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicDiagnosisWaterEntity> list = service.queryClinicDiagnosis(new AbsQueryClinicReqVo(pid));
//		for (ClinicDiagnosisWaterEntity entity : list) {
////			EntityUtils.save2EsDB(index, entity);
//		}
//		
//	}
//	
//	public static void checkClinicRegWaterEntity() throws Exception {
//		String pid = "710955";
//		PatientEntity patient = null;
//		ElasticIndex index = ElasticIndex.clinicregisetrinfo;
//		QZZGHosClientConfig config = new QZZGHosClientConfig();
//		IClinicService service = new ClinicServiceImpl(new QZZGHosClientConfig());
//		patient = ModuleManage.getInstance().getPersionInfoService(config).getPatientEntity(pid);
//		List<ClinicRegWaterEntity> list = service.queryClinicRegisetrInfo(new AbsQueryClinicReqVo(pid));
//		for (ClinicRegWaterEntity entity : list) {
//			String clinicNum = entity.getMedicalNum();
//			entity.setClinicNum(clinicNum);
////			EntityUtils.save2EsDB(index, entity);
//		}
		ElasticIndex[] indexs = ElasticIndex.values();
		String esUrl = "127.0.0.1:9200";
		Map<String, Object> _search = ElasticBus.create()
				.setSize(20)
				.createSimpleQuery()
		.createElasticQuerySimpleBool()
//		.addMustTerm("_id", "36543")
		.getSearch();
		try{
			for (ElasticIndex index : indexs) {
				String ips = esUrl;
				ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
				.set_search(_search);
				ElasticRestResponse resp = sender.simpleQuery(index.name());
				resp.setParse(new IElasticQueryResponseScrollParse() {
					@Override
					public void parse(ElasticQueryResponse response,
							ElasticQueryResponseHits_Hits hist, String _source,int i) {
						String id = hist.get_id();
						System.out.println("total="+ response.getHits().getTotal() +"id="+id +" data = [ "+_source +" ]");
					}
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(01);
	}
	
}
