package com.kasite.client.crawler.config;

import com.coreframework.db.DatabaseEnum;

public enum MyDatabaseEnum implements DatabaseEnum{
	/** HIS*/
	hisdb,
	/** 数据采集质控系统 */
	crawler_zk,
	/** emr 心电图*/
	his_ecg,
	/** lis */
	lis,

}