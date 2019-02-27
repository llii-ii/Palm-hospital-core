package com.kasite;

import com.kasite.client.crawler.config.RpcConfig;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileQueue;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;


public class Shutdown {
	
	public static void main(String[] args) {
		RpcConfig config = (RpcConfig) SpringContextUtils.getBean("rpcConfig");
		config.shutdown();
		//告诉所有上传队列接口暂时无法使用，需要全部转入日志后再终止
		UploadFileQueue.setStatus(false);
		try {
			while(UploadFileQueue.getQueueSize() > 0) {
				System.out.println("数据队列中还有数据在等待处理。。。请稍等。"+UploadFileQueue.getQueueSize());
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}