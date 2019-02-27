package com.kasite.core.log.exec.handler;
//package com.yihu.exec.handler;
//
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import net.sf.json.JSONObject;
//
//import com.yihu.exec.QueueHolder;
//import com.yihu.exec.RunAlways;
//import com.yihu.main.CountUtil;
//
//public class RpcLogHandler implements LogDealHandler{
//	
//	private QueueHolder queue=new QueueHolder(5000);
//	private DiyLogHandler handler=DiyLogHandler.get();
//	
//	public void insert2DB(JSONObject json) throws SQLException {
//		String content=json.getString("content");
//		long currTimes=json.getLong("currTimes");
//		Date d=new Date(currTimes);
//		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
//		JSONObject c=JSONObject.fromObject(content);
//		CountUtil.redis.hincrBy(sd.format(d),c.getString("method"),1);
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
//		return "direct-rpc".equals(jo.opt("moduleName"));
//	}
//
//
//
//	@Override
//	public void exec() throws Exception {
//		JSONObject json=queue.take();
//		insert2DB(json);
//		this.handler.addLog(json);
//	}
//
//
//
//	@Override
//	public String stats() {
//		return "rpc:"+queue.size();
//	}
//
//	@Override
//	public boolean isBusy() {
//		return queue.size()>2000;
//	}
//	public static void main(String[] args) {
//		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
//		KasiteConfig.print(sd.format(new Date()));
//	}
//	
//}
