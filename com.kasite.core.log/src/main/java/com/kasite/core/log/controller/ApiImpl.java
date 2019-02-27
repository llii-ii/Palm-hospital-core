package com.kasite.core.log.controller;
//package com.kasite.client.business.module.logconsumer.controller;
//
//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//import net.sf.json.JSONArray;
//
//import org.apache.commons.lang.StringUtils;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.index.IndexNotFoundException;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.MatchQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.QueryStringQueryBuilder;
//import org.elasticsearch.index.query.RangeQueryBuilder;
//import org.elasticsearch.index.query.TermQueryBuilder;
//import org.elasticsearch.index.query.WildcardQueryBuilder;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.stereotype.Service;
//
//import com.common.json.JSONObject;
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.coreframework.log.LogBody;
//import com.coreframework.log.Logger;
//import com.coreframework.remoting.standard.DateOper;
//import com.coreframework.util.AppConfig;
//import com.coreframework.vo.ServiceResult;
//import com.yihu.apm.comm.Code;
//import com.yihu.apm.dao.Dao;
//import com.yihu.apm.enums.ApmSqlEnum;
//import com.yihu.apm.enums.MyDatabaseEnum;
//import com.yihu.apm.service.IApi;
//import com.yihu.apm.vo.ApmAppInfo;
//import com.yihu.apm.vo.ApmOperationLog;
//import com.yihu.apm.vo.ApmOrg;
//import com.yihu.apm.vo.ApmWarnSubscribe;
//import com.yihu.base.service.ESUtil;
//import com.yihu.das.dbutils.JsonConfig;
//import com.yihu.utils.BaseFunction;
//import com.yihu.utils.CommonLog;
//import com.yihu.utils.Pager;
//import com.yihu.utils.StringUtil;
//import com.yihu.utils.Utils;
//
///**
// * 日志相关Service
// * @author chenzhibin <br> 2017-5-10 上午10:22:30
// */
//@Service
//public class ApiImpl implements IApi {
//	
//	/**
//	 * 时间获取 
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
//	/**
//	 * 根据appInfoId获取appId
//	 * @author chenzhibin <br> 2017-5-16 下午15:51:30 
//	 * @throws Exception 
//	 */
//	private static String getAppId(String appInfoId, String licenseKey) throws Exception {
//		Sql sql = DB.me().createSql(ApmSqlEnum.queryAppInfo);
//		StringBuffer condition = new StringBuffer();
//		if (!StringUtil.isEmpty(licenseKey)) {
//			condition.append(" and licenseKey = ? ");
//			sql.addParamValue(licenseKey);
//		}
//		if (!StringUtil.isEmpty(appInfoId)) {
//			condition.append(" and appInfoId = ? ");
//			sql.addParamValue(appInfoId);
//		}
//		sql.addVar("@condition", condition.toString());
//		
//		ApmAppInfo appInfo = DB.me().queryForBean(MyDatabaseEnum.Apm, sql,
//				ApmAppInfo.class);
//		return appInfo.getAppId();
//	}
//	
//	/**
//	 * 获取该组织ES数据存储方案 
//	 * 1按天存储,2按月存储
//	 * @author chenzhibin <br> 2017-6-7 上午11:07:30
//	 * @throws Exception 
//	 */
//	private static Integer getEsStoreType(String licenseKey) throws Exception {
//		Integer esStoreType = null;
//		Sql sql = DB.me().createSql(ApmSqlEnum.queryApmOrg);
//		StringBuffer condition = new StringBuffer();
//		condition.append(" and licenseKey = ? ");
//		sql.addParamValue(licenseKey);
//		
//		sql.addVar("@condition", condition.toString());
//		
//		ApmOrg apmOrgInfo = DB.me().queryForBean(MyDatabaseEnum.Apm, sql,
//				ApmOrg.class);
//		if (apmOrgInfo != null) {
//			esStoreType = apmOrgInfo.getEsStoreType();
//		}
//		//如果为空，默认为1
//		if (esStoreType == null) {
//			esStoreType = 1;
//		}
//		
//		return esStoreType;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> querySliceCallLogByPager(String licensekey, 
//			String apiName, String startTime, String endTime, Map<String, Object> dYN_map,
//			Pager pager) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			int alltotal = 0;
//			boolean isGettedData = false;
//			TransportClient client = null;
//			List<String> rlist = new ArrayList<String>();
//			client = ESUtil.getClient();
//			List<String []> dateList = findDates(startTime, endTime);
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			Collections.reverse(dateList);
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client
//								.prepareSearch(ESUtil.ES_INDEX_REQ + dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_REQ)
//								.addSort("_inserttime", SortOrder.DESC)
//								.setFrom(pagestart).setSize(pageSize);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_REQ + dates[0].substring(5, 7) + dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_REQ)
//								.addSort("_inserttime", SortOrder.DESC)
//								.setFrom(pagestart).setSize(pageSize);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//							.rangeQuery("_inserttime")
//							.from(dates[0])
//							.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					// 授权码
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey);
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// API名称
//					if (!"".equals(apiName)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("_api", "*" + apiName + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//   不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key
//									+ "】参数值不能为空！");
//						}
//						if ("_paramtype".equals(key) || "_outtype".equals(key)
//								|| "_mills".equals(key) 
//								|| "_code".equals(key)
//								|| "resp_mills".equals(key)) {
//							try {
//								if ("_mills".equals(key) || "resp_mills".equals(key)) {
//									for (String v : value.toString().split("\\|")) {
//										if (value.toString().indexOf(",") == -1) {
//											continue;
//										}
//										String[] millsArr = (v.toString() + " ")
//												.split(",");// s,e | s, | ,e
//										String m1 = millsArr[0].trim();
//										String m2 = millsArr[1].trim();
//										rangeQueryBuilder = null;
//										if (!"".equals(m1)) {
//											rangeQueryBuilder = QueryBuilders
//													.rangeQuery(key)
//													.from(Long.parseLong(m1));
//										}
//										if (!"".equals(m2)) {
//											if (rangeQueryBuilder != null) {
//												rangeQueryBuilder.to(Long.parseLong(m2));
//											} else {
//												rangeQueryBuilder = QueryBuilders
//														.rangeQuery(key)
//														.to(Long.parseLong(m2));
//											}
//										}
//										if (rangeQueryBuilder != null) {
//											if("UNEQUEL".equals(condition)){
//												boolQueryBuilder.mustNot(rangeQueryBuilder);
//											}else{
//												boolQueryBuilder.must(rangeQueryBuilder);
//											}
//										}
//									}
//									continue;
//								} else if ("_code".equals(key)) {
//									for (String v : value.toString().split("\\|")) {
//										TermQueryBuilder termQueryBuilder = QueryBuilders
//												.termQuery(key, Long.parseLong(v.trim()));
//										if("UNEQUEL".equals(condition)){
//											boolQueryBuilder.mustNot(termQueryBuilder);
//										}else{
//											boolQueryBuilder.should(termQueryBuilder);
//											//boolQueryBuilder.must(termQueryBuilder);
//										}
//									}
//									boolQueryBuilder.minimumNumberShouldMatch(1);
//									continue;
//								}
//								for (String v : value.toString().split("\\|")) {
//									QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//											.queryStringQuery("\"" + Long.parseLong(v.trim())+ "\"")
//											.field(key);
//									if ("UNEQUEL".equals(condition)) {
//										boolQueryBuilder.mustNot(queryStringQueryBuilder);
//									} else {
//										boolQueryBuilder.must(queryStringQueryBuilder);
//									}
//								}
//							} catch (NumberFormatException e) {
//								e.printStackTrace();
//								return new ServiceResult<JSONObject>(-1, "【" + key
//										+ "】参数值有误:类型不配置！" + value);
//							}
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								WildcardQueryBuilder wildcardQueryBuilder = null;
//								if ("_clientid".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), v.toString().trim());
//								} else if ("param".equals(key.trim()) || "result".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim().toLowerCase() + "*");
//								} else {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim() + "*");
//								}		
//								
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(wildcardQueryBuilder);
//								} else {
//									boolQueryBuilder.must(wildcardQueryBuilder);
//								}
//							}
//						}
//					}
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try{
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						//e.printStackTrace();
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					long total = responsesearch.getHits().getTotalHits();
//					
//					if (!isGettedData) {
//						if (total >= (pagestart + pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = 0;
//							isGettedData = true;
//						} else if (total > pagestart && total < (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							
//							pagestart = 0;
//							pageSize = (int) (pager.pageSize-rlist.size());
//						}else{
//							pagestart = (int) (pagestart-total);
//						}
//						
//					}
//					KasiteConfig.print("total===" + total);
//					alltotal += total;
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//		
//			
//			for (int i = 0; i < rlist.size(); i++) {
//				try {
//					net.sf.json.JSONObject  row = net.sf.json.JSONObject.fromObject(rlist.get(i));
//
//					//屏蔽隐私类数据
//					String api = row.optString("_api").toString();
//					net.sf.json.JSONObject param = net.sf.json.JSONObject.fromObject(row.optString("param"));
//
//					ResourceBundle resourceBundle = ResourceBundle.getBundle("apiCallLogConf");
//					Enumeration eum = resourceBundle.getKeys();
//					while (eum.hasMoreElements()) {
//						String popKey = (String)eum.nextElement();
//						String popValue =resourceBundle.getString(popKey);
//						net.sf.json.JSONObject joValue = net.sf.json.JSONObject.fromObject(popValue);
//						String value =  joValue.optString("params");
//						String [] values = value.split(",");
//						String otherValue =  joValue.optString("otherParams");
//						if (api != null && api.equals(popKey)) {
//							for (int k = 0; k < values.length; k++) {
//								if (otherValue == null || otherValue.isEmpty()) {
//									if (values[k] != null && !values[k].isEmpty() && param.containsKey(values[k])) {
//										param.put(values[k], "****");
//										row.put("param", param);
//										rlist.set(i, row.toString());
//									}
//								} else {
//									if (values[k] != null && !values[k].isEmpty() && param.containsKey(values[k])) {
//										String otherParam = param.optString(values[k]);
//										if (otherParam != null && !otherParam.isEmpty() && otherParam.contains(otherValue)) {
//
//											String[] others = otherParam.split(",");
//											for (int j = 0; j < others.length; j++) {
//												String shield = others[j].split(":")[0];
//												if (shield !=null && !shield.isEmpty() && shield.equals(otherValue)) {
//													others[j] = otherValue+ ":****";
//												}
//											}
//											String newValue = otherParam;
//											if (others.length > 0) {
//												newValue = StringUtils.join(others, ",");
//											}
//											//KasiteConfig.print("other = "+ newValue);
//											param.put(values[k], newValue);
//											row.put("param", param);
//											rlist.set(i, row.toString());
//
//										}
//									}
//								}
//							}
//							break ;
//						}
//
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					Logger.get().error(e);
//				}
//			}
//			
//			KasiteConfig.print("alltotal===" + alltotal);
//			
//			jo.put("rows", rlist);
//			
//			jo.put("total", alltotal);
//			if(rlist.size() == 0 && alltotal != 0){
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//	
//	@Override
//	public ServiceResult<JSONObject> querySliceDiyLogByPager(String licensekey,
//			String appName, String moduleName, String level, String startTime,
//			String endTime, Map<String, Object> dYN_map, Pager pager) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			int alltotal = 0;
//			boolean isGettedData = false;
//			TransportClient client = null;
//			List<String> rlist = new ArrayList<String>();
//			client = ESUtil.getClient();
//			List<String []> dateList = findDates(startTime, endTime);
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			Collections.reverse(dateList);
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_DIY + dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_DIY)
//								.addSort("_inserttime", SortOrder.DESC)
//								.setFrom(pagestart).setSize(pageSize);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_DIY + dates[0].substring(5, 7) + dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_DIY)
//								.addSort("_inserttime", SortOrder.DESC)
//								.setFrom(pagestart).setSize(pageSize);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//								.rangeQuery("_inserttime")
//								.from(dates[0])
//								.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					//授权码
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey);
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					//应用名称
//					if (!"".equals(appName)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("_appname", "*" + appName.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 模块名称
//					if (!"".equals(moduleName)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("_modulename", "*" + moduleName.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 等级
//					if (!"".equals(level)) {
//						QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//								.queryStringQuery(level)
//								.field("_level");
//						boolQueryBuilder.must(queryStringQueryBuilder);
//					}
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//   不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key
//									+ "】参数值不能为空！");
//						}
//						if ("_linenumber".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								try {
//									if ("_linenumber".equals(key)) {
//										if (v.toString().indexOf(",") == -1) {
//											continue;
//										}
//										String[] lineNumberArr = (v.toString() + " ").split(",");
//										String m1 = lineNumberArr[0].trim();
//										String m2 = lineNumberArr[1].trim();
//										rangeQueryBuilder = null;
//										if (!"".equals(m1)) {
//											rangeQueryBuilder = QueryBuilders
//													.rangeQuery("_linenumber")
//													.from(Long.parseLong(m1));
//										}
//										if (!"".equals(m2)) {
//											if (rangeQueryBuilder != null) {
//												rangeQueryBuilder.to(Long.parseLong(m2));
//											} else {
//												rangeQueryBuilder = QueryBuilders
//														.rangeQuery("_linenumber")
//														.to(Long.parseLong(m2));
//											}
//										}
//										if (rangeQueryBuilder != null) {
//											if("UNEQUEL".equals(condition)){
//												boolQueryBuilder.mustNot(rangeQueryBuilder);
//											}else{
//												boolQueryBuilder.must(rangeQueryBuilder);
//											}
//										}
//										continue;
//									}
//									QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//											.queryStringQuery("\"" + v.toString().toLowerCase() + "\"")
//											.field(key);
//									if("UNEQUEL".equals(condition)){
//										boolQueryBuilder.mustNot(queryStringQueryBuilder);
//									}else{
//										boolQueryBuilder.must(queryStringQueryBuilder);
//									}
//								} catch (NumberFormatException e) {
//									e.printStackTrace();
//									return new ServiceResult<JSONObject>(-1, "【" + key
//											+ "】参数值有误:类型不配置！" + v);
//								}
//							}
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								WildcardQueryBuilder wildcardQueryBuilder = null;
//								if ("content".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim().toLowerCase() + "*");
//								} else {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim() + "*");
//								}		
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(wildcardQueryBuilder);
//								} else {
//									boolQueryBuilder.must(wildcardQueryBuilder);
//								}
//							}
//						}
//					}
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try{
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					long total = responsesearch.getHits().getTotalHits();
//					
//					if (!isGettedData) {
//						if (total >= (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = 0;
//							isGettedData = true;
//						} else if (total > pagestart && total < (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = (int) (pager.pageSize - rlist.size());
//						} else {
//							pagestart = (int) (pagestart-total);
//						}
//						
//					}
//					KasiteConfig.print("total===" + total);
//					alltotal += total;
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//			
//			KasiteConfig.print("alltotal===" + alltotal);
//			jo.put("rows", rlist);
//			jo.put("total", alltotal);
//			if (rlist.size() == 0 && alltotal != 0) {
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> querySliceDBLogByPager(String licensekey, 
//			String appName, String sysid, String ip, String startTime,
//			String endTime, String filter, String optype, String dbtype,
//			boolean isConflictPK, Map<String, Object> dYN_map, Pager pager,
//			boolean descByDate) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			int alltotal = 0;
//			boolean isGettedData = false;
//			TransportClient client = null;
//			List<String> rlist = new ArrayList<String>();
//			client = ESUtil.getClient();
//			//获取应用ID
//			if (!StringUtil.isEmpty(appName)) {
//				appName =  getAppId(appName, licensekey);
//			}
//			List<String []> dateList = findDates(startTime, endTime);  
//			if (descByDate) {
//				Collections.reverse(dateList);
//			}
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_DB 
//										+ dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_DB)
//								.addSort("_inserttime",descByDate?SortOrder.DESC:SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_DB 
//										+ dates[0].substring(5, 7)
//										+ dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_DB)
//								.addSort("_inserttime",descByDate?SortOrder.DESC:SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//								.rangeQuery("_inserttime")
//								.from(dates[0])
//								.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					//应用名称
//					if (!"".equals(appName)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("_appname", "*" + appName.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// licensekey
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 系统名称
//					if (!"".equals(sysid)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("sysid", sysid.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// ip
//					if (!"".equals(ip)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("_ip", ip.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					//数据库类型
//					if (!"".equals(dbtype)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("type", dbtype.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					if(isConflictPK){
//						WildcardQueryBuilder wildcardQueryBuilder1 = QueryBuilders
//								.wildcardQuery("stack", "*重*");
//						WildcardQueryBuilder wildcardQueryBuilder2 = QueryBuilders
//								.wildcardQuery("stack", "*unique*");
//						WildcardQueryBuilder wildcardQueryBuilder3 = QueryBuilders
//								.wildcardQuery("stack", "*duplicate*");
//						boolQueryBuilder.mustNot(wildcardQueryBuilder1);
//						boolQueryBuilder.mustNot(wildcardQueryBuilder2);
//						boolQueryBuilder.mustNot(wildcardQueryBuilder3);
//					}
//					if (!"".equals(filter)) {						
//						if("timeout".equalsIgnoreCase(filter)){
//							//超时条件
//							QueryStringQueryBuilder queryLevelBuilder = QueryBuilders
//									.queryStringQuery("WARN")
//									.field("_level");		
//							boolQueryBuilder.must(queryLevelBuilder);	
//						} else if("exception".equalsIgnoreCase(filter)) {
//							//异常条件
//							QueryStringQueryBuilder queryLevelBuilder = QueryBuilders
//									.queryStringQuery("ERROR")
//									.field("_level");		
//							boolQueryBuilder.must(queryLevelBuilder);		
//							QueryStringQueryBuilder queryResultBuilder = QueryBuilders
//									.queryStringQuery("FAIL")
//									.field("result");		
//							boolQueryBuilder.must(queryResultBuilder);	
//							//忽略主键冲突
//							WildcardQueryBuilder wildcardQueryBuilder1 = QueryBuilders
//									.wildcardQuery("stack", "*重*");
//							WildcardQueryBuilder wildcardQueryBuilder2 = QueryBuilders
//									.wildcardQuery("stack", "*unique*");
//							WildcardQueryBuilder wildcardQueryBuilder3 = QueryBuilders
//									.wildcardQuery("stack", "*duplicate*");
//							boolQueryBuilder.mustNot(wildcardQueryBuilder1);
//							boolQueryBuilder.mustNot(wildcardQueryBuilder2);
//							boolQueryBuilder.mustNot(wildcardQueryBuilder3);
//						}
//					}
//					if (!"".equals(optype)) {
//						QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//								.queryStringQuery(optype)
//								.field("optype");
//						boolQueryBuilder.must(queryStringQueryBuilder);
//					}
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//   不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key
//									+ "】参数值不能为空！");
//						}
//						if ("time".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								if (value.toString().indexOf(",") == -1) {
//									continue;
//								}
//								String[] millsArr = (v.toString() + " ")
//										.split(",");// s,e | s, | ,e
//								String m1 = millsArr[0].trim();
//								String m2 = millsArr[1].trim();
//								rangeQueryBuilder = null;
//								if (!"".equals(m1)) {
//									rangeQueryBuilder = QueryBuilders
//											.rangeQuery(key)
//											.from(Long.parseLong(m1));
//								}
//								if (!"".equals(m2)) {
//									if (rangeQueryBuilder != null) {
//										rangeQueryBuilder.to(Long.parseLong(m2));
//									} else {
//										rangeQueryBuilder = QueryBuilders
//												.rangeQuery(key)
//												.to(Long.parseLong(m2));
//									}
//								}
//								if (rangeQueryBuilder != null) {
//									if("UNEQUEL".equals(condition)){
//										boolQueryBuilder.mustNot(rangeQueryBuilder);
//									}else{
//										boolQueryBuilder.must(rangeQueryBuilder);
//									}
//								}
//							}
//							continue;
//						} else if ("conid".equals(key) || "insert".equals(key) 
//								|| "update".equals(key) || "lineNumber".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								TermQueryBuilder termQueryBuilder = QueryBuilders
//										.termQuery(key, Long.parseLong(v.trim()));
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(termQueryBuilder);
//								} else {
//									boolQueryBuilder.must(termQueryBuilder);
//								}
//							}
//							continue;
//						} else if("db".equals(key.trim())) {
//							QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//									.queryStringQuery(value.toString())
//									.field("db");
//							if ("UNEQUEL".equals(condition)) {
//								boolQueryBuilder.mustNot(queryStringQueryBuilder);
//							} else {
//								boolQueryBuilder.must(queryStringQueryBuilder);
//							}
//						} else if ("sql".equals(key.trim())) {
//							for (String v : value.toString().split("\\|")) {
//								MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery(key.trim(),
//										"*" + v.toString().trim().toLowerCase() + "*");
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(matchQueryBuilder);
//								} else {
//									boolQueryBuilder.must(matchQueryBuilder);
//								}
//							}
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								WildcardQueryBuilder wildcardQueryBuilder = null;
//								if ("sysid".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery("sysid", v.toString().trim());
//								} else {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim() + "*");
//								}		
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(wildcardQueryBuilder);
//								} else {
//									boolQueryBuilder.must(wildcardQueryBuilder);
//								}
//							}
//						}
//					}
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try {
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					long total = responsesearch.getHits().getTotalHits();
//					
//					if (!isGettedData) {
//						if (total >= (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = 0;
//							isGettedData = true;
//						} else if (total > pagestart && total < (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = (int) (pager.pageSize-rlist.size());
//						} else {
//							pagestart = (int) (pagestart-total);
//						}
//						
//					}
//					KasiteConfig.print("total===" + total);
//					alltotal+=total;
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//			
//			KasiteConfig.print("alltotal===" + alltotal);
//			jo.put("rows", rlist);
//			jo.put("total", alltotal);
//			if (rlist.size() == 0 && alltotal != 0) {
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> queryJVMAndThreadAndDB(String licensekey,
//			String sysId, String appInfoId, String ip, String startTime,
//			String endTime) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "查询成功");
//		JSONObject jo = new JSONObject();
//		
//		try {
//			TransportClient client = null;
//			client = ESUtil.getClient();
//			//获取应用ID
//			String appId = "";
//			if (!StringUtil.isEmpty(appInfoId)) {
//				appId =  getAppId(appInfoId, licensekey);
//			}
//			List<String []> dateList = findDates(startTime, endTime);
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			
//			DecimalFormat df = new DecimalFormat("#.00");
//			JSONArray jvmArr = new JSONArray();
//			JSONArray jvmDBArr = new JSONArray();
//			net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
//			List<String> createDateList = new ArrayList<String>();  //时间
//			List<String> jvmGenPercentList = new ArrayList<String>();  //永久代百分比
//			List<String> jvmHeapPercentList = new ArrayList<String>();  //堆内存百分比
//			List<String> threadNumList = new ArrayList<String>();  //线程数
//			List<String> dbNameList = new ArrayList<String>(); //连接池数据库名称
//			//for循环查询
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_JVM 
//								+ dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_JVM)
//								.setSize(10000)
//								.addSort("createDate", SortOrder.ASC);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_JVM 
//								+ dates[0].substring(5, 7) + dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_JVM)
//								.setSize(10000)
//								.addSort("createDate", SortOrder.ASC);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//								.rangeQuery("createDate")
//								.from(dates[0])
//								.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					// 授权码
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 系统ID
//					if (!"".equals(sysId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("sysId", sysId.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					//应用ID
//					if (!"".equals(appId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("appId", appId.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// ip
//					if (!"".equals(ip)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("ip", "*" + ip.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try {
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					
//					//循环获取数据
//					for (Iterator<SearchHit> iterator = responsesearch.getHits()
//							.iterator(); iterator.hasNext();) {
//						SearchHit sh = iterator.next();
//						net.sf.json.JSONObject retJo = net.sf.json.JSONObject.
//								fromObject(sh.getSourceAsString());
//						
//						String createDate = retJo.optString("createDate");
//						String jvmGenMax = retJo.optString("jvmGenMax") == "" ? "0" : retJo.optString("jvmGenMax");
//						String jvmGenMin = retJo.optString("jvmGenMin") == "" ? "0" : retJo.optString("jvmGenMin");
//						String jvmGenPercent = retJo.optString("jvmGenPercent") == "" ? "0" : retJo.optString("jvmGenPercent");
//						String jvmHeapMax = retJo.optString("jvmHeapMax") == "" ? "0" : retJo.optString("jvmHeapMax");
//						String jvmHeapMin = retJo.optString("jvmHeapMin") == "" ? "0" : retJo.optString("jvmHeapMin");
//						String jvmHeapPercent = retJo.optString("jvmHeapPercent") == "" ? "0" : retJo.optString("jvmHeapPercent");
//						String threadNum = retJo.optString("threadNum") == "" ? "0" : retJo.optString("threadNum");
//						
//						obj = new net.sf.json.JSONObject();
//						obj.put("createDate", createDate);
//						obj.put("jvmGenMax", String.valueOf(Long.valueOf(jvmGenMax) / 1024 / 1024));
//						obj.put("jvmGenMin", String.valueOf(Long.valueOf(jvmGenMin) / 1024 / 1024));
//						obj.put("jvmGenPercent", df.format(Double.valueOf(jvmGenPercent) * 100));
//						obj.put("jvmHeapMax", String.valueOf(Long.valueOf(jvmHeapMax) / 1024 / 1024));
//						obj.put("jvmHeapMin", String.valueOf(Long.valueOf(jvmHeapMin) / 1024 / 1024));
//						obj.put("jvmHeapPercent", df.format(Double.valueOf(jvmHeapPercent) * 100));
//						jvmArr.add(obj);
//						
//						//获取数据库连接数
//						JSONArray connArr = retJo.optJSONArray("connections");
//						if (connArr != null && connArr.size() > 0) {
//							for (int k = 0; k < connArr.size(); k++) {
//								net.sf.json.JSONObject arrJson = net.sf.json.JSONObject.fromObject(connArr.getJSONObject(k));
//								obj = new net.sf.json.JSONObject();
//								obj.put("createDate", createDate);
//								obj.put("aliaName", arrJson.optString("aliaName"));
//								obj.put("count", arrJson.optString("count"));
//								obj.put("name", arrJson.optString("name"));
//								obj.put("ip", arrJson.optString("ip"));
//								jvmDBArr.add(obj);
//								if (!dbNameList.contains(arrJson.optString("aliaName"))) {
//									dbNameList.add(arrJson.optString("aliaName"));
//								}
//							}
//						}
//						
//						
//						createDateList.add(createDate);
//						jvmGenPercentList.add(df.format(Double.valueOf(jvmGenPercent) * 100));
//						jvmHeapPercentList.add(df.format(Double.valueOf(jvmHeapPercent) * 100));
//						threadNumList.add(threadNum);
//					}
//					
//					jo.put("createDate", createDateList);
//					jo.put("jvmGenPercent", jvmGenPercentList);
//					jo.put("jvmHeapPercent", jvmHeapPercentList);
//					jo.put("threadNum", threadNumList);
//					jo.put("jvmArr", jvmArr);
//					jo.put("jvmDBArr", jvmDBArr);
//					jo.put("dbName", dbNameList);
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		
//		sr.setResult(jo);
//		return sr;
//	}
//
//	@Override
//	public JSONObject getApmAppMachine(String licensekey, String appId,
//			String ip) throws Exception {
//		Sql sql = DB.me().createSql(ApmSqlEnum.queryApmAppMachineList);
//		
//		StringBuffer condition = new StringBuffer();
//		if (!StringUtil.isEmpty(licensekey)) {
//			condition.append(" and m.licensekey = ? ");
//			sql.addParamValue(licensekey);
//		}
//		if (!StringUtil.isEmpty(appId)) {
//			condition.append(" and m.appId = ? ");
//			sql.addParamValue(appId);
//		}
//		if (!StringUtil.isEmpty(ip)) {
//			condition.append(" and m.ip like ? ");
//			sql.addParamValue("%" + ip + "%");
//		}
//		sql.addVar("@condition", condition.toString());
//		JSONObject jo = DB.me().queryForJson(MyDatabaseEnum.Apm, sql);
//
//		return jo;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> queryOnlineApp(String licensekey,
//			String appInfoId, String ip, String appId, String dataTime) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "查询成功");
//		JSONObject jo = new JSONObject();
//		
//		try {
//			TransportClient client = null;
//			client = ESUtil.getClient();
//			List<String> rList = new ArrayList<String>();
//			List<String> ipList = new ArrayList<String>(); //用来ip去重，取最新数据
//			List<String []> dateList = findDates(dataTime, "");
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			
//			//for循环查询
//			for (String[] dates : dateList) {
//				SearchRequestBuilder builder = null;
//				if (esStoreType == 2) {
//					builder = client.prepareSearch(ESUtil.ES_INDEX_JVM 
//							+ dates[0].substring(5, 7))
//							.setTypes(ESUtil.ES_TYPE_JVM)
//							.setSize(100)
//							.addSort("createDate", SortOrder.DESC);
//				} else {
//					builder = client.prepareSearch(ESUtil.ES_INDEX_JVM 
//							+ dates[0].substring(5, 7) + dates[0].substring(8, 10))
//							.setTypes(ESUtil.ES_TYPE_JVM)
//							.setSize(100)
//							.addSort("createDate", SortOrder.DESC);
//				}
//				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//				// 时间范围
//				RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//							.rangeQuery("createDate")
//							.from(dates[0])
//							.to(dates[1]);
//				if (rangeQueryBuilder != null) {
//					boolQueryBuilder.must(rangeQueryBuilder);
//				}
//				// 授权码
//				if (!StringUtil.isEmpty(licensekey)) {
//					WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//							.wildcardQuery("licenseKey", licensekey.trim());
//					boolQueryBuilder.must(wildcardQueryBuilder);
//				}
//				// 系统ID
//				if (!StringUtil.isEmpty(appInfoId)) {
//					WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//							.wildcardQuery("appInfoId", appInfoId.trim());
//					boolQueryBuilder.must(wildcardQueryBuilder);
//				}
//				//应用ID
//				if (!StringUtil.isEmpty(appId)) {
//					WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//							.wildcardQuery("appId", appId.trim());
//					boolQueryBuilder.must(wildcardQueryBuilder);
//				}
//				// ip
//				if (!StringUtil.isEmpty(ip)) {
//					WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//							.wildcardQuery("ip", "*" + ip.trim() + "*");
//					boolQueryBuilder.must(wildcardQueryBuilder);
//				}
//
//				if (boolQueryBuilder.hasClauses()) {
//					builder.setQuery(boolQueryBuilder);
//				}
//				KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//				SearchResponse responsesearch;
//				try {
//					responsesearch = builder.execute().actionGet();
//				} catch (Exception e) {
//					responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//				}
//
//				//循环获取数据
//				for (Iterator<SearchHit> iterator = responsesearch.getHits()
//						.iterator(); iterator.hasNext();) {
//					SearchHit sh = iterator.next();
//					net.sf.json.JSONObject retJo = net.sf.json.JSONObject.
//							fromObject(sh.getSourceAsString());
//					if (!ipList.contains(retJo.optString("ip"))) {
//						ipList.add(retJo.optString("ip"));
//						rList.add(retJo.toString());
//					}
//				}
//
//				jo.put("rows", rList);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		
//		sr.setResult(jo);
//		return sr;
//	}
//	
//	
//	@Override
//	public ServiceResult<JSONObject> queryApmAppInfoMobile(String licenseKey,
//			 String sysId, String appInfoId, String searchVal) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "查询成功");
//		JSONObject jo = new JSONObject();
//		
//		try {
//			Sql sql = DB.me().createSql(ApmSqlEnum.queryApmAppInfoList);
//			
//			StringBuffer condition = new StringBuffer();
//			if (!StringUtil.isEmpty(licenseKey)) {
//				condition.append(" and a.licenseKey = ? ");
//				sql.addParamValue(licenseKey);
//			}
//			if (!StringUtil.isEmpty(sysId)) {
//				condition.append(" and a.sysId = ? ");
//				sql.addParamValue(sysId);
//			}
//			if (!StringUtil.isEmpty(appInfoId)) {
//				condition.append(" and a.appInfoId = ? ");
//				sql.addParamValue(appInfoId);
//			}
//			if (!StringUtil.isEmpty(searchVal)) {
//				condition.append(" and (a.appId like '%" + searchVal + "%'");
//				condition.append(" or a.appName like '%" + searchVal + "%'");
//				condition.append(" or b.sysName like '%" + searchVal + "%')");
//			}
//			sql.addVar("@condition", condition.toString());
//			
//			JsonConfig cfg = new JsonConfig(); 
//			cfg.setResultPropertyName("rows");
//			sql.setUseLimitSql();
//			jo = DB.me().queryForJson(MyDatabaseEnum.Apm, sql, cfg);
// 
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		
//		sr.setResult(jo);
//		return sr;
//	}
//	
//	@Override
//	public Integer saveApmWarnSubscribeMobile(ApmWarnSubscribe bean) {
//		try {
//			//判断该用户是否已经订阅该应用如果有则进行修改，否则新增
//			ApmWarnSubscribe queryBean = new ApmWarnSubscribe();
//			queryBean.setUserId(bean.getUserId());
//			queryBean.setAppInfoId(bean.getAppInfoId());
//			ApmWarnSubscribe warnSub = Dao.getApmWarnSubscribe(queryBean);
//			Integer code = null;
//			if (warnSub == null) {
//				//新增
//				Timestamp nowTime = DateOper.getNowDateTime();
//				bean.setCreateDate(nowTime);
//				bean.setLastUpdateDate(nowTime);
//				code = Dao.saveApmWarnSubscribe(bean);
//				
//				//保存操作日志
//				ApmOperationLog log=new ApmOperationLog();
//				log.setUserID(bean.getCreateUserId());
//				log.setUserName(bean.getCreateUserName());
//				log.setOperationType(ApmOperationLog.Constant.OPERATIONLOG_TYPE_ADD);
//				log.setOperationTable(ApmOperationLog.Constant.APM_WARN_SUBSCRIBE_TABLE);
//				log.setOperationContent(net.sf.json.JSONObject.fromObject(bean).toString());
//				log.setOperationKey(bean.getWarnSubId());           
//				CommonLog.addLog(log);
//			} else {
//				//修改
//				
//				//查询修改前数据
//				queryBean = new ApmWarnSubscribe();
//				queryBean.setWarnSubId(warnSub.getWarnSubId());
//				ApmWarnSubscribe oldBean = Dao.getApmWarnSubscribe(queryBean);
//				
//				//修改
//				bean.setWarnSubId(warnSub.getWarnSubId());
//				bean.setLastUpdateDate(DateOper.getNowDateTime());
//				code = Dao.updateApmWarnSubscribe(bean);
//				
//				//查询修改后数据
//				queryBean = new ApmWarnSubscribe();
//				queryBean.setWarnSubId(warnSub.getWarnSubId());
//				ApmWarnSubscribe newBean = Dao.getApmWarnSubscribe(queryBean);
//				
//				String operatorContent = BaseFunction.makeCompareInfo(oldBean, newBean);
//				if (operatorContent.length() > 0) {
//					//保存操作日志
//					ApmOperationLog log=new ApmOperationLog();
//					log.setUserID(bean.getCreateUserId());
//					log.setUserName(bean.getCreateUserName());
//					log.setOperationType(ApmOperationLog.Constant.OPERATIONLOG_TYPE_UPDATE);
//					log.setOperationTable(ApmOperationLog.Constant.APM_WARN_SUBSCRIBE_TABLE);
//					log.setOperationContent(operatorContent);
//					log.setOperationKey(bean.getWarnSubId());           
//					CommonLog.addLog(log);
//				}
//			}
//			return code;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Code.ERROR;
//		}
//	}
//	
//	@Override
//	public JSONObject queryApmWarnSubscribeMobile(String licenseKey, String userId) throws Exception {
//		Sql sql = DB.me().createSql(ApmSqlEnum.queryApmWarnSubscribeListMobile);
//		
//		StringBuffer condition = new StringBuffer();
//		if (!StringUtil.isEmpty(licenseKey)) {
//			condition.append(" and w.licenseKey = ? ");
//			sql.addParamValue(licenseKey);
//		}
//		if (!StringUtil.isEmpty(userId)) {
//			condition.append(" and u.userId = ? ");
//			sql.addParamValue(userId);
//		}
//		
//		sql.addVar("@condition", condition.toString());
//		
//		JsonConfig cfg = new JsonConfig();
//		cfg.setTotalPropertyName("total");
//		cfg.setResultPropertyName("appinfoidRows");
//		sql.setUseLimitSql();
//		JSONObject jo = DB.me().queryForJson(MyDatabaseEnum.Apm, sql, cfg);
//
//		return jo;
//	}
//	
//	@Override
//	public JSONObject getApmWarnSubscribeMobile(String licenseKey, String userId,
//			String appInfoId) throws Exception {
//		Sql sql = DB.me().createSql(ApmSqlEnum.getApmWarnSubscribeMobile);
//		
//		StringBuffer condition = new StringBuffer();
//		if (!StringUtil.isEmpty(licenseKey)) {
//			condition.append(" and  licenseKey = ? ");
//			sql.addParamValue(licenseKey);
//		}
//		if (!StringUtil.isEmpty(userId)) {
//			condition.append(" and  userId = ? ");
//			sql.addParamValue(userId);
//		}
//		if (!StringUtil.isEmpty(appInfoId)) {
//			condition.append(" and  appInfoId = ? ");
//			sql.addParamValue(appInfoId);
//		}
//		
//		sql.addVar("@condition", condition.toString());
//		
//		JsonConfig cfg = new JsonConfig();
//		cfg.setTotalPropertyName("total");
//		cfg.setResultPropertyName("rows");
//		sql.setUseLimitSql();
//		JSONObject jo = DB.me().queryForJson(MyDatabaseEnum.Apm, sql, cfg);
//
//		return jo;
//	}
//
//	@Override
//	public JSONObject getApmAppCollecyionConfig(String appInfoId,
//			String licenseKey) throws Exception {
//		Sql sql = DB.me().createSql(ApmSqlEnum.queryApmAppCollectionConfigValiList);
//		
//		StringBuffer condition = new StringBuffer();
//		if (!StringUtil.isEmpty(licenseKey)) {
//			condition.append(" and  licenseKey = ? ");
//			sql.addParamValue(licenseKey);
//		}
//		if (!StringUtil.isEmpty(appInfoId)) {
//			condition.append(" and  appInfoId = ? ");
//			sql.addParamValue(appInfoId);
//		}
//		
//		sql.addVar("@condition", condition.toString());
//		
//		JsonConfig cfg = new JsonConfig();
//		cfg.setTotalPropertyName("total");
//		cfg.setResultPropertyName("rows");
//		sql.setUseLimitSql();
//		JSONObject jo = DB.me().queryForJson(MyDatabaseEnum.Apm, sql, cfg);
//
//		return jo;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> querySliceDBCommonLogByPager(
//			String licensekey, String appInfoId, String sysid, String ip,
//			String startTime, String endTime, Map<String, Object> dYN_map,
//			Pager pager, boolean descByDate) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			int alltotal = 0;
//			boolean isGettedData = false;
//			TransportClient client = null;
//			String appId = "";
//			List<String> rlist = new ArrayList<String>();
//			client = ESUtil.getClient();
//			//获取应用ID
//			if (!StringUtil.isEmpty(appInfoId)) {
//				appId =  getAppId(appInfoId, licensekey);
//			}
//			List<String []> dateList = findDates(startTime, endTime);  
//			if (descByDate) {
//				Collections.reverse(dateList);
//			}
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_SQL 
//										+ dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_SQL)
//								.addSort("createDate", descByDate ? SortOrder.DESC : SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_SQL 
//										+ dates[0].substring(5, 7)
//										+ dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_SQL)
//								.addSort("createDate", descByDate ? SortOrder.DESC : SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//								.rangeQuery("createDate")
//								.from(dates[0])
//								.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					//应用ID
//					if (!"".equals(appId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("appId", "*" + appId.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					//应用主键ID
//					if (!"".equals(appInfoId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("appInfoId", appInfoId.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// licensekey
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 系统名称
//					if (!"".equals(sysid)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("sysId", sysid.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// ip
//					if (!"".equals(ip)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("ip", "*" + ip.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//   不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key
//									+ "】参数值不能为空！");
//						}
//						if ("time".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								if (value.toString().indexOf(",") == -1) {
//									continue;
//								}
//								String[] millsArr = (v.toString() + " ")
//										.split(",");// s,e | s, | ,e
//								String m1 = millsArr[0].trim();
//								String m2 = millsArr[1].trim();
//								rangeQueryBuilder = null;
//								if (!"".equals(m1)) {
//									rangeQueryBuilder = QueryBuilders
//											.rangeQuery(key)
//											.from(Long.parseLong(m1));
//								}
//								if (!"".equals(m2)) {
//									if (rangeQueryBuilder != null) {
//										rangeQueryBuilder.to(Long.parseLong(m2));
//									} else {
//										rangeQueryBuilder = QueryBuilders
//												.rangeQuery(key)
//												.to(Long.parseLong(m2));
//									}
//								}
//								if (rangeQueryBuilder != null) {
//									if("UNEQUEL".equals(condition)){
//										boolQueryBuilder.mustNot(rangeQueryBuilder);
//									}else{
//										boolQueryBuilder.must(rangeQueryBuilder);
//									}
//								}
//							}
//							continue;
//						} else if ("conid".equals(key) || "insert".equals(key) 
//								|| "update".equals(key) || "lineNumber".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								TermQueryBuilder termQueryBuilder = QueryBuilders
//										.termQuery(key, Long.parseLong(v.trim()));
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(termQueryBuilder);
//								} else {
//									boolQueryBuilder.must(termQueryBuilder);
//								}
//							}
//							continue;
//						} else if("db".equals(key.trim())) {
//							QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders
//									.queryStringQuery(value.toString())
//									.field("db");
//							if ("UNEQUEL".equals(condition)) {
//								boolQueryBuilder.mustNot(queryStringQueryBuilder);
//							} else {
//								boolQueryBuilder.must(queryStringQueryBuilder);
//							}
//						} else if ("sql".equals(key.trim())) {
//							for (String v : value.toString().split("\\|")) {
//								MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery(key.trim(),
//										"*" + v.toString().trim().toLowerCase() + "*");
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(matchQueryBuilder);
//								} else {
//									boolQueryBuilder.must(matchQueryBuilder);
//								}
//							}
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								WildcardQueryBuilder wildcardQueryBuilder = null;
//								if ("sysid".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery("sysid", v.toString().trim());
//								} else {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim() + "*");
//								}		
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(wildcardQueryBuilder);
//								} else {
//									boolQueryBuilder.must(wildcardQueryBuilder);
//								}
//							}
//						}
//					}
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try {
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					long total = responsesearch.getHits().getTotalHits();
//					
//					if (!isGettedData) {
//						if (total >= (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = 0;
//							isGettedData = true;
//						} else if (total > pagestart && total < (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = (int) (pager.pageSize-rlist.size());
//						} else {
//							pagestart = (int) (pagestart-total);
//						}
//						
//					}
//					KasiteConfig.print("total===" + total);
//					alltotal+=total;
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//			
//			KasiteConfig.print("alltotal===" + alltotal);
//			jo.put("rows", rlist);
//			jo.put("total", alltotal);
//			if (rlist.size() == 0 && alltotal != 0) {
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//
//	@Override
//	public ServiceResult<JSONObject> querySliceWebLogByPager(String licensekey,
//			String appInfoId, String sysid, String ip, String startTime,
//			String endTime, Map<String, Object> dYN_map, Pager pager,
//			boolean descByDate) {
//		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
//		JSONObject jo = new JSONObject();
//		try {
//			int pagestart = pager.start >= 0 ? pager.start : 0;
//			int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
//			int alltotal = 0;
//			boolean isGettedData = false;
//			TransportClient client = null;
//			String appId = "";
//			List<String> rlist = new ArrayList<String>();
//			client = ESUtil.getClient();
//			//获取应用ID
//			if (!StringUtil.isEmpty(appInfoId)) {
//				appId =  getAppId(appInfoId, licensekey);
//			}
//			List<String []> dateList = findDates(startTime, endTime);  
//			if (descByDate) {
//				Collections.reverse(dateList);
//			}
//			//获取ES数据存储方案(1按天存储,2按月存储)
//			Integer esStoreType = AppConfig.getInteger("esStoreType", 1);
//			
//			for (String[] dates : dateList) {
//				try {
//					SearchRequestBuilder builder = null;
//					if (esStoreType == 2) {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_WEB 
//										+ dates[0].substring(5, 7))
//								.setTypes(ESUtil.ES_TYPE_WEB)
//								.addSort("createDate", descByDate ? SortOrder.DESC : SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					} else {
//						builder = client.prepareSearch(ESUtil.ES_INDEX_WEB 
//										+ dates[0].substring(5, 7)
//										+ dates[0].substring(8, 10))
//								.setTypes(ESUtil.ES_TYPE_WEB)
//								.addSort("createDate", descByDate ? SortOrder.DESC : SortOrder.ASC)
//								.setFrom(pagestart).setSize(pageSize);
//					}
//					BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//					// 时间范围
//					RangeQueryBuilder rangeQueryBuilder = QueryBuilders
//								.rangeQuery("createDate")
//								.from(dates[0])
//								.to(dates[1]);
//					if (rangeQueryBuilder != null) {
//						boolQueryBuilder.must(rangeQueryBuilder);
//					}
//					//应用名称
//					if (!"".equals(appId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("appId", "*" + appId.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					//应用主键ID
//					if (!"".equals(appInfoId)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("appInfoId", appInfoId.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// licensekey
//					if (!"".equals(licensekey)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("licenseKey", licensekey.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 系统名称
//					if (!"".equals(sysid)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("sysId", sysid.trim());
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// ip
//					if (!"".equals(ip)) {
//						WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders
//								.wildcardQuery("ip", "*" + ip.trim() + "*");
//						boolQueryBuilder.must(wildcardQueryBuilder);
//					}
//					// 动态参数
//					for (String key : dYN_map.keySet()) {
//						Object value = dYN_map.get(key).toString();
//						String condition = ""; 
//						if (key.startsWith("UNE")) {//   不等于
//							key = key.replaceFirst("UNE", "");
//							condition = "UNEQUEL";
//						}
//						if ("".equals(key)) {
//							return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
//						}
//						if ("".equals(value)) {
//							return new ServiceResult<JSONObject>(-1, "【" + key
//									+ "】参数值不能为空！");
//						}
//						if ("time".equals(key)) {
//							for (String v : value.toString().split("\\|")) {
//								if (value.toString().indexOf(",") == -1) {
//									continue;
//								}
//								String[] millsArr = (v.toString() + " ")
//										.split(",");// s,e | s, | ,e
//								String m1 = millsArr[0].trim();
//								String m2 = millsArr[1].trim();
//								rangeQueryBuilder = null;
//								if (!"".equals(m1)) {
//									rangeQueryBuilder = QueryBuilders
//											.rangeQuery(key)
//											.from(Long.parseLong(m1));
//								}
//								if (!"".equals(m2)) {
//									if (rangeQueryBuilder != null) {
//										rangeQueryBuilder.to(Long.parseLong(m2));
//									} else {
//										rangeQueryBuilder = QueryBuilders
//												.rangeQuery(key)
//												.to(Long.parseLong(m2));
//									}
//								}
//								if (rangeQueryBuilder != null) {
//									if("UNEQUEL".equals(condition)){
//										boolQueryBuilder.mustNot(rangeQueryBuilder);
//									}else{
//										boolQueryBuilder.must(rangeQueryBuilder);
//									}
//								}
//							}
//							continue;
//						} else {
//							for (String v : value.toString().split("\\|")) {
//								WildcardQueryBuilder wildcardQueryBuilder = null;
//								if ("sysid".equals(key.trim())) {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery("sysid", v.toString().trim());
//								} else {
//									wildcardQueryBuilder = QueryBuilders
//											.wildcardQuery(key.trim(), "*" + v.toString().trim() + "*");
//								}		
//								if ("UNEQUEL".equals(condition)) {
//									boolQueryBuilder.mustNot(wildcardQueryBuilder);
//								} else {
//									boolQueryBuilder.must(wildcardQueryBuilder);
//								}
//							}
//						}
//					}
//					if (boolQueryBuilder.hasClauses()) {
//						builder.setQuery(boolQueryBuilder);
//					}
//					KasiteConfig.print(boolQueryBuilder.hasClauses() + "===\n" + builder.toString());
//					SearchResponse responsesearch;
//					try {
//						responsesearch = builder.execute().actionGet();
//					} catch (Exception e) {
//						responsesearch = builder.setFrom(0).setSize(0).execute().actionGet();
//					}
//					long total = responsesearch.getHits().getTotalHits();
//					
//					if (!isGettedData) {
//						if (total >= (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = 0;
//							isGettedData = true;
//						} else if (total > pagestart && total < (pagestart+pageSize)) {
//							for (Iterator<SearchHit> iterator = responsesearch.getHits()
//									.iterator(); iterator.hasNext();) {
//								SearchHit sh = iterator.next();
//								rlist.add(sh.getSourceAsString());
//							}
//							pagestart = 0;
//							pageSize = (int) (pager.pageSize-rlist.size());
//						} else {
//							pagestart = (int) (pagestart-total);
//						}
//						
//					}
//					KasiteConfig.print("total===" + total);
//					alltotal+=total;
//				} catch (IndexNotFoundException e) {
//					KasiteConfig.print(Utils.getException(e));
//					continue;
//				}
//			}
//			
//			KasiteConfig.print("alltotal===" + alltotal);
//			jo.put("rows", rlist);
//			jo.put("total", alltotal);
//			if (rlist.size() == 0 && alltotal != 0) {
//				sr.setCode(999);
//			}
//		} catch (Exception e) {
//			Logger.get().error(
//					new LogBody()
//							.set("类：", this.getClass().getName())
//							.set("方法：",
//									Thread.currentThread().getStackTrace()[1]
//											.getMethodName()).set("异常：", e));
//			return new ServiceResult<JSONObject>(-1, Utils.getException(e));
//		}
//		sr.setResult(jo);
//		return sr;
//	}
//}
