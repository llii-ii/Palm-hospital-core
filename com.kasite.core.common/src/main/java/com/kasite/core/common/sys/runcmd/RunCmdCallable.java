package com.kasite.core.common.sys.runcmd;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.StringUtil;

public class RunCmdCallable implements Callable<String> {
	public static String runData=null; 
	private String cmd;
	private String path;
	/** 是否windows系统 */
	public static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");
	public RunCmdCallable(String cmd,String path) {
		this.cmd = cmd;
		this.path = path;
	}
	@Override
	public String call() throws Exception {
		return runCommand(cmd, path).toJSONString();
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
		CommandExecutor c = null;
		try {
			if (!"".equals(path) && path != null) {
				file = new File(path);
			}
			KasiteConfig.print("执行命令：" + cmd);
			c = new CommandExecutor(cmds, file);
			c.execute();
			returnStr = c.getOutput();
			
			result.put("result", returnStr);
			result.put("code", 10000);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			if(null != c && StringUtil.isNotBlank(c.getOutput())) {
				result.put("result", c.getOutput());
			}else {
				result.put("result", e.getMessage());
			}
			result.put("exception", StringUtil.getException(e));
			result.put("code", -14444);
			return result;
		}
	}
}
