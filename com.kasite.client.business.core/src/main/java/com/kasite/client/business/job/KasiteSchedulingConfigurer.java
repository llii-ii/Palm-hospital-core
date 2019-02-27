package com.kasite.client.business.job;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author linjf
 * TODO
 */
@Configuration
@EnableScheduling // 定时任务注解
public class KasiteSchedulingConfigurer implements SchedulingConfigurer {

	/**
	 * @param taskRegistrar
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		 taskRegistrar.setScheduler(taskExecutor());
	}

//	@Bean(destroyMethod = "shutdown")
//	public Executor taskExecutor() {
//		return new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
//			private AtomicInteger max = new AtomicInteger(0);
//
//			@Override
//			public Thread newThread(Runnable r) {
//				return new Thread(r, "KasiteScheConfig-" + max.incrementAndGet());
//			}
//		});
//	}
	
	@Bean(destroyMethod="shutdown")
	public Executor taskExecutor() {
		//初始化一个10核心线程的，定时器线程池
	    return Executors.newScheduledThreadPool(10);
	}
}
