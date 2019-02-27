package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqQueryOrderSettlementPayList extends AbsReq {

	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryOrderSettlementPayList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		if (dataEl == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.memberId = XMLUtil.getString(dataEl, "MemberId", true);
	}

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

}
