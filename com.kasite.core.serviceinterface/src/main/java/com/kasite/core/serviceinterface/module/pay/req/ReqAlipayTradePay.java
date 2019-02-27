package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 支付宝统一收单交易支付接口请求参数(不包含公共参数)
 * alipay.trade.pay
 * 
 * @author 無
 *
 */
public class ReqAlipayTradePay  extends AbsReq {
	/** 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复 */
	private String outTradeNo;
	/** 支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code */
	private String scene;
	/** 支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准 */
	private String authCode;
	/** 销售产品码 */
	private String productCode;
	/** 订单标题 */
	private String subject;
	/** 买家的支付宝用户id，如果为空，会从传入了码值信息中获取买家ID */
	private String buyerId;
	/** 如果该值为空，则默认为商户签约账号对应的支付宝用户ID */
	private String sellerId;
	/**
	 * 此处存【分】单位
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	 * 如果同时传入【可打折金额】和【不可打折金额】，该参数可以不用传入； 
	 * 如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
	 */
	private int totalAmount;
	/**
	 * 此处存【分】单位
	 * 参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。 
	 * 如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】
	 */
	private Integer discountableAmount;
	/** 订单描述 */
	private String body;
	/** 订单包含的商品列表信息，Json格式 */
	private String goodsDetail;
	/** 商户操作员编号 */
	private String operatorId;
	/** 商户门店编号 */
	private String storeId;
	/** 商户机具终端编号 */
	private String terminalId;
	/** 业务扩展参数 */
	private String extendParams;
	/**
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，
	 * 都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
	 */
	private String timeoutExpress;
	
	private Integer isLimitCredit;

	
	
	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}

	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getDiscountableAmount() {
		return discountableAmount;
	}

	public void setDiscountableAmount(Integer discountableAmount) {
		this.discountableAmount = discountableAmount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getExtendParams() {
		return extendParams;
	}

	public void setExtendParams(String extendParams) {
		this.extendParams = extendParams;
	}

	public String getTimeoutExpress() {
		return timeoutExpress;
	}

	public void setTimeoutExpress(String timeoutExpress) {
		this.timeoutExpress = timeoutExpress;
	}

	public ReqAlipayTradePay(InterfaceMessage msg, String outTradeNo, String scene, String authCode, String productCode,
			String subject, String buyerId, String sellerId, Integer totalAmount, Integer discountableAmount, String body,
			String goodsDetail, String operatorId, String storeId, String terminalId, String extendParams,
			String timeoutExpress) throws AbsHosException {
		super(msg);
		this.outTradeNo = outTradeNo;
		this.scene = scene;
		this.authCode = authCode;
		this.productCode = productCode;
		this.subject = subject;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.totalAmount = totalAmount;
		this.discountableAmount = discountableAmount;
		this.body = body;
		this.goodsDetail = goodsDetail;
		this.operatorId = checkOperatorId(operatorId);
		this.storeId = storeId;
		this.terminalId = terminalId;
		this.extendParams = extendParams;
		this.timeoutExpress = timeoutExpress;
	}
	
}
