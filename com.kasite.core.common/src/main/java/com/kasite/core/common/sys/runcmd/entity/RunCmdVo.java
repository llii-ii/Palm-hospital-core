package com.kasite.core.common.sys.runcmd.entity;


public class RunCmdVo {
	private String cmd;
	private String callback;
	private String path;
	private String sourcepath;
	private String targetpath;
	private int timeout;
	
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getSourcepath() {
		return sourcepath;
	}
	public void setSourcepath(String sourcepath) {
		this.sourcepath = sourcepath;
	}
	public String getTargetpath() {
		return targetpath;
	}
	public void setTargetpath(String targetpath) {
		this.targetpath = targetpath;
	}
}
