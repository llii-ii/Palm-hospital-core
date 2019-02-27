package com.kasite.core.common.sys.info;

import java.io.Serializable;


public class DiskInfo implements Serializable{
	private static final long serialVersionUID = -8444036548332293498L;
	/**盘符名称*/
	private String diskname;
	/**总大小*/
	private long totalSize;
	/**剩余大小*/
	private long freeSize;
	/**使用率*/
	private int usedPoint;
	public String getDiskname() {
		return diskname;
	}
	public void setDiskname(String diskname) {
		this.diskname = diskname;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public long getFreeSize() {
		return freeSize;
	}
	public void setFreeSize(long freeSize) {
		this.freeSize = freeSize;
	}
	public int getUsedPoint() {
		return usedPoint;
	}
	public void setUsedPoint(int usedPoint) {
		this.usedPoint = usedPoint;
	}
}
