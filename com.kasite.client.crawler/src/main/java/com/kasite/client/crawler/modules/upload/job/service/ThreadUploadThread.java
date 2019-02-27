package com.kasite.client.crawler.modules.upload.job.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.modules.utils.Uploadutil;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.httpclient.http.StringUtils;

public class ThreadUploadThread implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(ThreadUploadThread.class);
	
	private String name;
	public ThreadUploadThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if(UploadFileQueue.getStatus()) {
					UploadFileVo fileVo = UploadFileQueue.takeFile();
					File file = fileVo.getFile();
					if(file.exists() && file.isFile()) {
						String password = fileVo.getPassword();
						String md5 = fileVo.getMd5();
						if(StringUtils.isBlank(md5)) {
							md5 = Uploadutil.getFileMd5(file);
						}
						boolean isZkPackage = fileVo.isZkPackage();
						boolean isSuccess = false;
						int code = fileVo.getCode();
						if(UploadFileQueue.getStatus()) {
							SoapResponseVo vo = null;
							try {
								long time = System.currentTimeMillis();
//								if(fileVo.isZkPackage()) {
//									logger.info("质控包上传："+ name +" code : "+code );
//								}
								vo = Uploadutil.UploadFile(file, password,md5,fileVo);
								code = null != vo?vo.getCode():-14444;
								if(Convent.getIsPrint()) {
									logger.info("name : "+name +" code : "+code +" times: "+ (System.currentTimeMillis() - time)); 
								}
							}catch (Exception e) {
								e.printStackTrace();
								logger.error("文件上传失败："+file.getPath());
							}
							if(code == 200) {
								file.delete();
								isSuccess = true;
							}else {
								UploadFileQueue.setStatus(false);
							}
						}
						//上传失败
						if(!isSuccess) {
							//file.delete();//zip 文件不删除。另外写入一个本地异常日志文件。启另外一个线程重新上传并判断接口是否处于正常可用状态。
							Uploadutil.writeErrorLog(file, password, md5, code, isZkPackage,fileVo.getPackType());
						}
					}
				}else {
					Thread.sleep(5000);
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("上传任务线程出现异常",e);
			}
		}
	}
	
	
}
