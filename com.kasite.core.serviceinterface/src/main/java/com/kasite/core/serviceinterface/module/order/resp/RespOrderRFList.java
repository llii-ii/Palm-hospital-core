package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

/**
 * 智付后台-交易管理-交易明细
 * @author zhaoy
 * TODO
 */
public class RespOrderRFList extends AbsResp implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 服务ID 006:住院号 007:就诊卡
	 */
	private String serviceId;
	
	/**
	 * 交易时间
	 */
	private String transTime;
	
	/**
	 * 渠道流水号
	 */
	private String channelSerialNo;
	
	/**
	 * 医院的流水号
	 */
	private String hisSerialNo;
	
	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 交易金额
	 */
	private Integer transMoney;
	
	/**
	 * 订单金额
	 */
	private Integer orderMoney;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 业务状态
	 */
	private Integer bizState;
	
	/**
	 * 就诊卡号/住院号
	 */
	private String cardNo;
	
	/**
	 * 患者姓名
	 */
	private String nickName;
	
	/**
	 * 患者手机号
	 */
	private String nickMobile;
	
	/**银行简拼***/
	private String bankShortName;
	/**所属银行***/
	private String bankName;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getChannelSerialNo() {
		return channelSerialNo;
	}

	public void setChannelSerialNo(String channelSerialNo) {
		this.channelSerialNo = channelSerialNo;
	}

	public String getHisSerialNo() {
		return hisSerialNo;
	}

	public void setHisSerialNo(String hisSerialNo) {
		this.hisSerialNo = hisSerialNo;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Integer getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(Integer transMoney) {
		this.transMoney = transMoney;
	}

	public Integer getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(Integer orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Integer getBizState() {
		return bizState;
	}

	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickMobile() {
		return nickMobile;
	}

	public void setNickMobile(String nickMobile) {
		this.nickMobile = nickMobile;
	}

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
