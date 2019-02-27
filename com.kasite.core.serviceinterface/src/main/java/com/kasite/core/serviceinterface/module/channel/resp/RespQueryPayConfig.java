package com.kasite.core.serviceinterface.module.channel.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

/**
 * 支付配置信息
 * 
 * @author zhaoy
 *
 */
public class RespQueryPayConfig extends AbsResp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 门诊支付最小金额（分）
	 */
	private Integer opMinPayMoney;
	
	/**
	 * 门诊支付最大金额（分）
	 */
	private Integer opMaxPayMoney;
	
	/**
	 * 住院充值最小金额（分）
	 */
	private Integer ihMinPayMoney;
	
	/**
	 * 住院充值最大金额（分）
	 */
	private Integer ihMaxPayMoney;
	
	/**
	 * 是否启用支付（1启用，0禁用）
	 */
	private Integer isEnablePay;
	
	/**
	 * 账单通知时间
	 */
	private String billNotifyTime;
	
	/**
	 * 门诊预交金充值单人单日限制(分)
	 */
	private Integer opPayMoneyLimit;
	
	/**
	 * 住院预交金充值单人单日限制（分）
	 */
	private Integer ihPayMoneyLimit;

	public Integer getOpMinPayMoney() {
		return opMinPayMoney;
	}

	public void setOpMinPayMoney(Integer opMinPayMoney) {
		this.opMinPayMoney = opMinPayMoney;
	}

	public Integer getOpMaxPayMoney() {
		return opMaxPayMoney;
	}

	public void setOpMaxPayMoney(Integer opMaxPayMoney) {
		this.opMaxPayMoney = opMaxPayMoney;
	}

	public Integer getIhMinPayMoney() {
		return ihMinPayMoney;
	}

	public void setIhMinPayMoney(Integer ihMinPayMoney) {
		this.ihMinPayMoney = ihMinPayMoney;
	}

	public Integer getIhMaxPayMoney() {
		return ihMaxPayMoney;
	}

	public void setIhMaxPayMoney(Integer ihMaxPayMoney) {
		this.ihMaxPayMoney = ihMaxPayMoney;
	}

	public Integer getIsEnablePay() {
		return isEnablePay;
	}

	public void setIsEnablePay(Integer isEnablePay) {
		this.isEnablePay = isEnablePay;
	}

	public String getBillNotifyTime() {
		return billNotifyTime;
	}

	public void setBillNotifyTime(String billNotifyTime) {
		this.billNotifyTime = billNotifyTime;
	}

	public Integer getOpPayMoneyLimit() {
		return opPayMoneyLimit;
	}

	public void setOpPayMoneyLimit(Integer opPayMoneyLimit) {
		this.opPayMoneyLimit = opPayMoneyLimit;
	}

	public Integer getIhPayMoneyLimit() {
		return ihPayMoneyLimit;
	}

	public void setIhPayMoneyLimit(Integer ihPayMoneyLimit) {
		this.ihPayMoneyLimit = ihPayMoneyLimit;
	}
	
}
