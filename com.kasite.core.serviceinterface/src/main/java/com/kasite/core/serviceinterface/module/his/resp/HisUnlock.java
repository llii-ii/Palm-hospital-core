/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 释号出参
 * @author lsq
 * version 1.0
 * 2017-7-5下午4:58:55
 */
public class HisUnlock  extends AbsResp {
	
	/**
	 * 调用HIS的返回值 字符串
	 */
	private String store;
	/***
	 * 是否解锁成功
	 */
	private boolean isUnLockSuccess;
	/**
	 * 如果解锁失败 并且有返回文案，则提示返回文案给前端用户
	 */
	private String message;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isUnLockSuccess() {
		return isUnLockSuccess;
	}

	public void setUnLockSuccess(boolean isUnLockSuccess) {
		this.isUnLockSuccess = isUnLockSuccess;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
	
	
}
