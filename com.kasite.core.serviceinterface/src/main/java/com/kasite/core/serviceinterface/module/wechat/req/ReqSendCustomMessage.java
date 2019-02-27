package com.kasite.core.serviceinterface.module.wechat.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqSendCustomMessage
 * @author: lcz
 * @date: 2018年8月3日 上午11:26:05
 */
public class ReqSendCustomMessage extends AbsReq{

	/**
	 * 
	 * @Title: ReqSendCustomMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @param content
	 * @throws AbsHosException
	 */
	public ReqSendCustomMessage(InterfaceMessage msg,String wxKey,String content) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		this.content = content;
	}
	
	private String wxKey;
	private String content;
	
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
