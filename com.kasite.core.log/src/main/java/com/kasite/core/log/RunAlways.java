package com.kasite.core.log;


import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.LockSupport;

import com.kasite.core.log.exec.handler.LogDealHandler;



public class RunAlways {
	public static void run(LogDealHandler handler){
		while(true){
			try {
				handler.exec();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void submit(Runnable task,Executor executor){
		while(true){
			try {
				executor.execute(task);
				return;
			} catch (RejectedExecutionException e) {
				try{
					LockSupport.parkNanos(10000);
				}catch (Exception e1) {
				}
			}catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	public static long currentTimeMillis(boolean print) {
		if(!print){
			return 0;
		}
		return System.currentTimeMillis();
	}
}
