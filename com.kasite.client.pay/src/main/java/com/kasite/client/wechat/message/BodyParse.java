package com.kasite.client.wechat.message;

import java.util.Map;

/**
 * @author MECHREV
 */
public interface BodyParse<T> {

	/**
	 * 消息转化
	 * @param <T>
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public T parse(Map<String,String> map) throws Exception;
	
	/**
	 * 消息基类转化
	 * @param map
	 * @throws Exception
	 */
	public void parseBasic(Map<String,String> map) throws Exception;
	
}
