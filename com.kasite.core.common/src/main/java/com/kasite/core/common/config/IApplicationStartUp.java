package com.kasite.core.common.config;

import org.springframework.context.event.ContextRefreshedEvent;

public interface IApplicationStartUp {
	/**
	 * 系统启动完后需要马上执行的任务／线程／  继承实现这里接口系统启动的时候会默认执行这个方法。外部不捕获异常，异常内部自行消化
	 */
	void init(ContextRefreshedEvent event);
}
