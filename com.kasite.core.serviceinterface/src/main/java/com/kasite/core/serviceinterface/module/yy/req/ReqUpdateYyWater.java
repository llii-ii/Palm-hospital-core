package com.kasite.core.serviceinterface.module.yy.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqUpdateYyWater extends AbsReq{

	private String orderId;
	private Integer pushState;
	private String pushRemark;
	private Integer num;
	
	
	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the pushState
	 */
	public Integer getPushState() {
		return pushState;
	}
	/**
	 * @param pushState the pushState to set
	 */
	public void setPushState(Integer pushState) {
		this.pushState = pushState;
	}
	/**
	 * @return the pushRemark
	 */
	public String getPushRemark() {
		return pushRemark;
	}
	/**
	 * @param pushRemark the pushRemark to set
	 */
	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}
	
	public ReqUpdateYyWater(InterfaceMessage msg, String orderId, Integer pushState, String pushRemark,Integer num)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.pushState = pushState;
		this.pushRemark = pushRemark;
		this.num = num;
	}
	
	


	
	
	
	
}
