package com.kasite.core.common.sys.runcmd;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.sys.runcmd.entity.RunCmdVo;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class RunCmdThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RunCmdThread.class);
	private RunCmdVo vo;
	private String uuid;
	private String name;
	private Api api;
	public static String runData=null;
	/** 是否windows系统 */
	public static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");
	public RunCmdThread(RunCmdVo vo,Api api) {
		this.vo = vo;
		this.api = api;
	}
	
	@Override
	public void run() {
		if(null != vo) {
			String cmd = vo.getCmd();
			String path = vo.getPath();
			String callback = vo.getCallback();
			ExecutorService exec = Executors.newCachedThreadPool();  
			RunCmdCallable target = new RunCmdCallable(cmd, path);
			if(vo.getTimeout()<=0 || vo.getTimeout() > 1000) {
				vo.setTimeout(10);
			}
			String result = task(target,exec, vo.getTimeout()); // 任务成功结束后等待计算结果，不需要等到15秒  
	        exec.shutdown();  
			if(StringUtil.isNotBlank(callback)) {
				String s = DesUtil.encrypt(result,"UTF-8");
				SoapResponseVo respVo = HttpRequestBus.create(callback, RequestType.post)
						.setHeaderHttpParam("token", api.getToken())
						.addHttpParam("uuid", uuid)
						.addHttpParam("name", name)
						.addHttpParam("result", s)
						.send();
				logger.info(respVo.getCode() +"||"+respVo.getResult());
			}
		}
	}
	public static String task(RunCmdCallable target,ExecutorService exec, int timeout) {  
        Future<String> future = exec.submit(target);  
        String taskResult = null;  
        try {  
            // 等待计算结果，最长等待timeout秒，timeout秒后中止任务  
            taskResult = future.get(timeout, TimeUnit.SECONDS);  
        } catch (InterruptedException e) {  
        		taskResult = "主线程在等待计算结果时被中断！";  
        } catch (ExecutionException e) {  
        		taskResult = "主线程等待计算结果，但计算抛出异常！";  
        } catch (TimeoutException e) {  
        		taskResult = "主线程等待计算结果超时，因此中断任务线程！";  
            exec.shutdownNow();  
        }
        return taskResult;
    }  
	
}
