package com.kasite.client.pay.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 每日对账统计对象
 * @author linjianfa
 *
 */
@Table(name="P_BILL_DATE_STATS")
public class BillDateStats {

	/**
	 * 账单日期
	 */
	@Id
	@KeySql(useGeneratedKeys=true)
	private String billDate;
	
	/**
	 * HIS账单笔数
	 */
	private Integer hisBillCount;
	
	/**
	 * 已核对笔数
	 */
	private Integer checkedBillCount;
	
	/**
	 * 核对异常笔数
	 */
	private Integer abnormalBillCount;
	
	
	/**
	 * 应收金额
	 */
	private Integer receivableMoney;
	
	/**
	 * 已收金额
	 */
	private Integer receivedMoney;
	
	
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
	
	/**
	 * 应退金额
	 */
	private Integer refundableMoney;
	
	/**
	 * 已退金额
	 */
	private Integer refundedMoney;

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

	public Integer getCheckedBillCount() {
		return checkedBillCount;
	}

	public void setCheckedBillCount(Integer checkedBillCount) {
		this.checkedBillCount = checkedBillCount;
	}

	public Integer getAbnormalBillCount() {
		return abnormalBillCount;
	}

	public void setAbnormalBillCount(Integer abnormalBillCount) {
		this.abnormalBillCount = abnormalBillCount;
	}

	public Integer getReceivableMoney() {
		return receivableMoney;
	}

	public void setReceivableMoney(Integer receivableMoney) {
		this.receivableMoney = receivableMoney;
	}

	public Integer getReceivedMoney() {
		return receivedMoney;
	}

	public void setReceivedMoney(Integer receivedMoney) {
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

	public Integer getRefundableMoney() {
		return refundableMoney;
	}

	public void setRefundableMoney(Integer refundableMoney) {
		this.refundableMoney = refundableMoney;
	}

	public Integer getRefundedMoney() {
		return refundedMoney;
	}

	public void setRefundedMoney(Integer refundedMoney) {
		this.refundedMoney = refundedMoney;
	}

	
	
}
