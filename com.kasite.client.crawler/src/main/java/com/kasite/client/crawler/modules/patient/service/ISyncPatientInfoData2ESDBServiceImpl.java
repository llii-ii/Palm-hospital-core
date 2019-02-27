package com.kasite.client.crawler.modules.patient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.ModuleManage;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
@Service
public class ISyncPatientInfoData2ESDBServiceImpl implements ISyncPatintInfoData2ESDBService{
	
	@Autowired
	private EntityUtils entityUtils;
	
	@Override
	public void sync(ReqQueryPatientListVo vo) throws Exception {
		IPersionInfoService service = ModuleManage.getInstance().getPersionInfoService(vo.getGetHosKey());
		List<PatientEntity> list = service.queryPatientEntityList(vo);
		for (PatientEntity entity : list) {
			entityUtils.save2EsDB(ElasticIndex.persion, entity);
		}
	}


//	
//	public static void main(String[] args) throws Exception {
////		ReqQueryPatientListVo vo = new ReqQueryPatientListVo();
////		vo.setPageIndex(1);
////		vo.setPageSize(10);
////		vo.setIdCardId("350500550419151");
////		ISyncPatientInfoData2ESDBServiceImpl imp  = new ISyncPatientInfoData2ESDBServiceImpl();
////		imp.sync(vo);
//		String tableName = "persion";
//		Map<String, Object> _search = ElasticBus.create()
//				.setSize(10)
//				.createSimpleQuery()
//		.createElasticQuerySimpleBool()
////		.addMustTerm("_id", "36543")
//		.getSearch();
//		System.out.println(JSONObject.toJSON(_search).toString());
//		try{
//			String ips = eSCon
//			ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
//			.set_search(_search);
//			ElasticRestResponse resp = sender.simpleQuery(tableName);
//			resp.setParse(new IElasticQueryResponseScrollParse() {
//				@Override
//				public void parse(ElasticQueryResponse response,
//						ElasticQueryResponseHits_Hits hist, String _source,int index) {
//					String id = hist.get_id();
//					System.out.println("total="+ response.getHits().getTotal() +"id="+id +" data = [ "+_source +" ]");
////					try {
////						Thread.sleep(100);
////						ElasticRestClientBus.create(ips,ElasticRestTypeEnum.DELETE).setIndex(tableName).delete(id);
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
//				}
//			});
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.exit(0);
//		
//	}
	
}
