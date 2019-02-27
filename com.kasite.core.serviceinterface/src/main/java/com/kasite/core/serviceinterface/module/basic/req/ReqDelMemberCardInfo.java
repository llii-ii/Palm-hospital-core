/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lcy
 * 删除用户入参类
 * @version 1.0 
 * 2017-7-4上午9:04:43
 */
public class ReqDelMemberCardInfo extends AbsReq{
	
	private String memberId;
	private String cardType;
	private String cardNo;

	public ReqDelMemberCardInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.memberId = getDataJs().getString("MemberId");
			this.cardType = getDataJs().getString("CardType");
			this.cardNo = getDataJs().getString("CardNo");
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			this.memberId=XMLUtil.getString(ser, "MemberId", false);
			this.cardType=XMLUtil.getString(ser, "CardType", false);
			this.cardNo=XMLUtil.getString(ser, "CardNo", false);
		}
		if(StringUtil.isBlank(this.memberId)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数成员ID不能为空。");
		}
		if(StringUtil.isBlank(this.cardType)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数卡类型不能为空。");
		}
		if(StringUtil.isBlank(this.cardNo)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数卡号不能为空。");
		}
	}

	
	
	public ReqDelMemberCardInfo(InterfaceMessage msg, String memberId, String cardType, String cardNo)
			throws AbsHosException {
		super(msg);
		this.memberId = memberId;
		this.cardType = cardType;
		this.cardNo = cardNo;
	}



	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}



	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}



	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}



	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}
