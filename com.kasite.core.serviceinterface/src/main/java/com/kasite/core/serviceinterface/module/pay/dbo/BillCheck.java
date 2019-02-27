package com.kasite.core.serviceinterface.module.pay.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 对账账单表
 * 
 * @author zhaoy
 *
 */
@Table(name="P_BILL_CHECK")
public class BillCheck implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private String id;
	
	/** 全流程支付/退费订单号  **/
	private String orderNo;
	
	private String orderId;
	
	/** HIS支付/退费订单号  **/
	private String hisOrderNo;
	
	/** 商户支付/退费订单号  **/
	private String merchNo;
	
	/** 商户配置  **/
	private String configkey;
	
	/** 支付时间  **/
	private Timestamp payDate;
	
	/** 业务处理时间  **/
	private Timestamp hisBizDate;
	
	/** 交易时间  **/
	private Timestamp transDate;
	
	/** 支付方式: 微信,wechat 支付宝,zfb 银联,yl  **/
	private String payMethod;
	
	private String payMethodName;
	
	/** 交易渠道  **/
	private String channelId;
	
	private String channelName;
	
	/** 订单类型: 1,支付订单 2,退款订单  **/
	private Integer billType;
	
	/** 服务号 ***/
	private String serviceId;
	
	/** His交易金额  **/
	private Integer hisPrice;
	
	/** 商户交易金额  **/
	private Integer merchPrice;
	
	/** 核对状态: 0,账平  1,长款  -1,短款  **/
	private Integer checkState;
	
	/** 处理方式: 1退款，2冲正，3登帐 **/
	private Integer dealway;
	
	/** 处理人 **/
	private String dealby;
	
	/** 处理状态 **/
	private Integer dealState;
	
	private Timestamp dealDate;
	
	/** 处理信息备注 **/
	private String dealRemark;
	
	private String createBy;
	
	private Timestamp createDate;
	
	private String updateBy;
	
	private Timestamp updateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public Timestamp getHisBizDate() {
		return hisBizDate;
	}

	public void setHisBizDate(Timestamp hisBizDate) {
		this.hisBizDate = hisBizDate;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMethodName() {
		return payMethodName;
	}

	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
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

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getHisPrice() {
		return hisPrice;
	}

	public void setHisPrice(Integer hisPrice) {
		this.hisPrice = hisPrice;
	}

	public Integer getMerchPrice() {
		return merchPrice;
	}

	public void setMerchPrice(Integer merchPrice) {
		this.merchPrice = merchPrice;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Integer getDealway() {
		return dealway;
	}

	public void setDealway(Integer dealway) {
		this.dealway = dealway;
	}

	public String getDealby() {
		return dealby;
	}

	public void setDealby(String dealby) {
		this.dealby = dealby;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}

	public String getDealRemark() {
		return dealRemark;
	}

	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
	}

	public Timestamp getDealDate() {
		return dealDate;
	}

	public void setDealDate(Timestamp dealDate) {
		this.dealDate = dealDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		
		this.updateBy = updateBy;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
}
