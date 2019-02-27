package com.kasite.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;


/**
 * @author linjianfa
 * @Description: 商户调用https工具类
 * @version: V1.0  
 * 2017年10月16日 下午6:17:58
 */
public class HttpsClientUtils {
	
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpsClientUtils.class);
	
	private static int BUFFER_SIZE = 4096;  
	
	/**
	 * 
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static HttpsURLConnection getHttpsConnect(String requestUrl)
			throws Exception {
		HttpsURLConnection httpUrlConn = null;

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			/**
			 * 判断是否需要走代理地址
			 */
			requestUrl = KasiteConfig.proxyUrl(requestUrl);
			
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

		} catch (ConnectException ce) {
			logger.error("调用微信接口请求异常无法连接到对方服务器："+requestUrl,ce);
			throw new Exception("http服务器连接超时");
		} catch (Exception e) {
			logger.error("调用微信接口请求异常："+requestUrl,e);
			throw new Exception("http请求超时");
		} finally {
			// 释放资源
			if (httpUrlConn != null)
				httpUrlConn.disconnect();
		}
		return httpUrlConn;
	}
	/**
	 * post请求HTTPS
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(ApiModule.WeChat api,String requestUrl,String reqParam) throws Exception{
		return httpsPost(null, api, requestUrl, reqParam);
	}
	/**
	 * post请求HTTPS
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(String orderId,ApiModule.WeChat api,String requestUrl,String reqParam) throws Exception{
		HttpsURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		RequestType type = RequestType.post;
		String retVal = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			/**
			 * 判断是否需要走代理地址
			 */
			requestUrl = KasiteConfig.proxyUrl(requestUrl);
			
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			//HttpsURLConnection.setDefaultSSLSocketFactory(getSSLContext().getSocketFactory());
			httpUrlConn.setSSLSocketFactory(ssf);
//			httpUrlConn.setConnectTimeout(Integer.getInteger("WX_CONNECT_TIMEOUT", 5000));
//			httpUrlConn.setReadTimeout(Integer.getInteger("WX_SOCKET_TIMEOUT", 5000));
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（POST）
			httpUrlConn.setRequestMethod(KstHosConstant.POST);

			// 当有数据需要提交时
			if (null != reqParam) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(reqParam.getBytes("UTF-8"));
				outputStream.close();
			}

			// 响应码为200表示成功，否则失败。
			int responseCode = httpUrlConn.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new Exception("http响应状态"+responseCode);
            }			
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
	        byte[] data = new byte[BUFFER_SIZE];  
	        int count = -1;  
	        while((count = inputStream.read(data,0,BUFFER_SIZE)) != -1) { 
	            outStream.write(data, 0, count);  
	        }
	        data = null;  
	        
	        outStream.flush();
	        outStream.close();
			inputStream.close();
			retVal =outStream.toString("UTF-8");
			isSuccess = true;
		} catch (ConnectException ce) {
			logger.error(api.getName()+"调用微信接口请求异常无法连接到对方服务器："+requestUrl,ce);
			throw new Exception("http服务器连接超时");
		} catch (Exception e) {
			logger.error(api.getName()+"调用微信接口请求异常："+requestUrl,e);
			throw new Exception("http请求超时");
		} finally {
			// 释放资源
			if(httpUrlConn!=null){
				httpUrlConn.disconnect();
			}
			LogUtil.saveCallWeChatLog(orderId, api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,type,isSuccess);
		}
		return retVal;
	}
	
	/**
	 * 银联post请求HTTPS
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String UnionPayhttpsPost(String orderId,ApiModule.UnionPay api,String requestUrl,String reqParam,String encoding) throws Exception{
		HttpsURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		RequestType type = RequestType.post;
		String retVal = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			//httpUrlConn.setHostnameVerifier();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded;charset=" + encoding);
			// 设置请求方式（POST）
			httpUrlConn.setRequestMethod(KstHosConstant.POST);

			// 当有数据需要提交时
			if (null != reqParam) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(reqParam.getBytes(encoding));
				outputStream.close();
			}

			// 响应码为200表示成功，否则失败。
			int responseCode = httpUrlConn.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new Exception("http响应状态"+responseCode);
            }			
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
	        byte[] data = new byte[BUFFER_SIZE];  
	        int count = -1;  
	        while((count = inputStream.read(data,0,BUFFER_SIZE)) != -1) { 
	            outStream.write(data, 0, count);  
	        }
	        data = null;  
	        
	        outStream.flush();
	        outStream.close();
			inputStream.close();
			retVal =outStream.toString("UTF-8");
			isSuccess = true;
		} catch (ConnectException ce) {
			logger.error(api.getName()+"调用微信接口请求异常无法连接到对方服务器："+requestUrl,ce);
			throw new Exception("http服务器连接超时");
		} catch (Exception e) {
			logger.error(api.getName()+"调用微信接口请求异常："+requestUrl,e);
			throw new Exception("http请求超时");
		} finally {
			// 释放资源
			if(httpUrlConn!=null){
				httpUrlConn.disconnect();
			}
			LogUtil.saveCallUnionPayLog(orderId, api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,type,isSuccess);
		}
		return retVal;
	}
	
	/**
	 * 威富通post请求HTTPS
	 * @param api
	 * @param requestUrl
	 * @param reqParam
	 * @param encoding
	 * @return
	 * @throws Exception
	 * 官方demo，调用方式为http。为考虑以后升级https，将调用方法放置此处
	 */
	public static String SwiftpasshttpsPost(String orderId,ApiModule.Swiftpass api,String requestUrl,String reqParam,String encoding){
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String retVal = null;
		try {
			SoapResponseVo soapResponseVo = HttpRequestBus.create(requestUrl, RequestType.post).setParam(reqParam)
					.contentType("text/xml;utf-8").send();
			if(HttpStatus.SC_OK == soapResponseVo.getCode()) {
				retVal = soapResponseVo.getResult();
			}else {
				throw new Exception("http响应状态"+soapResponseVo.getCode());
			}
			isSuccess = true;
		}catch (Exception e) {
			logger.error(api.getName()+"调用威富通接口请求异常："+requestUrl,e);
		} finally {
			LogUtil.saveCallSwiftpassLog(orderId, api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,RequestType.post,isSuccess);
		}
		return retVal;
	}
	
	/**
	 * get请求HTTPS
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String httpsGet(ApiModule.WeChat api,String requestUrl,String reqParam) throws Exception{
		HttpsURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		RequestType type = RequestType.get;
		String retVal = null;
		try {
			requestUrl = requestUrl.replaceAll("\r|\n", "");
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			/**
			 * 判断是否需要走代理地址
			 */
			requestUrl = KasiteConfig.proxyUrl(requestUrl);
			
			
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
//			httpUrlConn.setConnectTimeout(Integer.getInteger("WX_CONNECT_TIMEOUT", 5000));
//			httpUrlConn.setReadTimeout(Integer.getInteger("WX_SOCKET_TIMEOUT", 5000));
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（POST）
			httpUrlConn.setRequestMethod(KstHosConstant.GET);
			httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != reqParam) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(reqParam.getBytes("UTF-8"));
				outputStream.close();
			}

			// 响应码为200表示成功，否则失败。
			int responseCode = httpUrlConn.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new Exception("http响应状态"+responseCode);
            }			
			
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
	        byte[] data = new byte[BUFFER_SIZE];  
	        int length = -1;  
	        
	        while((length = inputStream.read(data,0,BUFFER_SIZE)) != -1){ 
	            outStream.write(data, 0, length);  
	        }
	        data = null;  
	        
	        outStream.flush();
	        outStream.close();
			inputStream.close();
			retVal =outStream.toString("UTF-8");
			isSuccess = true;
		} catch (ConnectException ce) {
			logger.error(api.getName()+"调用微信接口请求异常无法连接到对方服务器："+requestUrl,ce);
			throw new Exception("http服务器连接超时");
		} catch (Exception e) {
			logger.error(api.getName()+"调用微信接口请求异常："+requestUrl,e);
			throw new Exception("http请求超时");
		}finally{
			// 释放资源
			if(httpUrlConn!=null){
				httpUrlConn.disconnect();
			}
			LogUtil.saveCallWeChatLog(null,api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,type,isSuccess);
		}
		return retVal;
	}

	
	
	/**
	 * 素材下载
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String materialDownload(ApiModule.WeChat api,String requestUrl,String reqParam,String materPath) throws Exception{
		HttpsURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		RequestType type = RequestType.get;
		String retVal = null;
		try {
			requestUrl = requestUrl.replaceAll("\r|\n", "");
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			/**
			 * 判断是否需要走代理地址
			 */
			requestUrl = KasiteConfig.proxyUrl(requestUrl);
			
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
//			httpUrlConn.setConnectTimeout(Integer.getInteger("WX_CONNECT_TIMEOUT", 5000));
//			httpUrlConn.setReadTimeout(Integer.getInteger("WX_SOCKET_TIMEOUT", 5000));
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（POST）
			httpUrlConn.setRequestMethod(KstHosConstant.GET);
			httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != reqParam) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(reqParam.getBytes("UTF-8"));
				outputStream.close();
			}

			// 响应码为200表示成功，否则失败。
			int responseCode = httpUrlConn.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new Exception("http响应状态"+responseCode);
            }			
			
			inputStream = httpUrlConn.getInputStream();
			
			byte[] data = new byte[BUFFER_SIZE]; 
		    int len = -1; 
		    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		    FileOutputStream fileOutputStream = new FileOutputStream(materPath); 
		    while ((len = inputStream.read(data,0,BUFFER_SIZE)) != -1) { 
		        fileOutputStream.write(data, 0, len); 
		        outStream.write(data, 0, len);  
		    }
	        data = null;  
	        outStream.flush();
	        outStream.close();
			inputStream.close();
			fileOutputStream.close();
			retVal =outStream.toString("UTF-8");
			isSuccess = true;
		} catch (ConnectException ce) {
			logger.error(api.getName()+"调用微信接口请求异常无法连接到对方服务器："+requestUrl,ce);
			throw new Exception("http服务器连接超时");
		} catch (Exception e) {
			logger.error(api.getName()+"调用微信接口请求异常："+requestUrl,e);
			throw new Exception("http请求超时");
		}finally{
			// 释放资源
			if(httpUrlConn!=null){
				httpUrlConn.disconnect();
			}
			LogUtil.saveCallWeChatLog(null, api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,type,isSuccess);
		}
		return retVal;
	}
	
	/**
	 * post请求HTTPS
	 * @param requestUrl
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(String orderId,ApiModule.NetPay api,String requestUrl,String reqParam) throws Exception{
		if( requestUrl.contains("https")) {//如果是https请求
			HttpsURLConnection httpUrlConn = null;
			InputStream inputStream = null;
			long start = System.currentTimeMillis();
			boolean isSuccess = false;
			RequestType type = RequestType.post;
			String retVal = null;
			try {
				// 创建SSLContext对象，并使用我们指定的信任管理器初始化
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				
				/**
				 * 判断是否需要走代理地址
				 */
				requestUrl = KasiteConfig.proxyUrl(requestUrl);
				
				URL url = new URL(requestUrl);
				httpUrlConn = (HttpsURLConnection) url.openConnection();
				//HttpsURLConnection.setDefaultSSLSocketFactory(getSSLContext().getSocketFactory());
				httpUrlConn.setSSLSocketFactory(ssf);
//				httpUrlConn.setConnectTimeout(Integer.getInteger("WX_CONNECT_TIMEOUT", 5000));
//				httpUrlConn.setReadTimeout(Integer.getInteger("WX_SOCKET_TIMEOUT", 5000));
				httpUrlConn.setDoOutput(true);
				httpUrlConn.setDoInput(true);
				httpUrlConn.setUseCaches(false);
				// 设置请求方式（POST）
				httpUrlConn.setRequestMethod(KstHosConstant.POST);

				// 当有数据需要提交时
				if (null != reqParam) {
					OutputStream outputStream = httpUrlConn.getOutputStream();
					// 注意编码格式，防止中文乱码
					outputStream.write(reqParam.getBytes("UTF-8"));
					outputStream.close();
				}

				// 响应码为200表示成功，否则失败。
				int responseCode = httpUrlConn.getResponseCode();
	            if (responseCode != HttpStatus.SC_OK) {
	                throw new Exception("http响应状态"+responseCode);
	            }			
				// 将返回的输入流转换成字符串
				inputStream = httpUrlConn.getInputStream();
				
				ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		        byte[] data = new byte[BUFFER_SIZE];  
		        int count = -1;  
		        while((count = inputStream.read(data,0,BUFFER_SIZE)) != -1) { 
		            outStream.write(data, 0, count);  
		        }
		        data = null;  
		        
		        outStream.flush();
		        outStream.close();
				inputStream.close();
				retVal =outStream.toString("UTF-8");
				isSuccess = true;
			} catch (ConnectException ce) {
				logger.error(api.getName()+"调用招行一网通接口请求异常无法连接到对方服务器："+requestUrl,ce);
				throw new Exception("http服务器连接超时");
			} catch (Exception e) {
				logger.error(api.getName()+"调用招行一网通接口请求异常："+requestUrl,e);
				throw new Exception("http请求超时");
			} finally {
				// 释放资源
				if(httpUrlConn!=null){
					httpUrlConn.disconnect();
				}
				LogUtil.saveCallNetPayLog(orderId, api, reqParam, retVal,  null , System.currentTimeMillis() - start, requestUrl ,type,isSuccess);
			}
			return retVal;
		}else {
			//http请求的是测试地址
			return HttpRequestBus.create(requestUrl, RequestType.post).setParam(reqParam).send().getResult();
		}
		
	}
	
	/**
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param createTime
	 *            消息创建时间
	 * @return String
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

}