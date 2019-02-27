package com.kasite.core.log;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.log.exec.handler.DiyLogHandler;
import com.kasite.core.log.exec.handler.LogDealHandler;
import com.kasite.core.log.exec.handler.ReqLogHandler;



public class HandlerFactory {

	private static List<LogDealHandler> handlers=new ArrayList<LogDealHandler>();
	private static List<Thread> handlerThreads=new ArrayList<Thread>();
	private static LogDealHandler defaultHandler=DiyLogHandler.get();
	static{
		new Thread(defaultHandler,"diy log handler").start();
		addHandler(new ReqLogHandler());
//		addHandler(new DBLogHandler());
//		addHandler(new PushLogHandler());
//		addHandler(new MongoLogHandler());
//		addHandler(new AsyncBusLogHandler());
//		addHandler(new RpcLogHandler());
//		addHandler(new RespLogHandler());
	}
	private static void addHandler(LogDealHandler h){
		if(handlers.contains(h)){
			return;
		}
		handlers.add(h);
		Thread t=new Thread(h,h.getClass().getName());
		handlerThreads.add(t);
		t.start();
		
	}
	public static LogDealHandler getHandler(JSONObject json) {
		for(LogDealHandler h:handlers){
			if(h.accept(json)){
				return h;
			}
		}
		return defaultHandler;
	}
	public static void stop(){
		for(Thread t:handlerThreads){
			t.interrupt();
		}
	}
	public static boolean isBusy(){
		if(defaultHandler.isBusy()){
			return true;
		}
		for(LogDealHandler h:handlers){
			if(h.isBusy()){
				return true;
			}
		}
		return false;
	}
	
	public static String status(){
		String s=defaultHandler.stats();
		for(LogDealHandler h:handlers){
			s+=".  "+h.stats();
		}
		return s;
	}

}
