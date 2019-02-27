package com.kasite.client.order.bean.dbo;import java.sql.Timestamp;import javax.persistence.Id;import javax.persistence.Table;/** * @author lq * @Description:  订单支付 * @version: V1.0   * 2017-7-11 下午14:28:49 */@Table(name="O_PAYORDER")
public class PayOrder{	 	/**订单ID*/	@Id 	private String orderId;  	/**支付交易流水号*/ 	private String transactionNo;
 	/**支付金额（分）*/ 	private Integer price; 
 	/**操作人ID*/ 	private String operatorId;  
 	/**操作人姓名*/ 	private String operatorName;  
 	/**开始日期*/ 	private Timestamp beginDate;  
 	/**结束日期*/ 	private Timestamp endDate;  
 	/**支付状态*/ 	private Integer payState;  	/**备注*/ 	private String remark; 	/**渠道ID*/ 	private String channelId; 	/** 	 * 支付的时候对应的使用的支付渠道配置信息 	 */ 	private String configKey; 	 	/** 	 * 支付账号 	 */ 	private String accNo; 		public String getConfigKey() {		return configKey;	}	public void setConfigKey(String configKey) {		this.configKey = configKey;	}	public String getOrderId() {		return orderId;	}	public void setOrderId(String orderId) {		this.orderId = orderId;	}	public Integer getPrice() {		return price;	}	public void setPrice(Integer price) {		this.price = price;	}	public String getOperatorId() {		return operatorId;	}	public void setOperatorId(String operatorId) {		this.operatorId = operatorId;	}	public String getOperatorName() {		return operatorName;	}	public void setOperatorName(String operatorName) {		this.operatorName = operatorName;	}	public Timestamp getBeginDate() {		return beginDate;	}	public void setBeginDate(Timestamp beginDate) {		this.beginDate = beginDate;	}	public Timestamp getEndDate() {		return endDate;	}	public void setEndDate(Timestamp endDate) {		this.endDate = endDate;	}	public Integer getPayState() {		return payState;	}	public void setPayState(Integer payState) {		this.payState = payState;	}	public String getRemark() {		return remark;	}	public void setRemark(String remark) {		this.remark = remark;	}	public String getChannelId() {		return channelId;	}	public void setChannelId(String channelId) {		this.channelId = channelId;	}	public String getTransactionNo() {		return transactionNo;	}	public void setTransactionNo(String transactionNo) {		this.transactionNo = transactionNo;	}	public String getAccNo() {		return accNo;	}	public void setAccNo(String accNo) {		this.accNo = accNo;	}		}