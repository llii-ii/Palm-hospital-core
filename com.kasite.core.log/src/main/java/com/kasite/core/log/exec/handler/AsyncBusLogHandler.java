package com.kasite.core.log.exec.handler;
//package com.kasite.client.business.module.logconsumer.exec.handler;
//
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.coreframework.log.LogInfo;
//import com.kasite.client.business.module.logconsumer.EsUtil;
//import com.kasite.client.business.module.logconsumer.RunAlways;
//import com.kasite.client.business.module.logconsumer.exec.QueueHolder;
//
//public class AsyncBusLogHandler implements LogDealHandler{
//	protected static final Logger logger = LoggerFactory.getLogger(AsyncBusLogHandler.class);
//	private QueueHolder queue=new QueueHolder(5000);
//	private SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private SimpleDateFormat rawf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
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
//		String insertDate=f.format(new Date());
//		for(JSONObject log:logs){
//			Map<String ,Object> temp=new HashMap<String,Object>();
//			JSONObject jo=new JSONObject();
//			String context=log.getString("content");
//			JSONObject json = JSON.parseObject(context);
//			temp.put("AuthInfo",json.getString("authInfo"));
////		jo.put("MessageId",json.getString("sequenceNo"));
//			jo.put("Api",json.getString("api"));
//			temp.put("Param",json.getString("param"));
//			jo.put("SequenceNo",json.getString("sequenceNo"));
//			jo.put("ParamType",json.getString("paramType"));
//			jo.put("OutType",json.getString("outType"));
//			jo.put("ClientIp",json.getString("clientIp"));
//			jo.put("SendTime",extractDate(json.getString("sendTime")));
//			
//			jo.put("V",json.getString("v"));
//			temp.put("Result",json.getString("result"));
//			jo.put("AcceptorId",json.getString("acceptorId"));
//			jo.put("AcceptorIp",json.getString("acceptorIp"));
//			jo.put("Mills",json.getString("mills"));
//			jo.put("AcceptorTime",extractDate(json.getString("acceptorTime")));
//			jo.put("InsertTime",insertDate);
//			try{
////				LogInfo info=(LogInfo) JSON.toJavaObject(log, LogInfo.class);
////				String path=info.getHeader().getPath();
////				if(path!=null && path.startsWith("#")){
////					jo.put(LogHelper.GID, path.substring(1));
////				}
//				Date sendTime=rawf.parse(json.getString("sendTime"));
//				Date acceptorTime=rawf.parse(json.getString("acceptorTime"));
//				long time=acceptorTime.getTime()-sendTime.getTime();
//				jo.put("WaitTime",time);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//			String r=json.getString("result");
//			if(r.startsWith("{")){
//				
//				JSONObject  resultJson= JSON.parseObject(r);
//				int code=resultJson.getIntValue("Code");
//				String message=resultJson.getString("Message");
//				
//				if(json.getString("api").equalsIgnoreCase("accountnoncore.LogWs.saveLoginLog")&&code==10000)
//				{
//					return;
//				}
//				jo.put("Code",code);
//				jo.put("Message",message);
//			}
//			String clientId="未知";
//			try
//			{
//				JSONObject  authInfoJson = JSON.parseObject(json.getString("authInfo"));
//				clientId = authInfoJson.getString("ClientId");
//			}
//			catch(Exception e)
//			{
//				
//			}
//			jo.put("ClientId",clientId);
//			JSONObject t = (JSONObject) JSON.toJSON(temp);
////			String tempstr= t.toJSONString();
////			JSONObject j2=JSONObject.fromObject(tempstr);
//			jo.putAll(t);
//			list.add(jo);
//		}
//		logger.trace("async:{}",list);
//		EsUtil.insertAsyncBatch(list);
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
//		return "AsyncBus-CallLog".equals(jo.getString("moduleName"));
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
//		return "async:"+queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>1000;
//	}
//	
//}
