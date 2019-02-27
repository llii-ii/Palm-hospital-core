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
public class ReqQueryOrderSettlementList extends AbsReq {
	
	/** 卡号 */
	private String cardNo;
	
	/**
	 * 用户ID
	 */
	private String memberId;
	
	/** 卡类型 */
	private String cardType;
	/**
	 * his订单号
	 */
	private String hisOrderId;
	/**
	 * 处方号
	 */
	private String prescNo;
	
	/** 是否结算 */
	private String isSettlement;
	/**
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryOrderSettlementList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);

		if (dataEl == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.memberId = XMLUtil.getString(dataEl, "MemberId", false);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", false);
		this.cardType = XMLUtil.getString(dataEl, "CardType", false);
		this.isSettlement = XMLUtil.getString(dataEl, "IsSettlement", false);
		this.beginDate = XMLUtil.getString(dataEl, "BeginDate", false);
		this.endDate = XMLUtil.getString(dataEl, "EndDate", false);
		this.hisOrderId = XMLUtil.getString(dataEl, "HisOrderId", false);
		this.prescNo = XMLUtil.getString(dataEl, "PrescNo", false);
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

	public String getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(String isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	
	
}
