package com.kasite.client.hospay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.coreframework.remoting.Server;


/**
 * 系统启动完成后执行此类的  onApplicationEvent 方法
 * @author daiyanshui
 */

@Component
@Primary
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("====================================================================================================================");
		logger.info("............................................ 载入全局配置信息........................................................");
		logger.info("====================================================================================================================");

		
		
		
		
		logger.info("====================================================================================================================");
		logger.info(".................................................系统启动完成。........................................................");
		logger.info("====================================================================================================================");
		
	}
	
	/**
	 * 启动实例服务
	 */
	public static void startInstanceServer(int port) throws Exception {
		System.out.println("启动Rpc服务,Port=" + port);
		if (port != 0) {
			Server server = Server.getInstance(port);
			server.start();
		}
		System.out.println("启动Rpc服务,完成.");
	}
	public static void main(String[] args) {
//		Scanner input = new Scanner(System.in);
//        String val = null;       // 记录输入的字符串
//        while(true) {
//        	System.out.print("你要执行什么操作：1、runCmd 2、getResult ");
//            val = input.nextLine();
//            if("1".equals(val)) {
//            	System.out.println("执行命令：runCmd");	
//            }else if("2".equals(val)){
//            	System.out.println("执行命令：getResult");	
//            }else {
//            	System.out.println("未知命令");
//            }
//        }
	}
}
