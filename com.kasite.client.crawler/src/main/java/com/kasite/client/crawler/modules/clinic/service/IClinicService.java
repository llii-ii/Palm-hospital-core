package com.kasite.client.crawler.modules.clinic.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.clinic.vo.AllClinicDataVo;

/**
 * 门诊 病人信息查询。
 * @author daiyanshui
 *
 */
public interface IClinicService {
	/**
	 * 执行一次查询  查询指定用户的所有门诊信息
	 * @param pid
	 * @param orgCode
	 * @return 
	 * @throws Exception
	 */
	AllClinicDataVo queryAll(String pid) throws Exception;
	/**
	 * 查询门诊就诊信息（挂号流水）
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicRegWaterEntity> queryClinicRegisetrInfo(AbsQueryClinicReqVo reqVo) throws Exception;
	
	/**
	 * 诊断记录
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicDiagnosisWaterEntity> queryClinicDiagnosis(AbsQueryClinicReqVo reqVo) throws Exception;
	
	/**
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicFreeSummaryEntity> queryClinicFreeSummary(AbsQueryClinicReqVo reqVo) throws Exception;
	
	
	/**
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicPrescriptionDetailEntity> queryClinicPrescriptionDetail(AbsQueryClinicReqVo reqVo) throws Exception;
	

	/**
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicBillEntity> queryClinicBill(AbsQueryClinicReqVo reqVo) throws Exception;
	

	/**
	 * @param reqVo
	 * @return
	 * @throws Exception 
	 */
	public List<ClinicCaseEntity> queryClinicCase(AbsQueryClinicReqVo reqVo) throws Exception;
	
	
	
	
	/**
	 * 查询门诊信息
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	public List<JSONObject> queryClinicData2DataCloud(AbsQueryClinicReqVo reqVo) throws Exception;
	
	
	
}
