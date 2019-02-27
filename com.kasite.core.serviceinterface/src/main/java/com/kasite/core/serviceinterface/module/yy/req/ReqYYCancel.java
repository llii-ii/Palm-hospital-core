package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**退号入参
 * @author lsq
 * version 1.0
 * 2017-7-7上午10:59:29
 */
public class ReqYYCancel extends AbsReq{
	/**订单id*/
	private String orderId;
	/**取消原因*/
	private String reason;
	/**操作人ID*/
	private String operatorId;
	/**操作人姓名*/
	private String operatorName;

	
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public ReqYYCancel(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(ser, "OrderId", false);
		this.reason = XMLUtil.getString(ser, "Reason", false);
		this.operatorId = XMLUtil.getString(ser, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(ser, "OperatorName", false,super.getOperatorName());
	}

	/**
	 * @Title: ReqYYCancel
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param reason
	 * @param operatorId
	 * @param operatorName
	 * @throws AbsHosException
	 */
	public ReqYYCancel(InterfaceMessage msg, String orderId, String reason, String operatorId, String operatorName) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.reason = reason;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
	}
	
	
}
