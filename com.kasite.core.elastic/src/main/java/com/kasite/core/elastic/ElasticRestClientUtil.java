package com.kasite.core.elastic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;

public class ElasticRestClientUtil {
	
	private final static String ES_TYPE = "logtable";
	
	private String ips;
	
	private static Map<String, ElasticRestClientUtil> map;
	
	public static synchronized ElasticRestClientUtil getInstall(String ips)	{
		if(null == map){
			map = new HashMap<String, ElasticRestClientUtil>();
		}
		ElasticRestClientUtil value = map.get(ips);
		if(null == value){
			value = new ElasticRestClientUtil(ips);
			map.put(ips, value);
		}
		return value;
	}
	
	private ElasticRestClientUtil(String ips) {
		this.ips = ips;
		this.client = create(ips);
	}
	public RestClient getClient() {
		return client;
	}
	private RestClient client;
	public Response simpleQuery(String tablename,String queryParam) throws Exception{
		return simpleQuery(tablename, queryParam, false);
	}
	public Response simpleQuery(String tablename,String queryParam,boolean scroll) throws Exception{
		String INDEX = tablename;
		//params是url中的参数，即query部分
		Map<String, String> params = Collections.singletonMap("pretty", "false");// 结果是否格式化 可选
		HttpEntity entity = new NStringEntity(queryParam, ContentType.APPLICATION_JSON);
		//_search也要用POST
		Response resp = client.performRequest("POST", INDEX + "/_search", params, entity);
		return  resp;
	}
	
	public boolean isExists(String tablename) throws Exception{
		//查询索引是否存在  使用HEAD
		try {
			Response resp = client.performRequest("HEAD", tablename);
			return resp.getStatusLine().getReasonPhrase().equals("OK");
		}catch (Exception e) {
			e.printStackTrace();
			closeClient();
			throw e;
		}
	}
	
	private synchronized void closeClient() throws Exception {
		if(null != client) {
			client.close();	
		}
		client = null;
		client = create(ips);
	}
	
	public boolean createIndex(String tablename,String params) throws Exception{
		//params是url中的参数，即query部分
		try {
			Map<String, String> map = Collections.singletonMap("pretty", "false");// 结果是否格式化 可选
			HttpEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON);
			Response resp = client.performRequest("PUT", tablename,map,entity);
			return resp.getStatusLine().getReasonPhrase().equals("OK");
		}catch (Exception e) {
			e.printStackTrace();
			closeClient();
			throw e;
		}
	}
	//默认IO线程数 4 核
	private int ioThreadCount = 4;
	
	public int getIoThreadCount() {
		return ioThreadCount;
	}

	public void setIoThreadCount(int ioThreadCount) {
		this.ioThreadCount = ioThreadCount;
	}

	private synchronized RestClient create(String ips) {
		String[] ip = ips.split(",");
		HttpHost[] hosts = new HttpHost[ip.length];
		for (int i = 0; i < ip.length; i++) {
			String hosurl = ip[i];
			String[] hosipurl = hosurl.split(":");
			hosts[i] = new HttpHost(hosipurl[0],Integer.parseInt(hosipurl[1]),"http");
		}
		/*
		 * 如果想要自动发现集群中的其它es服务器，需要添加 elasticsearch-rest-client-sniffer.jar
		 * 黑名单和自动重试都是RestClient实现的，与HttpClinet无关
		 * RestClient里面有个blacklist（黑名单），黑名单有截止时间。如果全部加入了黑名单，就随机选择一个使用。
		 * socket异常，或者code==502、503、504，RestClient会换host重试（同host是否重试，由httpClient决定）,直到超过maxRetryTimeoutMillis或所有可用路由重试完毕。参见RestClinet的retryIfPossible方法。
		 * 其它类型的异常，比如code==400、404之类的，就不会重试。
		 * 返回值的code<300认为成功，其它的performRequest()都会抛出异常
		 * 
		 */
		RestClientBuilder builder = RestClient.builder(hosts)
				.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
					@Override
					public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
						/*
						 *  设置socket超时时间,connectionRequestTimeout是从连接池获取连接的超时时间
						 *  根据源码可知，如果没有设置以下参数，restClient会采用默认参数，而不是httpclient默认的-1
						 */
						return requestConfigBuilder
								.setConnectTimeout(20000) //ip不可到达时，这个超时才有意义。如果ip可到达，只是port不可用，大概1s就能知道，这个时间没意义
								.setSocketTimeout(30000)
								.setConnectionRequestTimeout(1000);
					}
				}).setMaxRetryTimeoutMillis(10000) // 请求重试的最大时间，超过这个时间，就不再重试了。而不是指整个请求的超时时间（请求超过这个时间是有可能的）
				.setFailureListener(new FailureListener(){
					public void onFailure(HttpHost host) {
						System.out.println(host.toHostString()+"请求失败");
			        }
				})
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					/*
					 * 这里可以设置几乎所有的HttpAsyncClient参数，包括连接池之类的.
					 * 参见RestClientBuilder.createHttpClient().
					 * 开启连接的地方见DefaultConnectingIOReactor
					 * 因为用的是NIO，connectionTime实际上是代码判断，SocketChannel没办法配置（soTimeout可以）。
					 * DefaultIOReactorConfig的配置属于连接池，RequestConfig属于HttpClinet。
					 * 所以RequestConfig会覆盖DefaultIOReactorConfig的配置
					 */
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						return httpClientBuilder
								.setDefaultIOReactorConfig(IOReactorConfig.custom()
										.setConnectTimeout(3000)//这个会被覆盖，没有用
										// 设置异步读取http返回值的线程数为2(执行AbstractIOReactor的线程)，默认是cpu的核心数
										.setIoThreadCount(getIoThreadCount())
										.build());
					}
				});
		return builder.build();
	}
	
	public int delete(String index,String id) throws Exception {
		try {
			if (StringUtil.isBlank(id)) {
				return 0;
			}
			String endpoint = index + "/" + ES_TYPE + "/" + id;
			//入参对象
			Response resp = client.performRequest("DELETE", endpoint);
			String str = EntityUtils.toString(resp.getEntity());
			if(StringUtil.isBlank(resp)) {
				throw new Exception("操作失败，返回值为空");
			}
			JSONObject respJs = JSONObject.parseObject(str);
			if(respJs==null || (respJs.get("_shards")==null)) {
				throw new Exception("操作失败:"+resp);
			}
			JSONObject js = respJs.getJSONObject("_shards");
			return js.getIntValue("successful");
		} catch (ResponseException e) {
			e.printStackTrace();
			closeClient();
			throw e;
		}
	}

	/**
	 * 新增数据进ES
	 * @param index
	 * @param json
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int insert(String index,JSONObject json,String id) throws Exception {
		try {
			if (json == null || json.isEmpty()) {
				return 0;
			}
			Map<String, String> paramMap = new HashMap<String, String>();//url地址上的参数
			paramMap.put("pretty", "true");// 可选
			paramMap.put("type", ES_TYPE);
			String endpoint = index + "/" + ES_TYPE + "/" + id;
			//入参对象
			HttpEntity entity = new NStringEntity(json.toString(), ContentType.APPLICATION_JSON);
			Response resp = client.performRequest("POST", endpoint, paramMap, entity);
			String str = null;
			if(null != resp) {
				str = EntityUtils.toString(resp.getEntity());
			}
			if(StringUtil.isBlank(resp)) {
				throw new Exception("操作失败，返回值为空");
			}
			JSONObject respJs = JSONObject.parseObject(str);
			if(respJs==null || (respJs.get("_shards")==null)) {
				throw new Exception("操作失败:"+resp);
			}
			JSONObject js = respJs.getJSONObject("_shards");
			return js.getIntValue("successful");
		} catch (ResponseException e) {
			e.printStackTrace();
			closeClient();
			throw e;
		}
	}
}
