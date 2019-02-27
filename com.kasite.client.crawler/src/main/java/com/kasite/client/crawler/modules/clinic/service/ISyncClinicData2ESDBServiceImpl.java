package com.kasite.client.crawler.modules.clinic.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.ModuleManage;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.utils.EntityUtils;
@Component
public class ISyncClinicData2ESDBServiceImpl implements ISyncClinicData2ESDBService{
	private static final Logger logger = LoggerFactory.getLogger(ISyncClinicData2ESDBServiceImpl.class);
	
	@Autowired
	private EntityUtils entityUtils;
	
	@Override
	public void sync(String hosKey,PatientEntity vo) throws Exception {
		try {
			IClinicService service = ModuleManage.getInstance().getClinicService(hosKey);
			List<ClinicRegWaterEntity> list = service.queryClinicRegisetrInfo(new AbsQueryClinicReqVo(vo));//(vo);
			for (ClinicRegWaterEntity entity : list) {
				try {
					entityUtils.save2EsDB(ElasticIndex.clinicregisetrinfo, entity);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("新增门诊就诊信息出现异常：",e,entity);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("查询门诊就诊信息出现异常：",e);
		}
		
		
		try {
			IClinicService service = ModuleManage.getInstance().getClinicService(hosKey);
			List<ClinicDiagnosisWaterEntity> list = service.queryClinicDiagnosis(new AbsQueryClinicReqVo(vo));//(vo);
			for (ClinicDiagnosisWaterEntity entity : list) {
				try {
					entityUtils.save2EsDB(ElasticIndex.clinicdiagnosis, entity);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("新增门诊就诊信息出现异常：",e,entity);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("查询门诊就诊信息出现异常：",e);
		}
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		
		
	}


}
