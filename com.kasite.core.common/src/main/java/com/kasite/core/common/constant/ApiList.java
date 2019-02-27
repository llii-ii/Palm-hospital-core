package com.kasite.core.common.constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.DateOper;

public class ApiList {
	protected static final Logger logger = LoggerFactory.getLogger(ApiList.class);
	private static ApiList me = new ApiList();
	private Map<String, List<ApiMethodName>> map = new HashMap<>();
	
	/**
	 * 统计每天的调用数据
	 * key = date value = <key = api | value=callInfo>
	 * 每 1 分钟 往硬盘中刷一次数据当天的数据
	 */
	private Map<String,JSONObject> callInfoMap = new HashMap<>();
	private Map<String, String> mapApi = new HashMap<>();
	
	/**
	 * 获取调用日志统计
	 * @return
	 */
	public Map<String,JSONObject> getCallReport(){
		return this.callInfoMap;
	}
	/**
	 * 返回所有的接口信息
	 * @return
	 */
	public Map<String, List<ApiMethodName>> getAllApiModule(){
		return map;
	}
	
	private JSONObject getJson(String api,String clientId,String datetime) throws Exception{
		String key= clientId+"#"+api;
		String date = DateOper.getNow("yyyyMMdd");
		JSONObject m = callInfoMap.get(date);
		if(m == null) {
			m = new JSONObject();
			callInfoMap.put(date, m);
		}
		JSONObject json= m.getJSONObject(key);
		if(json!=null){
			return json;
		}
		json=new JSONObject();
		json.put("_tj_api", api);
//		json.put("_tj_appId", KasiteConfig.getAppId());
		json.put("_tj_clientid", clientId);
		json.put("_tj_datetime", datetime);
		m.put(key, json);
		return json;
	}
	/**
	 * 修改json中的值，增加它的数目
	 * @param json
	 * @param key
	 * @param added
	 */
	private void add(JSONObject json,String key,long added){
		//改变为0的，不需要记录
		if(added==0){
			return;
		}
		Long v= json.getLongValue(key);
		v+=added;
		json.put(key, v);
	}
	
	public void setCallInfo(String clientId,String api,long mills,int code) throws Exception {
		//修改cached中的值
		String _inserttime = DateOper.getNow("yyyy-MM-dd");//.getnow
		JSONObject cached = this.getJson(api,clientId,_inserttime);
		if (0 <= mills && mills < 100) {
			add(cached,"_tj_mills_0_100", 1);
		} else if (100 <= mills && mills < 300) {
			add(cached,"_tj_mills_100_300", 1);
		} else if (300 <= mills && mills < 500) {
			add(cached,"_tj_mills_300_500", 1);
		} else if (500 <= mills && mills < 1000) {
			add(cached,"_tj_mills_500_1000", 1);
		} else if (1000 <= mills && mills < 3000) {
			add(cached,"_tj_mills_1000_3000", 1);
		} else if (3000 <= mills && mills < 8000) {
			add(cached,"_tj_mills_3000_8000", 1);
		} else if (8000 <= mills) {
			add(cached,"_tj_mills_8000", 1);
		}
		add(cached,"_tj_total", 1);
		add(cached,"_totalMills", mills);
		if (code == -14444 || code == 30000 || code == 500 ) {
			//这类异常都需要告警
			add(cached,"_tj_app_fail", 1);
		}else{
			add(cached,"_tj_success_count", 1);
		}
		if (code == 10000) {
			add(cached,"_tj_exec_success_count", 1);
			add(cached,"_tj_exec_success_mills", mills);
		}else {
			//如果不是10000 都记录失败
			add(cached,"_tj_fail", 1);
		}
//		//添加时间
//		this.hours.add(inserttime);
		run();
	}
	
	private ApiList() {
		
	} 
	public static ApiList me() {
		return me;
	}
	public void addApi(String moduleName,ApiMethodName methodName) {
		List<ApiMethodName> apiList = map.get(moduleName);
		if(null == apiList) {
			apiList = new ArrayList<>();
		}
		String api = methodName.getName();
		String[] apis = api.split("\\.");
		if(apis.length > 2) {
			mapApi.put(apis[0]+"."+apis[2], api);
		}
		apiList.add(methodName);
		map.put(moduleName, apiList);
	}
	/**
	 * 根据模块获取对应的 api 列表
	 * @param key
	 * @return
	 */
	public List<ApiMethodName> getModuleApiList(ApiMethodName key){
		return map.get(key.getModuleName());
	}
	
	/**
	 * 获取API全路径  a.b.c
	 * 通过 a.c 的方式获取
	 * @return 
	 */
	public String getApi(String module,String methodName) {
		return mapApi.get(module+"."+methodName);
	}
	static {
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
				try {
//					Thread.sleep(10000);
					logger.info("读取Api统计日志文件====================");
					read();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("读取统计日志文件异常",e);
				}
//			}
//		});
//		t.setName("ReadApiReport");
//		t.start();
	}
	private static void read() throws Exception {
		File f = new File(getPath());
		if(f.exists()) {
			byte[] b = Files.readAllBytes(Paths.get(getPath()));
			if(null != b && b.length > 0) {
				String str = new String(b,"utf-8");
				logger.info(str);
				JSONObject json = JSONObject.parseObject(str);
				String date = DateOper.getNow("yyyyMMdd");
				JSONObject jsonObj = json.getJSONObject(date);
				if(null != jsonObj) {
					me().callInfoMap.put(date, jsonObj);
				}
			}
		}
	}
	private static String getPath() throws ParseException {
		String nowDate = DateOper.getNow("yyyyMMdd");
		StringBuffer dir = KasiteConfig.getLocalConfigPathByName("tj_call_api_info",false,true,false);
		dir.append(File.separator).append(nowDate).append(".json");
		return dir.toString();
	}
	public JSONObject apiJson() {
		JSONObject json = new JSONObject();
		for (Entry<String, List<ApiMethodName>> entry : map.entrySet()) {
			List<ApiMethodName> list = entry.getValue();
			for (ApiMethodName m : list) {
				json.put(m.getName(), m.getApiName());
			}
		}
		return json;
		
	}
	
	public void print() {
		KasiteConfig.print(apiJson().toJSONString());
	}
	private static long times = 0;
	private static boolean isRun = false;
	public void run() {
		try {
			if(!isRun) {
				isRun = true;
				if(times == 0 || System.currentTimeMillis() - times > 60000) {
					times = System.currentTimeMillis();
					
					File file = new File(getPath());
					OutputStream out = null;
					OutputStreamWriter fw = null;
					try {
						out = new FileOutputStream(file);
						fw = new OutputStreamWriter(out, "utf-8");
						fw.write(JSON.toJSONString(callInfoMap));
						fw.flush();
			            fw.close();
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("保存调用客户端的配置信息异常。");
					}finally {
						if(null != fw) {
							fw.close();
						}
						if(null != out) {
							out.close();
						}
					}
					String removeDateKey = DateOper.formatDate(DateOper.getAddDays(-1),"yyyyMMdd");
					callInfoMap.remove(removeDateKey);
				}
				isRun = false;
			}
		}catch (Exception e) {
			logger.error("统计接口调用日志异常",e);
			isRun = false;
		}
		
		
		
	}
	
	
	
	
}
