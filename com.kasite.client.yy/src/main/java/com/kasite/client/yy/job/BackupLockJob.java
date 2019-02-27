package com.kasite.client.yy.job;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.serviceinterface.module.job.dao.IJobMapper;

/**
 * @author linjf TODO
 */
@Component
public class BackupLockJob{

	private boolean flag = true;
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	@Autowired
	IJobMapper jobMapper;

	@Autowired
	KasiteConfigMap config;
	/**
	 * 备份清理锁号
	 * 每晚0点执行
	 */
	@Transactional
	public void backupLockData() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				KasiteConfig.print("执行过期锁号数据备份清理作业。");
				Calendar date = Calendar.getInstance();
				date.add(Calendar.MONTH, -3);
				String backTable = "YY_LOCK" + "_" + date.get(Calendar.YEAR);
				String dataTable = "YY_LOCK";
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
