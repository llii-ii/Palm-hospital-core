package com.kasite.core.common.util.wechat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.httpclient.http.RequestType;

/**
 * 微信SSL 退款交互处理
 * 
 * @author leo
 * 
 */
public class ClientCustomSSL {
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientCustomSSL.class);
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	private final static int RETRY_COUNT = 3;

	public static String call(String orderId,ApiModule.WeChat api,String url,String configKey,String arrayToXml) throws Exception {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = configKey+"|"+arrayToXml;
		String result = "";
		try {
			String merchantid = null;
			// 商户号，密码
			if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
				merchantid = KasiteConfig.getWxPay(WXPayEnum.wx_parent_mch_id, configKey);
			} else {
				// 如果不存在父商户，则为普通商户模式
				merchantid = KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey);
			}
			
			String certPath = KasiteConfig.getWxPayCertPath(configKey);
			
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(certPath));
			try {
				keyStore.load(instream, merchantid.toCharArray());
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
				throw e;
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, merchantid.toCharArray()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String xmlStr = "";
			int n = 0;
			try {
				long flag = System.currentTimeMillis();
				LogUtil.info(log, "\r\n" + "[" + flag + "]微信退款处理发送：\r\n" + arrayToXml);
				// 可能报超时异常，3次机会
				while (n++ < RETRY_COUNT) {
					try {
						HttpPost httpPost = new HttpPost(url);
						httpPost.setEntity(new StringEntity(arrayToXml, "UTF-8"));
						CloseableHttpResponse response = httpclient.execute(httpPost);
						xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
						response.close();
						break;
					} catch (IOException e) {
						e.printStackTrace();
						LogUtil.error(log, e);
						// LogUtil.info(log,"\r\n休息一秒");
						// 休息一秒
						Thread.sleep(1000);
					}
				}
				LogUtil.info(log, "\r\n" + "[" + flag + "]微信退款处理返回：\r\n" + xmlStr);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用微信接口异常："+api.getName()+"|"+reqParamStr,e);
				throw new RRException(RetCode.Pay.ERROR_TRADESTATE,"调用微信接口异常："+api.getName()+"|"+reqParamStr+"|exp="+e.getMessage());
			} finally {
				httpclient.close();
			}
			result = xmlStr;
			return xmlStr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用微信接口异常："+api.getName()+"|"+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallWeChatLog(orderId,api, reqParamStr, result,  null , System.currentTimeMillis() - start, url ,RequestType.post,isSuccess);
		}

	}

//	public static String call(ApiModule.WeChat api, String configKey,String refundurl, String arrayToXml) throws Exception {
//		// 商户号，密码
////		String merchantid = TenpayConstant.MCH_ID;
////		if (!StringUtil.isEmpty(TenpayConstant.PARENT_APP_ID)) {
////			merchantid = TenpayConstant.PARENT_MCH_ID;
////		}
//		long start = System.currentTimeMillis();
//		boolean isSuccess = false;
//		String reqParamStr = configKey+"|"+refundurl+"|"+arrayToXml;
//		String result = "";
//		try {
//			// 商户号，密码
//			String merchantid = KasiteConfig.getWxPayMchKey(configKey);
//			String certPath = KasiteConfig.getWxPayCertPath(configKey);
//					
//					
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			FileInputStream instream = new FileInputStream(new File(certPath));
//			try {
//				keyStore.load(instream, merchantid.toCharArray());
//			} catch (Exception e) {
//				e.printStackTrace();
//				LogUtil.error(log, e);
//			} finally {
//				instream.close();
//			}
//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, merchantid.toCharArray()).build();
//			// Allow TLSv1 protocol only
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
//					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//			String xmlStr = "";
//			int n = 0;
//			try {
//				// 可能报超时异常，3次机会
//				while (n++ < RETRY_COUNT) {
//					try {
//						HttpPost httpPost = new HttpPost(refundurl);
//						httpPost.setEntity(new StringEntity(arrayToXml, "UTF-8"));
//						CloseableHttpResponse response = httpclient.execute(httpPost);
//						xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//						response.close();
//						break;
//					} catch (IOException e) {
//						e.printStackTrace();
//						// 休息一秒
//						Thread.sleep(1000);
//					}
//				}
//				isSuccess = true;
//			} catch (Exception e) {
//				e.printStackTrace();
//				JSONObject param = new JSONObject();
//				param.put("url", refundurl);
//				param.put("param", arrayToXml);
//				LogUtil.error(log, param.toJSONString(),e);
//				logger.error("调用微信接口异常："+api.getName()+"|"+reqParamStr,e);
//			} finally {
//				httpclient.close();
//			}
//			result = xmlStr;
//			logger.info("返回值:" + xmlStr);
//			return xmlStr;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("调用微信接口异常："+api.getName()+"|"+reqParamStr,e);
//			throw e;
//		}finally {
//			LogUtil.saveCallWeChatLog(api, reqParamStr, result,  null , System.currentTimeMillis() - start, refundurl ,RequestType.post,isSuccess);
//		}
//		
//	}
}
