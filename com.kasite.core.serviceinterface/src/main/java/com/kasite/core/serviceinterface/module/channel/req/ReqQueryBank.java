package com.kasite.core.serviceinterface.module.channel.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQueryBank extends AbsReq {

	public ReqQueryBank(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}

}
