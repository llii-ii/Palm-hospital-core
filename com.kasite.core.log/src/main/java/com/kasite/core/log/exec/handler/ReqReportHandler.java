package com.kasite.core.log.exec.handler;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.log.EsUtil;
import com.kasite.core.log.LogDealImpl;
import com.kasite.core.log.RunAlways;
import com.kasite.core.log.exec.QueueHolder;


/**
 * 本地内存里保存了每小时的汇总信息。
 * 每小时的5分以后，或者应用程序关闭的时候，将本地的汇总信息发送到redis中
 * @author youtl
 *
 */
public class ReqReportHandler implements LogDealHandler {
	protected static final Logger logger = LoggerFactory.getLogger(ReqReportHandler.class);
	private long lastSend=0;
	private final long PERIOD=Long.getLong("report.PERIOD", 1000*60*10);//1000*60*10 //10分钟 执行一次
	private Map<String,JSONObject> map=new ConcurrentHashMap<String,JSONObject>();
	private Set<String> hours=new HashSet<String>();
	
	
	/**
	 * 
	 * 列有：
	 * tj_api
	 * tj_clientid
	 * tj_datetime
	 * tj_mills_0_100
	 * tj_mills_100_300
	 * tj_mills_300_500
	 * tj_mills_500_1000
	 * tj_mills_1000_3000
	 * tj_mills_3000_8000
	 * tj_mills_8000
	 * tj_total
	 * totalMills
	 * tj_success_count
	 * tj_app_fail
	 * 
	 * 
	 * @param api
	 * @param clientId
	 * @return
	 */
	private JSONObject getJson(String api,String clientId,String datetime){
		String key=api+"#"+clientId+"#"+datetime;
		JSONObject json=map.get(key);
		if(json!=null){
			return json;
		}
		json=new JSONObject();
		json.put("_tj_api", api);
		json.put("_tj_appId", KasiteConfig.getAppId());
		json.put("_tj_clientid", clientId);
		json.put("_tj_datetime", datetime);
//		add(json,"_tj_mills_0_100", 0);
//		add(cached,"_tj_mills_100_300", 1);
//		add(cached,"_tj_mills_300_500", 1);
//		add(cached,"_tj_mills_500_1000", 1);
//		add(cached,"_tj_mills_1000_3000", 1);
//		add(cached,"_tj_mills_3000_8000", 1);
//		add(cached,"_tj_mills_8000", 1);
//		add(cached,"_tj_total", 1);
//		add(cached,"_totalMills", mills);
//		add(cached,"_tj_app_fail", 1);
		
		
		
		map.put(key, json);
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
	
	public static void main(String[] args) {
		//api
		
	}
	
	
	private void store(List<JSONObject> jsons) {
		for (JSONObject json : jsons) {
			try {
				String api = json.getString("api");
				//这个insertTime只到小时
				String inserttime = json.getString("inserttime");
				String clientid = json.getString("clientid");
				Integer mills = json.getInteger("mills");
				Integer code = json.getInteger("code");
				//修改cached中的值
				JSONObject cached = this.getJson(api,clientid,inserttime);
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
				//添加时间
				this.hours.add(inserttime);
				ApiList.me().setCallInfo(clientid, api, mills, code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
	private QueueHolder queue=new QueueHolder(10000);
	
	@Override
	public void run() {
		RunAlways.run(this);
	}

	@Override
	public boolean addLog(JSONObject jo) {
		return queue.addLog(jo, 2000);
	}

	@Override
	public boolean accept(JSONObject jo) {
		return true;
	}

	@Override
	public String stats() {
		return "req report:"+queue.size();
	}

	@Override
	public boolean isBusy() {
		return queue.size()>5000;
	}
	
	private boolean willSend(){
		if(LogDealImpl.isShutdown()){
			return true;
		}
		if(System.currentTimeMillis()-this.lastSend<PERIOD){
			return false;
		}
		return true;
//		//每小时5分钟之后才发送数据
//		Calendar cal=Calendar.getInstance();
//		return cal.get(Calendar.MINUTE)>=5;
	}
	
	@Override
	public void exec() throws Exception {
		List<JSONObject> jsons=queue.takeBatch(1000);
		if(jsons!=null){
			store(jsons);
//			if(Log.isEnable(Log.ON)){
//				KasiteConfig.print(hours);
//				KasiteConfig.print(map.values());
//			}
		}
		if(!this.willSend()){
			return;
		}
		long time=System.currentTimeMillis();
		send2Es();
		hours.clear();
		map.clear();
		this.lastSend=time;
		logger.debug("send2Es(ms):"+(System.currentTimeMillis()-time));
	}
	//发送数据到redis
	private void send2Es() {
		if(hours.isEmpty()||map.isEmpty()){
			logger.debug("empty msg,so do not send!! hours size:"+hours.size()+",map size:"+map.size());
			return;
		}
		//将报表数据写入到ES数据库中
		try {
			EsUtil.insertBatch(map.values(), EsUtil.ESIndex.report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("接口日志报表数据写入ES库抛出异常！",e);
		}
		
		
		
//		Redis redis=this.getRedis();
//		redis.sadd("req_report_datetime", hours.toArray(new String[hours.size()]));
//		redis.exec(new ReqReportCallBack(map.values()));
//		redis.sadd("req_report_datetime", hours.toArray(new String[hours.size()]));//再做一次，以免中间那步还没执行完，元数据就已经被删除了
	}
	
	

}
