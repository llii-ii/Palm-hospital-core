package com.kasite.client.wechat.message;

import org.dom4j.Element;


/**
 * @author MECHREV
 */
public interface BodyBuilder<T> {

	/**
	 * 消息创建
	 * @param root
	 * @throws Exception
	 * @return String
	 */
	public String make(Element root)throws Exception;
	
	/**
	 * 基类消息创建
	 * @param root
	 * @throws Exception
	 */
	public void makeBasic(Element root)throws Exception;
}
