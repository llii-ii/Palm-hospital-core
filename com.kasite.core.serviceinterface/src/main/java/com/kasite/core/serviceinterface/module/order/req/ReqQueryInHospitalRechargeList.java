package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询预交金充值记录入参类
 * @author lcy
 * @version 1.0 
 * 2017-7-20上午11:52:28
 */
public class ReqQueryInHospitalRechargeList extends AbsReq{
	/**住院号*/
	private String hospitalNo;      
	/**开始时间*/
	private String beginDate;     
	/**结束时间*/
	private String endDate;     
	/**支付方式*/
	private String orderType;     
	/** 退费或者充值*/
	private String chargeType;  
	
	private String memberId;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public ReqQueryInHospitalRechargeList(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", true );
		this.beginDate = XMLUtil.getString(ser, "BeginDate", false);
		this.endDate = XMLUtil.getString(ser, "EndDate", false);
		this.orderType=XMLUtil.getString(ser, "OrderType", false);
		this.chargeType=XMLUtil.getString(ser, "ChargeType", false);
	}
	
}
