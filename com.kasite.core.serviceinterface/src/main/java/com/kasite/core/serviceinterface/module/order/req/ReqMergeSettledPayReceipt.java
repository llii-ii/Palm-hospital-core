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
public class ReqMergeSettledPayReceipt extends AbsReq {

	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/**
	 * 用户ID
	 */
	private String memberId;
	
	/**
	 * 合并支付的单据号，多个用 ,(英文逗号)隔开
	 */
	private String receiptNos;
	
	/**
	 * 合并支付的单据价格（分），多个用 ,(英文逗号)隔开
	 */
	private String receiptPrices;
	
	/**
	 *合并支付的单据名称，多个用 ,(英文逗号)隔开
	 */
	private String receiptNames;
	
	/**
	 * 合并支付的单据所属挂号ID，多个用 ,(英文逗号)隔开
	 */
	private String receiptHisRegIds;
	
	/**
	 * 合并支付总金额(分)
	 */
	private Integer totalPrice;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqMergeSettledPayReceipt(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		if (dataEl == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.memberId = XMLUtil.getString(dataEl, "MemberId", true);
		this.totalPrice = XMLUtil.getInt(dataEl, "TotalPrice", true);
		this.receiptNos = XMLUtil.getString(dataEl, "ReceiptNos", true);
		this.receiptPrices = XMLUtil.getString(dataEl, "ReceiptPrices", true);
		this.receiptNames = XMLUtil.getString(dataEl, "ReceiptNames", true);
		this.receiptHisRegIds = XMLUtil.getString(dataEl, "ReceiptHisRegId", false);
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

	public String getReceiptNos() {
		return receiptNos;
	}

	public void setReceiptNos(String receiptNos) {
		this.receiptNos = receiptNos;
	}

	public String getReceiptPrices() {
		return receiptPrices;
	}

	public void setReceiptPrices(String receiptPrices) {
		this.receiptPrices = receiptPrices;
	}

	public String getReceiptNames() {
		return receiptNames;
	}

	public void setReceiptNames(String receiptNames) {
		this.receiptNames = receiptNames;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getReceiptHisRegIds() {
		return receiptHisRegIds;
	}

	public void setReceiptHisRegIds(String receiptHisRegIds) {
		this.receiptHisRegIds = receiptHisRegIds;
	}
	
}
