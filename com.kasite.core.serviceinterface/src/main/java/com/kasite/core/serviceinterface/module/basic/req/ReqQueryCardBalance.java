/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询余额入参类
 * @author lcy
 * @version 1.0 
 * 2017-7-10上午9:58:33
 */
public class ReqQueryCardBalance extends AbsReq {
	
	private String cardNo;
	private String cardType;
	public ReqQueryCardBalance(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.memberId = XMLUtil.getString(ser, "MemberId", false);
		this.cardType = XMLUtil.getString(ser, "CardType", true);
		this.cardNo = XMLUtil.getString(ser, "CardNo", true);
	}
	private String memberId;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	/**
	 * @param msg
	 * @param cardNo
	 * @param cardType
	 * @throws AbsHosException
	 */
	public ReqQueryCardBalance(InterfaceMessage msg, String cardNo, String cardType) throws AbsHosException {
		super(msg);
		this.cardNo = cardNo;
		this.cardType = cardType;
	}
	
	
}
