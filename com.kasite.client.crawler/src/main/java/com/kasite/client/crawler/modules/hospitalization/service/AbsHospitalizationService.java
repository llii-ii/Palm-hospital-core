package com.kasite.client.crawler.modules.hospitalization.service;

import java.util.List;

import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.client.AbsCommonClientCallService;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.vo.AllHospitalizationDataVo;
import com.kasite.client.crawler.modules.hospitalization.vo.QueryHospitalizationReqVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;

/**
 * 住院相关信息公共模块抽象部分
 * @author daiyanshui
 *
 * @param <T>
 */
public abstract class AbsHospitalizationService extends AbsCommonClientCallService  implements IHospitalizationService{
	
	public abstract EntityUtils getEntityUtils();
	
	@Override
	public AllHospitalizationDataVo queryAll2EsDB(String pid) throws Exception {
		List<HospitalizationEntity> list = queryHospitalization(new QueryHospitalizationReqVo(pid));
		ElasticIndex index = ElasticIndex.hospitalization;
		for (HospitalizationEntity entity : list) {
			getEntityUtils().save2EsDB(index, entity);
		}
		index = ElasticIndex.hospitalizationbill;
		List<HospitalizationBillEntity> list2 = queryHospitalizationBill(new QueryHospitalizationReqVo(pid));
		for (HospitalizationBillEntity entity : list2) {
			getEntityUtils().save2EsDB(index, entity);
		}
		index = ElasticIndex.hospitalizationdiagnosis;
		List<HospitalizationDiagnosisEntity> list3 = queryHospitalizationDiagnosis(new QueryHospitalizationReqVo(pid));
		for (HospitalizationDiagnosisEntity entity : list3) {
			getEntityUtils().save2EsDB(index, entity);
		}
		index = ElasticIndex.hospitalizationfreesummary;
		List<HospitalizationFreeSummaryEntity> list4 = queryHospitalizationFreeSummary(new QueryHospitalizationReqVo(pid));
		for (HospitalizationFreeSummaryEntity entity : list4) {
			getEntityUtils().save2EsDB(index, entity);
		}
		index = ElasticIndex.hospitalizationperscriptiondetail;
		List<HospitalizationPerscriptionDetailEntity> list5 = queryHospitalizationPerscriptionDetail(new QueryHospitalizationReqVo(pid));
		for (HospitalizationPerscriptionDetailEntity entity : list5) {
			getEntityUtils().save2EsDB(index, entity);
		}
		index = ElasticIndex.hospitalizationsummary;
		List<HospitalizationSummaryEntity> list6 = queryHospitalizationSummary(new QueryHospitalizationReqVo(pid));
		for (HospitalizationSummaryEntity entity : list6) {
			getEntityUtils().save2EsDB(index, entity);
		}
		AllHospitalizationDataVo vo = new AllHospitalizationDataVo();
		vo.setHospitalizationBillList(list2);
		vo.setHospitalizationDiagnosisList(list3);
		vo.setHospitalizationFreeSummaryList(list4);
		vo.setHospitalizationList(list);
		vo.setHospitalizationPerscriptionDetailList(list5);
		vo.setHospitalizationSummaryList(list6);
		return vo;
	}
	
}
