package com.kasite.core.serviceinterface.module.pay.dto;

/**
 * 交易日报表统计(重新统计)
 * 
 * @author zhaoy
 *
 */
public class BillCountByDateVo {

	/**
	 * 渠道号
	 */
	private String channelId;
	
	/**
	 * 交易笔数
	 */
	private Integer count;
	
	/**
	 * 交易金额
	 */
	private Long moneySum;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getMoneySum() {
		return moneySum;
	}

	public void setMoneySum(Long moneySum) {
		this.moneySum = moneySum;
	}
	
}
