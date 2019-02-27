package com.kasite.client.crawler.modules.patient.service;

import java.util.List;

import com.common.json.JSONObject;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;

/**
 * 人口信息学基础信息同步。
 * @author daiyanshui
 */
public interface IPersionInfoService {
	
	
	public PatientEntity getPatientEntity(String patientId);
	/**
	 * 查询人口信息学并保存到es数据库中
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	public List<PatientEntity> queryPatientList2EsDB(ReqQueryPatientListVo reqVo) throws Exception;
	
	/**
	 * 查询his库或者his接口中的人口信息学信息
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	public List<PatientEntity> queryPatientEntityList(ReqQueryPatientListVo reqVo) throws Exception;
	
	/***
	 * 查询人口信息学信息 返回json格式内容。  
	 * 直连数据库查询
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	List<JSONObject> queryPersionJsonList(ReqQueryPatientListVo reqVo) throws Exception;
	
}
