package com.kasite.core.common.sys.runcmd;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;



public class CommandExecutor extends SysCmdOperation {

	private String[] command;
	private StringBuffer output;

	public CommandExecutor(String[] execString) {
		this(execString, null);
	}

	public CommandExecutor(String[] execString, File dir) {
		this(execString, dir, null);
	}

	public CommandExecutor(String[] execString, File dir,
			Map<String, String> env) {
		this(execString, dir, env, 0L);
	}

	/**
	 * 初始化参数设置
	 */
	public CommandExecutor(String[] execString, File dir,
			Map<String, String> env, long timeout) {
		command = execString.clone();
		if (dir != null) {
			setWorkingDirectory(dir);
		}
		if (env != null) {
			setEnvironment(env);
		}
		timeOutInterval = timeout;
	}

	/** 命令执行 */
	public void execute() throws IOException {
		this.run();
	}

	protected String[] getExecString() {
		return command;
	}

	protected void parseExecResult(BufferedReader lines) throws IOException {
		output = new StringBuffer();
		char[] buf = new char[512];
		int nRead;
		while ((nRead = lines.read(buf, 0, buf.length)) > 0 && nRead!=0) {
			output.append(buf, 0, nRead);
		}
	}

	/** 获取命令返回信息 */
	public String getOutput() {
		return (output == null) ? "" : output.toString();
	}

	public void setOutput(StringBuffer output) {
		this.output = output;
	}

	/**
	 * 命令转换String
	 * 
	 * 
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String[] args = getExecString();
		for (String s : args) {
			if (s.indexOf(' ') >= 0) {
				builder.append('"').append(s).append('"');
			} else {
				builder.append(s);
			}
			builder.append(' ');
		}
		return builder.toString();
	}
}
