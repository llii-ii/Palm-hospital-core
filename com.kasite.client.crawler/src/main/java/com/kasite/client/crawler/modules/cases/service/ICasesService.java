package com.kasite.client.crawler.modules.cases.service;

import java.util.List;

import com.kasite.client.crawler.modules.cases.entity.QueryCheckEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryCheckItemEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryDiseaseEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryHospitalRecordEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryOperationEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryReportEntity;
import com.kasite.client.crawler.modules.cases.vo.ReqQueryCasesVo;

/**
 * 
 * @className: ICasesService
 * @author: lcz
 * @date: 2018年6月11日 下午7:26:00
 */
public interface ICasesService {
	
	
	List<QueryHospitalRecordEntity> queryHospitalRecordList(ReqQueryCasesVo reqVo) throws Exception;
	List<QueryDiseaseEntity> queryDiseaseList(ReqQueryCasesVo reqVo) throws Exception;
	List<QueryOperationEntity> queryOperationList(ReqQueryCasesVo reqVo) throws Exception;
	List<QueryReportEntity> queryReportList(ReqQueryCasesVo reqVo) throws Exception;
	List<QueryCheckEntity> queryCheckList(ReqQueryCasesVo reqVo) throws Exception;
	List<QueryCheckItemEntity> queryCheckItemList(ReqQueryCasesVo reqVo) throws Exception;
	
	
	
}
