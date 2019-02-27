package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqRevokeOrder extends AbsReq{

	/**订单ID*/
	private String orderId; 
	/**操作人员ID*/
	private String operatorId; 
	/**操作人员姓名*/
	private String operatorName;
	/**是否撤销商户订单 1撤销商户  0不撤销商户只撤销本地*/
	private Integer isRevokeMerchantOrder;
	
	public String getOrderId() {
		return orderId;
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

	public Integer getIsRevokeMerchantOrder() {
		return isRevokeMerchantOrder;
	}

	public void setIsRevokeMerchantOrder(Integer isRevokeMerchantOrder) {
		this.isRevokeMerchantOrder = isRevokeMerchantOrder;
	}
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqRevokeOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
//		this.orderId =  XMLUtil.getString(dataEl, "OrderId", true);
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		if(StringUtil.isEmpty(orderId)){
			this.orderId = XMLUtil.getString(dataEl, "orderId", true);
		}
		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false);
		if(StringUtil.isEmpty(operatorId)){
			this.operatorId = XMLUtil.getString(dataEl, "operatorId", false,super.getOpenId());
		}
		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false);
		if(StringUtil.isEmpty(operatorName)){
			this.operatorName = XMLUtil.getString(dataEl, "operatorName", false,super.getOperatorName());
		}
//		this.operatorId =  XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
//		this.operatorName =  XMLUtil.getString(dataEl, "OperatorName", true,super.getOperatorName());
		this.isRevokeMerchantOrder =  XMLUtil.getInt(dataEl, "IsRevokeMerchantOrder",1);
	}

	/**
	 * @Title: ReqRevokeOrder
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param operatorId
	 * @param operatorName
	 * @param isRevokeMerchantOrder
	 * @throws AbsHosException
	 */
	public ReqRevokeOrder(InterfaceMessage msg, String orderId, String operatorId, String operatorName, Integer isRevokeMerchantOrder) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.isRevokeMerchantOrder = isRevokeMerchantOrder;
	}

	
}
