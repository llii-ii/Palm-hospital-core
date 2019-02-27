package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqSyncLocalOrderState extends AbsReq{

	/**订单ID*/
	private String orderId; 
	/**
	 * HIS订单状态
	 */
	private int hisBizState;
	/**
	 * HIS支付状态 TODO 目前未实现
	 */
	private int hisPayState;
	/**
	 * 如果是查询HIS订单 要返回业务类型。
	 */
	private BusinessTypeEnum busType;
	
	/**
	 * 如果在前面的业务逻辑有做本地订单的查询就不要再查一次了
	 */
	private RespOrderLocalList orderLocal;
	
	public RespOrderLocalList getOrderLocal() {
		return orderLocal;
	}

	public void setOrderLocal(RespOrderLocalList orderLocal) {
		this.orderLocal = orderLocal;
	}

	public BusinessTypeEnum getBusType() {
		return busType;
	}

	public void setBusType(BusinessTypeEnum busType) {
		this.busType = busType;
	}

	/**操作人员ID*/
	private String operatorId; 
	/**操作人员姓名*/
	private String operatorName;
	
	public String getOrderId() {
		return orderId;
	}

	public int getHisBizState() {
		return hisBizState;
	}

	public void setHisBizState(int hisBizState) {
		this.hisBizState = hisBizState;
	}

	public int getHisPayState() {
		return hisPayState;
	}

	public void setHisPayState(int hisPayState) {
		this.hisPayState = hisPayState;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public ReqSyncLocalOrderState(InterfaceMessage msg, String orderId, int hisBizState, int hisPayState,
			BusinessTypeEnum busType, String operatorId, String operatorName) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.hisBizState = hisBizState;
		this.hisPayState = hisPayState;
		this.busType = busType;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
	}

	public ReqSyncLocalOrderState(InterfaceMessage msg, BusinessTypeEnum busType) throws AbsHosException {
		super(msg);
		this.orderId = getDataJs().getString("OrderId");
		this.hisBizState = getDataJs().getInteger("HisBizState");
		this.hisPayState = getDataJs().getInteger("HisPayState");
		this.busType = busType;
		this.operatorId = this.getOperatorId();
		this.operatorName = this.getOperatorName();
	}

}
