package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * 智付后台-交易管理-交易日报
 * 
 * @author zhaoy
 * TODO
 */
public class RespOrderReport extends AbsResp implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	/**
	 * 交易渠道
	 */
	private String channelName;
	
	/**
	 * 服务ID 
	 */
	private String serviceId;
	
	/**
	 * 字典表对应的value
	 */
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
	
	private List<RespOrderReport> respOrderReportList = new ArrayList<RespOrderReport>();

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

	public List<RespOrderReport> getRespOrderReportList() {
		return respOrderReportList;
	}

	public void setRespOrderReportList(List<RespOrderReport> respOrderReportList) {
		this.respOrderReportList = respOrderReportList;
	}
	
}
