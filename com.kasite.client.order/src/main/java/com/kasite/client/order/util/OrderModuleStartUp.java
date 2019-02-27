package com.kasite.client.order.util;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.IApplicationStartUp;

/**
 * @author linjf
 * TODO 订单模块组件初始化管理
 */
@Component
public class OrderModuleStartUp implements IApplicationStartUp{

	/**
	 * 初始化注入组件部分
	 */
	@Override
	public void init(ContextRefreshedEvent event) {
		
	}

}
