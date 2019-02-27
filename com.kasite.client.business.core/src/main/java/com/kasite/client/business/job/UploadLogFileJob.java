package com.kasite.client.business.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coreframework.util.DateOper;
import com.coreframework.util.FileOper;
import com.kasite.core.common.config.LogFileZipConfig;
import com.kasite.core.common.config.LogfilezipVo;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

/**
 * 上传数据日志文件
 */
@Component // 使spring管理
public class UploadLogFileJob {
	protected final static Logger logger = LoggerFactory.getLogger(UploadLogFileJob.class);
	private boolean flag = true;
	@Autowired
	private LogFileZipConfig logFileZipConfig;

	/**
	 * @param arg0
	 * @throws JobExecutionException
	 */

	@Scheduled(cron = "0 0/1 * * * ?") // "0 0/5 * * * ?"每5分钟一次 "0/10 * * * * ? " 每10秒
	public void execute() {
		try {
			if (flag) {
				flag = false;
				if (null != logFileZipConfig && null != logFileZipConfig.getLogfilezip() && logFileZipConfig.getLogfilezip().size() > 0) {
					List<LogfilezipVo> list = logFileZipConfig.getLogfilezip();
					for (LogfilezipVo vo : list) {
						String uploadFileDir = vo.getDirs();
						// 判断当前文件夹下是否有需要上传的文件，如果有的话将文件移到temp文件目录
						File dirFile = new File(uploadFileDir);
						File[] fs = dirFile.listFiles();
						List<File> fileList = new ArrayList<>();

						if (null != fs && 0 < fs.length) {
							for (File f : fs) {
								// 是否文件
								if (f.isFile()) {
									String fileName = f.getName();
									if (fileName.endsWith(".wechatlog")) {
										fileList.add(f);
									}
								}
							}

							// 按照最后修改时间排序
							Collections.sort(fileList, new Comparator<File>() {
								public int compare(File f1, File f2) {
									long diff = f1.lastModified() - f2.lastModified();
									if (diff > 0)
										return 1;
									else if (diff == 0)
										return 0;
									else
										return -1;
								}

								public boolean equals(Object obj) {
									return true;
								}
							});
							// 移除最后一个最新修改时间的文件
//							fileList.remove(fileList.size() - 1);
							for (File file : fileList) {
								String newFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(file.getName())) + File.separator + "temp";
								File f = new File(newFilePath);
								if(!f.exists()) {
									f.mkdirs();
								}
								FileOper.moveFile(file.getAbsolutePath(), newFilePath+ File.separator
								          + DateOper.getNow("yyyyMMddHHmmss")+"_"+file.getName());
							}

						}

						String url = vo.getUrl();
						String isUpdate = vo.getIsupdate();
						if (StringUtil.isNotBlank(isUpdate) && isUpdate.equals("false")) {
							logger.info("定时任务设置为不启动：url = " + url);
							return;
						}
						try {
							File file = WxMsgUtil.create(uploadFileDir).zipFiles();
							 if(null != file && file.exists() && file.isFile()) {
							 SoapResponseVo respVo = HttpRequestBus
							 .create(url, RequestType.fileUploadPost)
							 .setFile("file", file).send();
							 logger.info("任务执行结果状态：HttpStatusCode:"+respVo.getCode()+"Result:"+respVo.getResult());
							 }
						} catch (Exception e) {
							logger.error("异常：", e);
							e.printStackTrace();
						}
					}
				}
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上传文件异常", e);
		} finally {
			flag = true;
		}

	}
}
