package com.kasite.core.serviceinterface.module.alipay.req;

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
	 * @param zfbKey
	 * @param content
	 * @throws AbsHosException
	 */
	public ReqSendCustomMessage(InterfaceMessage msg,String zfbKey,String content) throws AbsHosException {
		super(msg);
		this.zfbKey = zfbKey;
		this.content = content;
	}
	
	private String zfbKey;
	private String content;
	public String getZfbKey() {
		return zfbKey;
	}
	public void setZfbKey(String zfbKey) {
		this.zfbKey = zfbKey;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
