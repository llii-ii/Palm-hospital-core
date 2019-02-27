package com.kasite.client.business.module.backstage.order.export;

import java.util.ArrayList;
import java.util.List;

public class ExportOrderReportVo {

	/**
	 * 交易渠道
	 */
	private String channelName;
	
	/**
	 * 服务ID 006:门诊 充值  007:住院 充值
	 */
	private String serviceId;
	
	private String serviceValue;
	
	/**
	 * 交易笔数
	 */
	private int transCount;
	
	/**
	 * 交易金额
	 */
	private String transMoney;
	
	/**
	 * 冲正笔数
	 */
	private int reverseCount;
	
	/**
	 * 冲正金额
	 */
	private String reverseMoney;
	
	private List<ExportOrderReportVo> voList = new ArrayList<ExportOrderReportVo>();

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

	public String getServiceValue() {
		return serviceValue;
	}

	public void setServiceValue(String serviceValue) {
		this.serviceValue = serviceValue;
	}

	public int getTransCount() {
		return transCount;
	}

	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}

	public String getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(String transMoney) {
		this.transMoney = transMoney;
	}

	public int getReverseCount() {
		return reverseCount;
	}

	public void setReverseCount(int reverseCount) {
		this.reverseCount = reverseCount;
	}

	public String getReverseMoney() {
		return reverseMoney;
	}

	public void setReverseMoney(String reverseMoney) {
		this.reverseMoney = reverseMoney;
	}

	public List<ExportOrderReportVo> getVoList() {
		return voList;
	}

	public void setVoList(List<ExportOrderReportVo> voList) {
		this.voList = voList;
	}
	
}
