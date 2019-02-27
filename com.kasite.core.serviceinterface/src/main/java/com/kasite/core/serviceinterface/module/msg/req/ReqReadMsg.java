package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryAllTemplateList
 * @author: zwl
 * @date: 2018年8月3日 上午11:32:22
 */
public class ReqReadMsg extends AbsReq{

	private String msgId;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public ReqReadMsg(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.msgId = XMLUtil.getString(dataEl, "msgId", true);
	}
}
