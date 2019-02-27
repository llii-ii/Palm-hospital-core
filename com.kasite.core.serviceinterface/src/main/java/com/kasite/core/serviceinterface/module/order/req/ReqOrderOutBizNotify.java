package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqOrderOutBizNotify extends AbsReq{

	/**订单ID*/
	private String orderId; 
	
	/**
	 * 全流程退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 业务类型1支付业务2退费业务
	 */
	private Integer outBizType;
	
	/**操作人员ID*/
	private String operatorId; 
	/**操作人员姓名*/
	private String operatorName;

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqOrderOutBizNotify(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(service, "OrderId", true);
		this.operatorId =  XMLUtil.getString(service, "OperatorId", true);
		this.operatorName =  XMLUtil.getString(service, "OperatorName", true);
		this.outBizType =  XMLUtil.getInt(service, "OutBizType", true);
		this.refundOrderId =  XMLUtil.getString(service, "RefundOrderId", false);
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

	public Integer getOutBizType() {
		return outBizType;
	}

	public void setOutBizType(Integer outBizType) {
		this.outBizType = outBizType;
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

	
	
}
