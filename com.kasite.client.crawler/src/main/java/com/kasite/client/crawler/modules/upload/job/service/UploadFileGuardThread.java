package com.kasite.client.crawler.modules.upload.job.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.client.crawler.modules.upload.job.UploadJob;

/**
 * 上传任务的守护线程 如果上传失败 这个守护线程负责重新上传。 上传的过程中并且监控上传接口的正常状态。
 * 
 * @author daiyanshui
 *
 */
public class UploadFileGuardThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ThreadUploadThread.class);

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000*10);//休眠10秒钟
				UploadJob.readLogFile();
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("守护线程发现异常.",e);
			}
		}
	}

}
