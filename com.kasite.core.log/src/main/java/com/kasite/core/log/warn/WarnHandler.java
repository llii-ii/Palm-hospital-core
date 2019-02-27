package com.kasite.core.log.warn;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.sys.handler.IWarnNotifyHandler;
import com.kasite.core.common.util.ExpiryMap;
import com.yihu.wsgw.api.InterfaceMessage;
@Component("warnHandler")
public class WarnHandler {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MSG);
	private static long timeout = 29000;// 超时阀值时间 29秒
	private static int times = 60;//每分钟 超过29秒请求达到 60次的时候发送告警
	private final static int KeyLifeDelay = 1000 * 60;//redis key 生命周期延迟N秒结束
	protected static final Logger logger = LoggerFactory.getLogger(WarnHandler.class);
	private static ExpiryMap<String, Long> cacheMap = new ExpiryMap<>();
	private static List<IWarnNotifyHandler> notifyList = new ArrayList<>();
	public void addWarnNotify(IWarnNotifyHandler notify) {
		notifyList.add(notify);
	}
	
	static {
//		try {
//			long t = KasiteConfig.getApiTimeout()
//			if (t > 0) {
//				timeout = t;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	/**
	 * 告警类型
	 * 返回未知错误 500 -14444 异常
	 * 并且 20000 <= code <= 30000 异常代码在 20000 -- 30000 间也发送 
	 */
	enum WarnType{
		warntype_01,
		warntype_02,
		warntype_03,
		;
	}
	/**
	 * @param time
	 *            耗时
	 * @param api
	 *            API名称a.b.c
	 * @param code
	 *            (20000<=code && code<=30000)||code==-14444 异常
	 * @param datetime
	 * 			  时间           
	 * @return String
	 * @throws Exception 
	 */
	public void deal(long time, String api, int code, Date datetime,boolean isWSGW) throws Exception {
		//调用接口网关的，不加入告警
		if(api.startsWith("WSGW.")){
			logger.info("{} ignore because is WSGW",api);
			return;
		}
		// api预警
		dealWarn(api, time, code, datetime);
		
		//
		WarnType type = null;
		boolean isSend = false;
		//异常
		if( (20000<=code && code<=30000)|| code==-14444 || code== 500 ) {
			//立即发送告警 一级异常发送告警
			type = WarnType.warntype_01;
			isSend = true;
		}
		//29s请求超时 每分钟达到 60 发送告警 
		if(time > timeout) {
			String key = api+"_"+WarnType.warntype_02;
			Long t = cacheMap.get(key);
			if(null == t) {
				t = 0l;
			}
			t = t++;
			cacheMap.put(key, t, KeyLifeDelay);
			if(t > times) {
				type = WarnType.warntype_02;
				isSend = true;
			}
		}
		
		if(notifyList.size() > 0 && isSend && null != type) {
			InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(getClass(), api, 
					null, null, null, "WarnHandler", null,null);
			String appId = KasiteConfig.getAppId();
			String title = "接口日志异常告警";
			switch (type) {
			case warntype_01:
				title = "("+api+":"+code+")系统发生未知异常";
				break;
			case warntype_02:
				title = "("+api+")接口出现大量超时";
				break;
			default:
				break;
			}
			String remark = "";
			String webUrl = KasiteConfig.getKasiteHosWebAppUrl();
			//http://192.168.202.148:8670/module/log/warnLog.html?_api={0}&_code={1}&_dayTime={2}&_endTime={3}
			String url = MessageFormat.format(webUrl + "/module/log/warnLog.html?_api={0}&_code={1}&_dayTime={2}&_endTime={3}", api,code,
					DateOper.getNow("yyyy-MM-dd")+" 00:00:00",DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
			JSONObject json = new JSONObject();
			json.put("url", url);
			json.put("title", title);
			for (IWarnNotifyHandler notify : notifyList) {
				notify.notify(json);
			}
			
			
//			ReqMaintenanceMsg t = new ReqMaintenanceMsg(msg, appId, title, color, level, remark, ip);
//			t.setUrl(url);
//			LogUtil.info(log, "发送告警消息："+JSON.toJSONString(t));
//			CommonReq<ReqMaintenanceMsg> reqSendMsg = new CommonReq<ReqMaintenanceMsg>(t);
//			msgService.sendMaintenancenMsg(reqSendMsg);
		}
//		
//		dealWarn(api.split("\\.")[0], time, code, datetime);// 子系统预警
//		if(isWSGW){
//			dealWarn("WSGW", time, code, datetime);// 接口网关预警,只有接口网关调用别人才告警。别人调它就不管了
//		}
		
	}
	
	/**
	 * 告警处理
	 * @param warnObject
	 * @param time
	 * @param code -14444 需要立即发  500 需要立即发  其它的累计 1分钟 超过 60 发
	 * @param datetime
	 * @return
	 */
	private void dealWarn(String api, long time, int code, Date datetime) {
		
		
		
		
		
	}

//	/**
//	 * 告警
//	 * @param warnType
//	 * @param warnObject
//	 * @param datetime
//	 * @param keyLifeSeconds
//	 * @param warnValue
//	 * @param appendWarnMsg
//	 * @return
//	 * @throws ParseException 
//	 */
//	private static void warn(String warnType, final String warnObject, Date datetime,
//			int keyLifeSeconds, int warnValue, String appendWarnMsg,boolean isDay) throws Exception {
//		String dateTimeStr = DateOper.formatDate(datetime, "yyyyMMddHHmm");
//		boolean daywarn=keyLifeSeconds>3600; // true表示是按天的预警
//		if(daywarn){//按每分
//			dateTimeStr = DateOper.formatDate(datetime, "yyyyMMdd");
//		}
//		String key = warnType + "," + warnObject + "," + dateTimeStr;// redis key
//		long value = CountUtil.doCount(key, keyLifeSeconds);// 当前Redis量
//		if(warnValue != 0 && value>=warnValue){//有异常
//			String v2String=CountUtil.getValue(key);
////				Log.debug("key:{},value from incr:{},from get:"+v2String,key,value);
//			Long v2=Long.parseLong(v2String);
//			if(value>v2){
//				Log.error("########key:"+key+",value from incr:"+value+",from get:"+v2String);
//				value=v2;
//			}
//		}
//		String timeKey=makeTimeKey(warnType,warnObject);
//		if (warnValue != 0 && value >= warnValue&&CountUtil.exceed(timeKey, 300*ConfigUtil.getInstance().getWarnInterval())) {
//			WarnLog warnLog = new WarnLog();
//			warnLog.setWarnObject(warnObject);
//			String warnLogDT = DateOper.formatDate(datetime, "yyyy-MM-dd HH:mm");
//			warnLog.setDesc(warnObject + appendWarnMsg + "[" + value + "/"
//					+ warnValue + "]"+warnLogDT);
//			warnLog.setInsertTime(System.currentTimeMillis());
//			warnLog.setLimit(warnValue);
//			warnLog.setValue(value);
//			// warnLog.setWarnBy(warnByList);
//			warnLog.setWarnType(warnType);
//			warnLog.setKey(key);
//			sendWarnMQ(warnLog,timeKey,isDay);
//			CountUtil.setLastTime(timeKey, System.currentTimeMillis());
//		}
//	}

//	/**
//	 * 发送告警消息队列
//	 * 
//	 * @param warnLog
//	 * @throws Exception
//	 */
//	private static void sendWarnMQ(WarnLog warnLog,String timeKey,boolean isDay) throws Exception {
//		JSONObject jsobj = JSONObject.fromObject(warnLog);
//		String msg=jsobj.toString();
//		ServerConsole.client.send("warns-log", msg);
//		Log.debug("warn:{}",msg);
//	}
//	
//	private static String makeTimeKey(String warnType, String warnObject){
//		return "warn_"+warnType+"_"+warnObject;
//	}

}
