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
public class ReqMsgUserOpenIdList extends AbsReq{

	private int cardType;
	private String cardNo;
	private int openType;
	private String openId;
	private int state;
	private String orgId;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqMsgUserOpenIdList(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getInt(dataEl, "cardType", false);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", false);
		this.openId = XMLUtil.getString(dataEl, "openId", false);
		this.openType = XMLUtil.getInt(dataEl, "openType", false);
		this.state = XMLUtil.getInt(dataEl, "state", false);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
	}
	public ReqMsgUserOpenIdList(InterfaceMessage msg,String cardNo,int cardType) throws AbsHosException {
		super(msg);
		this.cardType = cardType;
		this.cardNo = cardNo;
	}
	public ReqMsgUserOpenIdList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getInt(dataEl, "cardType", false);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", false);
		this.openId = XMLUtil.getString(dataEl, "openId", false);
		this.openType = XMLUtil.getInt(dataEl, "openType", false);
		this.state = XMLUtil.getInt(dataEl, "state", false);
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
