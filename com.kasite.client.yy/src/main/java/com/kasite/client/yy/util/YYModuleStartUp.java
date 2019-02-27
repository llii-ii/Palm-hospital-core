package com.kasite.client.yy.util;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.IApplicationStartUp;

/**
 * @author daiys
 * 预约挂号模块启动线程 注入需要执行的插件模块
 */
@Component
public class YYModuleStartUp implements IApplicationStartUp{
	
	@Override
	public void init(ContextRefreshedEvent event) {
		
		
	}

}
