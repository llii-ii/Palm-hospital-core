package com.kasite.client.hospay.module.bill.entity.bill.bo;

import java.sql.Timestamp;

/**
 * 每日对账统计对象
 * @author cc
 *
 */
public class BillDateStats {

	/**
	 * 账单日期
	 */
	private String billDate;
	
	/**
	 * HIS账单笔数
	 */
	private Integer hisBillCount;
	
	/**
	 * 已核对笔数
	 */
	private Integer checkBillCount;
	
	/**
	 * 核对异常笔数
	 */
	private Integer abnormalBillCount;
	
	
	/**
	 * 应收金额
	 */
	private String receivableMoney;
	
	/**
	 * 已收金额
	 */
	private String receivedMoney;
	
	
	/**
	 * 核对状态1正常，-1异常
	 */
	private Integer checkState;
	
	/**
	 * 新增日期
	 */
	private Timestamp createDate;
	
	/**
	 * 商户账单笔数
	 */
	private Integer merchBillCount;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Integer getHisBillCount() {
		return hisBillCount;
	}

	public void setHisBillCount(Integer hisBillCount) {
		this.hisBillCount = hisBillCount;
	}

	public Integer getCheckBillCount() {
		return checkBillCount;
	}

	public void setCheckBillCount(Integer checkBillCount) {
		this.checkBillCount = checkBillCount;
	}

	public Integer getAbnormalBillCount() {
		return abnormalBillCount;
	}

	public void setAbnormalBillCount(Integer abnormalBillCount) {
		this.abnormalBillCount = abnormalBillCount;
	}

	public String getReceivableMoney() {
		return receivableMoney;
	}

	public void setReceivableMoney(String receivableMoney) {
		this.receivableMoney = receivableMoney;
	}

	public String getReceivedMoney() {
		return receivedMoney;
	}

	public void setReceivedMoney(String receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getMerchBillCount() {
		return merchBillCount;
	}

	public void setMerchBillCount(Integer merchBillCount) {
		this.merchBillCount = merchBillCount;
	}

	
	
}
