package com.kasite.core.common.sys.threads;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.handler.KasticMessageHandler;
import com.kasite.core.common.sys.verification.VerificationBuser;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.httpclient.http.StringUtils;
/***
 * 每个客户端程序都启动这个线程，线程会并注册到中心系统获取消息
 * @author daiyanshui
 *
 */
public class ThreadMessageStatus implements Runnable{
	private static long sleepTime = 60000 ;//每1分钟同步一次消息
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	static {
		GetMessageStatusUrl = KasiteConfig.getMessageStatusUrl();
		appId = KasiteConfig.getAppId();
		appSecret = KasiteConfig.getAppSecret();
		try {
			LastReadTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 应用ID
	 */
	static String appId;
	/**
	 * 应用安全码
	 */
	static String appSecret;
	/**
	 * 获取消息状态码的接口地址
	 */
	static String GetMessageStatusUrl;
	/**
	 * 机构编码
	 */
	static String OrgCode;
	
	static String LastReadTime;
	
	@Override
	public void run() {
		if(KasiteConfig.isConnectionKastieCenter()) {
			logger.info("启动 从中心同步消息线程。");
			while(true) {
				try {
					String token = VerificationBuser.create().getCenterToken(false);
					SoapResponseVo vo = HttpRequestBus.create(GetMessageStatusUrl, RequestType.post)
							.setHeaderHttpParam("access_token", token)
							.addHttpParam("lastReadTime", LastReadTime)
							.sendCluster();
					if(200 == vo.getCode()) {
						String statusStr = vo.getResult();
						try {
							JSONObject json = (JSONObject) JSON.parse(statusStr);
							int code = json.getIntValue("code");
							if(code == 10000) {
								String lastReadTime = json.getString("lastReadTime");
								JSONArray arrList = json.getJSONArray("result");
								for (Object obj : arrList) {
									JSONObject o = (JSONObject) obj;
									int status = o.getIntValue("status");
									String gid = o.getString("gid");
									logger.info("收到中心系统的消息： 消息状态 status = "+ status +" 消息gid = "+ gid);
									ThreadPoolFactory.sync_center_msg_pool.execute(new KasticMessageHandler(status,gid));
									if(StringUtils.isNotBlank(lastReadTime)) {
										LastReadTime = lastReadTime;
										//保存本地最后修改时间确认已经读取即可
									}
									logger.debug(LastReadTime);
								}
							}
						}catch (Exception e) {
							e.printStackTrace();
							logger.error("获取消息状态异常",e);
						}
					}else if(4000 == vo.getCode()) {
						VerificationBuser.create().getCenterToken(true);
					}
					Thread.sleep(sleepTime);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("获取消息状态异常",e);
				}
			}
		}else {
			logger.info("不需要启动连接中心线程。");
		}
		
	}

	
}
