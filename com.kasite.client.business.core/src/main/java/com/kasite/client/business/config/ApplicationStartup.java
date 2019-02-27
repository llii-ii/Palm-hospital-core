package com.kasite.client.business.config;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.IApplicationStartUp;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;


/**
 * 系统启动完成后执行此类的  onApplicationEvent 方法
 * @author daiyanshui
 */

@Component
@Primary
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		logger.info("====================================================================================================================");
		logger.info("............................................ 载入全局配置信息........................................................");
		logger.info("====================================================================================================================");
		ApiList.me();
		Collection<IApplicationStartUp> list=new LinkedList<>(event.getApplicationContext().getBeansOfType(IApplicationStartUp.class).values());
        for (IApplicationStartUp obj :list){
        	try {
        		logger.info("执行初始化任务："+ obj.getClass().getName());
        		obj.init(event);
        	}catch (Exception e) {
        		e.printStackTrace();
			}
        }

        //TODO 支付完成订单业务回调接口注入
        //定义一个业务完成接口，只要有完成业务的都注入进去然后支付完成的时候就统一回调对应的业务逻辑
        //这类业务逻辑不允许抛出异常，至少返回状态失败或成功，并且业务失败的时候会做重试 3此后不再重试 订单状态变更为业务执行失败，支付完成：--》异常订单需要人工介入核实
        logger.info("加载注入各类插件 start");
        HandlerBuilder.get().init(event);
        logger.info("加载注入各类插件 end");
        
		logger.info("====================================================================================================================");
		logger.info(".................................................系统启动完成。........................................................");
		logger.info("====================================================================================================================");
		
	}
	
	public static void main(String[] args) {
//		Scanner input = new Scanner(System.in);
//        String val = null;       // 记录输入的字符串
//        while(true) {
//        	System.out.print("你要执行什么操作：1、runCmd 2、getResult ");
//            val = input.nextLine();
//            if("1".equals(val)) {
//            	KasiteConfig.print("执行命令：runCmd");	
//            }else if("2".equals(val)){
//            	KasiteConfig.print("执行命令：getResult");	
//            }else {
//            	KasiteConfig.print("未知命令");
//            }
//        }
	}
}
