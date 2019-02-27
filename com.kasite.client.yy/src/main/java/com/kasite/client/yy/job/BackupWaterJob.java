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
 * 预约流水表备份清理作业
 * 
 * @author mhd
 * @version 1.0 2017-7-20 上午10:58:08
 */
@Component
public class BackupWaterJob{

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
	public void backupWaterData() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				KasiteConfig.print("执行预约流水表数据备份清理作业。");
				Calendar date = Calendar.getInstance();
				date.add(Calendar.MONTH, -12);
				String backTable = "YY_WATER" + "_" + date.get(Calendar.YEAR);
				String dataTable = "YY_WATER";
				String column = "CREATETIME";
				int month = 12;
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
