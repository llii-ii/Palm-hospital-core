package com.kasite.core.log.exec.handler;
//package com.yihu.exec.handler;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.sf.json.JSONObject;
//
//import com.yihu.exec.QueueHolder;
//import com.yihu.exec.RunAlways;
//import com.yihu.framework.util.Log;
//import com.yihu.main.EsUtil;
//
//public class PushLogHandler implements LogDealHandler{
//	
//	private QueueHolder queue=new QueueHolder(5000);
//	private String extractDate(String d){
//		if(d==null||d.isEmpty()){
//			return null;
//		}
//		if(d.length()<=19){
//			return d;
//		}
//		return d.substring(0,19);
//	}
//	public void insert2DB(List<JSONObject> logs) throws Exception {
//		if(logs==null||logs.isEmpty()){
//			return;
//		}
//		List<JSONObject> list=new ArrayList<JSONObject>(logs.size());
//		for(JSONObject log:logs){
//			Map<String,Object> jo=new HashMap<String,Object>();
//			String context=log.getString("content");
//			String header=log.getString("header");
//			JSONObject json=JSONObject.fromObject(context);
//			jo.put("msgId",json.optString("msgId"));
//			jo.put("msgType",json.optInt("msgType"));
//			jo.put("appId",json.optString("appId"));
//			jo.put("appName",json.optString("appName"));
//			jo.put("pushUrl",json.optString("pushUrl"));
//			jo.put("params",json.optString("params"));
//			jo.put("createDate",extractDate(json.optString("createDate")));
//			jo.put("createBy",json.optString("createBy"));
//			jo.put("sendTime",json.optInt("sendTime"));
//			jo.put("status",json.optInt("status"));
//			jo.put("clientIp",JSONObject.fromObject(header).optString("ip"));
//			jo.put("result",json.optString("result"));
//			jo.put("version",json.optString("version"));
//			
//			list.add(JSONObject.fromObject(jo));
//		}
//		Log.trace("pushlog:{}",list);
//		EsUtil.insertPushLogBatch(list);
//	}
//
//	@Override
//	public void run() {
//		RunAlways.run(this);
//	}
//
//
//
//	@Override
//	public boolean addLog(JSONObject jo) {
//		return queue.addLog(jo, 3000);
//	}
//
//
//
//	@Override
//	public boolean accept(JSONObject jo) {
//		return "api-pushlog".equals(jo.opt("moduleName"));
//	}
//
//
//
//	@Override
//	public void exec() throws Exception {
//		insert2DB(queue.takeBatch2(Integer.getInteger("es-batch",1000)));
//	}
//
//
//
//	@Override
//	public String stats() {
//		return "pushlog:"+queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>1000;
//	}
//	
//}
