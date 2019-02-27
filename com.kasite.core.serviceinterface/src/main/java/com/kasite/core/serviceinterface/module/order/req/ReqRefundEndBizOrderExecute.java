package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 退费完成后订单回调接口实现入参
 */
public class ReqRefundEndBizOrderExecute extends AbsReq{
	/**订单ID*/
	private String orderId; 
	/**操作人员ID*/
	private String operatorId; 
	/**操作人员姓名*/
	private String operatorName;
	/**支付订单*/
	private String transActionNo;
	/**支付金额*/
	private Integer price;
	/**支付结果参数 如： 微信的返回微信内容 xmlContent
	 * <xml>
   <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
   <attach><![CDATA[支付测试]]></attach>
   <bank_type><![CDATA[CFT]]></bank_type>
   <fee_type><![CDATA[CNY]]></fee_type>
   <is_subscribe><![CDATA[Y]]></is_subscribe>
   <mch_id><![CDATA[10000100]]></mch_id>
   <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
   <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
   <out_trade_no><![CDATA[1409811653]]></out_trade_no>
   <result_code><![CDATA[SUCCESS]]></result_code>
   <return_code><![CDATA[SUCCESS]]></return_code>
   <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
   <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
   <time_end><![CDATA[20140903131540]]></time_end>
   <total_fee>1</total_fee>
<coupon_fee><![CDATA[10]]></coupon_fee>
<coupon_count><![CDATA[1]]></coupon_count>
<coupon_type><![CDATA[CASH]]></coupon_type>
<coupon_id><![CDATA[10000]]></coupon_id>
<coupon_fee><![CDATA[100]]></coupon_fee>
   <trade_type><![CDATA[JSAPI]]></trade_type>
   <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
</xml>
	 * */
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTransActionNo() {
		return transActionNo;
	}

	public void setTransActionNo(String transActionNo) {
		this.transActionNo = transActionNo;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

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

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqRefundEndBizOrderExecute(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(service, "OrderId", true);
		this.transActionNo =  XMLUtil.getString(service, "TransActionNo", true);
		this.price =  XMLUtil.getInt(service, "Price", true);
		this.content =  XMLUtil.getString(service, "Content", false);
		this.operatorId =  XMLUtil.getString(service, "OperatorId", false,super.getOpenId());
		this.operatorName =  XMLUtil.getString(service, "OperatorName", false,super.getOperatorName());
	}

	
}
