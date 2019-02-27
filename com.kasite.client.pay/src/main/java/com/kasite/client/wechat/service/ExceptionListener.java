package com.kasite.client.wechat.service;

import com.kasite.core.common.exception.MsgListenerException;

/**
 * @author MECHREV
 */
public abstract interface ExceptionListener {

	/**
	 * onExcption
	 * @param e
	 */
	public abstract void onExcption(MsgListenerException e);
	
}
