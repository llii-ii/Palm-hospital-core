package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqAddMerchantNotifyFailure extends AbsReq{

	private Long merchantNotifyId;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddMerchantNotifyFailure(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}

	public Long getMerchantNotifyId() {
		return merchantNotifyId;
	}

	public void setMerchantNotifyId(Long merchantNotifyId) {
		this.merchantNotifyId = merchantNotifyId;
	}

	/**
	 * @param msg
	 * @param merchantNotifyId
	 * @throws AbsHosException
	 */
	public ReqAddMerchantNotifyFailure(InterfaceMessage msg, Long merchantNotifyId) throws AbsHosException {
		super(msg);
		this.merchantNotifyId = merchantNotifyId;
	}

	
}
