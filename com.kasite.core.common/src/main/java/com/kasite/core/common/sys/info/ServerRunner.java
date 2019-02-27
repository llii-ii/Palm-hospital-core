package com.kasite.core.common.sys.info;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenw
 * 
 */
public class ServerRunner {
	private static ServerRunner instance = null;
	private static final Object lock = new Object();
	private static final java.util.concurrent.atomic.AtomicLong 
	  current=new AtomicLong();
	private static final java.util.concurrent.atomic.AtomicLong 
	  total=new AtomicLong();
	
	private long maxActive;
	private long maxActiveTime;
	private ServerRunner()
	{
		
	}
	public void start()
	{
		long l=current.incrementAndGet();
		if(l>maxActive)
		{
			maxActive=l;
			maxActiveTime=System.currentTimeMillis();
		}
		total.incrementAndGet();
		//activeReqList.add(a);
	}
	public void end(long mills,boolean isSuccess)
	{
		current.decrementAndGet();
	}
	public StatisticsProperty getRunStatistics()
	{
		
		StatisticsProperty  s=new StatisticsProperty();
		s.setMaxActive(maxActive<0?0:maxActive);
		long actives=current.get();
		s.setActives(actives<0?0:actives);
		s.setTotal(total.get());
		s.setMaxActiveTime(this.maxActiveTime);
		//s.setOvertime(overtime.get());
		return s;
	}
	public static ServerRunner getInstance() {
		synchronized (lock) {

			if (instance == null) {

				instance = new ServerRunner();

			}
			return instance;

		}
	}
}
