//package test.com.kasite.core.elastic;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.impl.nio.reactor.IOReactorConfig;
//import org.apache.http.nio.entity.NStringEntity;
//import org.apache.http.util.EntityUtils;
//import org.elasticsearch.client.Response;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClient.FailureListener;
//
//import com.common.json.JSONObject;
//import com.coreframework.log.LogBody;
//import com.coreframework.log.Logger;
//import com.coreframework.util.AppConfig;
//import com.coreframework.vo.ServiceResult;
//import com.kasite.core.httpclient.http.StringUtils;
//
//import net.sf.json.JSONArray;
//
//public class querydiy {
//	
//	public static final RestClient client = create();
//	
//	private static RestClient create() {
//		String urls = AppConfig.getValue("Es6Url"); //es6地址，比如127.0.0.1:9200
//		List<HttpHost> hosts = new ArrayList<>();
//		for (String url : urls.split(",")) {
//			if (url.isEmpty()) {
//				continue;
//			}
//			String[] ipPort = url.split(":");
//			String ip = ipPort[0];
//			int port = Integer.parseInt(ipPort[1]);
//			hosts.add(new HttpHost(ip, port, "http"));
//		}
//		
//		return RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]))
//				.setRequestConfigCallback(requestConfigBuilder -> {
//					return requestConfigBuilder.setConnectTimeout(AppConfig.getInteger("es.connectionTimeout", 2000))
//							.setSocketTimeout(AppConfig.getInteger("es.socketTimeout", 20000))
//							.setConnectionRequestTimeout(AppConfig.getInteger("es.connectionRequestTimeout", 2000));
//				}).setMaxRetryTimeoutMillis(AppConfig.getInteger("es.maxRetryTimeoutMillis", 60000))
//				.setFailureListener(new FailureListener() {
//					public void onFailure(HttpHost host) {
//						Logger.get().console("#######{}- failed", host.toHostString());
//					}
//				}).setHttpClientConfigCallback(httpClientBuilder -> {
//					return httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom()
//							// 设置异步读取http返回值的线程数为2(执行AbstractIOReactor的线程)，默认是cpu的核心数
//							.setIoThreadCount(2).build());
//				}).build();
//	}
//	
//	/**
//	 * 天时间获取 
//	 */
//	private static List<String[]> findDates(String start, String end) throws Exception {
//		List<String[]> lDate = new ArrayList<String[]>();
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Calendar calBegin = Calendar.getInstance();
//		calBegin.setTime(sd.parse(start));
//		Calendar calEnd = Calendar.getInstance();
//		if ("".equals(end)) {
//			end = sdf.format(new Date());
//		}
//		calEnd.setTime(sd.parse(end));
//		do {
//			String[] times = new String[2];
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(calBegin.getTime());
//			cal.set(Calendar.HOUR_OF_DAY, 0);
//			cal.set(Calendar.MINUTE, 0);
//			cal.set(Calendar.SECOND, 0);
//			cal.set(Calendar.MILLISECOND, 0);
//			times[0] = sdf.format(cal.getTime());
//			cal.set(Calendar.HOUR, 23);
//			cal.set(Calendar.MINUTE, 59);
//			cal.set(Calendar.SECOND, 59);
//			cal.set(Calendar.MILLISECOND, 999);
//			times[1] = sdf.format(cal.getTime());
//			lDate.add(times);
//			calBegin.add(Calendar.DATE, 1);
//		} while (calBegin.compareTo(calEnd) <= 0);
//		lDate.get(0)[0] = start;
//		lDate.get(lDate.size() - 1)[1] = end;
//		return lDate;
//	}
//	
//	public ServiceResult<JSONObject> querySliceDiyLogByPager(String licensekey, String appName, String moduleName,
//			String level, String startTime, String endTime, Map<String, Object> dYN_map, Pager pager) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "查询成功");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			List<String> rlist = new ArrayList<String>();
//			List<String []> dateList = findDates(startTime, endTime);  
//			Collections.reverse(dateList);
//			int alltotal = 0;
//			for (String[] dates : dateList) {
//				try {
//					//时间段查询
//					net.sf.json.JSONObject timeFromTo = new net.sf.json.JSONObject();
//					timeFromTo.put("gte", dates[0]); //>=
//					timeFromTo.put("lte", dates[1]); //<=
//					
//					net.sf.json.JSONObject  time = new net.sf.json.JSONObject();
//					time.put("_inserttime", timeFromTo);
//					
//					net.sf.json.JSONObject range = new net.sf.json.JSONObject();
//					range.put("range", time);
//					
//					//相等查询
//					JSONArray mustArr = new JSONArray();
//					mustArr.add(range);
//					
//					/**
//					 * 查询中 wildcard可以用like和=，加*代表like查询
//					 * term代表=查询
//					 * range代表时间段查询
//					 */
//					//授权码
//					if (!StringUtils.isEmpty(licensekey)) {
//						net.sf.json.JSONObject api = new net.sf.json.JSONObject();
//						api.put("licenseKey", licensekey.trim().toLowerCase());
//						//字段等于查询
//						net.sf.json.JSONObject wildcard = new net.sf.json.JSONObject();
//						wildcard.put("wildcard", api);
//						mustArr.add(wildcard);
//					}
//					//应用名称
//					if (!StringUtils.isEmpty(appName)) {
//						net.sf.json.JSONObject api = new net.sf.json.JSONObject();
//						api.put("_appname", "*" + appName.trim().toLowerCase() + "*" );
//						//字段等于查询
//						net.sf.json.JSONObject wildcard = new net.sf.json.JSONObject();
//						wildcard.put("wildcard", api);
//						mustArr.add(wildcard);
//					}
//					//模块名称
//					if (!StringUtils.isEmpty(moduleName)) {
//						net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
//						obj.put("_modulename", "*" + moduleName.trim().toLowerCase() + "*" );
//						//字段等于查询
//						net.sf.json.JSONObject wildcard = new net.sf.json.JSONObject();
//						wildcard.put("wildcard", obj);
//						mustArr.add(wildcard);
//					}
//					//等级
//					if (!StringUtils.isEmpty(level)) {
//						net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
//						obj.put("_level", "*" + level.trim().toLowerCase() + "*" );
//						//字段等于查询
//						net.sf.json.JSONObject wildcard = new net.sf.json.JSONObject();
//						wildcard.put("wildcard", obj);
//						mustArr.add(wildcard);
//					}
//					
//					JSONArray mustnotArr = new JSONArray();
//					JSONArray shouldArr = new JSONArray();
//					
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key + "】参数值不能为空！");
//						}
//						
//						if ("_linenumber".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								if (value.toString().indexOf(",") == -1) {
//									continue;
//								}
//								String[] millsArr = (v.toString() + " ").split(",");// s,e | s, | ,e
//								String m1 = millsArr[0].trim();
//								String m2 = millsArr[1].trim();
//								net.sf.json.JSONObject millTime = new net.sf.json.JSONObject();
//								millTime.put("gte", m1);
//								if (!StringUtils.isEmpty(m2)) {
//									millTime.put("lte", m2);
//								}
//								
//								net.sf.json.JSONObject mill = new net.sf.json.JSONObject();
//								mill.put(key, millTime);
//								
//								net.sf.json.JSONObject millRange = new net.sf.json.JSONObject();
//								millRange.put("range", mill);
//								
//								if ("UNEQUEL".equals(condition)) {
//									mustnotArr.add(millRange);
//								} else {
//									mustArr.add(millRange);
//								}
//							}
//							continue;
//						} else if ("_code".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								net.sf.json.JSONObject code = new net.sf.json.JSONObject();
//								code.put(key, v);
//								
//								net.sf.json.JSONObject term = new net.sf.json.JSONObject();
//								term.put("term", code);
//								
//								if("UNEQUEL".equals(condition)){
//									mustnotArr.add(term);
//								}else{
//									shouldArr.add(term);
//								}
//							}
//						} else if ("content".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								net.sf.json.JSONObject val = new net.sf.json.JSONObject();
//								val.put(key, "*" + v.toLowerCase() + "*");
//								
//								net.sf.json.JSONObject wild = new net.sf.json.JSONObject();
//								wild.put("wildcard", val);
//								
//								if("UNEQUEL".equals(condition)){
//									mustnotArr.add(wild);
//								}else{
//									shouldArr.add(wild);
//								}
//							}
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								net.sf.json.JSONObject val = new net.sf.json.JSONObject();
//								val.put(key, "*" + v.toLowerCase() + "*");
//								
//								net.sf.json.JSONObject wild = new net.sf.json.JSONObject();
//								wild.put("wildcard", val);
//								if("UNEQUEL".equals(condition)){
//									mustnotArr.add(wild);
//								}else{
//									mustArr.add(wild);
//								}
//							}
//						}
//					}
//					
//					net.sf.json.JSONObject boolJo = new net.sf.json.JSONObject();
//					boolJo.put("must", mustArr);
//					boolJo.put("must_not", mustnotArr);
//					boolJo.put("should", shouldArr);
//					if (shouldArr.size() > 0) {
//						boolJo.put("minimum_should_match", "1");
//					}
//					
//					net.sf.json.JSONObject query = new net.sf.json.JSONObject();
//					query.put("bool", boolJo);
//					
//					net.sf.json.JSONObject queryJo = new net.sf.json.JSONObject();
//					queryJo.put("query", query);
//					if ((pagestart + pageSize) >= 10000) {
//						queryJo.put("from", 0);
//						queryJo.put("size", 0);
//					} else {
//						queryJo.put("from", pagestart);
//						queryJo.put("size", pageSize);
//					}
//					
//					net.sf.json.JSONObject sortJo = new net.sf.json.JSONObject();
//					sortJo.put("_inserttime", "desc");
//					queryJo.put("sort", sortJo);
//					
//					//params是url中的参数，即query部分
//					Map<String, String> params = Collections.singletonMap("pretty", "false");// 可选
//					HttpEntity entity = new NStringEntity(queryJo.toString(), ContentType.APPLICATION_JSON);
//					
//					String INDEX = "diy" + dates[0].substring(5, 7) + dates[0].substring(8, 10);
//					System.out.println(INDEX + "==" + queryJo.toString());
//					//_search也要用POST
//					Response resp = client.performRequest("POST", INDEX + "/_search", params, entity);
//					String ret = EntityUtils.toString(resp.getEntity());
//					
//					net.sf.json.JSONObject retJson = net.sf.json.JSONObject.fromObject(ret);
//					net.sf.json.JSONObject HitJson = retJson.optJSONObject("hits");
//					long total = HitJson.optLong("total");
//					JSONArray hitsArr = HitJson.getJSONArray("hits");
//					if (hitsArr.size() > 0) {
//						for (int i = 0; i < hitsArr.size(); i++) {
//							net.sf.json.JSONObject source = hitsArr.getJSONObject(i).getJSONObject("_source");
//							rlist.add(source.toString());
//						}
//					}
//					if( total >= (pagestart+pageSize)) {
//						pagestart = 0;
//						pageSize = 0;
//					} else if(total > pagestart && total < (pagestart+pageSize)){
//						pagestart = 0;
//						pageSize = (int) (pager.pageSize - rlist.size());
//					} else {
//						pagestart = (int) (pagestart-total);
//					}
//					alltotal += total;
//					System.out.println(total);
//				} catch (Exception e) {
//					e.printStackTrace();
//					if (StringUtils.getException(e).indexOf("No mapping found for") >= 0 ||
//							StringUtils.getException(e).indexOf("no such index") >= 0) {
//						continue;
//					} else {
//						return new ServiceResult<JSONObject>(-1, "查询ES异常，请稍后再试，异常原因：" + StringUtils.getException(e));
//					}
//				}
//			}
//			
//			System.out.println("alltotal===" + alltotal);
//			jo.put("rows", rlist);
//			jo.put("total", alltotal);
//			if(rlist.size()==0 && alltotal !=0){
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, StringUtils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//
//}