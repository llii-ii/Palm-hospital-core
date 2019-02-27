/**
 * 
 */
package com.kasite.core.common.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.kasite.core.common.config.KasiteConfig;

public class LoggerConsumer {

	boolean centerLog=true;
	private BlockingQueue<LogInfo> queue=new LinkedBlockingQueue<LogInfo>(10000);
	private long total=0;
	boolean localLog=false;
	public String info(){
		return "size:"+queue.size()+",total:"+total;
	}
	public LoggerConsumer(){
		Thread t=new Thread("LoggerConsumer"){
			private final int SIZE=50;
			private boolean stopped=false;
			private Thread thread;
			@Override
			public void run() {
				thread=Thread.currentThread();
				try{
					Runtime.getRuntime().addShutdownHook(new Thread(){
						@Override
						public void run() {
							stopped=true;
							thread.interrupt();
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
				}
				List<LogInfo> list=new ArrayList<LogInfo>(SIZE);
				while(true){
					try{
						LogInfo chuck=queue.poll(5, TimeUnit.SECONDS);
						if (chuck == null) {
							StorageHolder.getStorage().reset();
							continue;
						}
						list.add(chuck);
						queue.drainTo(list,SIZE-1);
						StorageHolder.getStorage().appendList(list);
						total+=list.size();
		    			list.clear();
		    			if(stopped){
		    				StorageHolder.getStorage().stop();
		    				return;
		    			}
					}catch (Exception e) {
						if(!list.isEmpty()){
							StorageHolder.getStorage().appendList(list);
						}
						try {
							StorageHolder.getStorage().reset();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
			}
			
		};
		t.setDaemon(true);
		t.start();
		KasiteConfig.addAppObserver(new Observer(){
			
			@Override
			public void update(Observable o, Object arg) {
				LoggerConsumer.this.localLog= (KasiteConfig.getValue("yihu.localLog.enable")!=null);
			}
		});
		
	}
 
	 /** 
     * onData用来发布事件，每调用一次就发布一次事件事件 
     * 它的参数会通过事件传递给消费者 
     * 
     * @param bb 
     */
    public void onData(LogInfo info) {
    	if(!LogLevel.isLogEnable()){
    		return;
    	}
    	if(localLog&&("wsgw-log".equals(info.getModuleName()) || (!Logger.systemLog.contains(info.getModuleName())))){
    		String log=info.getContent();
    		KasiteConfig.print(info.getHeader().getClassName()+"."+info.getHeader().getMethodName()+"():"+info.getHeader().getLineNumber()+" -- "+log);
    	}
		if(centerLog){
    		if(!queue.offer(info)){
    			System.err.println("##log buffer is full");
    		}
    	}
    } 
}
