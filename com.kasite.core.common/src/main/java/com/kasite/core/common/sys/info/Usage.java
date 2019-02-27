package com.kasite.core.common.sys.info;

import java.io.Serializable;


public class Usage implements Serializable{
	private static final long serialVersionUID = 4608209505741886525L;
	private long committed;
	private long init;
	private long max;
	private long used;
	public long getCommitted() {
		return committed;
	}
	public void setCommitted(long committed) {
		this.committed = committed;
	}
	public long getInit() {
		return init;
	}
	public void setInit(long init) {
		this.init = init;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
}
