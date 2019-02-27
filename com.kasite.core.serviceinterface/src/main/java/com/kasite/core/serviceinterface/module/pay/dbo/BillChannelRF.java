package com.kasite.core.serviceinterface.module.pay.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Table;

@Table(name="P_BILL_CHANNEL_RF")
public class BillChannelRF implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账单日期
	 */
	private String date;
	
	/**
	 * 交易渠道
	 */
	private String channelId;
	
	private String channelName;

	/**
	 * HIS账单笔数
	 */
	private Integer hisBillCount;
	
	/**
	 * 渠道账单笔数
	 */
	private Integer channelBillCount;
	
	/**
	 * 已勾兑笔数
	 */
	private Integer checkCount;
	
	/**
	 * his单边账笔数
	 */
	private Integer hisSingleBillCount;
	
	/**
	 * 渠道单边账笔数
	 */
	private Integer channelSingleBillCount;
	
	/**
	 * 金额不一致笔
	 */
	private Integer differPirceCount;
	
	/**
	 * his账单金额（分）
	 */
	private Long hisBillSum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private Long merchBillSum;
	
	/**
	 * 核对状态，1账平0账不平
	 */
	private Integer checkState;
	
	/**
	 * 新增日期
	 */
	private Timestamp createDate;
	
	/**
	 * 新增用户
	 */
	private String createBy;
	
	/**
	 * 更新日期
	 */
	private Timestamp updateDate;
	
	/**
	 * 更新用户
	 */
	private String updateBy;
	
	/**
	 * 处理状态:0,已处理 1,未处理
	 */
	private Integer dealState;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getHisBillCount() {
		return hisBillCount;
	}

	public void setHisBillCount(Integer hisBillCount) {
		this.hisBillCount = hisBillCount;
	}

	public Integer getChannelBillCount() {
		return channelBillCount;
	}

	public void setChannelBillCount(Integer channelBillCount) {
		this.channelBillCount = channelBillCount;
	}

	public Long getHisBillSum() {
		return hisBillSum;
	}

	public void setHisBillSum(Long hisBillSum) {
		this.hisBillSum = hisBillSum;
	}

	public Long getMerchBillSum() {
		return merchBillSum;
	}

	public void setMerchBillSum(Long merchBillSum) {
		this.merchBillSum = merchBillSum;
	}

	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}

	public Integer getHisSingleBillCount() {
		return hisSingleBillCount;
	}

	public void setHisSingleBillCount(Integer hisSingleBillCount) {
		this.hisSingleBillCount = hisSingleBillCount;
	}

	public Integer getChannelSingleBillCount() {
		return channelSingleBillCount;
	}

	public void setChannelSingleBillCount(Integer channelSingleBillCount) {
		this.channelSingleBillCount = channelSingleBillCount;
	}

	public Integer getDifferPirceCount() {
		return differPirceCount;
	}

	public void setDifferPirceCount(Integer differPirceCount) {
		this.differPirceCount = differPirceCount;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}
}
