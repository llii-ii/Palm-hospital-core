package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @version 1.0
 * 2017-7-24 下午2:29:38
 */
public class ReqDeleteBatAccount extends AbsReq{
	/**
	 * 主键
	 */
	private String accountId;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public ReqDeleteBatAccount (InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.accountId = XMLUtil.getString(dataEl, "AccountId", true);
	}
}
