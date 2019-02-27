package com.kasite.client.hospay.module.bill.entity.bill.dbo;

import java.sql.Timestamp;

/**
 * @author cc 每日汇总账单数据实体
 */
public class EveryDayBalance {

	/** 账单日期 */
	private String billDate;
	/** HIS账单笔数 */
	private String hisBills;
	/** 全流程账单笔数 */
	private String qlcBills;
	/** 渠道（微信/支付宝）账单笔数 */
	private String channelBills;
	/** 已核对笔数 */
	private String checkNum;
	/** 异常笔数 */
	private String abnormalNum;
	/** 剩余异常笔数 */
	private String overPlusErrNum;
	/** 应收费金额 */
	private String receivableMoney;
	/** 实收费金额 */
	private String alreadyReceivedMoney;
	/** 应退金额 */
	private String refundMoney;
	/** 实退金额 */
	private String alreadyRefundMoney;
	/** 核对结果 */
	private String checkResult;
	/** 账单生成日期*/
	private Timestamp createDate;
	/** 异常账单*/
	private Integer errorNum;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getHisBills() {
		return hisBills;
	}

	public void setHisBills(String hisBills) {
		this.hisBills = hisBills;
	}

	public String getQlcBills() {
		return qlcBills;
	}

	public void setQlcBills(String qlcBills) {
		this.qlcBills = qlcBills;
	}

	public String getChannelBills() {
		return channelBills;
	}

	public void setChannelBills(String channelBills) {
		this.channelBills = channelBills;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getAbnormalNum() {
		return abnormalNum;
	}

	public void setAbnormalNum(String abnormalNum) {
		this.abnormalNum = abnormalNum;
	}

	public String getOverPlusErrNum() {
		return overPlusErrNum;
	}

	public void setOverPlusErrNum(String overPlusErrNum) {
		this.overPlusErrNum = overPlusErrNum;
	}

	public String getReceivableMoney() {
		return receivableMoney==null?"0":receivableMoney;
	}

	public void setReceivableMoney(String receivableMoney) {
		this.receivableMoney = (receivableMoney==null?"0":receivableMoney);
	}

	public String getAlreadyReceivedMoney() {
		return alreadyReceivedMoney==null?"0":alreadyReceivedMoney;
	}

	public void setAlreadyReceivedMoney(String alreadyReceivedMoney) {
		this.alreadyReceivedMoney = (alreadyReceivedMoney==null?"0":alreadyReceivedMoney);
	}

	public String getRefundMoney() {
		return refundMoney==null?"0":refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney==null?"0":refundMoney;
	}

	public String getAlreadyRefundMoney() {
		return alreadyRefundMoney==null?"0":alreadyRefundMoney;
	}

	public void setAlreadyRefundMoney(String alreadyRefundMoney) {
		this.alreadyRefundMoney = (alreadyRefundMoney==null?"0":alreadyRefundMoney);
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}
	
}
