package com.kasite.core.common.sys.info;

import java.io.Serializable;


public class JvmInfo implements Serializable{
	private static final long serialVersionUID = 4469644875770153375L;
	private String name;//Tomcat进程名称
	private Long Uptime;//系统运行时间 Uptime
	private Usage edenSpace;//新生代
	private Usage permGen;//永久存储区
	private Usage tenuredGen;//旧生代
	private Usage heapMemoryUsage;//堆信息
	private Usage nonHeapMemoryUsage;//非堆信息
	private int threadCount;
	
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Usage getHeapMemoryUsage() {
		return heapMemoryUsage;
	}
	public void setHeapMemoryUsage(Usage heapMemoryUsage) {
		this.heapMemoryUsage = heapMemoryUsage;
	}
	public Usage getNonHeapMemoryUsage() {
		return nonHeapMemoryUsage;
	}
	public void setNonHeapMemoryUsage(Usage nonHeapMemoryUsage) {
		this.nonHeapMemoryUsage = nonHeapMemoryUsage;
	}
	public Long getUptime() {
		return Uptime;
	}
	public void setUptime(Long uptime) {
		Uptime = uptime;
	}
	public Usage getEdenSpace() {
		return edenSpace;
	}
	public void setEdenSpace(Usage edenSpace) {
		this.edenSpace = edenSpace;
	} 
	public Usage getPermGen() {
		return permGen;
	}
	public void setPermGen(Usage permGen) {
		this.permGen = permGen;
	}
	public Usage getTenuredGen() {
		return tenuredGen;
	}
	public void setTenuredGen(Usage tenuredGen) {
		this.tenuredGen = tenuredGen;
	}
	
}
