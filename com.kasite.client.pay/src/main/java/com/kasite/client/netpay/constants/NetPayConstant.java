package com.kasite.client.netpay.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linjf
 * 招行一网通全局变量
 */
public class NetPayConstant {

	
	/********************接口地址**********************/
	//正式
	public static final String URL_MB_EUSERPAY = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll?MB_EUserPay";
	public static final String URL_QUERYSINGLEORDER = "https://payment.ebank.cmbchina.com/NetPayment/BaseHttp.dll?QuerySingleOrder";
	public static final String URL_DOREFUND = "https://payment.ebank.cmbchina.com/NetPayment/BaseHttp.dll?DoRefund";
	public static final String URL_QUERYSETTLEDREFUND = "https://payment.ebank.cmbchina.com/NetPayment/BaseHttp.dll?QuerySettledRefund";
	public static final String URL_QUERYSETTLEDORDERBYMERCHANTDATE = "https://payment.ebank.cmbchina.com/NetPayment/BaseHttp.dll?QuerySettledOrderByMerchantDate";
	public static final String URL_QUERYREFUNDBYDATE = "https://payment.ebank.cmbchina.com/NetPayment/BaseHttp.dll?QueryRefundByDate";
	public static final String URL_DOBUSINESS = "https://b2b.cmbchina.com/CmbBank_B2B/UI/NetPay/DoBusiness.ashx";
	//测试
//	public static final String URL_MB_EUSERPAY = "http://121.15.180.66:801/netpayment/BaseHttp.dll?MB_EUserPay";
//	public static final String URL_QUERYSINGLEORDER = "http://121.15.180.66:801/Netpayment_dl/BaseHttp.dll?QuerySingleOrder";
//	public static final String URL_DOREFUND = "http://121.15.180.66:801/NetPayment_dl/BaseHttp.dll?DoRefund";
//	public static final String URL_QUERYSETTLEDREFUND = "http://121.15.180.66:801/netpayment_dl/BaseHttp.dll?QuerySettledRefund";
//	public static final String URL_QUERYSETTLEDORDERBYMERCHANTDATE = "http://121.15.180.66:801/NetPayment_dl/BaseHttp.dll?QuerySettledOrderByMerchantDate";
//	public static final String URL_QUERYREFUNDBYDATE = "http://121.15.180.66:801/Netpayment_dl/BaseHttp.dll?QueryRefundByDate";
//	public static final String URL_DOBUSINESS = "http://121.15.180.72/CmbBank_B2B/UI/NetPay/DoBusiness.ashx";
	
	/**
	 * 通用的订单超时时间-默认15分钟
	 */
	public final static String EXPIRETIMESPAN_15 ="15";
	/**
	 * 接口版本号,固定为“1.0”
	 */
	public final static String VSERSION_1_0 ="1.0";
	/**
	 * 	参数编码,固定为“UTF-8”
	 */
	public final static String CHARSET_UTF8 ="UTF-8";
	/**
	 * 签名算法,固定为“SHA-256”
	 */
	public final static String SIGNTYPE_SHA256 ="SHA-256";
	/**
	 * 回调异步通知签名为RSA
	 */
	public final static String SIGNTYPE_RSA ="RSA";
	
	/**
	 * A:储蓄卡支付，即禁止信用卡支付
	 */
	public final static String CARDTYPE_A ="A"; 
	/**
	 * A：按银行订单流水号查询
	 */
	public final static String TYPE_A ="A"; 
	/**
	 * B：按商户订单日期和订单号查询
	 */
	public final static String TYPE_B ="B"; 
	public final static String HASNEXT_Y ="Y"; 
	public final static String HASNEXT_N ="N"; 
	
	public final static String TXCODE_FBPK ="FBPK"; 
	  
	
	/********************出参code定义**********************/
	public final static String RSPCODE_SUC0000 ="SUC0000"; //成功
	public final static String RSPCODE_MSS3411 ="MSS3411"; //签名错误
	public final static String RSPCODE_MSS3804 ="MSS3804"; //直连退款失败
	public final static String RSPCODE_MSS3202 ="MSS3202"; //重复的直连退款请求，拒绝处理
	
	/********************订单状态**********************/
	public final static String ORDERSTATUS_0 ="0"; //,0:已结帐
	public final static String ORDERSTATUS_1 ="1"; //,1:已撤销
	public final static String ORDERSTATUS_2 ="2"; //,2:部分结帐
	public final static String ORDERSTATUS_4 ="4"; //,4:未结帐
	public final static String ORDERSTATUS_7 ="7"; //,7:冻结金额已经全部结账
	public final static String ORDERSTATUS_8 ="8"; //,8:冻结交易，冻结金额只结帐了一部分
	public final static String ORDERSTATUS_210 ="210"; //,210：已直接退款
	public final static String ORDERSTATUS_219 ="219"; //,219：直接退款已受理
	public final static String ORDERSTATUS_240 ="240"; //240：已授权退款
	public final static String ORDERSTATUS_249 ="249"; //249：授权退款已受理
	
	public static Map<String,String> publicKeyMap = new HashMap<String,String>(16);
	
	
	
}
