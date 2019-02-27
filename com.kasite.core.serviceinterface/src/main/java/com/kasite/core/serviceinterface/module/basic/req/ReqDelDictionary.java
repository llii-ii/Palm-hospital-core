package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqDelDictionary
 * @author: lcz
 * @date: 2018年8月28日 上午10:04:09
 */
public class ReqDelDictionary extends AbsReq{
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @Title: ReqDelDictionary
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqDelDictionary(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.id = getDataJs().getLong("Id");
		}
	}

}
