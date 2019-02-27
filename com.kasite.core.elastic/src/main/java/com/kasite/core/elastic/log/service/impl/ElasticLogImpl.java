package com.kasite.core.elastic.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.coreframework.util.DateOper;
import com.kasite.core.elastic.ElasticRestClientUtil;
import com.kasite.core.elastic.log.bo.EsLog;
import com.kasite.core.elastic.log.enums.MethodEnums;
import com.kasite.core.elastic.log.service.IElasticLog;
import com.kasite.core.elastic.log.util.EsMappingUtils;

/**
 * 
 * @className: ElasticLogImpl
 * @author: lcz
 * @date: 2018年5月28日 下午8:20:33
 */
public class ElasticLogImpl implements IElasticLog{

	private static boolean isExists = false;
	
	@Override
	public int saveEsLog(String ips,EsLog log) throws Exception {
		String month = DateOper.getNow("MM");
		String index = "log"+month;
		if(!isExists) {
			boolean isExts = ElasticRestClientUtil.getInstall(ips).isExists(index);
			if(!isExts) {
				//创建索引
				String params = EsMappingUtils.getIndexParams("1","1",EsLog.class).toString();
				boolean b = ElasticRestClientUtil.getInstall(ips).createIndex(index, params);
				if(b) {
					isExists = true;
				}
			}
		}
		return ElasticRestClientUtil.getInstall(ips).insert(index, JSON.parseObject(JSON.toJSONString(log)), log.getLogId());
	}
	

	/**
	 * 获取uuid值
	 */
	public static String getUUID() {
		return (IDSeed.next());
	}
	public static void main(String[] args) throws Exception {
		EsLog log = new EsLog();
		log.setLogId(getUUID());
		log.setMethodName(MethodEnums.queryBaseDept);
		log.setOperTime(DateOper.getNowDateTime());
		log.setOrderId(getUUID());
		log.setParams("{'HospitalId':'10123'}");
		log.setResults("[{'DeptCode':'10001','DeptName':'测试科室1'},{'DeptCode':'10002','DeptName':'测试科室2'}]");
//		int ss = new ElasticLogImpl().saveEsLog("127.0.0.1:9200", log);
		System.out.println(JSON.toJSONString(log));
//		System.out.println(ss);
	}
}
