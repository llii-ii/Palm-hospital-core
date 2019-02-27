package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询自助退费记录
 * @author linjf
 * TODO
 */
public class ReqQuerySelfRefundRecordInfo extends AbsReq {
	
private String selfRefundRecordId;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQuerySelfRefundRecordInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.selfRefundRecordId = XMLUtil.getString(dataEl, "SelfRefundRecordId", true);
	}

	public String getSelfRefundRecordId() {
		return selfRefundRecordId;
	}

	public void setSelfRefundRecordId(String selfRefundRecordId) {
		this.selfRefundRecordId = selfRefundRecordId;
	}
}
