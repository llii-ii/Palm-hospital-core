package com.kasite.client.yy.job;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.serviceinterface.module.job.dao.IJobMapper;

/**
 * 解锁表备份清理作业
 * 
 * @author mhd
 * @version 1.0 2017-7-20 上午10:54:25
 */
@Component
public class BackupUnLockJob{

	private boolean flag = true;
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	@Autowired
	IJobMapper jobMapper;

	@Autowired
	KasiteConfigMap config;
	/**
	 * 备份清理解锁表
	 * 每晚0点执行
	 */
	@Transactional
	public void backupUnLockData() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				KasiteConfig.print("执行解锁数据备份清理作业。");
				Calendar date = Calendar.getInstance();
				date.add(Calendar.MONTH, -3);
				String backTable = "YY_UNLOCK" + "_" + date.get(Calendar.YEAR);
				String dataTable = "YY_UNLOCK";
				String column = "CREATETIME";
				int month = 3;
				backData(backTable, dataTable, column, month);
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
//			LogUtil.error(log, e);
			e.printStackTrace();
		} finally {
			flag = true;
		}
	}
	
	
	public void backData(String backTable,String dataTable,String column,int month) {
		int num = jobMapper.getTable(backTable,KasiteConfig.getDataBaseName());
		if(num>0) {
			jobMapper.backData(backTable, dataTable, column, month);
		}else {
			jobMapper.createAndBackData(backTable, dataTable, column, month);
		}
		jobMapper.deleteData(dataTable, column, month);
	}
}
