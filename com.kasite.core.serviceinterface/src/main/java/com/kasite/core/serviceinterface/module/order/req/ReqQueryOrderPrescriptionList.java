package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
    
/**
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-26 下午2:17:11 查询医嘱订单入参
 **/
public class ReqQueryOrderPrescriptionList extends AbsReq {
	
	/**
	 * 用户成员唯一ID
	 */
	private String memberId;
	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/** 订单id */
	private String hisOrderId;
	/** 订单状态 */
	private String orderState;
	/** 处方单号 */
	private String prescNo;
	/** 订单类型 */
	private String serviceId;
	/**
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public ReqQueryOrderPrescriptionList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(service, "CardNo", false);
		this.cardType = XMLUtil.getString(service, "CardType", false);
		this.hisOrderId = XMLUtil.getString(service, "HisOrderId", false);
		this.prescNo = XMLUtil.getString(service, "PrescNo", false);
		this.orderState = XMLUtil.getString(service, "OrderState", false);
		this.beginDate = XMLUtil.getString(service, "BeginDate", false);
		this.endDate = XMLUtil.getString(service, "EndDate", false);
		this.serviceId = XMLUtil.getString(service, "ServiceId", false);
		this.memberId = XMLUtil.getString(service, "MemberId", false);
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	
	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
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

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
