package com.kasite.core.serviceinterface.module.order.req;

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
public class ReqQueryRefundableOrderList extends AbsReq{

	private String cardNo;
	
	private String cardType;
	
	private String hisMemberId;
	
	private String serviceId;
	
	private String transactionNo;
	
	private String channelId;
	
	/**
	 * 可退天数
	 */
	private String refundLimitDates;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryRefundableOrderList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.hisMemberId = XMLUtil.getString(dataEl, "HisMemberId", false);
		this.serviceId = XMLUtil.getString(dataEl, "ServiceId", false);
		this.transactionNo = XMLUtil.getString(dataEl, "TransactionNo", false);
		this.channelId = XMLUtil.getString(dataEl, "ChannelId", false);
	}
	
	
	
	/**
	 * @param msg
	 * @param cardNo
	 * @param cardType
	 * @param hisMemberId
	 * @param serviceId
	 * @throws AbsHosException
	 */
	public ReqQueryRefundableOrderList(InterfaceMessage msg, String cardNo, String cardType, String hisMemberId,
			String serviceId,String transactionNo,String channelId,String refundLimitDates) throws AbsHosException {
		super(msg);
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.hisMemberId = hisMemberId;
		this.serviceId = serviceId;
		this.transactionNo = transactionNo;
		this.channelId = channelId;
		this.refundLimitDates = refundLimitDates;
	}



	public ReqQueryRefundableOrderList(InterfaceMessage msg, String oprType) throws AbsHosException {
		super(msg);
		
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}



	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}



	/**
	 * @return the transactionNo
	 */
	public String getTransactionNo() {
		return transactionNo;
	}



	/**
	 * @param transactionNo the transactionNo to set
	 */
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	public String getRefundLimitDates() {
		return refundLimitDates;
	}



	public void setRefundLimitDates(String refundLimitDates) {
		this.refundLimitDates = refundLimitDates;
	}
	
	
	
}
