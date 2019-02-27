package com.kasite.client.pay.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf
 * 商户异步通知实体类
 */
@Table(name="P_MERCHANT_ORDER_CHECK")
public class MerchantOrderCheck {

	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;
	
	private String orderId;
	
	private String transactionNo;
	
	private Integer times;
	
	private String configKey;
	
	private String clientId;

	
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	
}
