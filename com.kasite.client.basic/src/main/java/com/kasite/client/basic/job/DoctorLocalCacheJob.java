package com.kasite.client.basic.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.basic.cache.IDoctorLocalCache;

/**
 * 
 * @className: DoctorLocalCacheJob
 * @author: lcz
 * @date: 2018年8月1日 下午2:19:11
 */
@Component
public class DoctorLocalCacheJob {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	@Autowired
	IDoctorLocalCache doctorLocalCache;
	private boolean flag = true;
	@Autowired
	KasiteConfigMap config;
	/**
	 * 医生缓存更新
	 * 每1小时执行一次
	 * @Description:
	 */
	public void syncSingleDoctorCache() {
		//这里捕获异常这么写，为了某些数据引起异常不影响其他数据继续新增或更新
		try {
			if (flag && config.isStartJob(DoctorLocalCacheJob.class)) {
				flag = false;
				doctorLocalCache.load();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
}
