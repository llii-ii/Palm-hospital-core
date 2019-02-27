package com.kasite.client.crawler.modules.patient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.common.json.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.ESConfig;
import com.kasite.client.crawler.config.ElasticsearchClientConfig;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.client.AbsCommonClientCallService;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.core.elastic.ElasticBus;
import com.kasite.core.elastic.ElasticRestClientBus;
import com.kasite.core.elastic.ElasticRestClientBusSender;
import com.kasite.core.elastic.ElasticRestResponse;
import com.kasite.core.elastic.ElasticRestTypeEnum;

/**
 * 人口信息学查询抽象接口。
 * 公共部分实现由此对象进行封装。
 * @author daiyanshui
 *
 */
public abstract class AbsPatientInfoService extends AbsCommonClientCallService implements IPersionInfoService{
	
	
	public abstract EntityUtils getEntityUtils();
	
	public abstract ESConfig getESConfig();
	
	private ElasticIndex index = ElasticsearchClientConfig.ElasticIndex.persion;
	
	@Override
	public List<PatientEntity> queryPatientEntityList(ReqQueryPatientListVo reqVo) throws DocumentException, Exception {
		return null;
	}

	@Override
	public List<JSONObject> queryPersionJsonList(ReqQueryPatientListVo reqVo) throws Exception {
		return null;
	}

	/**
	 * 是否必填在对象中直接用注解的方式定义。
	 * @param value
	 * @return
	 */
	public static String getReqParamString(String value) {
		if(StringUtil.isBlank(value)) {
			return new String();
		}
		return value;
	}
	
	public PatientEntity getPatientEntity(String patientId) {
		List<PatientEntity> retVal = new ArrayList<>(1);
		Map<String, Object> _search = ElasticBus.create()
				.setSize(10)
				.createSimpleQuery()
		.createElasticQuerySimpleBool()
		.addMustTerm("_id", patientId)
		.getSearch();
		try{
			String ips = getESConfig().getUrl();
			ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
			.set_search(_search);
			ElasticRestResponse resp = sender.simpleQuery(index.name());
			resp.setParse(new IElasticQueryResponseScrollParseImpl<PatientEntity>(retVal,PatientEntity.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		if(retVal.size() > 0) {
			return retVal.get(0);
		}else {
			return null;
		}
	}
	
	
	@Override
	public List<PatientEntity> queryPatientList2EsDB(ReqQueryPatientListVo reqVo) throws Exception {
		List<PatientEntity> list = queryPatientEntityList(reqVo);
		for (PatientEntity entity : list) {
			getEntityUtils().save2EsDB(index, entity);
		}
		return list;
	}
}
