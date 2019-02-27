package com.kasite.core.common.util;

import java.net.URLEncoder;

import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;

/**
 * 发送告警信息工具类
 * 
 * @author zhaoy
 *
 */
public class SendQVWarnUtil {

	/**
	 * 告警接口地址
	 */
	private static final String warnWebUrl = "https://springadmin.kasitesoft.com/weChat/push/textCardMessage";
	
	/**
	 * 支付场景告警
	 * 
	 * @param title
	 * @param desc
	 * @param api
	 * @param nowTime
	 * @throws Exception
	 */
	public static void sendWarnInfo(String clientId, String api, Long nowTime, Throwable e) throws Exception{
		String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
		if(StringUtil.isBlank(clientName)) {
			clientName = clientId;
		}
		String desc = "支付场景:"+clientName+"<br/>" +"异常信息:" + e.getMessage();
		String title = "医院:" + KasiteConfig.getOrgName();
		sendWarnInfo(title, desc, api, nowTime, null);
	}
	
	/**
	 * 支付场景告警
	 * 
	 * @param title
	 * @param desc
	 * @param api
	 * @param nowTime
	 * @throws Exception
	 */
	public static void sendWarnInfo(String title, String desc, String api, Long nowTime) throws Exception{
		sendWarnInfo(title, desc, api, nowTime, null);
	}
	
	/**
	 * 支付回调告警
	 * 
	 * @param title
	 * @param desc
	 * @param api
	 * @param nowTime
	 * @param orderid
	 * @throws Exception
	 */
	public static void sendWarnInfo(String title, String desc, String api, Long nowTime, String orderid) throws Exception{
		HttpRequstBusSender sender = HttpRequestBus.create(warnWebUrl, RequestType.post);
		sender.addHttpParam("title", title);
		sender.addHttpParam("desc", "服务器域名: "+KasiteConfig.getKasiteHosWebAppUrl()+ "<br/>" + desc);
		String url = KasiteConfig.getKasiteHosWebAppUrl()+"/module/log/warnLogForPay.html?api="+api+"&dayTime="+nowTime+"&orderid="+orderid;
		sender.addHttpParam("url", URLEncoder.encode(url, "UTF-8"));
		sender.addHttpParam("touser", "zhaoyan|caiyouhong");
		sender.addHttpParam("toparty", "");
		sender.addHttpParam("hosid", KasiteConfig.getOrgCode());
		sender.send();
	}
}
