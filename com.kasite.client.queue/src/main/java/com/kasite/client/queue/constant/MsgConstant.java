package com.kasite.client.queue.constant;
/**
 * 预约挂号常量类
 * @author Administrator
 *
 */
public class MsgConstant {
	
	/**挂号成功，已缴费*/
	public static final String MODETYPE_10101110="10101110";
	/**挂号成功，未缴费*/
	public static final String MODETYPE_10101111="10101111";
	/**取消成功，已退费*/
	public static final String MODETYPE_10101112="10101112";
	/**取消成功，已关闭*/
	public static final String MODETYPE_10101113="10101113";
	/**停诊通知*/
	public static final String MODETYPE_10101114="10101114";
	/**检测单支付推送*/
	public static final String MODETYPE_10101115="10101115";
	/**订单提醒推送*/
	public static final String MODETYPE_10101116="10101116";
	/**药单支付推送*/
	public static final String MODETYPE_10101117="10101117";
	/**排队消息推送*/
	public static final String MODETYPE_10101118="10101118";
	/**就诊卡充值推送*/
	public static final String MODETYPE_10101119="10101119";
	/**住院充值推送*/
	public static final String MODETYPE_10101120="10101120";
	/**随访消息推送*/
	public static final String MODETYPE_10101121="10101121";
	/**叫号推送*/
	public static final String MODETYPE_10101122="10101122";
	/**医嘱缴费退费推送*/
	public static final String MODETYPE_10101123="10101123";
	/**咨询医生回复推送*/
	public static final String MODETYPE_10201110="10201110";
	/**验证码发送*/
	public static final String MODETYPE_20101110="20101110";
	
	/**消息模块*/
	public static final String WX_100123="100123";
	public static final String ZFB_100125="100125";
	public static final String BD_100126="100126";
	public static final String PARAMTYPEBYIN ="IN";
	public static final String PARAMTYPEBYOUT ="OUT";
	public static final String OPERID ="<operId>";
	public static String CONTENT ="<content_@>";
	public static String DETAILURL ="<detailUrl>";
	/**时间戳*/
	public static final String CREATETIME ="<createTime>";
	

	/***************消息类型*******************/
	/**短信*/
	public static final String MSGTYPE_3 = "3";
	/**文本消息*/
	public static final String MSGTYPE_311 = "3.1.1";
	/**模板消息*/
	public static final String MSGTYPE_33 = "3.3";
}
