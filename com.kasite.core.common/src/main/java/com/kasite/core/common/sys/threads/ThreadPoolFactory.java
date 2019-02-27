package com.kasite.core.common.sys.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.log.LogBody;

/**
 * @author Administrator
corePoolSize

核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存keepAliveTime限制。除非将allowCoreThreadTimeOut设置为true。

maximumPoolSize

线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小的LinkedBlockingDeque时，这个值无效。

keepAliveTime

非核心线程的闲置超时时间，超过这个时间就会被回收。

unit

指定keepAliveTime的单位，如TimeUnit.SECONDS。当将allowCoreThreadTimeOut设置为true时对corePoolSize生效。

workQueue

线程池中的任务队列.

常用的有三种队列，SynchronousQueue,LinkedBlockingDeque,ArrayBlockingQueue。

如果线程数量<=核心线程数量，那么直接启动一个核心线程来执行任务，不会放入队列中。
如果线程数量>核心线程数，但<=最大线程数，并且任务队列是LinkedBlockingDeque的时候，超过核心线程数量的任务会放在任务队列中排队。
如果线程数量>核心线程数，但<=最大线程数，并且任务队列是SynchronousQueue的时候，线程池会创建新线程执行任务，这些任务也不会被放在任务队列中。这些线程属于非核心线程，在任务完成后，闲置时间达到了超时时间就会被清除。
如果线程数量>核心线程数，并且>最大线程数，当任务队列是LinkedBlockingDeque，会将超过核心线程的任务放在任务队列中排队。也就是当任务队列是LinkedBlockingDeque并且没有大小限制时，线程池的最大线程数设置是无效的，他的线程数最多不会超过核心线程数。
如果线程数量>核心线程数，并且>最大线程数，当任务队列是SynchronousQueue的时候，会因为线程池拒绝添加任务而抛出异常。
 */
public class ThreadPoolFactory{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	//创建一个消息的消费者线程池。用于消费 从中心获取的消息并处理
	public static final ThreadPoolExecutor sync_center_msg_pool = create("sync_center_msg",10,150,5,new SynchronousQueue<Runnable>());
	public  static ThreadPoolExecutor create(String name,int corePoolSize
			,int maximumPoolSize,int keepAliveTime, BlockingQueue<Runnable> workQueue)
	{
			ThreadPoolExecutor	pool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,
					keepAliveTime, TimeUnit.SECONDS,
					workQueue);
			pool.setThreadFactory(new ThreadPoolFactory.DefaultThreadFactory(name));
			pool.setRejectedExecutionHandler(new RejectedExecutionHandler(){
				protected Logger logger = LoggerFactory.getLogger(getClass());
			@Override
			public void rejectedExecution(Runnable r,
					ThreadPoolExecutor executor) {
					try
					{
						//线程池 满或者队列挤压的时候处理
						LogBody logbody = new LogBody(KasiteConfig.createAuthInfoVo(ThreadPoolFactory.class))
						.set("PoolSize", executor.getPoolSize())
						.set("ActiveCount", executor.getActiveCount())
						.set("TaskCount", executor.getTaskCount())
						.set("CompletedTaskCount", executor.getCompletedTaskCount())
						.set("QueueSize", executor.getQueue().size())
						.set("CorePoolSize", executor.getCorePoolSize())
						.set("LargestPoolSize", executor.getLargestPoolSize())
						.set("MaximumPoolSize", executor.getMaximumPoolSize());
						logger.error("线程池异常：",logbody.toString());
					}
					catch(Exception e)
					{
						
					}
			}});
			return pool;
	}
	public static class DefaultThreadFactory implements ThreadFactory {
    final AtomicInteger poolNumber = new AtomicInteger(1);
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public DefaultThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null)? s.getThreadGroup() :
                             Thread.currentThread().getThreadGroup();
      
        namePrefix = "A-"+name+"-" +
                      poolNumber.getAndIncrement();
    }
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                              namePrefix + threadNumber.getAndIncrement(),
                              0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
  }

}
