package com.kasite.core.common.sys.info;

import java.io.Serializable;


public class SysInfo implements Serializable{
	private static final long serialVersionUID = 5148240121064956389L;
	private String time;//当前时间
	private int cpuUsePoint; //cpu使用率
	private int memoryPoint;//内存使用百分比
	//private long maxmeory;//最大内存使用
	private long TotalPhysicalMemorySize;//系统物理内存
	private DiskInfo[] diskInfos;//磁盘信息
	private long TotalSwapSpaceSize;//总交换空间大小 (总的物理内存 + 虚拟内存)
	private long FreePhysicalMemorySize;//可用物理内存大小(剩余的物理内存)
	private long CommittedVirtualMemorySize;//虚拟内存
	private String Name;//操作系统名称
	
	public int getCpuUsePoint() {
		return cpuUsePoint;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public DiskInfo[] getDiskInfos() {
		return diskInfos;
	}
	public void setDiskInfos(DiskInfo[] diskInfos) {
		this.diskInfos = diskInfos;
	}
	public void setCpuUsePoint(int cpuUsePoint) {
		this.cpuUsePoint = cpuUsePoint;
	}
	public int getMemoryPoint() {
		return memoryPoint;
	}
	public void setMemoryPoint(int memoryPoint) {
		this.memoryPoint = memoryPoint;
	}
	public long getTotalPhysicalMemorySize() {
		return TotalPhysicalMemorySize;
	}
	public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
		TotalPhysicalMemorySize = totalPhysicalMemorySize;
	}
	public long getTotalSwapSpaceSize() {
		return TotalSwapSpaceSize;
	}
	public void setTotalSwapSpaceSize(long totalSwapSpaceSize) {
		TotalSwapSpaceSize = totalSwapSpaceSize;
	}
	public long getFreePhysicalMemorySize() {
		return FreePhysicalMemorySize;
	}
	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		FreePhysicalMemorySize = freePhysicalMemorySize;
	}
	public long getCommittedVirtualMemorySize() {
		return CommittedVirtualMemorySize;
	}
	public void setCommittedVirtualMemorySize(long committedVirtualMemorySize) {
		CommittedVirtualMemorySize = committedVirtualMemorySize;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
