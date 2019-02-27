package com.kasite.core.common.sys.runcmd;

import java.util.concurrent.Callable;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.Future;  
import java.util.concurrent.TimeUnit;  
import java.util.concurrent.TimeoutException;

import com.kasite.core.common.config.KasiteConfig;  
  
/** 
 * 启动一个任务，然后等待任务的计算结果，如果等待时间超出预设定的超时时间，则中止任务。 
 *  
 * @author Chen Feng 
 */  
public class TaskTimeoutDemo {  
  
    public static void main(String[] args) {  
        KasiteConfig.print("Start ...");  
        ExecutorService exec = Executors.newCachedThreadPool();  
        testTask(exec, 15); // 任务成功结束后等待计算结果，不需要等到15秒  
        testTask(exec, 5); // 只等待5秒，任务还没结束，所以将任务中止  
        exec.shutdown();  
        KasiteConfig.print("End!");  
    }  
  
    public static void testTask(ExecutorService exec, int timeout) {  
        MyTask task = new MyTask();  
        Future<Boolean> future = exec.submit(task);  
        Boolean taskResult = null;  
        String failReason = null;  
        try {  
            // 等待计算结果，最长等待timeout秒，timeout秒后中止任务  
            taskResult = future.get(timeout, TimeUnit.SECONDS);  
        } catch (InterruptedException e) {  
            failReason = "主线程在等待计算结果时被中断！";  
        } catch (ExecutionException e) {  
            failReason = "主线程等待计算结果，但计算抛出异常！";  
        } catch (TimeoutException e) {  
            failReason = "主线程等待计算结果超时，因此中断任务线程！";  
            exec.shutdownNow();  
        }  
  
        KasiteConfig.print("\ntaskResult : " + taskResult);  
        KasiteConfig.print("failReason : " + failReason);  
    }  
}  
  
class MyTask implements Callable<Boolean> {  
  
    @Override  
    public Boolean call() throws Exception {  
        // 总计耗时约10秒  
        for (int i = 0; i < 100L; i++) {  
            Thread.sleep(100); // 睡眠0.1秒  
            System.out.print('-');  
        }  
        return Boolean.TRUE;  
    }  
}  