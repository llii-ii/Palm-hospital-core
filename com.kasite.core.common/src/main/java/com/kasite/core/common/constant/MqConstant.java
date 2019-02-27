package com.kasite.core.common.constant;

/**
 * @author linjf 2017年11月14日 17:38:21
 * TODO mq全局变量
 */
public class MqConstant {
	/**
	 * 微信消息推送队列
	 */
	public final static String MQ_WXMSG_QUEUE_NAME = "wx_msg_queue";
	
	/**
	 * 短信验证码推送队列
	 */
	public final static String MQ_DXMSG_QUEUE_NAME = "dx_msg_queue";
	
	/**
	 * 订单支付队列
	 */
	public final static String MQ_ORDER_PAY_QUEUE_NAME = "order_pay_queue";
	
	/**
	 * 订单取消队列
	 */
	public final static String MQ_ORDER_CANCEL_QUEUE_NAME = "order_cancel_queue";
	
	/**
	 * 支付结果未知的订单队列
	 */
	public final static String MQ_ORDER_UNKNOWN_RESULT_NAME = "order_unknown_result";
}
