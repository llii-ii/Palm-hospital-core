package com.kasite.client.crawler.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.IDSeed;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.ESConfig;
import com.kasite.client.crawler.config.ElasticsearchClientConfig;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.elastic.ElasticRestClientUtil;

@Component
public class EntityUtils {
	
	@Autowired
	private ESConfig eSConfig;
	private static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);
	
	public void save2EsDB(ElasticsearchClientConfig.ElasticIndex index,String id,Object entity) throws Exception {
		JSONObject json = null;
		try { 
			if(!(entity instanceof JSONObject)) {
				json = (JSONObject) JSONObject.toJSON(entity);
			}
			json.put("inserttime", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
			if(Convent.getIsCheck()) {
				ValidatorUtils.validateEntity(entity,AddGroup.class);
			}
			
			int i = ElasticRestClientUtil.getInstall(eSConfig.getUrl())
					.insert(index.name(), json, id);
			if(i <= 0) {
				logger.error("save2EsDBError@", json); 
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("save2EsDB",e,json);
		}
	}
	public void save2EsDB(ElasticsearchClientConfig.ElasticIndex index,Object entity) throws Exception {
		String id = EntityUtils.getEntityId(entity);
		save2EsDB(index, id, entity);
	}
	
	
	/**
	 * 获取实例对象的ID
	 * @param patientEntity
	 * @return
	 */
	public static String getEntityId(Object object) {
//		if(null != object) {
//			StringBuffer ids = new StringBuffer();
//			Class<?> clazz = object.getClass();
//			Field[] fields = clazz.getDeclaredFields();
//			for (Field f : fields) {
//				EntityID eid = f.getAnnotation(EntityID.class);
//				if(null != eid) {
//					Object o = com.coreframework.util.ReflectionUtils.getFieldValue(object, f.getName());	
//					if(null != o) {
//						ids.append("_");
//						ids.append(o.toString());
//					}else {
//						throw new RRException("主键ID不能为空："+f.getName());
//					}
//				}
//			}
//			return ids.toString();
//		}
//		没有主键／生成一个UUID
		return IDSeed.next();
	}
}
