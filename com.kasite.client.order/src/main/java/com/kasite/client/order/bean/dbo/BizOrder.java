package com.kasite.client.order.bean.dbo;import java.util.Date;import javax.persistence.Id;import javax.persistence.Table;/** * @author lq * @Description:  订单业务类 * @version: V1.0   * 2017-7-10 下午13:48:49 */@Table(name="O_BIZORDER")
public class BizOrder{	@Id
	private String orderId;  

	private String operatorId;  

	private String operatorName;  

	private Date beginDate;  

	private Date endDate;  		private Integer bizState;		/**	 * 外部的业务订单号	 */	private String outBizOrderId;			public String getOutBizOrderId() {		return outBizOrderId;	}	public void setOutBizOrderId(String outBizOrderId) {		this.outBizOrderId = outBizOrderId;	}	public String getOrderId() {		return orderId;	}	public void setOrderId(String orderId) {		this.orderId = orderId;	}	public String getOperatorId() {		return operatorId;	}	public void setOperatorId(String operatorId) {		this.operatorId = operatorId;	}	public String getOperatorName() {		return operatorName;	}	public void setOperatorName(String operatorName) {		this.operatorName = operatorName;	}	public Date getBeginDate() {		return beginDate;	}	public void setBeginDate(Date beginDate) {		this.beginDate = beginDate;	}	public Date getEndDate() {		return endDate;	}	public void setEndDate(Date endDate) {		this.endDate = endDate;	}	public Integer getBizState() {		return bizState;	}	public void setBizState(Integer bizState) {		this.bizState = bizState;	}	
}