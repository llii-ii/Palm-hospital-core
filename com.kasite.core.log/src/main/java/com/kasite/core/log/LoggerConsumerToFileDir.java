/**
 * 
 */
package com.kasite.core.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.FileOper;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.IApplicationStartUp;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigInit;
import com.kasite.core.common.log.LogFileUtils;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.util.wxmsg.Zipper;
@Component
public class LoggerConsumerToFileDir implements IApplicationStartUp{
	private static boolean isStart = false;
	private static boolean isStartIng = false;
	@Autowired
	KasiteConfigInit config;
	
	public LoggerConsumerToFileDir(){
	}

	@Override
	public void init(ContextRefreshedEvent event) {
		boolean customLog = KasiteConfig.getESLog();
		if(!isStart && !customLog) {
			Thread t=new Thread("LoggerConsumerToFileDir"){
				private boolean stopped=false;
				private Thread thread;
				@Override
				public void run() {
					thread=Thread.currentThread();
					try{
						Runtime.getRuntime().addShutdownHook(new Thread(){
							@Override
							public void run() {
								stopped=true;
								thread.interrupt();
							}
						});
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					while(!stopped){
						try{
							boolean customLog = KasiteConfig.getCustomLog();
							if(!isStartIng && customLog) {
								isStartIng = true;
								KasiteConfig.print("========================启动文件方式处理日志线程===========================");	
							}
							if(customLog) {
								//判断是否启用 自定义文本日志
								File f = LogFileUtils.getLogedPath();
								if(f.exists() && f.isDirectory()) {
									File[] flist = f.listFiles();
									if(flist.length < 10) {
										//如果文件数量少于10个则每次休眠1秒
										Thread.sleep(500);
									}
									for (File logFile : flist) {
										if (logFile.exists() && logFile.isFile()) {
											BufferedReader reader = null;
											String temp = null;
											try {
												reader = new BufferedReader(new FileReader(logFile));
												while ((temp = reader.readLine()) != null) {
													try {
														if(StringUtil.isNotBlank(temp)) {
															JSONObject json = JSON.parseObject(temp);
															String content = json.getString("content");
															if(null != content && !"".equals(content)) {
																JSONObject contentJson = JSON.parseObject(content);
																String clientId = contentJson.getString("ClientId");
																String sign =  contentJson.getString("Sign");
																String sessionKey = contentJson.getString("SessionKey");
																if(StringUtil.isNotBlank(clientId) && StringUtil.isNotBlank(sign)&& StringUtil.isNotBlank(sessionKey)) {
																	String path = LogFileUtils.getLogInfoPath(sessionKey, clientId, sign).getAbsolutePath();
																	FileOper.write(path, temp+"\r\n", true);
																}
															}
														}
													} catch (Exception e) {
														e.printStackTrace();
														//如果系统异常则暂停2秒等待处理避免死循环一直处理占用线程。
														Thread.sleep(2000);
													} 
												
												}
											} catch (Exception e) {
												e.printStackTrace();
												KasiteConfig.print("日志解析异常");
											} finally {
												if (reader != null) {
													try {
														reader.close();
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											}
											File newFileDir = LogFileUtils.getTempFileDir();
											if(!newFileDir.exists()) {
												newFileDir.mkdirs();
											}
											String newfilepath = newFileDir.getAbsolutePath()+File.separator+logFile.getName();
											File newFile = new File(newfilepath);
											logFile.renameTo(newFile);
											logFile.delete();
										}
									}
									File newFileDir = LogFileUtils.getTempFileDir();
									if(null != newFileDir && newFileDir.listFiles().length > 50) {
										//压缩临时文件夹
										String uploadFileDir_zip = LogFileUtils.getZipFileDir().getAbsolutePath();
										String filename = System.currentTimeMillis() + "_" +IDSeed.next();
										Zipper.zipFileForAll(newFileDir, uploadFileDir_zip + File.separator + filename + ".zip",null);
										File[] fs = newFileDir.listFiles();
										for (File file : fs) {
											file.delete();
										}
									}
								}
							}else {
								Thread.sleep(10000);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			};
			t.setDaemon(true);
			t.start();
			isStart = true;
		}
	}
	
}
