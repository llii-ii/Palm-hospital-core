package com.kasite.core.log;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.log.exec.handler.LogDealHandler;
@Service
public class LogDealImpl implements LogDeal{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final static Pattern ln=Pattern.compile("\r\n");
	private final long startup=System.currentTimeMillis();
	private AtomicLong handled=new AtomicLong(0);
	private static boolean shutdowned=false;
	public static boolean isShutdown(){
		return shutdowned;
	}
	public static void shutdown(){
		shutdowned=true;
	}
	@Override
	public boolean isBusy() {
//		if(shutdowned){
//			Log.info("busy beause of shutdowned");
//			return true;
//		}
//		if(Log.isEnable(Log.ON)){
//			try{
//				Log.debug("startup:"+DateOper.formatDate(new Date(startup), "yyyy-MM-dd HH:mm:ss"));
//				long t= ((System.currentTimeMillis()-startup)/1000);
//				long count=handled.get();
//				Log.debug("spend second:"+t+",total handled:"+count+",average(second):"+count*1d/t);
//				Log.debug(HandlerFactory.status());
//			}catch(Exception e){
//				
//			}
//		}
//		if(HandlerFactory.isBusy()){
//			Log.info("busy!!!!!!!!!!!\n{}",HandlerFactory.status());
//			return true;
//		}
		return false;
	}

	@Override
	public String handle(String log ,String fileName) {
		if(shutdowned){
			logger.info("FAILED beause of shutdowned");
			return "FAILED";
		}
		try{
			List<JSONObject> jsons=parseJson(log);
			if(jsons!=null&&jsons.size()>0){
				logger.debug("log file :{},record size:{}",fileName,jsons.size());
				for(JSONObject json:jsons){
					LogDealHandler h=HandlerFactory.getHandler(json);
					boolean added=h.addLog(json);
					if(!added){
						logger.info("################add log timeout!!!");
						return "ERROR";
					}
					handled.incrementAndGet();
				}
			}
			return this.isBusy()?"OK#BUSY":"OK#OK";
		}catch(Throwable e){
			e.printStackTrace();
			//为了避免客户端死循环，因为数据错误是不可修复的
			if(net.sf.json.JSONException.class.isInstance(e)){
				return "OK#FAILED";
			}
			return "FAILED";
		}
	}

	private List<JSONObject> parseJson(String log) {
		if(log==null||log.isEmpty()){
			return null;
		}
		String[] js=ln.split(log);
		List<JSONObject> list=new ArrayList<JSONObject>(js.length);
		for(String j:js){
			if(StringUtils.isEmpty(j)){
				continue;
			}
			list.add(JSON.parseObject(j));
		}
		return list;
	}
//	
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) {
//		Log.setDebug();
//		LogDealImpl deal=new LogDealImpl();
//		deal.handle("{\"appenderFileName\":\"\",\"content\":\"{\\\"请求IP：\\\":\\\"192.168.53.43\\\",\\\"URI：\\\":\\\"http://192.168.53.43:8080/wxmgr/token/getJsTicket?oriId=gh_c408d026965c\\\",\\\"时间：\\\":\\\"2016-06-08 13:41:40\\\"}\",\"currTimes\":1465364500116,\"header\":{\"appName\":\"wxmgr\",\"className\":\"com.yihu.wxmgr.controller.BaseController\",\"ip\":\"10.37.129.2\",\"lineNumber\":24,\"methodName\":\"logRequest\",\"path\":\"/Applications/sts-bundle/pivotal-tc-server-developer-3.1.2.RELEASE/base/wtpwebapps/wxmgr\"},\"level\":\"ERROR\",\"moduleName\":\"wxmgr\"}", 
//					"fileName");
//	}

}
