package com.kasite.client.crawler.modules.clinic.service;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;

/**
 * 同步门诊信息数据到 ES数据库
 * @author daiyanshui
 *
 */
public interface ISyncClinicData2ESDBService {
	void sync(String hosKey, PatientEntity vo) throws Exception;
}
