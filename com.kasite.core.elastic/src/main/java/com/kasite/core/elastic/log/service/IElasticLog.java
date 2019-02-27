package com.kasite.core.elastic.log.service;

import com.kasite.core.elastic.log.bo.EsLog;

/**
 * 
 * @className: IElasticLog
 * @author: lcz
 * @date: 2018年5月28日 下午8:20:14
 */
public interface IElasticLog {
	
	int saveEsLog(String ips,EsLog log) throws Exception;
	
}
