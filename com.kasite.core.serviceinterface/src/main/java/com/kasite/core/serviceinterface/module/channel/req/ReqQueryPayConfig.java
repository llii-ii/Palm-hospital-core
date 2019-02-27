package com.kasite.core.serviceinterface.module.channel.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQueryPayConfig extends AbsReq {

	private Integer ihPayMoneyLimit;
	
	private Integer opPayMoneyLimit;
	
	public Integer getIhPayMoneyLimit() {
		return ihPayMoneyLimit;
	}

	public void setIhPayMoneyLimit(Integer ihPayMoneyLimit) {
		this.ihPayMoneyLimit = ihPayMoneyLimit;
	}

	public Integer getOpPayMoneyLimit() {
		return opPayMoneyLimit;
	}

	public void setOpPayMoneyLimit(Integer opPayMoneyLimit) {
		this.opPayMoneyLimit = opPayMoneyLimit;
	}

	public ReqQueryPayConfig(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.ihPayMoneyLimit = getDataJs().getInteger("IhPayMoneyLimit");
			this.opPayMoneyLimit = getDataJs().getInteger("OpPayMoneyLimit");
		}
	}

}
