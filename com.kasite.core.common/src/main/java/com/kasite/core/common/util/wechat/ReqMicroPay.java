package com.kasite.core.common.util.wechat;

/**
 * 微信提交刷卡支付请求入参(不包含公共参数)
 * https://api.mch.weixin.qq.com/pay/micropay
 * @author 無
 *
 */
public class ReqMicroPay {
	/**商品描述(必填)*/
	private String body;
	/**商品详情*/
	private String detail;
	/**附加数据  在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据*/
	private String attach;
	/**商户订单号(必填)*/
	private String outTradeNo;
	/**订单总金额(必填) 单位为分，只能为整数*/
	private int totalFee;
	/**货币类型  默认人民币：CNY*/
	private String feeType;
	/**终端IP(必填) 调用微信支付API的机器IP*/
	private String spbillCreateIp;
	/**订单优惠标记*/
	private String goodsTag;
	/**授权码 (必填)
	 * 扫码支付授权码，设备读取用户微信中的条码或者二维码信息
	 *（注：用户刷卡条形码规则：18位纯数字，以10、11、12、13、14、15开头）*/
	private String authCode;
	/**场景信息*/
	private String sceneInfo;
	
	/**
	 * 是否限制信用卡1是，0否
	 */
	private Integer isLimitCredit;
	
	
	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}
	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getGoodsTag() {
		return goodsTag;
	}
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getSceneInfo() {
		return sceneInfo;
	}
	public void setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
	}
	public ReqMicroPay(String body, String detail, String attach, String outTradeNo, int totalFee, String feeType,
			String spbillCreateIp, String goodsTag, String authCode, String sceneInfo) {
		super();
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.outTradeNo = outTradeNo;
		this.totalFee = totalFee;
		this.feeType = feeType;
		this.spbillCreateIp = spbillCreateIp;
		this.goodsTag = goodsTag;
		this.authCode = authCode;
		this.sceneInfo = sceneInfo;
	}
	
}
	
		
		
