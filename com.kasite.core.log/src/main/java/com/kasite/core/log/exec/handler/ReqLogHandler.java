package com.kasite.core.log.exec.handler;


import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.log.LogHelper;
import com.kasite.core.log.LogKey;
import com.kasite.core.log.RunAlways;
import com.kasite.core.log.exec.QueueHolder;
import com.kasite.core.log.warn.WarnHandler;


/**
 * 接口网关日志的入口。本入口处理告警
 * @author youtl
 *
 */
public class ReqLogHandler implements LogDealHandler {

	private QueueHolder queue=new QueueHolder(10000);
	private ReqEsHandler esHandler=new ReqEsHandler();
	private ReqReportHandler reportHandler=new ReqReportHandler();
	
	Executor pool = new ThreadPoolExecutor(20, 100,
			30, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(100));
	
	@Override
	public void run() {
		KstHosConstant.cachedThreadPool.execute(new Thread(reportHandler,"req report handler"));//.start();
		KstHosConstant.cachedThreadPool.execute(new Thread(esHandler,"req es handler"));
		RunAlways.run(this);
	}
	
	@Override
	public boolean addLog(JSONObject jo) {
		return queue.addLog(jo,5000);
	}
	@Override
	public boolean accept(JSONObject jo) {
		return "wsgw-log".equals(jo.getString("moduleName"));
	}


	@Override
	public void exec() throws Exception {
		JSONObject jo=this.queue.take();
		if(jo==null){
			return;
		}
		jo=LogHelper.createLog(jo);
		if(jo==null){
			return;
		}
		
		RunAlways.submit(new WarnRunner(jo,this), pool);
		while(true){
			if(esHandler.addLog(jo)){
				break;
			}
		}
	}

	//处理告警消息
	private static class WarnRunner implements Runnable{
		JSONObject jo;
		ReqLogHandler h;
		
		public WarnRunner(JSONObject jo,ReqLogHandler handler) {
			this.jo = jo;
			this.h=handler;
		}

		@Override
		public void run() {
			//转化为report支持的json格式，这样report可以直接插入
			try{
				String api = jo.getString(LogKey.api.toString());
				if(StringUtil.isBlank(api)) {
					api = "";
				}
				String currtimes = jo.getString(LogKey.inserttime);//yyyy-MM-dd HH:mm:ss
				String clientid = jo.getString(LogKey.clientid);
				int mills = jo.getIntValue(LogKey.mills);//ms
				int code = jo.getIntValue(LogKey.code.toString());//
				String wsgwurl = jo.getString(LogKey.wsgwurl.toString());
				
				JSONObject jsobj = new JSONObject();
				jsobj.put("api", api);
				jsobj.put("inserttime", currtimes.substring(0, 10));
				jsobj.put("clientid", clientid);
				jsobj.put("mills", mills);
				jsobj.put("code", code);
				jsobj.put("wsgwurl", wsgwurl);
				
				while(true){
					if(h.reportHandler.addLog(jsobj)){
						break;
					}
				}
//				//告警
//				if(api.split("\\.").length != 3){//a.b.c
//					Log.info("length of [{}] is not right",api);
//					return;
//				}
				Date datetime = DateOper.parse(currtimes);
				WarnHandler warnHandler = SpringContextUtil.getBean(WarnHandler.class);
				warnHandler.deal(mills, api, code, datetime,false);
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String stats() {
		return "req log:"+queue.size()+","+this.esHandler.stats();
	}

	@Override
	public boolean isBusy() {
		return queue.size()>2000||this.esHandler.isBusy();
	}
}
