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
//public class MongoLogHandler implements LogDealHandler{
//	
//	private QueueHolder queue=new QueueHolder(5000);
//	private int count=0;
//	private void insert2DB(List<JSONObject> jsons) throws Exception {
//		if(jsons==null||jsons.isEmpty()){
//			return;
//		}
//		boolean print=(++count)%1000==0;
//		long t = RunAlways.currentTimeMillis(print);
//		List<JSONObject> list=new ArrayList<JSONObject>(jsons.size());
//		for(JSONObject json:jsons){
//			try {
//				LogInfo info=(LogInfo) JSONObject.toBean(json, LogInfo.class);
//				JSONObject s = LogHelper.createMongoLog(info);
//				if(s!=null){
//					list.add(s);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				continue;
//			} 
//		}
//		EsUtil.insertDbLogBatch(list);
//		if(list.size()<200){
//			Thread.sleep(Integer.getInteger("es-sleep",1));
//		}
//		if(print){
//			KasiteConfig.print("insert into req es,size:"+jsons.size()+",espand time(ms):"+(System.currentTimeMillis()-t));
//		}
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
//		return "mongo".equals(jo.opt("moduleName"))||"mongo_query".equals(jo.opt("moduleName"));
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
//		return "mongo:"+queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>1000;
//	}
//	
//}
