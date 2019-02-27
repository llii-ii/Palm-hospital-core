package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 生成二维码请求对象
 * 
 * @author 無
 *
 */
public class ReqCreateQRCode extends AbsReq {

	/** 卡类型 */
	private String cardType;
	/** 卡号 */
	private String cardNo;
	/** 卡类型名称 */
	private String cardTypeName;
	/**
	 * his就诊人唯一ID
	 */
	private String hisMemberId;
	/**
	 * 二维码使用类型
	 */
	private String usageType;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public ReqCreateQRCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.cardTypeName = XMLUtil.getString(dataEl, "CardTypeName", true);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.usageType = XMLUtil.getString(dataEl, "UsageType", true);
	}

}
