package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * 订单明细类
 * @author 無
 *
 */
public class ReqOrderDetailLocal extends AbsReq{
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 关联处方单号【订单关联的资源ID 】
	 */
	private String prescNo;
	/**
	 * 支付流水号  
	 */
	private String transactionId;
	
	private String overState;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	
	public String getOverState() {
		return overState;
	}
	public void setOverState(String overState) {
		this.overState = overState;
	}
	public ReqOrderDetailLocal(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		this.prescNo = XMLUtil.getString(dataEl, "PrescNo", false);
		this.transactionId = XMLUtil.getString(dataEl, "TransactionId", false);
		this.overState = XMLUtil.getString(dataEl, "OverState", false);
		
		//兼容旧版本httppost
		/*
		String channelId 	 = ParamUtils.getValueFromMap(paramMap,"channelId");//传入的渠道id
		String sign 	 	 = ParamUtils.getValueFromMap(paramMap,"sign");//渠道id对应的密码，后台要进行校验
		String orderId 	 	 = ParamUtils.getValueFromMap(paramMap,"orderId");//平台订单id
		String cargeType 	 = ParamUtils.getValueFromMap(paramMap,"cargeType");//支付方式：1 支付宝  3 微信
		String transactionId = ParamUtils.getValueFromMap(paramMap,"transactionId");//支付流水号
		String hisOrderId 	 = ParamUtils.getValueFromMap(paramMap,"hisOrderId");//his订单id
		String authCode 	 = ParamUtils.getValueFromMap(paramMap,"authCode");//扫码支付授权码
		 */
		if(StringUtil.isBlank(orderId)) {
			this.orderId = XMLUtil.getString(dataEl, "orderId", false);
		}
		if(StringUtil.isBlank(prescNo)) {
			this.prescNo = XMLUtil.getString(dataEl, "hisOrderId", false);
		}
		if(StringUtil.isBlank(transactionId)) {
			//支付流水号
			this.transactionId = XMLUtil.getString(dataEl, "transactionId", false);
		}
		if( StringUtil.isBlank(orderId) && StringUtil.isBlank(prescNo)) {
			throw new ParamException("传入参数中[OrderId][PrescNo]不能同时为空！");
		}
	}
	/**
	 * @param msg
	 * @param orderId
	 * @param prescNo
	 * @throws AbsHosException
	 */
	public ReqOrderDetailLocal(InterfaceMessage msg, String orderId, String prescNo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.prescNo = prescNo;
	}
	/**
	 * @param msg
	 * @param orderId
	 * @param prescNo
	 * @param overState
	 * @throws AbsHosException
	 */
	public ReqOrderDetailLocal(InterfaceMessage msg, String orderId, String prescNo, String overState)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.prescNo = prescNo;
		this.overState = overState;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	
	
	
}
