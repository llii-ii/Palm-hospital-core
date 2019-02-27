package com.kasite.core.serviceinterface.module.order.req;

import java.sql.Timestamp;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @Description:  订单回调
 * @version: V1.0  
 * 2017-7-11 下午14:28:49
 */
public class ReqOrderCallback extends AbsReq {
	public ReqOrderCallback(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}
	private String id;
 	/**订单ID*/
 	private String orderId; 
	/**调用类型1.支付HIS2取消HIS3冲正*/
	private Integer callType; 
	/**创建日期*/
	private Timestamp createDate; 
	/**更新日期*/
	private Timestamp updateDate;  
	/**重试次数*/
	private Integer retryNum; 
	/**状态0重试中1重试成功-1重试失败*/
	private Integer state; 
	private String remark;
	/**存放必要调用信息*/
	private String content; 

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
	public Integer getCallType() {
		return callType;
	}
	public void setCallType(Integer callType) {
		this.callType = callType;
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
	public Integer getRetryNum() {
		return retryNum;
	}
	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
