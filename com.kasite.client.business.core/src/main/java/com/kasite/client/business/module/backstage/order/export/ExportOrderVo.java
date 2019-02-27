package com.kasite.client.business.module.backstage.order.export;

/**
 * <p>Title : ExportOrderVo</p>
 * <p>Description : </p>
 * <p>DevelopTools : Eclipse_x64_v4.7.1</p>
 * <p>DevelopSystem : windows7</p>
 * <p>Company : com.kst</p>
 * @author : HongHuaYu
 * @date : 2018年9月13日 下午3:42:43
 */
public class ExportOrderVo {

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
	
	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 订单金额
	 */
	private String orderMoney;
	
	/**
	 * 实际支付金额
	 */
	private String transMoney;
	
	/**
	 * 订单状态
	 */
	private String orderState;

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

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(String transMoney) {
		this.transMoney = transMoney;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

}
