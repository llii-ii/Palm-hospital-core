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
public class ReqSettleOrderSettlement extends AbsReq {

	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/** 订单总价 */
	private Integer totalPrice;
	/**
	 * 待结算的处方号
	 */
	private String prescNos;
	/**
	 * 待结算的处方号
	 */
	private String hisOrderIds;
	
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqSettleOrderSettlement(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		if (dataEl == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.memberId = XMLUtil.getString(dataEl, "MemberId", false);
		this.totalPrice = XMLUtil.getInt(dataEl, "TotalPrice", true);
		this.prescNos = XMLUtil.getString(dataEl, "PrescNos", true);
		this.hisOrderIds = XMLUtil.getString(dataEl, "HisOrderIds", true);
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

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPrescNos() {
		return prescNos;
	}

	public void setPrescNos(String prescNos) {
		this.prescNos = prescNos;
	}

	public String getHisOrderIds() {
		return hisOrderIds;
	}

	public void setHisOrderIds(String hisOrderIds) {
		this.hisOrderIds = hisOrderIds;
	}
	
	
	
}
