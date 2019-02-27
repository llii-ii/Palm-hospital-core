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
public class ReqMsgSourceList extends AbsReq{

	private String sourceId;
	private String sourceName;
	private int state;
	private String orgId;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public ReqMsgSourceList(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.sourceId = XMLUtil.getString(dataEl, "sourceId", false);
		this.sourceName = XMLUtil.getString(dataEl, "sourceName", false);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
		this.state = XMLUtil.getInt(dataEl, "state", false);
	}
	public ReqMsgSourceList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.sourceId = XMLUtil.getString(dataEl, "sourceId", false);
		this.sourceName = XMLUtil.getString(dataEl, "sourceName", false);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
		this.state = XMLUtil.getInt(dataEl, "state", false);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
