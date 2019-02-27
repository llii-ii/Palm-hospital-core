package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryFrontPayLimit
 * @author: lcz
 * @date: 2018年9月28日 上午11:27:02
 */
public class RespQueryFrontPayLimit extends AbsResp{
	
	private Integer opMinPayMoney;
	private Integer opMaxPayMoney;
	private Integer ihMinPayMoney;
	private Integer ihMaxPayMoney;
	private Integer isEnablePay;
	private String billNotifyTime;
	private Integer opPayMoneyLimit;
	private Integer ihPayMoneyLimit;
	
	/**
	 * @return the opMinPayMoney
	 */
	public Integer getOpMinPayMoney() {
		return opMinPayMoney;
	}
	/**
	 * @param opMinPayMoney the opMinPayMoney to set
	 */
	public void setOpMinPayMoney(Integer opMinPayMoney) {
		this.opMinPayMoney = opMinPayMoney;
	}
	/**
	 * @return the opMaxPayMoney
	 */
	public Integer getOpMaxPayMoney() {
		return opMaxPayMoney;
	}
	/**
	 * @param opMaxPayMoney the opMaxPayMoney to set
	 */
	public void setOpMaxPayMoney(Integer opMaxPayMoney) {
		this.opMaxPayMoney = opMaxPayMoney;
	}
	/**
	 * @return the ihMinPayMoney
	 */
	public Integer getIhMinPayMoney() {
		return ihMinPayMoney;
	}
	/**
	 * @param ihMinPayMoney the ihMinPayMoney to set
	 */
	public void setIhMinPayMoney(Integer ihMinPayMoney) {
		this.ihMinPayMoney = ihMinPayMoney;
	}
	/**
	 * @return the ihMaxPayMoney
	 */
	public Integer getIhMaxPayMoney() {
		return ihMaxPayMoney;
	}
	/**
	 * @param ihMaxPayMoney the ihMaxPayMoney to set
	 */
	public void setIhMaxPayMoney(Integer ihMaxPayMoney) {
		this.ihMaxPayMoney = ihMaxPayMoney;
	}
	/**
	 * @return the isEnablePay
	 */
	public Integer getIsEnablePay() {
		return isEnablePay;
	}
	/**
	 * @param isEnablePay the isEnablePay to set
	 */
	public void setIsEnablePay(Integer isEnablePay) {
		this.isEnablePay = isEnablePay;
	}
	/**
	 * @return the billNotifyTime
	 */
	public String getBillNotifyTime() {
		return billNotifyTime;
	}
	/**
	 * @param billNotifyTime the billNotifyTime to set
	 */
	public void setBillNotifyTime(String billNotifyTime) {
		this.billNotifyTime = billNotifyTime;
	}
	/**
	 * @return the opPayMoneyLimit
	 */
	public Integer getOpPayMoneyLimit() {
		return opPayMoneyLimit;
	}
	/**
	 * @param opPayMoneyLimit the opPayMoneyLimit to set
	 */
	public void setOpPayMoneyLimit(Integer opPayMoneyLimit) {
		this.opPayMoneyLimit = opPayMoneyLimit;
	}
	/**
	 * @return the ihPayMoneyLimit
	 */
	public Integer getIhPayMoneyLimit() {
		return ihPayMoneyLimit;
	}
	/**
	 * @param ihPayMoneyLimit the ihPayMoneyLimit to set
	 */
	public void setIhPayMoneyLimit(Integer ihPayMoneyLimit) {
		this.ihPayMoneyLimit = ihPayMoneyLimit;
	}
}
