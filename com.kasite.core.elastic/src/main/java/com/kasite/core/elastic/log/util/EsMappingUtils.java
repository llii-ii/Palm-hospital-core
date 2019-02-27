package com.kasite.core.elastic.log.util;

import java.lang.reflect.Field;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.elastic.log.bo.EsLog;

/**
 * 
 * @className: EsMappingUtils
 * @author: lcz
 * @date: 2018年5月29日 上午9:29:12
 */
public class EsMappingUtils {
	
	
	public static JSONObject getIndexParams(String numberOfShards, String numberOfReplicas,Class<?> clazz) {
		JSONObject params = new JSONObject();
		params.put("settings", getIndexSettings(numberOfShards, numberOfReplicas));
		params.put("mappings", getIndexMappings(clazz));
		return params;
	}
	
	public static JSONObject getIndexMappings(Class<?> clazz) {
		JSONObject logtable = new JSONObject();
		logtable.put("dynamic", "true");
		JSONObject all = new JSONObject();
		all.put("enabled", false);
		logtable.put("_all", all);
		logtable.put("properties", getIndexProperties(clazz));
		
		JSONObject mappings = new JSONObject();
		mappings.put("logtable", logtable);
		return mappings;
	}
	public static JSONObject getIndexSettings(String numberOfShards,String numberOfReplicas) {
		JSONObject settings = new JSONObject();
		JSONObject index = new JSONObject();
		index.put("number_of_shards", numberOfShards);
		index.put("number_of_replicas", numberOfReplicas);
		settings.put("index", index);
		return settings;
	}
	public static JSONObject getIndexProperties(Class<?> clazz) {
		JSONObject properties = new JSONObject();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if("serialVersionUID".equals(field.getName())) {
				continue;
			}
			JSONObject key = new JSONObject();
			if(field.getType().isAssignableFrom(Date.class)
					|| field.getType().isAssignableFrom(java.sql.Date.class)
					|| field.getType().isAssignableFrom(java.sql.Timestamp.class)) {
				key.put("format", "YYYY-MM-dd HH:mm:ss");
				key.put("type", "date");
			}else{
				key.put("type", "keyword");
			}
			properties.put(field.getName(), key);
		}
		return properties;
	}
	
	public static void main(String[] args) {
		JSONObject json = getIndexParams("1","1",EsLog.class);
		System.out.println(json);
	}
}
