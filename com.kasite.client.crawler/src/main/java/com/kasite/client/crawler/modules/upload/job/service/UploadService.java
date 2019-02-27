//package com.kasite.client.crawler.modules.upload.job.service;
//
//import java.io.File;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.kasite.client.crawler.modules.utils.Uploadutil;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//import com.kasite.core.httpclient.http.StringUtils;
//@Service("uploadService")
//public class UploadService {
//	
//	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);
//	
//	public void upload() {
//		try {
//			UploadFileVo fileVo = UploadFileQueue.takeFile();
//			File file = fileVo.getFile();
//			if(file.exists() && file.isFile()) {
//				String password = fileVo.getPassword();
//				String md5 = fileVo.getMd5();
//				if(StringUtils.isBlank(md5)) {
//					md5 = Uploadutil.getFileMd5(file);
//				}
//				boolean isSuccess = false;
//				int code = fileVo.getCode();
//				if(UploadFileQueue.getStatus()) {
//					SoapResponseVo vo = null;
//					try {
//						vo = Uploadutil.UploadFile(file, password,md5);
//					}catch (Exception e) {
//						e.printStackTrace();
//						logger.error("文件上传失败："+file.getPath());
//					}
//					code = null != vo?vo.getCode():-14444;
//					if(code == 200) {
//						file.delete();
//						isSuccess = true;
//					}else {
//						UploadFileQueue.setStatus(false);
//					}
//				}
//				//上传失败
//				if(!isSuccess) {
//					//file.delete();//zip 文件不删除。另外写入一个本地异常日志文件。启另外一个线程重新上传并判断接口是否处于正常可用状态。
//					Uploadutil.writeErrorLog(file, password, md5, code);
//				}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("上传任务线程出现异常",e);
//		}
//	}
//}
