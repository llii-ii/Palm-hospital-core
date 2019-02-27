package com.kasite.client.crawler.modules.upload.job.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.kasite.client.crawler.config.Convent;

public class UploadFileQueue {
	private static BlockingQueue<UploadFileVo> fileQueue = new LinkedBlockingQueue<>(Convent.getFileUploadThreadSize()+50);
	private static boolean uploadIsOk = false;
	/**
	 * 获取上传接口的状态，是否可以正常上传。
	 * @return
	 */
	public static boolean getStatus() { 
		return uploadIsOk;
	}
	//如果状态不对 将状态改成暂时无法上传。另外启动一个守护线程即可不用10个线程一直跑没有意义
	public static synchronized void setStatus(boolean status) {
		uploadIsOk = status;
	}
	public static boolean addFile(UploadFileVo file) {
		return fileQueue.offer(file);
	}
	
	public static UploadFileVo takeFile() throws Exception {
		return fileQueue.take();
	}
	
	public static int getQueueSize() {
		return fileQueue.size();
	}
}
