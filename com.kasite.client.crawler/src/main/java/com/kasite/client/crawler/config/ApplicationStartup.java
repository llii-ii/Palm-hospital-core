package com.kasite.client.crawler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.coreframework.remoting.Server;
import com.kasite.client.crawler.config.data.Data1_5Bus;
import com.kasite.client.crawler.config.data.DictBus1_5;
import com.kasite.client.crawler.config.data.Rule1_5Bus;
import com.kasite.client.crawler.modules.upload.job.service.ThreadUploadThread;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileGuardThread;


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
		/** 质控系统不加载标准集 */
		if (!Convent.getIsZK()) {
			Data1_5Bus.getInstall();
			Rule1_5Bus.getInstall();
			DictBus1_5.getInstall();
		}
		
//		try {
//			PropUtils.loadFromSysXml();
//			PropUtils.loadConfig(SysConfig.getCenterServerUrl(), SysConfig.getAppId(), SysConfig.getPort());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("载入全局配置信息异常",e);
//		}
//		
		if(ConfigBuser.create().getRpcConfigVo().isStartRpc()){
			logger.info("====================================================================================================================");
			logger.info("............................................启动 RPC 服务。........................................................");
			logger.info("====================================================================================================================");
			try {
				int port = ConfigBuser.create().getRpcConfigVo().getPort();
				startInstanceServer(port);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("启动 RPC 服务 异常",e);
			}
		}
		
		if (Convent.getIsStartJob()) {
			int size = Convent.getFileUploadThreadSize();
			logger.info("====================================================================================================================");
			logger.info(".........................................启动完成云平台上传线程 线程数:“"+ size +"”........................................");
			logger.info("====================================================================================================================");
		
			for (int i = 0; i < size; i++) { 
				String name = "ThreadUploadThread_"+i;
				Thread t = new Thread(new ThreadUploadThread(name));
				t.setName(name);
				t.start();
			}
			
			Thread guardThread = new Thread(new UploadFileGuardThread());
			guardThread.setDaemon(false);
			guardThread.setName("UploadThreadGuardThread");
			guardThread.start();
		}
		logger.info("====================================================================================================================");
		logger.info(".................................................系统启动完成。........................................................");
		logger.info("====================================================================================================================");
		
	}
	
	/**
	 * 启动实例服务
	 */
	private static void startInstanceServer(int port) throws Exception {
		System.out.println("启动Rpc服务,Port=" + port);
		if (port != 0) {
			Server server = Server.getInstance(port);
			server.start();
		}
		System.out.println("启动Rpc服务,完成.");
	}
}
