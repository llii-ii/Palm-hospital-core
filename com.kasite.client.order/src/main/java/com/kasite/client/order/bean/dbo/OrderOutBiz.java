package com.kasite.client.order.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf
 * TODO
 */
@Table(name="O_ORDER_OUT_BIZ")
public class OrderOutBiz {

	@KeySql(useGeneratedKeys=true)
	@Id
	private Long id;
	
	private String orderId;
	
	private String refundOrderId;
	
	private String operatorId;
	
	private String operatorName;
	
	private Timestamp createDate;
	
	private Timestamp updateDate;
	
	private String channelId;
	
	private Integer outBizType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getOutBizType() {
		return outBizType;
	}

	public void setOutBizType(Integer outBizType) {
		this.outBizType = outBizType;
	}
	
	
}
