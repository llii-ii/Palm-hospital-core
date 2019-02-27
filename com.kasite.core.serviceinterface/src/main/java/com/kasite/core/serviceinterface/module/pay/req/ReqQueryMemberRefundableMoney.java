package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 创建处方付二维码请求头
 * @author linjf
 * TODO
 */
public class ReqQueryMemberRefundableMoney extends AbsReq {
	
	private String cardNo;
	
	private String cardType;
	
	private String hisMemberId;
	
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryMemberRefundableMoney(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.hisMemberId = XMLUtil.getString(dataEl, "HisMemberId", false);
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

	
	
}
