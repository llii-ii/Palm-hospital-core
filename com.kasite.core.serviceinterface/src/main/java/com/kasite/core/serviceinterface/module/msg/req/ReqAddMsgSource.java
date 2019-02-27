package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
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
public class ReqAddMsgSource extends AbsReq{

	private String operatorId;
	private String sourceName;
	private String operatorName;
	private String orgId;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqAddMsgSource(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.sourceName = XMLUtil.getString(dataEl, "sourceName", true);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", true);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", true);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
	}
	public ReqAddMsgSource(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.sourceName = XMLUtil.getString(dataEl, "sourceName", true);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", true);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", true);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
