package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryFrontPayLimit
 * @author: lcz
 * @date: 2018年9月28日 上午11:26:39
 */
public class ReqQueryFrontPayLimit extends AbsReq{

	/**
	 * @Title: ReqQueryFrontPayLimit
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryFrontPayLimit(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}
	
}
