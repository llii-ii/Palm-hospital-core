package com.kasite.core.serviceinterface.module.order.dto;

public class OrderReportVo {

	/**
	 * 交易渠道
	 */
	private String channelName;
	
	/**
	 * 服务ID 006:门诊 充值  007:住院 充值
	 */
	private String serviceId;
	
	/**
	 * 交易笔数
	 */
	private int transCount;
	
	/**
	 * 交易金额
	 */
	private Long transMoney = 0L;
	
	/**
	 * 冲正笔数
	 */
	private int reverseCount;
	
	/**
	 * 冲正金额
	 */
	private Long reverseMoney = 0L;
	/**his账单笔数**/
	private Integer hisCount;
	/**渠道账单笔数**/
	private Integer merchCount;
	/**全流程订单笔数**/
	private Integer qlcCount;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getTransCount() {
		return transCount;
	}

	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}

	public Long getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(Long transMoney) {
		this.transMoney = transMoney;
	}

	public int getReverseCount() {
		return reverseCount;
	}

	public void setReverseCount(int reverseCount) {
		this.reverseCount = reverseCount;
	}

	public Long getReverseMoney() {
		return reverseMoney;
	}

	public void setReverseMoney(Long reverseMoney) {
		this.reverseMoney = reverseMoney;
	}

	public Integer getHisCount() {
		return hisCount;
	}

	public void setHisCount(Integer hisCount) {
		this.hisCount = hisCount;
	}

	public Integer getMerchCount() {
		return merchCount;
	}

	public void setMerchCount(Integer merchCount) {
		this.merchCount = merchCount;
	}

	public Integer getQlcCount() {
		return qlcCount;
	}

	public void setQlcCount(Integer qlcCount) {
		this.qlcCount = qlcCount;
	}
	
}
