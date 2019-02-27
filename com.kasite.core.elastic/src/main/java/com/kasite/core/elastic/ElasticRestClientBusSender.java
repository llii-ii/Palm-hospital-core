package com.kasite.core.elastic;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;


public class ElasticRestClientBusSender {
	
	private String param;
	private String method;
	private String endpoint;
	private RestClient client;
	private boolean scroll = false;
	private String scrollCacheTime = "1m";
	private Map<String, Object> _search;
	private ElasticRestClientUtil utils;
	private String index;
	
	public Map<String, Object> get_search() {
		return _search;
	}
	public ElasticRestClientBusSender set_search(Map<String, Object> _search) {
		this._search = _search;
		this.param = (com.alibaba.fastjson.JSONObject.toJSONString(_search));
		return this;
	}
	public ElasticRestClientBusSender setScrollCacheTime(String scrollCacheTime) {
		this.scrollCacheTime = scrollCacheTime;
		return this;
	}
	public ElasticRestClientBusSender setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}
	public ElasticRestClientBusSender setIndex(String index) {
		this.index = index;
		return this;
	}
	public boolean isScroll(){
		return scroll;
	}
	public ElasticRestClientBusSender setScroll(boolean scroll) {
		this.scroll = scroll;
		return this;
	}
	/**
	 * 设置请求方式类型Post／Get／
	 * @param method
	 */
	public void setMethod(ElasticRestTypeEnum method) {
		this.method = method.name();
	}
	
	public ElasticRestClientBusSender setIps(String ips) {
		this.utils = ElasticRestClientUtil.getInstall(ips);
		this.client = utils.getClient();
		return this;
	}
	public int delete(String id) throws Exception {
		return utils.delete(index, id);
	}
	
//	public ElasticRestClientBusSender setParam(String param) {
//		this.param = param;
//		return this;
//	}

	private String isScroll(String endpoint){
		if(scroll){
			endpoint = endpoint+"?scroll="+scrollCacheTime;
		}
		return endpoint;
	}
	
	public ElasticRestResponse scroll(String _scroll_id,int total,int index) throws Exception{
		ElasticRestResponse resp = new ElasticRestResponse(this);
		long begin=System.currentTimeMillis();
		//params是url中的参数，即query部分
		Map<String, String> params = Collections.singletonMap("pretty", "false");// 结果是否格式化 可选
		String endpoint = "_search/scroll";
		endpoint = isScroll(endpoint);
		endpoint+=("&scroll_id="+_scroll_id);
		Response response = client.performRequest("POST", endpoint , params);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200){
			String result = EntityUtils.toString(response.getEntity());
			resp.setResult(result);
		}
		System.out.println(total +"----------- "+ endpoint + "------------scroll index= "+ index +",耗时："+(System.currentTimeMillis()-begin));
		return resp;
	}
	
	public ElasticRestResponse simpleQuery(String index) throws Exception{
		ElasticRestResponse resp = new ElasticRestResponse(this);
//		long begin=System.currentTimeMillis();
		//params是url中的参数，即query部分
		Map<String, String> params = Collections.singletonMap("pretty", "false");// 结果是否格式化 可选
		HttpEntity entity = new NStringEntity(param, ContentType.APPLICATION_JSON);
		//_search也要用POST
		String endpoint = index+"/_search";
		endpoint = isScroll(endpoint);
		Response response = client.performRequest("POST", endpoint , params, entity);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200){
			String result = EntityUtils.toString(response.getEntity());
			resp.setResult(result);
		}
//		System.out.println("----------simpleQuery-------"+ endpoint +",耗时："+(System.currentTimeMillis()-begin));
		return  resp;
	}
	
	public ElasticRestResponse performRequest() throws Exception{
		ElasticRestResponse resp = new ElasticRestResponse(this);
		long begin=System.currentTimeMillis();
		//params是url中的参数，即query部分
		Map<String, String> params = Collections.singletonMap("pretty", "false");// 结果是否格式化 可选
		HttpEntity entity = new NStringEntity(param, ContentType.APPLICATION_JSON);
		//_search也要用POST
		Response response = client.performRequest( method, endpoint, params, entity);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200){
			String result = EntityUtils.toString(response.getEntity());
			resp.setResult(result);
		}
		return resp;
	}
	
	
}
