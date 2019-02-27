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
//public class DBLogHandler implements LogDealHandler{
//	
//	private QueueHolder queue=new QueueHolder(10000);
//	private int count=0;
//	public void insert2ES(List<JSONObject> jsons) throws Exception {
//		if(jsons==null||jsons.isEmpty()){
//			return;
//		}
//		boolean print=(++count)%1000==0;
//		long t = RunAlways.currentTimeMillis(print);
//		List<JSONObject> list=new ArrayList<JSONObject>(jsons.size());
//		for(JSONObject json:jsons){
//			try {
//				LogInfo info=(LogInfo) JSONObject.toBean(json, LogInfo.class);
//				JSONObject s = LogHelper.createDbLog(info);
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
//		return "db-jar".equals(jo.opt("moduleName"))||"db-write".equals(jo.opt("moduleName"));
//	}
//
//
//
//	@Override
//	public void exec() throws Exception {
//		insert2ES(queue.takeBatch2(Integer.getInteger("es-batch",500)));
//	}
//
//
//
//	@Override
//	public String stats() {
//		return "dblog:"+queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>5000;
//	}
//
//}
