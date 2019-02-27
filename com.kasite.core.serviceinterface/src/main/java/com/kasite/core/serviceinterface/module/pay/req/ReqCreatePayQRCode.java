package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 生成支付二维码请求入参
 * 
 * @author 無
 *
 */
public class ReqCreatePayQRCode extends AbsReq {
	private String hisOrderId;
	private Integer totalFee;
	private String subject;
	private String orderMemo;
	private String memberName;
	private String cardNo;
	private String cardType;
	private String operatorId;
	private String operatorName;
	private String serviceId;
	private String isOnlinePay;
	private Integer eqptType;
	private String hisMemberId;
	//是否扫码的时候绑定用户 0 不绑定，1绑定
	private Integer autoBindUser;
	
 
	public Integer getAutoBindUser() {
		return autoBindUser;
	}

	public void setAutoBindUser(Integer autoBindUser) {
		this.autoBindUser = autoBindUser;
	}

	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}

	/**
	 * 二维码用途（建议根据用途的名词使用拼音简称大写，如：腕带二维码=WD，扫码充值）
	 */
	private String usageType;

	public ReqCreatePayQRCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 1) {
			Element dataEl = root.element(KstHosConstant.DATA);
			this.hisOrderId = XMLUtil.getString(dataEl, "HisOrderId", false);
			this.totalFee = XMLUtil.getInt(dataEl, "TotalFee", false);
			this.subject = XMLUtil.getString(dataEl, "Subject", true);
			this.orderMemo = XMLUtil.getString(dataEl, "OrderMemo", false); 
			this.memberName = XMLUtil.getString(dataEl, "MemberName", false); 
			this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
			this.cardType = XMLUtil.getString(dataEl, "CardType", false);
			this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false, super.getOpenId());
			this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false, super.getOperatorName());
			this.serviceId = XMLUtil.getString(dataEl, "ServiceId", true);
			this.isOnlinePay = XMLUtil.getString(dataEl, "IsOnlinePay",false, "1");
			this.eqptType = XMLUtil.getInt(dataEl, "EqptType", true);
			this.usageType = XMLUtil.getString(dataEl, "UsageType", true);
			this.hisMemberId = XMLUtil.getString(dataEl, "HisMemberId", false);
			this.autoBindUser = XMLUtil.getInt(dataEl, "AutoBindUser", false);
		} else if (msg.getParamType() == 0) {
			throw new ParamException("暂时不支持Json参数配置。");
		}
	}	

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getIsOnlinePay() {
		return isOnlinePay;
	}

	public void setIsOnlinePay(String isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}

	public Integer getEqptType() {
		return eqptType;
	}

	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}

}
