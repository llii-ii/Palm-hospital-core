package com.kasite.client.crawler.modules.hospitalization.service;

import java.util.List;

import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.vo.AllHospitalizationDataVo;
import com.kasite.client.crawler.modules.hospitalization.vo.QueryHospitalizationReqVo;

/**
 * 住院相关信息接口定义
 * @author daiyanshui
 *
 */
public interface IHospitalizationService {

	public AllHospitalizationDataVo queryAll2EsDB(String pid)throws Exception;;
	
	/**
	 * 住院-入院记录
	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationEntity> queryHospitalization(QueryHospitalizationReqVo reqVo) throws Exception;
	
	/**
	住院-诊断记录(QueryHospitalizationDiagnosis)

	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationDiagnosisEntity> queryHospitalizationDiagnosis(QueryHospitalizationReqVo reqVo) throws Exception;
	/**
	 * 住院-费用汇总(QueryHospitalizationFreeSummary)
	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationFreeSummaryEntity> queryHospitalizationFreeSummary(QueryHospitalizationReqVo reqVo) throws Exception;
	/**
		住院-费用清单(QueryHospitalizationPerscriptionDetail)
	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationPerscriptionDetailEntity> queryHospitalizationPerscriptionDetail(QueryHospitalizationReqVo reqVo) throws Exception;
	/**
	 * 住院-票据信息(QueryHospitalizationBill)
	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationBillEntity> queryHospitalizationBill(QueryHospitalizationReqVo reqVo) throws Exception;
	/**
	 * 住院-出院小结(QueryHospitalizationSummary)
	 * @param reqVo
	 * @return
	 */
	public List<HospitalizationSummaryEntity> queryHospitalizationSummary(QueryHospitalizationReqVo reqVo) throws Exception;
	
}
