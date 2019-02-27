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
public class ReqAddMsgUserOpenId extends AbsReq{

	private int cardType;
	private int state;
	private String cardNo;
	private int openType;
	private String openId;
	private String orgId;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**操作人ID**/
	private String operatorId;
	/**操作人名称**/
	private String operatorName;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqAddMsgUserOpenId(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getInt(dataEl, "cardType", true);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", true);
		this.openId = XMLUtil.getString(dataEl, "openId", true);
		this.openType = XMLUtil.getInt(dataEl, "openType", true);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", true);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", true);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
		this.state = XMLUtil.getInt(dataEl, "state", true);
	}
	public ReqAddMsgUserOpenId(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getInt(dataEl, "cardType", true);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", true);
		this.openId = XMLUtil.getString(dataEl, "openId", true);
		this.openType = XMLUtil.getInt(dataEl, "openType", true);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", true);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", true);
		this.state = XMLUtil.getInt(dataEl, "state", true);
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
