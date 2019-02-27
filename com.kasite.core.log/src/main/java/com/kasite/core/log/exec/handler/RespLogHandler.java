package com.kasite.core.log.exec.handler;
//package com.yihu.exec.handler;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.sf.json.JSONObject;
//
//import com.coreframework.log.LogInfo;
//import com.yihu.exec.QueueHolder;
//import com.yihu.exec.RunAlways;
//import com.yihu.log.LogHelper;
//import com.yihu.main.EsUtil;
//
//public class RespLogHandler implements LogDealHandler{
//
//	private QueueHolder queue=new QueueHolder(1000);
//	@Override
//	public void run() {
//		RunAlways.run(this);
//	}
//
//	@Override
//	public boolean addLog(JSONObject jo) {
//		return queue.addLog(jo, 3000);
//	}
//
//	@Override
//	public boolean accept(JSONObject jo) {
//		return "response-log".equals(jo.optString("moduleName"));
//	}
//
//	@Override
//	public void exec() throws Exception {
//		List<JSONObject> jsons=queue.takeBatch2(1000);
//		if(jsons==null||jsons.isEmpty()){
//			return;
//		}
//		List<String> list=new ArrayList<String>(jsons.size());
//		for(JSONObject json:jsons){
//			try{
//				LogInfo info=(LogInfo) JSONObject.toBean(json, LogInfo.class);
//				String s=LogHelper.createResponseLog(info);
//				if(s!=null&&s.length()>0){
//					list.add(s);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//		}
//		EsUtil.insertRespBatch(list);
//	}
//
//
//	@Override
//	public String stats() {
//		return "response-log:"+this.queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>500;
//	}
//
//}
