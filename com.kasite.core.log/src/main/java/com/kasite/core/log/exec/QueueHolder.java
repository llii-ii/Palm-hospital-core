package com.kasite.core.log.exec;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.log.LogDealImpl;



public class QueueHolder {
	private BlockingQueue<JSONObject> queue;
	public QueueHolder(int capacity){
		queue=new LinkedBlockingQueue<JSONObject>(10000);
	}
	
	public QueueHolder(BlockingQueue<JSONObject> q){
		queue=q;
	}
	
	public boolean addLog(JSONObject jo,int timeout) {
		try {
			queue.offer(jo, timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 
	 * @return null表示规定时间内，没有收到新数据
	 */
	public List<JSONObject> takeBatch(int count){
		JSONObject chuck;
		try {
			chuck = queue.poll(1000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			return null;
		}
		if (chuck == null) {
			return null;
		}
		List<JSONObject> list=new ArrayList<JSONObject>(count);
		list.add(chuck);
		queue.drainTo(list,count-1);
		return list;
	}
	
	/*
	 * 最少要2秒
	 */
	public List<JSONObject> takeBatch2(int count){
		long t=System.currentTimeMillis()+Integer.getInteger("es-queue-wait",2000);
		JSONObject chuck;
		try {
			chuck = queue.poll(1000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			return null;
		}
		if (chuck == null) {
			return null;
		}
		List<JSONObject> list=new ArrayList<JSONObject>(count);
		list.add(chuck);
		queue.drainTo(list,count-list.size());
		if(!LogDealImpl.isShutdown()&&list.size()<count/2){
			LockSupport.parkUntil(t);
		}
		queue.drainTo(list,count-list.size());
		return list;
	}
	
	public JSONObject take() throws InterruptedException{
		return queue.poll(Integer.MAX_VALUE,TimeUnit.MILLISECONDS);
	}
	
	public int size(){
		return queue.size();
	}
}
