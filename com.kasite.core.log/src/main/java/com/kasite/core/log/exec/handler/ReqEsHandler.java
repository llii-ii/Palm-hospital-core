package com.kasite.core.log.exec.handler;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.log.EsUtil;
import com.kasite.core.log.RunAlways;
import com.kasite.core.log.exec.QueueHolder;


public class ReqEsHandler implements LogDealHandler{

	private QueueHolder queue=new QueueHolder(10000);
	@Override
	public void run() {
		RunAlways.run(this);
	}

	@Override
	public boolean addLog(JSONObject jo) {
		return queue.addLog(jo, 2000);
	}

	@Override
	public boolean accept(JSONObject jo) {
		return true;
	}

	@Override
	public void exec() throws Exception {
		insert2ES(queue.takeBatch2(Integer.getInteger("es-batch",500)));
	}

	private int count=0;
	
	private void insert2ES(List<JSONObject> jsons) throws Exception {
		if(jsons==null||jsons.isEmpty()){
			return;
		}
		boolean print=(++count)%1000==0;
		long t = currentTimeMillis(print);
		EsUtil.insertBatch(jsons,EsUtil.ESIndex.req);
		if(print){
			KasiteConfig.print("insert into req es,size:"+jsons.size()+",espand time(ms):"+(System.currentTimeMillis()-t));
		}
	}
	private long currentTimeMillis(boolean print) {
		if(!print){
			return 0;
		}
		return System.currentTimeMillis();
	}

	@Override
	public String stats() {
		return "req es:"+queue.size();
	}

	@Override
	public boolean isBusy() {
		return queue.size()>Integer.getInteger("es-busy",8000);
	}

}
