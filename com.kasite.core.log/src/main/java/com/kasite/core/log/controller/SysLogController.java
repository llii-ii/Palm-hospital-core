package com.kasite.core.log.controller;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.vo.ServiceResult;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.elastic.ElasticBus;
import com.kasite.core.elastic.ElasticQueryResponseJsonVo;
import com.kasite.core.elastic.ElasticQuerySimpleBool;
import com.kasite.core.elastic.ElasticRangeEnum;
import com.kasite.core.elastic.ElasticRestClientBus;
import com.kasite.core.elastic.ElasticRestClientBusSender;
import com.kasite.core.elastic.ElasticRestResponse;
import com.kasite.core.elastic.ElasticRestTypeEnum;
import com.kasite.core.log.EsUtil;
import com.kasite.core.log.EsUtil.ESIndex;

//@Api(value = "系统用户日志查询")
@RestController
@RequestMapping("/log/api/")
public class SysLogController {
	protected static final Logger logger = LoggerFactory.getLogger(SysLogController.class);
	/**
	 * 请求日志查询
	 */
	@PostMapping("/querySliceCallLogByPager.do")
//	@ApiOperation(value = "Api请求日志查询", notes = "日志查询")
	public R querySliceCallLogByPager(HttpServletRequest request,
			HttpServletResponse response) throws Exception  {
		response.setContentType("application/json;charset=UTF-8");
		R rvJSON = new R();
		String sEcho = request.getParameter("sEcho");
		String apiName = request.getParameter("p_apiName") == null ? "" : request.getParameter("p_apiName").trim();
		String startTime = request.getParameter("p_startTime") == null ? "" : request.getParameter("p_startTime");
		String endTime = request.getParameter("p_endTime") == null ? "" : request.getParameter("p_endTime");
		String licensekey = request.getParameter("licensekey") == null ? "" : request.getParameter("licensekey");
		int pageSize = StringUtil.isEmpty(request.getParameter("iDisplayLength")) ? 0 : Integer.valueOf(request.getParameter("iDisplayLength"));
		int pageIndex = StringUtil.isEmpty(request.getParameter("iDisplayStart")) ? 0 : Integer.valueOf(request.getParameter("iDisplayStart"));
		pageIndex = pageIndex/pageSize+1;
		Pager pager = new Pager(pageIndex, pageSize);
		
		Map<String, String[]> map = request.getParameterMap();
		Map<String, Object> DYN_map = new HashMap<String, Object>();
		for (String key : map.keySet()) {
			if (key.startsWith("DYN_")) {
				Object value = map.get(key)[0].toString();
				key = key.replaceFirst("DYN_", "");
				KasiteConfig.print("动态参数:" + key + "=" + value);
				DYN_map.put(key, value);
			}
		}
		
		ServiceResult<JSONObject> sr = querySliceCallLogByPager(licensekey, 
				apiName, startTime, endTime, DYN_map, pager);
		
		if (sr.getCode() < 0) {
			rvJSON.put("Code", sr.getCode());
			rvJSON.put("Message", sr.getMessage());
			rvJSON.put("iTotalDisplayRecords", 0);
			rvJSON.put("iTotalRecords", 0);
			rvJSON.put("aaData", "[]");
		} else {
			List<String> rlist = new ArrayList<String>();
			if (sr.getResult().getJSONArray("rows").size() > 0) {
				for (int i = 0; i < sr.getResult().getJSONArray("rows").size(); i++) {
					rlist.add(sr.getResult().getJSONArray("rows").get(i).toString());
				}
			}
			rvJSON.put("sEcho", sEcho);
			rvJSON.put("Code", sr.getCode());
			rvJSON.put("Message", "成功");
			rvJSON.put("iTotalDisplayRecords", sr.getResult().get("total"));
			rvJSON.put("iTotalRecords", sr.getResult().get("total"));
			rvJSON.put("aaData", rlist.toString());
		}
		return rvJSON;
	}
	
	/**
	 * 请求日志查询
	 */
	@PostMapping("/querySliceCallLogInfo.do")
//	@ApiOperation(value = "Api请求日志详情查询", notes = "日志详情查询")
	public R querySliceCallLogInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception  {
		response.setContentType("application/json;charset=UTF-8");
		R rvJSON = new R();
		String sEcho = request.getParameter("sEcho");
		String apiName = request.getParameter("p_apiName") == null ? "" : request.getParameter("p_apiName").trim();
		String queryTime = request.getParameter("p_queryTime");
		String[] arr = findDates(queryTime);
		ServiceResult<JSONObject> sr = querySliceCallLogInfo(apiName, arr[0], arr[1]);
		
		if (sr.getCode() < 0) {
			rvJSON.put("Code", sr.getCode());
			rvJSON.put("Message", sr.getMessage());
			rvJSON.put("iTotalDisplayRecords", 0);
			rvJSON.put("iTotalRecords", 0);
			rvJSON.put("aaData", "[]");
		} else {
			List<String> rlist = new ArrayList<String>();
			if (sr.getResult().getJSONArray("rows").size() > 0) {
				for (int i = 0; i < sr.getResult().getJSONArray("rows").size(); i++) {
					rlist.add(sr.getResult().getJSONArray("rows").get(i).toString());
				}
			}
			rvJSON.put("sEcho", sEcho);
			rvJSON.put("Code", sr.getCode());
			rvJSON.put("Message", "成功");
			rvJSON.put("iTotalDisplayRecords", sr.getResult().get("total"));
			rvJSON.put("iTotalRecords", sr.getResult().get("total"));
			rvJSON.put("aaData", rlist.toString());
		}
		return rvJSON;
	}
	
	/**
	 * 查询Api调用日志
	 * @param licensekey
	 * @param apiName
	 * @param startTime
	 * @param endTime
	 * @param dYN_map
	 * @param pager
	 * @return
	 */
	private ServiceResult<JSONObject> querySliceCallLogByPager(String licensekey, String apiName, String startTime,
			String endTime, Map<String, Object> dYN_map, Pager pager) {
		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
		JSONObject jo = new JSONObject();
		int pagestart = pager.start >= 0 ? pager.start : 0;
		int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
		int alltotal = 0;
		boolean isGettedData = false;
		List<JSONObject> rlist = new ArrayList<JSONObject>();
		try {
			List<String []> dateList = findDates(startTime, endTime);
			Collections.reverse(dateList);
			for (String[] dates : dateList) {
				String indexName = EsUtil.getEsIndex(ESIndex.req, dates[0]);
				Map<String, Object> sort = new HashMap<>();
				sort.put("_inserttime", "desc");
				ElasticQuerySimpleBool query = ElasticBus.create().setFrom(pagestart).setSize(pageSize).setSort(sort).
				createSimpleQuery().createElasticQuerySimpleBool()
				.addMustRange(ElasticRangeEnum.gte, "_inserttime", dates[0])
				.addMustRange(ElasticRangeEnum.lte,"_inserttime", dates[1]);
				
				//应用名称
				if (!"".equals(apiName)) {
					query.addMustWildcard("_api", "*" + apiName.trim() + "*");
				}
				// 动态参数
				for (String key : dYN_map.keySet()) {
					Object value = dYN_map.get(key).toString();
					String condition = "";
					if (key.startsWith("UNE")) {//   不等于
						key = key.replaceFirst("UNE", "");
						condition = "UNEQUEL";
					}
					if ("".equals(key)) {
						return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
					}
					if ("".equals(value)) {
						return new ServiceResult<JSONObject>(-1, "【" + key
								+ "】参数值不能为空！");
					}
					
					if ("_paramtype".equals(key) || "_outtype".equals(key)
							|| "_mills".equals(key) 
							|| "_code".equals(key)
							|| "resp_mills".equals(key)) {
						try {
							if ("_mills".equals(key) || "resp_mills".equals(key)) {
								for (String v : value.toString().split("\\|")) {
									if (value.toString().indexOf(",") == -1) {
										continue;
									}
									String[] millsArr = (v.toString() + " ")
											.split(",");// s,e | s, | ,e
									String m1 = millsArr[0].trim();
									String m2 = millsArr[1].trim();
									query.addMustRange(ElasticRangeEnum.gte, key, Long.parseLong(m1))
									.addMustRange(ElasticRangeEnum.lte, key , Long.parseLong(m2));
								}
								continue;
							} else if ("_code".equals(key)) {
								for (String v : value.toString().split("\\|")) {
									if ("UNEQUEL".equals(condition)) {
										query.addMustNotTerm(key, Long.parseLong(v.trim()));//(key.trim(), "*" + v.toString().trim().toLowerCase() + "*");
									} else {
										query.addMustTerm(key.trim(), Long.parseLong(v.trim()));
									}
								}
								continue;
							}
							for (String v : value.toString().split("\\|")) {
								if ("UNEQUEL".equals(condition)) {
									query.addMustNotTerm(key, Long.parseLong(v.trim()));
								} else {
//										boolQueryBuilder.must(queryStringQueryBuilder);
									query.addMustTerm(key, Long.parseLong(v.trim()));
								}
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return new ServiceResult<JSONObject>(-1, "【" + key
									+ "】参数值有误:类型不配置！" + value);
						}
					} else if("_orderid".equals(key)){
						for (String v : value.toString().split("\\|")) {
							query.addShouldMatch(key.trim(), v.trim());
							query.addShouldMatch("param", v.trim());
							query.addShouldMatch("result", v.trim());
						}
					}else {
						for (String v : value.toString().split("\\|")) {
							if ("_clientid".equals(key.trim())) {
								if ("UNEQUEL".equals(condition)) {
									query.addMustNotWildcard(key.trim(), v.toString().trim());
								} else {
									query.addMustWildcard(key.trim(), v.toString().trim());
								}
							} else {
								if ("UNEQUEL".equals(condition)) {
									query.addMustNotWildcard(key.trim(),  "*"+v.toString().trim()+ "*");
								} else {
									query.addMustWildcard(key.trim(), "*" + v.toString().trim() + "*");
								}
							}
							
						}
					}
				}
				
				Map<String, Object> _search = query.getSearch();
				ElasticRestClientBusSender sender = ElasticRestClientBus.create(KasiteConfig.getESUrl(),ElasticRestTypeEnum.POST)
						.set_search(_search);
				ElasticRestResponse resp = sender.simpleQuery(indexName);
				ElasticQueryResponseJsonVo	jsonResp = resp.setParseToJson();
				List<JSONObject> list = jsonResp.get_sources();
				for (JSONObject jsonObject : list) {
					rlist.add(jsonObject);
				}
				long total = jsonResp.getTotal();
				if (!isGettedData) {
					if (total >= (pagestart+pageSize)) {
						pagestart = 0;
						pageSize = 0;
						isGettedData = true;
					} else if (total > pagestart && total < (pagestart+pageSize)) {
						pagestart = 0;
						pageSize = (int) (pager.pageSize - rlist.size());
					} else {
						pagestart = (int) (pagestart-total);
					}
				}
//					KasiteConfig.print("total===" + total);
				alltotal += total;
			}
//				KasiteConfig.print("alltotal===" + alltotal);
			jo.put("rows", rlist);
			jo.put("total", alltotal);
			if (rlist.size() == 0 && alltotal != 0) {
				sr.setCode(999);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询ES数据库异常：",e);
			return new ServiceResult<JSONObject>(-1, e.getMessage());
		}
		sr.setResult(jo);
		return sr;
	}
	


	@PostMapping("/querySliceDiyLogByPager.do")
//	@ApiOperation(value = "自定义日志查询", notes = "日志查询")
	public R querySliceDiyLogByPager(
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("application/json;charset=UTF-8");
		R rvJSON = new R();
		String sEcho = request.getParameter("sEcho");
		String appName = request.getParameter("p_appName") == null ? "" : request.getParameter("p_appName");
		String moduleName = request.getParameter("p_moduleName") == null ? "" : request.getParameter("p_moduleName");
		String level = request.getParameter("p_level") == null ? "" : request.getParameter("p_level");
		String startTime = request.getParameter("p_startTime") == null ? "" : request.getParameter("p_startTime");
		String endTime = request.getParameter("p_endTime") == null ? "" : request.getParameter("p_endTime");
		String licensekey = request.getParameter("licensekey") == null ? "" : request.getParameter("licensekey");
		int pageSize = StringUtil.isEmpty(request.getParameter("iDisplayLength")) ? 0 : Integer.valueOf(request.getParameter("iDisplayLength"));
		int pageIndex = StringUtil.isEmpty(request.getParameter("iDisplayStart")) ? 0 : Integer.valueOf(request.getParameter("iDisplayStart"));
		pageIndex = pageIndex/pageSize+1;
		Pager pager = new Pager(pageIndex, pageSize);
		
		Map<String, String[]> map = request.getParameterMap();
		Map<String, Object> DYN_map = new HashMap<String, Object>();
		for (String key : map.keySet()) {
			if (key.startsWith("DYN_")) {
				Object value = map.get(key)[0].toString();
				key = key.replaceFirst("DYN_", "");
				logger.debug("动态参数:" + key + "=" + value);
				DYN_map.put(key, value);
			}
		}
	
		ServiceResult<JSONObject> sr = querySliceDiyLogByPager(licensekey, appName,
				moduleName, level, startTime, endTime, DYN_map, pager);
		
		List<String> rlist = new ArrayList<String>();
		if (sr.getCode() > 0 && sr.getResult().getJSONArray("rows").size() > 0) {
			for (int i = 0; i < sr.getResult().getJSONArray("rows").size(); i++) {
				rlist.add(sr.getResult().getJSONArray("rows").get(i).toString());
			}
		}
		Object total = 0;
		if(sr.getCode() > 0) {
			total = sr.getResult().get("total");
		}
		rvJSON.put("sEcho", sEcho);
		rvJSON.put("Code", sr.getCode());
		rvJSON.put("Message", "成功");
		rvJSON.put("iTotalDisplayRecords", total);
		rvJSON.put("iTotalRecords", total);
		rvJSON.put("aaData", rlist.toString());
		return rvJSON;
	}
	
	
	public ServiceResult<JSONObject> querySliceDiyLogByPager(String licensekey,
			String appName, String moduleName, String level, String startTime,
			String endTime, Map<String, Object> dYN_map, Pager pager) {
		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1, "");
		List<JSONObject> rlist = new ArrayList<JSONObject>();
		JSONObject jo = new JSONObject();
		boolean isGettedData = false;
		int pagestart = pager.start >= 0 ? pager.start : 0;
		int pageSize = pager.pageSize >= 0 ? pager.pageSize : 0;
		int alltotal = 0;
		try {
			List<String []> dateList = findDates(startTime, endTime);
			Collections.reverse(dateList);
			for (String[] dates : dateList) {
				String indexName = EsUtil.getEsIndex(ESIndex.diy, dates[0]);//EsUtil.ESIndex.diy + dates[0].substring(5, 7);
				Map<String, Object> sort = new HashMap<>();
				sort.put("_inserttime", "desc");
				ElasticQuerySimpleBool query = ElasticBus.create().setFrom(pagestart).setSize(pageSize)
						.setSort(sort)
				.createSimpleQuery().createElasticQuerySimpleBool()
				.addMustRange(ElasticRangeEnum.gte, "_inserttime", dates[0])
				.addMustRange(ElasticRangeEnum.lte,"_inserttime", dates[1]);
				//应用名称
				if (!"".equals(appName)) {
					query.addMustWildcard("_sign", "*" + appName.trim() + "*");
				}
				// 模块名称
				if (!"".equals(moduleName)) {
					query.addMustWildcard("_modulename", "*" + moduleName.trim() + "*");
				}
				// 等级
				if (!"".equals(level)) {
					query.addMustTerm("_level", level);
				}
				// 动态参数
				for (String key : dYN_map.keySet()) {
					Object value = dYN_map.get(key).toString();
					String condition = "";
					if (key.startsWith("UNE")) {//   不等于
						key = key.replaceFirst("UNE", "");
						condition = "UNEQUEL";
					}
					if ("".equals(key)) {
						return new ServiceResult<JSONObject>(-1, "请选择参数名称！");
					}
					if ("".equals(value)) {
						return new ServiceResult<JSONObject>(-1, "【" + key
								+ "】参数值不能为空！");
					}

					if ("_paramtype".equals(key) || "_outtype".equals(key)
							|| "_mills".equals(key) 
							|| "_code".equals(key)
							|| "resp_mills".equals(key)) {
						QueryKey k = QueryKey.valueOf(key.trim());
						if(null != k) {
							if("UNEQUEL".equals(condition)) {
								query.addMustNotTerm(k.name(), value);
							}else{
								query.addMustTerm(k.name(), value);
							}
							continue;
						}
					}
					
					for (String v : value.toString().split("\\|")) {
						
						if("UNEQUEL".equals(condition)) {
							if ("content".equals(key.trim())) {
								query.addMustNotWildcard(key.trim(), "*" + v.toString().trim().toLowerCase() + "*");
							} else {
								query.addMustNotWildcard(key.trim(), "*" + v.toString().trim() + "*");
							}
						}else {
							if ("content".equals(key.trim())) {
								query.addMustWildcard(key.trim(), "*" + v.toString().trim().toLowerCase() + "*");
							} else {
								query.addMustWildcard(key.trim(), "*" + v.toString().trim() + "*");
							}
						}
					}
				}
				
				Map<String, Object> _search = query.getSearch();
				ElasticRestClientBusSender sender = ElasticRestClientBus.create(KasiteConfig.getESUrl(),ElasticRestTypeEnum.POST)
						.set_search(_search);
				ElasticRestResponse resp = sender.simpleQuery(indexName);
				ElasticQueryResponseJsonVo	jsonResp = resp.setParseToJson();
				List<JSONObject> list = jsonResp.get_sources();
				for (JSONObject jsonObject : list) {
					rlist.add(jsonObject);
				}
				long total = jsonResp.getTotal();
				if (!isGettedData) {
					if (total >= (pagestart+pageSize)) {
						pagestart = 0;
						pageSize = 0;
						isGettedData = true;
					} else if (total > pagestart && total < (pagestart+pageSize)) {
						pagestart = 0;
						pageSize = (int) (pager.pageSize - rlist.size());
					} else {
						pagestart = (int) (pagestart-total);
					}
				}
//				KasiteConfig.print("total===" + total);
				alltotal += total;
			}
//			KasiteConfig.print("alltotal===" + alltotal);
			jo.put("rows", rlist);
			jo.put("total", alltotal);
			if (rlist.size() == 0 && alltotal != 0) {
				sr.setCode(999);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询ES数据库异常：",e);
			return new ServiceResult<JSONObject>(-1, e.getMessage());
		}
		sr.setResult(jo);
		return sr;
	}
	
	/**
	 * 查询Api调用日志详情
	 * 
	 * @param licensekey
	 * @param apiName
	 * @param startTime
	 * @param endTime
	 * @param dYN_map
	 * @param pager
	 * @return
	 */
	public ServiceResult<JSONObject> querySliceCallLogInfo(String apiName, String startTime, String endTime) throws Exception{
		apiName = URLDecoder.decode(apiName,"UTF-8");
		ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>(1,"");
		JSONObject jo = new JSONObject();
		List<JSONObject> rlist = new ArrayList<JSONObject>();
		try {
			if(StringUtil.isNotBlank(startTime)) {
				String indexName = EsUtil.getEsIndex(ESIndex.req, startTime);
				Map<String, Object> sort = new HashMap<>();
				sort.put("_inserttime", "desc");
				ElasticQuerySimpleBool query = ElasticBus.create().setFrom(0).setSize(10).setSort(sort).
				createSimpleQuery().createElasticQuerySimpleBool()
				.addMustRange(ElasticRangeEnum.gte, "_inserttime", startTime)
				.addMustRange(ElasticRangeEnum.lte,"_inserttime", endTime);
				
				//应用名称
				if (!"".equals(apiName)) {
					query.addMustWildcard("_api", "*" + apiName.trim() + "*");
				}
				
				Map<String, Object> _search = query.getSearch();
				ElasticRestClientBusSender sender = ElasticRestClientBus.create(KasiteConfig.getESUrl(),ElasticRestTypeEnum.POST)
						.set_search(_search);
				ElasticRestResponse resp = sender.simpleQuery(indexName);
				ElasticQueryResponseJsonVo	jsonResp = resp.setParseToJson();
				List<JSONObject> list = jsonResp.get_sources();
				for (JSONObject jsonObject : list) {
					rlist.add(jsonObject);
				}
			}
			jo.put("rows", rlist);
			if (rlist.size() == 0) {
				sr.setCode(999);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询ES数据库异常：",e);
			return new ServiceResult<JSONObject>(-1, e.getMessage());
		}
		sr.setResult(jo);
		return sr;
	}
	
	/**
	 * 时间获取 
	 */
	private static List<String[]> findDates(String start, String end) throws Exception {
		List<String[]> lDate = new ArrayList<String[]>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(sd.parse(start));
		Calendar calEnd = Calendar.getInstance();
		if ("".equals(end)) {
			end = sdf.format(new Date());
		}
		calEnd.setTime(sd.parse(end));
		do {
			String[] times = new String[2];
			Calendar cal = Calendar.getInstance();
			cal.setTime(calBegin.getTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			times[0] = sdf.format(cal.getTime());
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			times[1] = sdf.format(cal.getTime());
			lDate.add(times);
			calBegin.add(Calendar.DATE, 1);
		} 
		while (calBegin.compareTo(calEnd) <= 0);
		lDate.get(0)[0] = start;
		lDate.get(lDate.size() - 1)[1] = end;
		return lDate;
	}
	
	/**
	 * 时间获取 
	 */
	private static String[] findDates(String queryTime) throws Exception {
		String[] arr = new String[2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.parseLong(queryTime));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, -2);
		arr[0] = sdf.format(cal.getTime());
		cal.add(Calendar.SECOND, 4);
		arr[1] = sdf.format(cal.getTime());
		return arr;
	}
	enum QueryKey{
		_sessionkey,
		_clientid,
		_configkey,
		_appname,
		_classname,
	}
	
}
