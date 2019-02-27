package com.kasite.core.common.sys.runcmd;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.runcmd.entity.RunCmdVo;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;



public class GetCmdThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(GetCmdThread.class);
	private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool(); 
	private Gson gson = new Gson();
	private String url;
	private String uuid;
	private String name;
	private Sys sys;
	private Api api;
	public GetCmdThread(Sys sys,Api api) {
		super();
		this.url = api.getGetCmdUrl();
		this.uuid = sys.getUuid();
		this.name = sys.getName();
		this.sys = sys;
		this.api = api;
	}

	private static int exception_sleeptime = 2000;//异常休眠10秒重连
	public static String runData=null;
	/** 是否windows系统 */
	public static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");

	@Override
	public void run() {
		while(true) {
			try {
				RunCmdVo vo = getServerCmd();
				if(null != vo) {
					RunCmdThread target = new RunCmdThread(vo, api);
					fixedThreadPool.execute(target);
				}else {
					Thread.sleep(Long.parseLong(sys.getRuncmd_time()));
				}
			}catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(exception_sleeptime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static String[] getFullCmd(String cmd) {
		return WINDOWS ? new String[] { "cmd", "/c", cmd } : new String[] {
				"/bin/sh", "-c", cmd };
	}
	
	public JSONObject runCommand(String cmd, String path) {
		String[] cmds = getFullCmd(cmd);
		JSONObject result = new JSONObject();
		String returnStr = "";
		File file = null;
		try {
			if (!"".equals(path) && path != null) {
				file = new File(path);
			}
			KasiteConfig.print("执行命令：" + cmd);
			CommandExecutor c = new CommandExecutor(cmds, file);
			c.execute();
			returnStr = c.getOutput();
			
			result.put("result", returnStr);
			result.put("code", 10000);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result.put("exception", StringUtil.getException(e));
			result.put("result", e.getMessage());
			result.put("code", -14444);
			return result;
		}
	}
	/**
	 * 获取服务端需要执行的命令
	 * @param uuid
	 * @return
	 * @throws Exception 
	 */
	public RunCmdVo getServerCmd() throws Exception {
		RunCmdVo vo = null;
		SoapResponseVo respVo = HttpRequestBus.create(url, RequestType.get)
				.setHeaderHttpParam("token", api.getToken())
				.addHttpParam("uuid", uuid)
				.addHttpParam("name", name).send();
		if (null != respVo && respVo.getCode() == 200) {
			String result = respVo.getResult();
			if(!"1".equals(result)) {
				result = DesUtil.decrypt(result,"UTF-8");
				logger.debug("服务端需要执行的命令||" + result);
				vo = gson.fromJson(result, RunCmdVo.class);
			}
		}else if(null != respVo.getException()){
			throw respVo.getException();
		}
		return vo;
	}

}
