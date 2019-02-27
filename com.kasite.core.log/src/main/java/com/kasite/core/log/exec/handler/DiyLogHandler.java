package com.kasite.core.log.exec.handler;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.log.LogInfo;
import com.kasite.core.log.EsUtil;
import com.kasite.core.log.LogHelper;
import com.kasite.core.log.RunAlways;
import com.kasite.core.log.exec.QueueHolder;

public class DiyLogHandler implements LogDealHandler{
	protected static final Logger logger = LoggerFactory.getLogger(DiyLogHandler.class);
	private static DiyLogHandler inst=new DiyLogHandler();
	private DiyLogHandler(){
		
	}
	private QueueHolder queue=new QueueHolder(10000);
	private int count=0;
	public void insert2ES(List<JSONObject> jsons) throws Exception {
		if(jsons==null||jsons.isEmpty()){
			return;
		}
		boolean print=(++count)%1000==0;
		long t = RunAlways.currentTimeMillis(print);
		List<JSONObject> list=new ArrayList<JSONObject>(jsons.size());
		for(JSONObject json:jsons){
			try {
				LogInfo info= JSON.toJavaObject(json, LogInfo.class);
//				LogInfo info=(LogInfo) JSONObject.toBean(json, LogInfo.class);
				JSONObject s = LogHelper.createDiyLog(info);
				if(s!=null){
					list.add(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("解析日志异常：",e);
				continue;
			} 
		}
		EsUtil.insertBatch(list,EsUtil.ESIndex.diy);
		if(list.size()<200){
			Thread.sleep(Integer.getInteger("es-sleep",1));
		}
		if(print){
			KasiteConfig.print("insert into req es,size:"+jsons.size()+",espand time(ms):"+(System.currentTimeMillis()-t));
		}
	}

	@Override
	public void run() {
		RunAlways.run(this);
	}



	@Override
	public boolean addLog(JSONObject jo) {
		return queue.addLog(jo, 3000);
	}



	@Override
	public boolean accept(JSONObject jo) {
		return true;
	}



	@Override
	public void exec() throws Exception {
		insert2ES(queue.takeBatch2(Integer.getInteger("es-batch",500)));
	}



	@Override
	public String stats() {
		return "diy:"+queue.size();
	}

	@Override
	public boolean isBusy() {
		return queue.size()>5000;
	}

	public static DiyLogHandler get() {
		return inst;
	}
	
	
}
