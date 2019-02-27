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
public class ReqQueryOpenId extends AbsReq{

	private String cardNo;
	private String cardType;
	private String memberName;
	private String idCardNo;
	private String mobile;
	private String channelId;
	private String orgId;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqQueryOpenId(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", false);
		this.channelId = XMLUtil.getString(dataEl, "channelId", false);
		this.cardType = XMLUtil.getString(dataEl, "cardType", false);
		this.memberName = XMLUtil.getString(dataEl, "memberName", true);
		this.idCardNo = XMLUtil.getString(dataEl, "idCardNo", false);
		this.mobile = XMLUtil.getString(dataEl, "mobile", false);
		this.orgId = XMLUtil.getString(dataEl, "orgId", true);
		if(StringUtil.isBlank(cardNo)&&StringUtil.isBlank(idCardNo)){
			throw new RRException("cardNo和idCardNo不能为空。");
		}
	}
	public ReqQueryOpenId(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardNo = XMLUtil.getString(dataEl, "cardNo", false);
		this.cardType = XMLUtil.getString(dataEl, "cardType", false);
		this.memberName = XMLUtil.getString(dataEl, "memberName", true);
		this.idCardNo = XMLUtil.getString(dataEl, "idCardNo", false);
		this.mobile = XMLUtil.getString(dataEl, "mobile", false);
		this.channelId = XMLUtil.getString(dataEl, "channelId", false);
		if(StringUtil.isBlank(cardNo)&&StringUtil.isBlank(idCardNo)){
			throw new RRException("cardNo和idCardNo不能为空。");
		}
		this.orgId = XMLUtil.getString(dataEl, "HosId", true);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
