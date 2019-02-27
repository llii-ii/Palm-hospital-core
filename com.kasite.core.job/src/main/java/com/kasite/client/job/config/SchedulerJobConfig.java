package com.kasite.client.job.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerJobConfig {
	
	@Autowired
	@Qualifier("masterDataSource")
	DataSource dataSource;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setDataSource(dataSource);
		/***** quartz参数配置 ****/
		Properties prop = new Properties();
		// ID为自动获取 每一个必须不同
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		// 调度标识名 集群中每一个实例都必须使用相同的名称
		prop.put("org.quartz.scheduler.instanceName", "KasiteScheduler");
		
        // Quartz内置了一个“更新检查”特性，因此Quartz项目每次启动后都会检查官网，Quartz是否存在新版本。这个检查是异步的，不影响Quartz项目本身的启动和初始化。
		//设置org.quartz.scheduler.skipUpdateCheck的属性为true来跳过更新检查
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
		/***** 线程池配置 ****/
		// 线程池的实现类
		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		// 指定线程数，至少为1（无默认值）(一般设置为1-100直接的整数合适)
		prop.put("org.quartz.threadPool.threadCount", "20");
		// 设置线程的优先级（最大为java.lang.Thread.MAX_PRIORITY 10，最小为Thread.MIN_PRIORITY 1，默认为5）
		prop.put("org.quartz.threadPool.threadPriority", "5");
		/***** JobStore配置 ****/
		// 数据保存方式为数据库持久化
		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		// 是否加入集群
		prop.put("org.quartz.jobStore.isClustered", "true");
		// 调度实例失效的检查时间间隔
		prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
		// 信息保存时间 默认值60秒
		prop.put("org.quartz.jobStore.misfireThreshold", "12000");
		// 表的前缀，默认QRTZ_
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");

		factory.setQuartzProperties(prop);

		factory.setSchedulerName("KasiteScheduler");
		// 延时启动 单位秒
		factory.setStartupDelay(30);
		factory.setApplicationContextSchedulerContextKey("applicationContext");
		// 可选，QuartzScheduler
		// 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
		factory.setOverwriteExistingJobs(true);
		// 设置自动启动，默认为true
		factory.setAutoStartup(true);

		return factory;
	}
}
