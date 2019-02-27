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
public class ReqQuerySelfRefundRecordList extends AbsReq {
	
	private String cardNo;
	
	private String cardType;
	
	private String hisMemberId;
	
	private String memberId;
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQuerySelfRefundRecordList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.hisMemberId = XMLUtil.getString(dataEl, "HisMemberId", false);
		this.memberId = XMLUtil.getString(dataEl, "MemberId", false);
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


	public String getHisMemberId() {
		return hisMemberId;
	}


	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	
	
}