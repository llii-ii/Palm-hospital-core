package com.kasite.core.common.resp;

import org.dom4j.Document;

import com.alibaba.fastjson.JSONObject;

/**
 * 返回值处理接口
 * @className: IResp
 * @author: lcz
 * @date: 2018年7月19日 下午6:13:27
 */
public interface IRespHandler {
	
	/**
	 * 返回值处理方法
	 * @Description: 
	 * @return
	 */
	Document parse();
	JSONObject parseJSON();
	String parseString();
}
