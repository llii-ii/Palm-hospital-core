package com.kasite.core.httpclient.http;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
public class HttpClientManager  {
	private static HttpClientManager instance;
	private final static Object object = new Object();
	private static ConcurrentHashMap<String, CloseableHttpClient> poolMap;
	//默认值
	public final static int DEFAULT_HTTP_MAXTOTAL = HttpRequestConfigVo.DEFAULT_HTTP_MAXTOTAL; 
	public final static int DEFAULT_HTTP_MAXPERROUTE = HttpRequestConfigVo.DEFAULT_HTTP_MAXPERROUTE; 
	public final static int DEFAULT_HTTP_MAXROUTE = HttpRequestConfigVo.DEFAULT_HTTP_MAXROUTE; 
	public final static int DEFAULT_HTTP_TIMEOUT = HttpRequestConfigVo.DEFAULT_HTTP_TIMEOUT; 
	public final static int DEFAULT_HTTP_EXECUTIONCOUNT = HttpRequestConfigVo.DEFAULT_HTTP_EXECUTIONCOUNT; 
	public final static int DEFAULT_HTTP_CONNECTTIMEOUT = HttpRequestConfigVo.DEFAULT_HTTP_CONNECTTIMEOUT;//连接超时
	public final static int DEFAULT_HTTP_SOCKETTIMEOUT = HttpRequestConfigVo.DEFAULT_HTTP_SOCKETTIMEOUT;
	public HttpRequestConfigVo getDefaultHttpRequestConfig(){
		HttpRequestConfigVo vo = new HttpRequestConfigVo();
		/**最大连接数 */
		Integer http_maxTotal = DEFAULT_HTTP_MAXTOTAL;
		/**每个路由基础的连接 */
		Integer http_maxPerRoute = DEFAULT_HTTP_MAXPERROUTE;
		/**目标主机的最大连接数 */
		Integer http_maxRoute = DEFAULT_HTTP_MAXROUTE;
		/**请求的超时 */
		Integer http_timeOut = DEFAULT_HTTP_TIMEOUT;
		/**请求重试次数 */
		Integer http_executionCount = DEFAULT_HTTP_EXECUTIONCOUNT;
		vo.setHttp_executionCount(http_executionCount);
		vo.setHttp_maxPerRoute(http_maxPerRoute);
		vo.setHttp_maxRoute(http_maxRoute);
		vo.setHttp_maxTotal(http_maxTotal);
		vo.setHttp_timeOut(http_timeOut);
		return vo;
	}
	
	public void config(HttpRequestBase httpRequestBase, Integer timeOut,Integer connectTimeout,Integer socketTimeout) {
		// 设置Header等
		// httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
		// httpRequestBase
		// .setHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// httpRequestBase.setHeader("Accept-Language",
		// "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
		// httpRequestBase.setHeader("Accept-Charset",
		// "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
		if(null == timeOut || 0<= timeOut){
			timeOut = DEFAULT_HTTP_TIMEOUT;
		}
		if(null == connectTimeout || 0<= connectTimeout){
			connectTimeout = DEFAULT_HTTP_CONNECTTIMEOUT;
		}
		if(null == socketTimeout || 0<= socketTimeout){
			socketTimeout = DEFAULT_HTTP_SOCKETTIMEOUT;
		}
		/*
connectionRequestTimout：指从连接池获取连接的timeout
connetionTimeout：指客户端和服务器建立连接的timeout， 
就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
socketTimeout：指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
		 */
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(socketTimeout).build();
		httpRequestBase.setConfig(requestConfig);
	}
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @throws Exception 
	 * @create 2017年06月30日
	 */
	public CloseableHttpClient createHttpClient(String clientKey,HttpRequestConfigVo config,String url) throws Exception {
		return createHttpClient(clientKey,config.getHttp_maxTotal(), config.getHttp_maxPerRoute(), config.getHttp_maxRoute(), config.getHttp_executionCount(), url,false);
	}
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @throws Exception 
	 * @create 2017年06月30日
	 */
	public CloseableHttpClient createHttpClient(String clientKey,String url) throws Exception {
		return createHttpClient(clientKey,DEFAULT_HTTP_MAXTOTAL, DEFAULT_HTTP_MAXPERROUTE, DEFAULT_HTTP_MAXROUTE, DEFAULT_HTTP_EXECUTIONCOUNT, url,false);
	}
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @throws Exception 
	 * @create 2017年06月30日
	 */
	public CloseableHttpClient createHttpClient(String clientKey,int maxTotal,int maxPerRoute, int maxRoute,final int http_executionCount,String url) throws Exception {
		return createHttpClient(clientKey,maxTotal, maxPerRoute, maxRoute, http_executionCount, url,false);
	}
	private static String getMapKey(String url){
		return "HTTP_"+url;
	}
	
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @throws Exception 
	 * @create 2017年06月30日
	 */
	public void close(String clientKey) throws Exception {
		String key = getMapKey(clientKey);
		CloseableHttpClient client = poolMap.remove(key);
		if(null != client){
			client.close();
		}
	}
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @throws Exception 
	 * @create 2017年06月30日
	 */
	public CloseableHttpClient createHttpClient(String clientKey,Integer maxTotal,Integer maxPerRoute, Integer maxRoute, Integer http_executionCount,String url,boolean ishttps) throws Exception {
		String key = getMapKey(clientKey);
		CloseableHttpClient client = poolMap.get(key);
		if(null == client){
			synchronized (object) {
				try{
					//如果传入的值 == 0  则使用默认的参数。
					if(null == maxTotal || maxTotal <= 0){
						maxTotal = DEFAULT_HTTP_MAXTOTAL;
					}
					if(null == maxPerRoute || maxPerRoute <= 0){
						maxPerRoute = DEFAULT_HTTP_MAXPERROUTE;
					}
					if(null == maxRoute || maxRoute <= 0){
						maxRoute = DEFAULT_HTTP_MAXROUTE;
					}
					if(null == http_executionCount || http_executionCount <= 0){
						http_executionCount = DEFAULT_HTTP_EXECUTIONCOUNT;
					}
					client = createHttpClient2(maxTotal, maxPerRoute, maxRoute, http_executionCount, url, ishttps);
					poolMap.put(key, client);
				}catch(Exception e){
					e.printStackTrace();
					throw e;
				}
			}
		}
		return client;
	}
	
	//关闭某个URL的连接
	public void closeHttpClient(String url) throws Exception{
		try{
			String key = getMapKey(url);
			CloseableHttpClient client = poolMap.remove(key);
			if(null != client){
				client.close();
			}
			client = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author daiys
	 * @create 2017年06月30日
	 */
	private static CloseableHttpClient createHttpClient2(int maxTotal,int maxPerRoute, int maxRoute,int http_executionCount,String url,boolean ishttps) {
//		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory(); 
		ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
		Registry<ConnectionSocketFactory> registry = null;
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		registry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", plainsf)
				.register("https", sslsf) 
				.build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
//		 //创建目标主机连接
//        HttpHost httpHost = HttpHost.create(url);
////		// 将目标主机的最大连接数增加
//		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandlerImpl(http_executionCount);
		//创建客户端
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();
		
		return httpClient;
	}
	static {
		synchronized (object) {
			Version.setVersion();
			instance = new HttpClientManager();
		}
	}
	private HttpClientManager() {
		poolMap = new ConcurrentHashMap<String, CloseableHttpClient>();
	}
	public static HttpClientManager getInstance() {
		return instance;
	}
	public void printVersion() {
		System.out.println(System.getProperty("vers.http.httpclientmanager"));
	}

	/**
	 * 异常字符串处理方法
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionStack(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}
}
