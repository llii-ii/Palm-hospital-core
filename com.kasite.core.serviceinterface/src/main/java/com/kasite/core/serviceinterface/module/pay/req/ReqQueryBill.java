package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 对账列表查询请求入参实体类
 * 
 * @author zhaoy
 *
 */
public class ReqQueryBill extends AbsReq{

	public ReqQueryBill(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.billCheckId = getDataJs().getString("BillCheckId");
			this.startDate = getDataJs().getString("StartDate");
			this.endDate = getDataJs().getString("EndDate");
			this.queryDate = getDataJs().getString("QueryDate");
			this.merchType = getDataJs().getString("MerchType");
			this.channelId = getDataJs().getString("ChannelId");
			this.checkState = getDataJs().getString("CheckState");
			this.dealWay = getDataJs().getInteger("DealWay");
			this.dealState = getDataJs().getInteger("DealState");
			this.hisOrderNo = getDataJs().getString("HisOrderNo");
			this.orderNo = getDataJs().getString("OrderNo");
			this.orderId = getDataJs().getString("OrderId");
			this.merchNo = getDataJs().getString("MerchNo");
			this.initCheckState = getDataJs().getInteger("InitCheckState");
			this.queryYear = getDataJs().getString("QueryYear");
			this.payType = getDataJs().getString("PayType");
			this.dateType = getDataJs().getInteger("DateType");
			this.orderRule = getDataJs().getInteger("OrderRule");
			this.configKey = getDataJs().getString("ConfigKey");
			this.transType = getDataJs().getString("TransType");
			this.isCheckOut = getDataJs().getString("IsCheckOut");
			this.fileType = getDataJs().getString("FileType");
			this.payKey = getDataJs().getString("PayKey");
			this.billSingleType = getDataJs().getInteger("BillSingleType");
		}
	}
	
	public ReqQueryBill(InterfaceMessage msg, String refundOrderId, String refundNo) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.billCheckId = getDataJs().getString("BillCheckId");
			this.orderNo = getDataJs().getString("OrderNo");
			this.payKey = getDataJs().getString("PayKey");
			this.billSingleType = getDataJs().getInteger("BillSingleType");
			this.refundOrderId = refundOrderId;
			this.refundNo = refundNo;
		}
	}
	
	public ReqQueryBill(InterfaceMessage msg, String billCheckId, String orderId, 
			Integer billSingleType, String refundOrderId, String refundNo) throws AbsHosException {
		super(msg);
		this.billCheckId = billCheckId;
		this.orderId = orderId;
		this.billSingleType = billSingleType;
		this.refundOrderId = refundOrderId;
		this.refundNo = refundNo;
	}
	
	/**
	 * 查询对账开始时间
	 */
	private String startDate;
	
	/**
	 * 查询对账结束时间
	 */
	private String endDate;
	
	private String queryDate;
	
	/**
	 * 商户号
	 */
	private String configKey;
	
	/**
	 * 服务类型
	 */
	private String transType;
	
	/**
	 * 商户类型,微信：WX，支付宝：ZFB,银联：YL等
	 */
	private String merchType;
	
	/**
	 * 交易渠道
	 */
	private String channelId;
	
	/**
	 * 核对状态:null全部 0账平 1账不平
	 */
	private String checkState;
	
	/**
	 * 处理方式，1退款，2冲正，3登帐
	 */
	private Integer dealWay;
	
	/**
	 * 处理状态 0未处置 1已处置
	 */
	private Integer dealState;
	
	/**
	 * HIS支付/退费订单号
	 */
	private String hisOrderNo;
	
	/**
	 * 全流程支付/退费订单号
	 */
	private String orderNo;
	
	/**
	 * 商户支付/退费订单号
	 */
	private String merchNo;
	
	private Integer initCheckState;
	
	private String queryYear;
	
	private String payType;
	
	//1,按日对账 2,按月对账
	private Integer dateType;
	
	private String billCheckId;
	
	/**
	 * 银行卡号
	 */
	private String bankNo;
	
	/**
	 * 勾兑状态
	 */
	private String isCheckOut;
	
	/**
	 * 文件类型
	 */
	private String fileType;
	
	/**二级校验密码***/
	private String payKey;
	
	private Integer billSingleType;
	
	private String orderId;
	
	private String refundOrderId;
	
	private String refundNo;
	
	/**排序规则**/
	private Integer orderRule;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getMerchType() {
		return merchType;
	}

	public void setMerchType(String merchType) {
		this.merchType = merchType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public Integer getDealWay() {
		return dealWay;
	}

	public void setDealWay(Integer dealWay) {
		this.dealWay = dealWay;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public Integer getInitCheckState() {
		return initCheckState;
	}

	public void setInitCheckState(Integer initCheckState) {
		this.initCheckState = initCheckState;
	}

	public String getQueryYear() {
		return queryYear;
	}

	public void setQueryYear(String queryYear) {
		this.queryYear = queryYear;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getDateType() {
		return dateType;
	}

	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}

	public String getBillCheckId() {
		return billCheckId;
	}

	public void setBillCheckId(String billCheckId) {
		this.billCheckId = billCheckId;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getIsCheckOut() {
		return isCheckOut;
	}

	public void setIsCheckOut(String isCheckOut) {
		this.isCheckOut = isCheckOut;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

	public Integer getBillSingleType() {
		return billSingleType;
	}

	public void setBillSingleType(Integer billSingleType) {
		this.billSingleType = billSingleType;
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

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Integer getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(Integer orderRule) {
		this.orderRule = orderRule;
	}

}
