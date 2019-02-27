package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
    /**
 *@author caiyouhong
 *@version 1.0 
 *@time 2017-7-26 下午3:49:02 医嘱订单支付
 **/
public class ReqCancelSettlementOrder extends AbsReq {
	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/** 本地订单 */
	private String orderId;
	/** 处方ID */
	private String prescNo;
	/** 渠道ID */
	private String channelId;
	/** 订单商户id */
	private String merchantType;
	

	public ReqCancelSettlementOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(service, "CardNo", true);
		this.cardType = XMLUtil.getString(service, "CardType", true);
		this.orderId = XMLUtil.getString(service, "OrderId", false);
		this.prescNo = XMLUtil.getString(service, "PrescNo", false);
		this.channelId = XMLUtil.getString(service, "ChannelId", true);
		this.merchantType = XMLUtil.getString(service, "MerchantType", true);
		
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}


}
