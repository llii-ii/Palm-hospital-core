package com.kasite.client.crawler.modules.upload.job;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.XMDataCloudConfig;
import com.kasite.client.crawler.modules.upload.datacloud.IXMDataCloudImpl;
import com.kasite.client.crawler.modules.upload.datacloud.IXMDataCloudService;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileQueue;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileVo;
import com.kasite.client.crawler.modules.utils.Uploadutil;
import com.kasite.client.crawler.modules.utils.Zipper;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.FileUtils;
import com.kasite.core.httpclient.http.SoapResponseVo;
 
@Component
public class UploadJob {
	private static final Logger logger = LoggerFactory.getLogger(UploadJob.class);
	private static boolean isNotRun = true;
	private static boolean isNotRun_dealData = true;
	private static boolean isNotRun_ReadLogFile = true;
	private static boolean isNotRun_dealBasicData = true;
	@Autowired
	private IXMDataCloudService xmDataCloudService;
	@Autowired
	private XMDataCloudConfig xMDataCloudConfig;
	
	/**
	 * 每3秒执行一次 文件读取
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void readLogFileJob() {
		readLogFile();
	}
	
	/**
	 * 数据采集 每天晚上1点执行
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 1 * * ?") 
	public void dealData() throws Exception {
		if(isNotRun_dealData && Convent.getIsStartJob()) {
			isNotRun_dealData = false;
			try {
				long start = System.currentTimeMillis();
				String date = DateOper.getNow("yyyy-MM-dd");
				String endDate = DateOper.addDate(date,-1);
				String startDate = DateOper.addDate(date,-2);
				logger.info("事件时间：" + startDate + "的数据采集开始");
				xmDataCloudService.assemblyData(startDate, endDate,null);
				String collectTime= (System.currentTimeMillis()-start)/1000+"";
				logger.info("事件时间：" + startDate + "的数据采集完成,耗时：" + collectTime + "秒");
				//质控后台作业时间新增
				Uploadutil.uploadWorkTime(startDate,xMDataCloudConfig.getOrg_code(), collectTime, collectTime, collectTime, collectTime);
			}catch (Exception e) {
				logger.error("数据处理异常",e);
			}
			isNotRun_dealData = true;
		}
	}
	
	/**
	 * 执行文件读取动作
	 */
	public static synchronized void readLogFile() {
		if(isNotRun_ReadLogFile && Convent.getIsStartJob()) {
			isNotRun_ReadLogFile = false;
			try {
//				logger.debug("日志文件处理");
				String errorFile = Convent.getErrorlogDir();
				File file = new File(errorFile);
				if (file.exists()) {
					File[] files = file.listFiles();// 获取异常文件夹下的各个日期的文件夹
					for (File file2 : files) {
						if (file2.isDirectory()) {// 如果是文件夹
							File[] logdirfiles = file.listFiles();// 获取日志文件目录
							for (File logfiledir : logdirfiles) {
								if (logfiledir.isDirectory()) {
									String logfiledirname = logfiledir.getName();//如果文件目录超过30天则直接删除这个文件夹。不做上传
									int size = 0;
									try {
//										logger.debug("处理数据日志文件夹："+ logfiledir.getPath());
										Date date = DateOper.parse(logfiledirname);
										Date now = DateOper.getNowDate();
										size = DateOper.getDateDiff(now, date, "d");
										if(size > Convent.getZipTempDays()) {
											logger.error("文件夹目录超过30天 移除文件夹："+ logfiledir.getPath() +" 文件夹下文件数量："+logfiledir.listFiles().length);
											FileUtils.delFolder(logfiledir.getPath());
											continue;
										}
									}catch (Exception e) {
										e.printStackTrace();
										logger.error("文件夹删除异常",e);
									}
									File[] logfiles = logfiledir.listFiles();// 获取日志文件
									if(logfiles.length > 200) {
										logger.error("正在处理日志文件超过200个文件需要暂停生成："+ logfiles.length +" path:"+logfiledir.getPath());
										IXMDataCloudImpl.stop();
									}else {
										IXMDataCloudImpl.start();
									}
//									logger.debug("Parse DataFileDir："+logfiledirname +" File Size："+ logfiles.length);
									/**非今天的空目录 删除*/
									if(size>0 && logfiles.length==0){
										logfiledir.delete();
										continue;
									}
									for (File file3 : logfiles) {
										String fileName = file3.getName();
										if (fileName.lastIndexOf(".log") < 0) {
											continue;
										}
										// 解析日志文件。
										byte[] data = Files.readAllBytes(file3.toPath());
										String logstr = new String(data, StandardCharsets.UTF_8);
										String[] logstrs = logstr.split("\t");
										String filePath = logstrs[0];
										String password = logstrs[1];
										String md5 = logstrs[2];
										int code = 0;
										boolean isZkPackage = false;
										String packType ="1";
										if(logstrs.length >=4) {
											code = Integer.parseInt(logstrs[3]);
											String isPackage = logstrs[4];
											if("true".equals(isPackage)) {
												isZkPackage = true;
											}
										}
										if(logstrs.length >5) {
											packType = logstrs[5];
										}
										boolean isRSA = false;
										if(logstrs.length >6) {
											String isPackage = logstrs[6];
											if("true".equals(isPackage)) {
												isRSA = true;
											}
										}
										
										File zipFile = new File(filePath);
										if (zipFile.exists() && zipFile.isFile()) {
											
											UploadFileVo fileVo = new UploadFileVo();
											fileVo.setFile(zipFile);
											fileVo.setMd5(md5);
											fileVo.setZkPackage(isZkPackage);
											fileVo.setPassword(password);
											fileVo.setCode(code);
											fileVo.setPackType(packType);
											fileVo.setRSA(isRSA);
											
											if (!UploadFileQueue.getStatus()) {
												logger.debug("ApiIsErrorNot Test UploadFile.");
												SoapResponseVo vo = Uploadutil.UploadFile(zipFile, password, md5,fileVo);
												code = null != vo ? vo.getCode() : -1;
												if (code == 200) {
													logger.debug("ApiIsErrorNot Test isSuccess, Delete File:"+ file3.getName());
													UploadFileQueue.setStatus(true);
													zipFile.delete();
													file3.delete();
													continue;
												}else {
													logger.error("Upload error is code "+ code + "\r\nresult="+vo.getResult()+"\r\nexception="+vo.getException());
													Thread.sleep(30000);//休息30秒 接口无法上传文件
													continue;
												}
											}
											fileVo.setCode(code);
											boolean isNotFull = UploadFileQueue.addFile(fileVo);
											if(isNotFull) {
												file3.delete();
											}
										}else {
											file3.delete();
										}
									}
								}
							}

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("守护线程发现异常", e);
			}	
			isNotRun_ReadLogFile = true;
		}
		
	}
	
	/**
	 *  每2秒执行一次  压缩并加密
	 * @throws Exception
	 */
	@Scheduled(cron = "0/2 * * * * ?")
	public void work() throws Exception {
		if(isNotRun && Convent.getIsStartJob()) {
			isNotRun = false;
			try {
				if(Convent.getIsUpload()) {
					logger.debug("压缩文件。。");
					// 每分钟检查指定目录下的文件是否有存在，存在即加密并上传。然后删除文件。
					String datadir = Convent.getDataDir();
					String zipdir = Convent.getZipDir();
					String nowDate = DateOper.getNow("yyyyMMdd");
					File dataDir = new File(datadir);
					File zipDir = new File(zipdir + File.separator + nowDate);
					if (!zipDir.exists()) {
						zipDir.mkdirs();
					}
					// 压缩后的文件生成按照日期每天生成一个文件夹 yyyyMMdd
					if (dataDir.exists()) {
						File[] files = dataDir.listFiles();
						if(files.length > 1000) {
							logger.error("执行压缩文件，需要文件夹数量："+ files.length);
						}
						for (File file : files) {
							String fileName = file.getName();
							//文件夹以 _Temp 结尾的文件都是还在生成数据的文件，不能做压缩操作等数据采集完成后才可以上传
							if(fileName.lastIndexOf(Convent.TEMPDATAFILENAME) >= 0) {
								continue;
							}
							//文件名长度大于30 并且是文件夹的才操作。
							if (fileName.length() > 30 && file.isDirectory()) {

								// 获取一个随机密码
								String pwd = Uploadutil.getStringRandom(Convent.passwordsize);
								String newFileName = UUID.randomUUID().toString().replaceAll("-", "");
								
								UploadFileVo vo = new UploadFileVo();
								if(fileName.indexOf(Convent.ZKPACKAGE)>=0) {
									vo.setZkPackage(true);
								}
								/**包类型*/
								if(fileName.indexOf(Convent.FJGPACKAGE)>=0) {
									vo.setPackType("2");
								}
								File zipfile = Zipper.zipFileForAll(file, zipDir + File.separator + newFileName + ".zip", pwd);
							
								vo.setFile(zipfile);
								vo.setPassword(pwd);
								
								String md5 = Uploadutil.getFileMd5(zipfile);
								vo.setMd5(md5);
								Uploadutil.writeErrorLog(zipfile, pwd, md5, -144443, vo.isZkPackage(),vo.getPackType());
								FileUtils.delFolder(file.getPath());
							}
						}
					}
				}
			}catch (Exception e) {
				logger.error("压缩文件夹异常",e);
			}
			isNotRun = true;
		}
	}

	/**
	 * 医疗资源 每周5晚上1点执行
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 1 ? * FRI")
	public void dealBasicData() throws Exception {
		if(isNotRun_dealBasicData && Convent.getIsStartJob()) {
			isNotRun_dealBasicData = false;
			try {
				xmDataCloudService.assemblyBasicData();
			}catch (Exception e) {
				logger.error("数据处理异常",e);
			}
			isNotRun_dealBasicData = true;
		}
	}
}