package com.kasite.client.wechat.service;

import com.kasite.client.wechat.message.MessageContent;
import com.kasite.core.common.exception.MsgListenerException;

/**
 * @author MECHREV
 */
public interface MessageListener {


	/**
	 * onMessage
	 * @return
	 * @param mgsContent
	 * @throws MsgListenerException
	 */
	public String onMessage(MessageContent mgsContent)throws MsgListenerException;
	
	
}
