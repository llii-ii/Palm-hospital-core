package com.kasite.client.zfb.util;

/**
 * @author linjf
 * TODO 支付宝自定义全局变量
 */
public class AlipayConstant {

	final public static String CODE_10000 = "10000"; 
	
	final public static String CODE_10003 = "10003"; 
	
	
	/**
	 * 订单交易状态：交易结束，不可退款
	 */
	final public static String TRADE_STATUS_FINISHED = "TRADE_FINISHED"; 
	/**
	 * 订单交易状态：交易支付成功
	 */
	final public static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS"; 
	/**
	 * 订单交易状态：未付款交易超时关闭，或支付完成后全额退款
	 */
	final public static String TRADE_STATUS_CLOSED = "TRADE_CLOSED"; 
	/**
	 * 订单交易状态：交易创建，等待买家付款
	 */
	final public static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY"; 
	
	/************************支付宝sub_code代码*****************************************/
	
	/**
	 * 返回sub_code，无效的支付二维码
	 */
	final public static String SUB_CODE_PAYMENT_AUTH_CODE_INVALID = "ACQ.PAYMENT_AUTH_CODE_INVALID"; 
	
	/**
	 * 返回sub_code，系统错误	重新发起请求
	 */
	final public static String SUB_CODE_SYSTEM_ERROR = "ACQ.SYSTEM_ERROR"; 
	
	/**
	 * 返回sub_code，检查请求参数，修改后重新发起请求
	 */
	final public static String SUB_CODE_INVALID_PARAMETER = "ACQ.INVALID_PARAMETER"; 
	/**
	 * 返回sub_code，参数无效	
	 */
	final public static String SUB_CODE_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST"; 
	
	

	
}
