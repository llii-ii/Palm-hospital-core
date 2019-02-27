package com.kasite.client.swiftpass.constants;


/**
 * @author linjf
 * 威富通全局变量
 */
public class SwiftpassConstants {
	
    /**
     * 版本号，version默认值是2.0
     */
    public final static String VERSION = "2.0";
    
    /**
     * 可选值 UTF-8 ，默认为 UTF-8
     */
    public final static String CHARSET = "UTF-8";
    
    /**
     * 签名类型，取值：MD5默认：MD5
     */
    public final static String SIGN_TYPE = "MD5";
    
    
    public final static String SWIFTPASS_GATEWAY_URL="https://pay.swiftpass.cn/pay/gateway";
    public final static String SWIFTPASS_DOWNLOAD_URL="https://download.swiftpass.cn/gateway";
    
    //退款渠道 ORIGINAL-原路退款，默认
    public final static String REFUND_CHANNEL_ORIGINAL="ORIGINAL";
    
    /*************威富通的商户类型**************/
    public final static String MCH_TYPE_WX="WX";
    public final static String MCH_TYPE_ZFB="ZFB";
    public final static String MCH_TYPE_YL="YL";
    
    
    /*************威富通的账单类型**************/
    public final static String BILL_TYPE_ALL="ALL";
    public final static String BILL_TYPE_SUCCESS="SUCCESS";
    public final static String BILL_TYPE_REFUND="REFUND";
    
    /*************威富通的API**************/
    public final static String  PAY_WEIXIN_JSPAY="pay.weixin.jspay";
    public final static String  UNIFIED_TRADE_REFUND="unified.trade.refund";
    public final static String  UNIFIED_TRADE_QUERY="unified.trade.query";
    public final static String  UNIFIED_TRADE_REFUNDQUERY="unified.trade.refundquery";
    public final static String  UNIFIED_TRADE_MICROPAY="unified.trade.micropay";
    public final static String  PAY_BILL_MERCHANT="pay.bill.merchant";
    
    /*************威富通的订单状态**************/
    //成功
    public final static String  TRADESTATE_SUCCESS="SUCCESS"; 
    //转入退款
    public final static String  TRADESTATE_REFUND="REFUND"; 
    //未支付
    public final static String  TRADESTATE_NOTPAY="NOTPAY"; 
    //已关闭
    public final static String  TRADESTATE_CLOSED="CLOSED"; 
    //已冲正
    public final static String  TRADESTATE_REVERSE="REVERSE"; 
    //已撤销
    public final static String  TRADESTATE_REVOKED="REVOKED"; 
    //用户支付中
    public final static String  TRADESTATE_USERPAYING="USERPAYING"; 
    //支付失败(其他原因，如银行返回失败)
    public final static String  TRADESTATE_PAYERROR="PAYERROR"; 
    //退款失败
    public final static String  TRADESTATE_FAIL="FAIL"; 
    //退款处理中
    public final static String  TRADESTATE_PROCESSING="PROCESSING"; 
    //未确定， 需要商户原退款单号重新发起
    public final static String  TRADESTATE_NOTSURE="NOTSURE"; 
    //转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者平台转账的方式进行退款。
    public final static String  TRADESTATE_CHANGE="CHANGE"; 
    
    /*************威富通的交易状态类型**************/
    public final static String TRADE_STATE_PAY_STR="支付成功";
    public final static String TRADE_STATE_REFUND_STR="转入退款";
}
