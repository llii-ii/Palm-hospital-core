package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqQuickPaymentQR extends AbsReq {
	
	/**
	 * his订单号或者处方号
	 */
	private String hisOrderId;
	private Integer totalMoney;
	private String priceName;
	private String orderMemo;
	private String cardNo;
	private String cardType;
	private String operatorId;
	private String operatorName;
	private String serviceId;
	private Integer eqptType;

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getEqptType() {
		return eqptType;
	}

	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQuickPaymentQR(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.totalMoney = XMLUtil.getInt(dataEl, "TotalMoney", true);
		this.priceName = XMLUtil.getString(dataEl, "PriceName", false);
		this.orderMemo = XMLUtil.getString(dataEl, "OrderMemo", false);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", false);
		this.cardType = XMLUtil.getString(dataEl, "CardType", false);		
		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false,super.getOperatorName());
		this.serviceId = XMLUtil.getString(dataEl, "ServiceId", true);
		this.eqptType = XMLUtil.getInt(dataEl, "EqptType", true);
		this.eqptType = XMLUtil.getInt(dataEl, "hisOrderId", true);
	}

}
