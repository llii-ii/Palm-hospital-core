package com.kasite.core.common.constant;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.kasite.core.common.service.BusinessTypeEnum;
import com.yihu.hos.constant.IConstant;

/**
 * 全局变量
 * 
 * @author linjf TODO
 */
public class KstHosConstant implements IConstant {
//	//payState 总共有5个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
//	//bizState 总共有3个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
//	//overState 总共有3个状态 5:订单取消  6:订单撤销  7:已关闭
//	/**支付状态 payState  0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成*/
//	public static final int ORDER_PAY_STATE_0 = 0;
//	/**支付状态 payState  0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成*/
//	public static final int ORDER_PAY_STATE_1 = 1;
//	/**支付状态 payState  0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成*/
//	public static final int ORDER_PAY_STATE_2 = 2;
//	/**支付状态 payState  0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成*/
//	public static final int ORDER_PAY_STATE_3 = 3;
//	/**支付状态 payState  0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成*/
//	public static final int ORDER_PAY_STATE_4 = 4;
//	
//	/**业务状态 bizState 总共有3个状态 0:未执行业务  1:订单业务完成  2:订单业务取消*/
//	public static final int ORDER_BIZ_STATE_0 = 0;
//	/**业务状态 bizState 总共有3个状态 0:未执行业务  1:订单业务完成  2:订单业务取消*/
//	public static final int ORDER_BIZ_STATE_1 = 1;
//	/**业务状态 bizState 总共有3个状态 0:未执行业务  1:订单业务完成  2:订单业务取消*/
//	public static final int ORDER_BIZ_STATE_2 = 2;
//	
//	/**业务最终状态 overState 总共有3个状态 5:订单取消  6:订单撤销  7:已关闭*/
//	public static final int ORDER_OVER_STATE_5 = 5;
//	/**业务最终状态 overState 总共有3个状态 5:订单取消  6:订单撤销  7:已关闭*/
//	public static final int ORDER_OVER_STATE_6 = 6;
//	/**业务最终状态 overState 总共有3个状态 5:订单取消  6:订单撤销  7:已关闭*/
//	public static final int ORDER_OVER_STATE_7 = 7;
//	
	
	/***
	 * 错误文案定义
	 */
	public static final String ERROR_SERVICEIMPLREP = "医院：{0} 的接口实现类:{1} 被覆盖！！！请核实！！！。";
	
	/**
	 * 如果有涉及支付类的订单，默认 30分钟内必须完成支付，不然就将订单取消，让用户重复发起
	 */
	public static final int ORDER_PAY_TIME = 30;
	
	/**
	 * 前端用户token保存天数：cookie中
	 */
	public static final int COOKIE_TOKEN_DAYS = 1;
	/**
	 * 逗号 半角\英文 , 
	 */
	public static final String COMSP = ",";

	/****************** 各个模块简称定义 *********************/
	public static final String MODULE_ORDER = "ORDER";
	public static final String MODULE_BASIC = "BASIC";
	public static final String MODULE_YY = "YY";
	public static final String MODULE_QUEUE = "QUEUE";
	public static final String MODULE_REPROT = "REPROT";
	public static final String MODULE_MSG = "MSG";
	public static final String MODULE_REPORTFROMS = "REPORTFROMS";
	public static final String MODULE_PAY = "PAY";
	public static final String MODULE_BILL = "BILL";
	public static final String MODULE_SURVEY = "SURVEY";
	public static final String MODULE_BAT = "BAT";
	public static final String MODULE_QR = "QR";
	public static final String MODULE_SMARTPAY = "SMARTPAY";

	/****************** log4j模块名称配置 *********************/	
	public static final String LOG4J_DEFAULT = "DEFAULT";
	public static final String LOG4J_ORDER = "ORDER";
	public static final String LOG4J_BASIC = "BASIC";
	public static final String LOG4J_YY = "YY";
	public static final String LOG4J_QUEUE = "QUEUE";
	public static final String LOG4J_REPROT = "REPROT";
	public static final String LOG4J_MSG = "MSG";
	public static final String LOG4J_REPORTFROMS = "REPORTFROMS";
	public static final String LOG4J_SYSTEM = "SYSTEM";
	public static final String LOG4J_PAY = "PAY";
	public static final String LOG4J_PAY_WX = "PAY_WX";
	public static final String LOG4J_PAY_ALI = "PAY_ALI";
	public static final String LOG4J_BILL = "BILL";
	public static final String LOG4J_SURVEY = "SURVEY";
	public static final String LOG4J_BAT = "BAT";
	public static final String LOG4J_QR = "QR";
	public static final String LOG4J_SMARTPAY = "SMARTPAY";
	public static final String LOG4J_JOB = "JOB";
	public static final String LOG4J_HIS = "HIS";
	public static final String LOG4J_MCOPY = "MCOPY";
	
	/****************** 日志级别 *********************/
	public static final String LOG_LEVEL_TRACE = "trace";
	public static final String LOG_LEVEL_DEBUG = "debug";
	public static final String LOG_LEVEL_INFO = "info";
	public static final String LOG_LEVEL_WARN = "warn";
	public static final String LOG_LEVEL_ERROR = "error";
	public static final String LOG_LEVEL_FATAL = "fatal";

	
	/********************* 通用渠道ID配置 ********************/
	/**掌医微信渠道*/
	public static final String WX_CHANNEL_ID = "100123";
	/**掌医支付宝渠道*/
	public static final String ZFB_CHANNEL_ID = "100125";
	/**健康之路渠道*/
	public static final String JKZL_CHANNEL_ID = "100129";
	/**Mini付渠道*/
	public static final String MINIPAY_CHANNEL_ID = "minipay";
	/**号池渠道*/
	public static final String NUMBERSOURCE_CHANNEL_ID = "100127";
	/**企业微信渠道*/
	public static final String QYWECHAT_CHANNEL_ID = "200001";
	/**医院窗口退款渠道*/
	public static final String WINCANCEL_CHANNEL_ID = "wincancel";
	/**自助机扫码付渠道*/
	public static final String SELFSERVICEPAY_CHANNEL_ID = "selfservicepay";
	/**当面付渠道*/
	public static final String SWEEPCODEPAY_CHANNEL_ID = "sweepcodepay";
	/**腕带付渠道*/
	public static final String WRISTBANDCODEPAY_CHANNEL_ID = "wristbandcodepay";
	/**卡面付渠道*/
	public static final String CARDFACECODEPAY_CHANNEL_ID = "cardfacecodepay";
	/**医生工作站快捷付渠道*/
	public static final String DOCTORSTATIONPAY_CHANNEL_ID = "doctorstationpay";
	/**处方付渠道*/
	public static final String PRESCRIPTIONCODEPAY_CHANNEL_ID = "prescriptioncodepay";
	/**护士工作站快捷付渠道*/
	public static final String NURSINGSTATIONPAY_CHANNEL_ID = "nursingstationpay";
	/**微信小程序渠道*/
	public static final String SMALLPRO_CHANNEL_ID = "smallpro";
	/**系统管理后台 渠道ID*/
	public static final String SYSTEMMANAGER_CHANNEL_ID = "systemmanager";
	public static final String UNKNOW_CHANNEL_ID = "UnknowChannelId";
	public static final String UNKNOW_CHANNEL_NAME = "未知渠道";
	
	public static final String WX_CHANNEL_NAME = "微信";
	public static final String ZFB_CHANNEL_NAME = "支付宝";
	public static final String JKZL_CHANNEL_NAME = "健康之路";
	/**微信账单下载失败*/
	public static final String WX_BILL_FAIL ="FAIL";

	/*********************请求头********************/
	/**支付宝请求头部*/
	public static final String USERAGET_ALIPAY = "alipay";
	/**微信请求头部*/
	public static final String USERAGET_WX = "micromessenger";
	
	/*********************支付宝秘钥加密方式********************/
	/**支付宝秘钥加密方式*/
	public static final String ALIPAY_RSA = "RSA";
	public static final String ALIPAY_RSA2 = "RSA2";
	
	/********************* 通用卡类型 *******************/
	/** 就诊卡 */
	public static final String CARDTYPE_1 = "1";
	/** 身份证 */
	public static final String CARDTYPE_2 = "2";
	/** 护照 */
	public static final String CARDTYPE_3 = "3";
	/** 户口簿 */
	public static final String CARDTYPE_4 = "4";
	/** 军官证 */
	public static final String CARDTYPE_5 = "5";
	/** 士兵证 */
	public static final String CARDTYPE_6 = "6";
	/** 回乡证 */
	public static final String CARDTYPE_7 = "7";
	/** 通行证 */
	public static final String CARDTYPE_8 = "8";
	/** 士临时身份证 */
	public static final String CARDTYPE_9 = "9";
	/** 外国人居留证 */
	public static final String CARDTYPE_10 = "10";
	/** 警官 */
	public static final String CARDTYPE_11 = "11";
	/** 医保卡 */
	public static final String CARDTYPE_12 = "12";
	/** 其它证件证 */
	public static final String CARDTYPE_13 = "13";
	/** 住院号 */
	public static final String CARDTYPE_14 = "14";
	/** 手机号 */
	public static final String CARDTYPE_15 = "15";

	/********************* 通用报表数据类型 ********************/
	/** 关注数 */
	public static final String RF_DATATYPE_1 = "1";
	/** 活跃用户数 */
	public static final String RF_DATATYPE_2 = "2";
	/** 绑定就诊卡数 */
	public static final String RF_DATATYPE_3 = "3";
	/** 门诊人数 */
	public static final String RF_DATATYPE_4 = "4";
	/** 成交次数 */
	public static final String RF_DATATYPE_5 = "5";
	/** 成交金额 */
	public static final String RF_DATATYPE_6 = "6";

	/********************* 时间段数据类型 ********************/
	/**
	 * 0 未知
	 */
	public static final String TIMENAME0 = "未知";
	/**
	 * 1 上午
	 */
	public static final String TIMENAME1 = "上午";
	/**
	 * 2 下午
	 */
	public static final String TIMENAME2 = "下午";
	/**
	 * 3 晚上
	 */
	public static final String TIMENAME3 = "晚上";
	/**
	 * 0 未知
	 */
	public static final int TIMEID0 = 0;
	/**
	 * 1 上午
	 */
	public static final int TIMEID1 = 1;
	/**
	 * 2 下午
	 */
	public static final int TIMEID2 = 2;
	/**
	 * 3 晚上
	 */
	public static final int TIMEID3 = 3;

	/********************* 订单类型 ********************/
	/** 挂号订单 */
	public static final String ORDERTYPE_0 = BusinessTypeEnum.ORDERTYPE_0.getCode();
	/** 西药 */
	public static final String ORDERTYPE_001 = BusinessTypeEnum.ORDERTYPE_001.getCode();
	/** 中成药 */
	public static final String ORDERTYPE_002 = BusinessTypeEnum.ORDERTYPE_002.getCode();
	/** 草药 */
	public static final String ORDERTYPE_003 = BusinessTypeEnum.ORDERTYPE_003.getCode();
	/** 非药品类型列表（检查） */
	public static final String ORDERTYPE_004 = BusinessTypeEnum.ORDERTYPE_004.getCode();
	/** 就诊卡充值 */
	public static final String ORDERTYPE_005 = BusinessTypeEnum.ORDERTYPE_005.getCode();
	/** 门诊 充值 */
	public static final String ORDERTYPE_006 = BusinessTypeEnum.ORDERTYPE_006.getCode();
	/** 住院 充值 */
	public static final String ORDERTYPE_007 = BusinessTypeEnum.ORDERTYPE_007.getCode();
	/** 诊间数据 */
	public static final String ORDERTYPE_008 = BusinessTypeEnum.ORDERTYPE_008.getCode();
	/** 当日挂号 */
	public static final String ORDERTYPE_009 = BusinessTypeEnum.ORDERTYPE_009.getCode();
	/** 病历复印 */
	public static final String ORDERTYPE_010 = BusinessTypeEnum.ORDERTYPE_010.getCode();
	/** 诊间订单合并支付 */
	public static final String ORDERTYPE_011 = BusinessTypeEnum.ORDERTYPE_011.getCode();
	/** 医技预约 */
	public static final String ORDERTYPE_012 = BusinessTypeEnum.ORDERTYPE_012.getCode();
	/** 所有订单 */
	public static final String ORDERTYPE_999 = "999";

	/****************************** 订单支付状态 ************************************/
	/** 待支付 */
	public static final Integer ORDERPAY_0 = 0;
	/** 正在支付 */
	public static final Integer ORDERPAY_1 = 1;
	/** 支付完成 */
	public static final Integer ORDERPAY_2 = 2;
	/** 正在退费 */
	public static final Integer ORDERPAY_3 = 3;
	/** 退费完成 */
	public static final Integer ORDERPAY_4 = 4;
	/** 退费失败 */
	public static final Integer ORDERPAY_7 = 7;
	/** 退款关闭 */
	public static final Integer ORDERPAY_9 = 9;
//  这两个状态，目前无用
//	/** 多条退费 */
//	public static final Integer ORDERPAY_5 = 5;
//	/** 全额退费 */
//	public static final Integer ORDERPAY_6 = 6;

	/****************************** 订单业务状态 ************************************/
	/** 订单业务待处理 */
	public static final Integer ORDERBIZSTATE_0 = 0;
	/** 订单业务完成 */
	public static final Integer ORDERBIZSTATE_1 = 1;
	/** 订单业务取消 */
	public static final Integer ORDERBIZSTATE_2 = 2;
	/** 未执行业务的订单状态 */
	public static final String ORDERBIZSTATE = "<>0";
	/****************************** 订单完结状态 ************************************/
	/**未完结 STRING型*/
	public static final String ORDEROVER_0_STR = "0";
	/**未完结*/
	public static final Integer ORDEROVER_0 = 0;
	/** 订单取消状态 */
	public static final Integer ORDEROVER_5 = 5;
	/** 订单撤销状态 */
	public static final Integer ORDEROVER_6 = 6;
	/** 交易结束，不可退费 */
	public static final Integer ORDEROVER_8 = 8;
	/****************************** HIS订单状态 ************************************/
	/**
	 * HIS订单状态0=未支付
	 */
	public static final Integer HISORDERSTATE_0 = 0;
	/**
	 * HIS订单状态1=支付中
	 */
	public static final Integer HISORDERSTATE_1 = 1;
	/**
	 *  HIS订单状态2=支付完成
	 */
	public static final Integer HISORDERSTATE_2 = 2;
	/**
	 * HIS订单状态3=退费中
	 */
	public static final Integer HISORDERSTATE_3 = 3;
	/**
	 * HIS订单状态4=退费完成
	 */
	public static final Integer HISORDERSTATE_4 = 4;
	/**
	 * HIS订单状态5=订单完成
	 */
	public static final Integer HISORDERSTATE_5= 5;
	/**
	 * HIS订单状态6=订单取消
	 */
	public static final Integer HISORDERSTATE_6 = 6;
	/**
	 * 线程池 配置
	 * 初始线程：10；最大线程：100；线程活跃时间：60L；单位：SECONDS（秒）；LinkedBlockingQueue；拒绝策略：
	 * CallerRunsPolicy
	 */
	public static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(10, 200, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(2048), new ThreadPoolExecutor.CallerRunsPolicy());

	/**
	 * false 字符串
	 */
	public static final String FALSE_STR = "false";

	/**
	 * true 字符串
	 */
	public static final String TRUE_STR = "true";

	/************ 消息模块 *************/
	/** 当日挂号成功 */
	public static final String MODETYPE_10101110 = "10101110";
	/** 预约挂号成功 */
	public static final String MODETYPE_10101111 = "10101111";
	/** 预约挂号取消成功 */
	public static final String MODETYPE_10101112 = "10101112";
	/** 当日挂号取消成功 */
	public static final String MODETYPE_10101113 = "10101113";
	/** 停诊通知 */
	public static final String MODETYPE_10101114 = "10101114";
	/** 检测单支付推送 */
	public static final String MODETYPE_10101115 = "10101115";
	/** 订单提醒推送 */
	public static final String MODETYPE_10101116 = "10101116";
	/** 药单支付推送 */
	public static final String MODETYPE_10101117 = "10101117";
	/** 排队消息推送 */
	public static final String MODETYPE_10101118 = "10101118";
	/** 就诊卡充值推送 */
	public static final String MODETYPE_10101119 = "10101119";
	/** 住院充值推送 */
	public static final String MODETYPE_10101120 = "10101120";
	/** 随访消息推送 */
	public static final String MODETYPE_10101121 = "10101121";
	/**叫号推送*/
	public static final String MODETYPE_10101122 = "10101122";
	/**医嘱缴费退费推送*/
	public static final String MODETYPE_10101123 = "10101123";
	/**诊间支付金额不一致退费推送*/
	public static final String MODETYPE_10101124 = "10101124";
	/**处方开药推送*/
	public static final String MODETYPE_10101125 = "10101125";
	/** 咨询医生回复推送 */
	public static final String MODETYPE_10201110 = "10201110";
	/** 验证码发送 */
	public static final String MODETYPE_20101110 = "20101110";
	/** 财务报表短信发送 */
	public static final String MODETYPE_20101111 = "20101111";
	/** 诊间结算成功发送 */
	public static final String MODETYPE_20101112 = "20101112";
	/** 医技预约 */
	public static final String MODETYPE_20101113 = "20101113";
	/** 医技取消预约 */
	public static final String MODETYPE_20101114 = "20101114";

	/** 消息模块 */
	public static final String PARAMTYPEBYIN = "IN";
	public static final String PARAMTYPEBYOUT = "OUT";
	public static final String OPERID = "<operId>";
	public static String CONTENT = "<content_@>";
	public static String DETAILURL = "<detailUrl>";
	/** 时间戳 */
	public static final String CREATETIME = "<createTime>";

	/*************** 消息类型 *******************/
	/** 短信 */
	public static final String MSGTYPE_3 = "3";
	/** 文本消息 */
	public static final String MSGTYPE_311 = "3.1.1";
	/** 模板消息 */
	public static final String MSGTYPE_33 = "3.3";
	/** 告警消息 */
	public static final String MSGTYPE_1 = "1";

	/********************* 通用XML节点定义 ********************/
	public static final String PCOUNT = "PCount";

	/********************* 入参XML节点定义 ********************/
	public static final String REQ = "Req";
	public static final String SUCCESSCODE = "10000";
	public static final Integer SUCCESS_CODE = 10000;
	
	/************ 预约挂号模块 *************/
	public static final String QUERYCLINICDEPTCODE = "1001";
	public static final String QUERYCLINICDOCTORCODE = "1002";
	public static final String QUERYNUMBERSCODE = "1003";
	public static final String LOCKORDERCODE = "1004";
	public static final String UNLOCKCODE = "1005";
	public static final String BOOKSERVICECODE = "1006";
	public static final String CANCELSERVICECODE = "1007";
	public static final String QUERYREGINFOCODE = "1008";
	public static final String QUERYCLINICSCHEDULECODE = "1009";
	public static final String ADDORDERCODE = "6001";
	public static final String ORDERCOMPLETECODE = "6002";
	public static final String ORDERLISTCODE = "6003";
	public static final String DRUGLISTCODE = "6004";
	public static final String NOTDRUGLISTCODE = "6005";
	public static final String ORDERISPAYMENTCODE = "6006";
	public static final String PAYFORCOMPLETIONCODE = "6007";
	public static final String ORDERISCANCELCODE = "6008";
	public static final String CANCELFORCOMPLETIONCODE = "6009";
	public static final String ORDERDETAILCODE = "6010";
	public static final String ORDERDETAILUPDATEDEALSTATECODE = "6011";
	public static final String ORDERBYDATECODE = "6012";
	public static final String ORDERDETAILUPDATECHECKSTATECODE = "6013";
	public static final String ADDORDEROVER = "6014";
	public static final String ORDEROVERCOMLETION = "6015";
	public static final String ORDEROVERCANCEL = "6016";
	public static final String ORDERLISTLOCAL = "6018";
	public static final String BOOKSERVICESENDMSG = "6020";
	public static final String QUERYQUEUE = "6021";
	public static final String ORDERDETAILLOCAL = "6022";

	/**调用his解锁如果his已经提前解锁返回代码-14005*/
	public static final String UNLOCKSUCC = "-1010";
	/************ 基础模块 *************/
	public static final String DEFAULTTRAN = "";
	public static final String QUERYHOSPITALCODE = "2001";
	public static final String QUERYBASEDEPTCODE = "2002";
	public static final String QUERYBASEDOCTORCODE = "2003";
	public static final String QUERYMEMBERLISTCODE = "2004";
	public static final String ADDMEMBERCODE = "2005";
	public static final String DELMEMBERINFOCODE = "2006";
	public static final String QUERYDICTIONARYINFOCODE = "2007";
	public static final String INSERTDICTIONARYINFOCODE = "2008";
	public static final String QUERYARTICLEINFOFORLISTCODE = "2009";
	public static final String BINDCLINICCARDCODE = "2010";
	public static final String QUERYDOCTORTITLE = "3001";
	public static final String DATACOLLECTIONCODE = "7006";
	/************消息模块*************/
	public static final String SENDMSGCODE = "7001";
	public static final String DELETESENDTEMPCODE = "7002";
	
	/********************* 渠道ID配置 ********************/
	public static final String WXCHANNELID = "100123";//TODO 将所有渠道ID写死判断的都改成通用判断
	public static final String ZFBCHANNELID = "100125";
	public static final String WXCHANNELNAME = "微信";
	public static final String ZFBCHANNELNAME = "支付宝";

	/************BAT管理*************/
	public static final String ADDACCOUNT ="15001";
	public static final String DELETEACCOUNT ="15002";
	public static final String UPDATEACCOUNT ="15003";
	public static final String GETBATACCOUNT ="15004";
	public static final String GETBATACCOUNTLIST ="15005";
	public static final String ADDMSGTEMPLATE ="15006";
	public static final String ADDMSGSEND ="15007";
	public static final String QUERYBATMSGSEND ="15008";
	public static final String GETBATMSGITEMBYID ="15009";
	public static final String GETBATACCOUNTBASEMSGLIST ="15010";
	public static final String ADDBATMSGITEM ="15011";
	public static final String DELETEBATMSGITEM ="15012";
	public static final String UPDATEBATMSGITEM ="15013";
	public static final String QUERYBATACCOUNT ="15014";
	public static final String QUERYBATUSERLIST ="15015";
	public static final String GETBATMSGITEMLISTBYIDS ="15016";
	public static final String GETMSGTEMPLATEBYITEMIDS ="15017";
	public static final String GETUSERGROUPLISTBYACCOUNTID ="15018";
	public static final String MSGSENDJOBFINISHNOTICE ="15019";
	public static final String QUERYBATUSEROPENIDLIST ="15020";
	public static final String GETALLBATACCOUNTLIST ="15021";
	public static final String DELETEUSERBYACCOUNTID ="15022";
	public static final String ADDUSERGROUP ="15101";
	public static final String ADDUSER ="15102";
	public static final String QUERYUSER="15103";
	
	/********************* 商户类型配置 ********************/

	/** 本地订单 */
	public static final Integer ORDER_MERCHANT_TYPE_0 = 0;
	/** 微信商户订单 */
	public static final Integer ORDER_MERCHANT_TYPE_1 = 1;
	/** 支付宝商户订单 */
	public static final Integer ORDER_MERCHANT_TYPE_2 = 2;
	/** 微信、支付宝商户 */
	public static final Integer ORDER_MERCHANT_TYPE_9 = 9;
	
	public static final Integer IS_ENABLE_0 = 1;
	public static final Integer IS_ENABLE_1 = 1;

	/**
	 * 是否同步撤销商户订单（支付宝，微信） isRevokeMerchantOrder == 0时，只撤销本地订单
	 * isRevokeMerchantOrder == 1时，会同步撤销商户订单（支付宝，微信）
	 */
	public static final Integer ISREVOKEMERCHANTORDER_0 = 0;
	public static final Integer ISREVOKEMERCHANTORDER_1 = 1;

	/**
	 * 二维码信息表状态 1为正常 -1为删除
	 */
	public static final Integer QRCODE_STATUS_0 = -1;
	public static final Integer QRCODE_STATUS_1 = 1;

	/** isonlinepay:1,线上支付 */
	public static final Integer ONLINEPAY = 1;
	/** isonlinepay:2,线上不支付 */
	public static final Integer OFFLINEPAY = 2;

	/** 订单回调用类型1.支付 */
	public static final int ORDER_CALL_BACK_TYPE_1 = 1;
	/** 订单回调用类型2取消 */
	public static final int ORDER_CALL_BACK_TYPE_2 = 2;
	/** 订单回调用类型3冲正 */
	public static final int ORDER_CALL_BACK_TYPE_3 = 3;

	/********************* HttpClient调用成功返回状态 ********************/
	public static final Integer STATUS_CODE = 200;
	/******************** HttpClient调用超时时间 *********************/
	public static final Integer CONNECTION_TIMEOUT = 20000;
	public static final Integer SO_TIMEOUT = 20000;
	/******************** HttpClient请求方式 *********************/
	public static final String POST = "POST";
	public static final String GET = "GET";

	/*********************** 数字常量 *****************************/
	public static final int NUMBER_0 = 0;
	public static final int NUMBER_1 = 1;
	public static final int NUMBER_2 = 2;
	public static final int NUMBER_3 = 3;
	public static final int NUMBER_4 = 4;
	public static final int NUMBER_5 = 5;
	public static final int NUMBER_6 = 6;
	public static final int NUMBER_7 = 7;
	public static final int NUMBER_8 = 8;
	public static final int NUMBER_9 = 9;
	public static final int NUMBER_10 = 10;
	public static final int NUMBER_11 = 11;
	public static final int NUMBER_12 = 12;
	public static final int NUMBER_100 = 100;
	public static final int NUMBER_400 = 400;
	public static final int NUMBER_1000 = 1000;

	/*********************** 字符串数字常量 *****************************/
	public static final String STRING_0 = "0";
	public static final String STRING_1 = "1";
	public static final String STRING_2 = "2";
	public static final String STRING_3 = "3";
	public static final String STRING_4 = "4";
	public static final String STRING_5 = "5";
	public static final String STRING_6 = "6";
	public static final String STRING_7 = "7";
	public static final String STRING_8 = "8";
	public static final String STRING_9 = "9";
	public static final String STRING_99 = "99";
	public static final String STRING_EMPTY = "";
	public static final String STRING_0000 = "0000";
	/*********************** 字母常量 *****************************/
	public static final String A_UPPER = "A";

	/*********************** 符号常量 *****************************/
	public static final String SYMBOL_COLON = ":";
	public static final String SYMBOL_RAIL = "-";
	public static final String SYMBOL_SPECK = ".";
	public static final String SYMBOL_COMMA = ",";
	public static final String SYMBOL_SLANT_SPECK = "\\.";
	public static final String SYMBOL_BACK_SLANT = "/";

	/*********************** 返回结果字段 *****************************/
	public static final String RESULT = "result";
	public static final String RESP = "Resp";
	public static final String RESPCODE = "RespCode";
	public static final String RESPMESSAGE = "RespMessage";
	public static final String TRANSACTIONCODE = "TransactionCode";
	public static final String DATA = "Data";
	public static final String DATA_1 = "Data_1";
	public static final String DATA_2 = "Data_2";
	public static final String DATA_3 = "Data_3";
	public static final String COLUMNS = "Columns";
	public static final String PINDEX = "PIndex";
	public static final String PSIZE = "PSize";
	public static final String PAGE = "Page";
	public static final String HOSID = "HosId";//医院ID

	/*********************** 日期格式 *****************************/
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_YYYYMM = "yyyyMM";

	/*********************** sql常量 *****************************/
	public static final String SQL_SELECT_UPPER = "SELECT";
	public static final String SQL_SELECT_LOWER = "select";

	/*********************** 操作系统常量 *****************************/
	public static final String SYSTEM_TYPE_WINDOWS = "windows";
	public static final String SYSTEM_TYPE_LINUX = "linux";
	public static final String SYSTEM_TYPE_UNIX = "unix";

	/*********************** 其他常量 *****************************/
	public static final String NULL = "null";
	public static final String UNKNOWN = "unknown";

	/*********************** CDATA *****************************/
	public static final String CDDATA_START = "<![CDATA[";
	public static final String CDDATA_END = "]]>";

	/*********************** 性别 *****************************/
	public static final String MAN = "男";
	public static final String WOMAN = "女";

	/*********************** 订单回调 处理状态 *****************************/
	/** HIS返回明确失败信息，需要退款的 */
	public static final int HIS_DEAL_STATE_0 = 0;
	/** HIS返回明确成功信息，业务完成的 */
	public static final int HIS_DEAL_STATE_1 = 1;
	/** HIS返回未知信息，或者重试信息的，需要重试的 */
	public static final int HIS_DEAL_STATE_2 = 2;
	/** hiS订单金额与支付交易金额不一致，需要退款的 */
	public static final int HIS_DEAL_STATE_3 = 3;
	
	/****************** 账单核对状态 *********************/
	/** 0正常 */
	public static final Integer BILL_CHECK_STATE_0 = 0;
	/** 1长款 */
	public static final Integer BILL_CHECK_STATE_1 = 1;
	/** 2处理 */
	public static final Integer BILL_CHECK_STATE_2 = 2;
	/** -1短款， */
	public static final Integer BILL_CHECK_STATE_1_NEGATIVE = -1;

	/** 支付账单 */
	public static final Integer BILL_ORDER_TYPE_1 = 1;
	/** 退费账单 */
	public static final Integer BILL_ORDER_TYPE_2 = 2;
	/**
	 * 二维码URL
	 */
	public static final String QRCODEURL ="QRCodeUrl";
	
	/****************** 文件类型 *********************/
	public static final String HEADERFIELD_GIF = "image/gif";
	public static final String HEADERFIELD_IMG = "application/x-img";
	public static final String HEADERFIELD_JPG = "application/x-jpg";
	public static final String HEADERFIELD_JPEG = "image/jpeg";
	public static final String HEADERFIELD_JPE = "image/jpe";
	
	
	/******************* api接口调用鉴权入参 ********************/
	public static String ClientId = "100123";
	public static String ClientName = "微信";
	public static String ClientVersion = "1.0";
	public static String Sign = "Sign";
	public static String SessionKey = "SessionKey";
	public static final String V = "1.0";
	public static final int INTYPE = 1;
	public static final int OUTTYPE = 1;
	
	/****************常用常量************************/
	public static final Integer I10 = 10;
	public static final Integer I0 = 0;	
	public static final Integer I1 = 1;	
	public static final Integer I2 = 2;	
	public static final Integer I3 = 3;	
	public static final Integer I4 = 4;	
	public static final Integer I5 = 5;		
	public static final Integer I6 = 6;		
	public static final Integer I7 = 7;		
	public static final Integer I8 = 8;		
	public static final Integer I9 = 9;		
	
	/********************挂号状态**********************/
	/**挂号状态0待支付*/
	public static final String BOOK_0 = "0";
	/**挂号状态1已挂号*/
	public static final String BOOK_1 = "1";
	/**挂号状态2已退号*/
	public static final String BOOK_2 = "2";
	/**挂号状态3已取消*/
	public static final String BOOK_3 = "3";
	/**挂号状态4正在挂号*/
	public static final String BOOK_4 = "4";
	
	/**订单全部状态*/
	public static final Integer ORDERSTATE_999 = 999;
	/**订单待支付状态*/
	public static final Integer ORDERSTATE_0 = OrderStateEnum.ORDERSTATE_0.state;
	/**订单已支付状态*/
	public static final Integer ORDERSTATE_1 = OrderStateEnum.ORDERSTATE_1.state;
	/**订单已完成状态*/
	public static final Integer ORDERSTATE_2 = OrderStateEnum.ORDERSTATE_2.state;
	/**订单退款中*/
	public static final Integer ORDERSTATE_3 = OrderStateEnum.ORDERSTATE_3.state;
	/**订单已退款*/
	public static final Integer ORDERSTATE_4 = OrderStateEnum.ORDERSTATE_4.state;
	/**订单退款失败*/
	public static final Integer ORDERSTATE_7 = OrderStateEnum.ORDERSTATE_7.state;
	
	
	public enum OrderStateEnum {
		ORDERSTATE_0(0,"待支付"),
		ORDERSTATE_1(1,"已支付"),
		ORDERSTATE_2(2,"已完成"),
		ORDERSTATE_3(3,"退款中"),
		ORDERSTATE_4(4,"已退款"),
		ORDERSTATE_7(7,"退款失败"),
		;
		private int state;
		private String name;
		OrderStateEnum(int state,String name){
			this.state = state;
			this.name = name;
		}
		public int getState() {
			return state;
		}
		public String getName() {
			return name;
		}
		public static OrderStateEnum valuesOf(int state) {
			for (OrderStateEnum f : OrderStateEnum.values()) {
				if(f.state == state) {
					return f;
				}
			}
			return null;
		}
		public static String des() {
			StringBuffer sbf = new StringBuffer();
			for (OrderStateEnum f : OrderStateEnum.values()) {
				sbf.append(f.getState()).append(":").append(f.getName());
			}
			return sbf.toString();
		}
		
		
	}
	
	
	
	/******************************  统计对账结果  ************************************/
	/**0账不平*/
	public static final Integer BILL_CHECK_RF_STATE_0 = 0;
	/**1账平*/
	public static final Integer BILL_CHECK_RF_STATE_1 = 1;
	/**2处置后账平*/
	public static final Integer BILL_CHECK_RF_STATE_2 = 2;
	
	/****************** 银行到账勾兑结果*********************/
	/**1已核对(账平)*/
	public static final Integer BANK_CHECK_STATE_0 = 0;
	/**2长款*/
	public static final Integer BANK_CHECK_STATE_1 = 1;
	/**3短款*/
	public static final Integer BANK_CHECK_STATE_T1 = -1;
	
	/****************** 银行到账勾兑状态 *********************/
	/**0未勾兑*/
	public static final Integer BANK_IS_CHECK_0 = 0;
	/**1已勾兑*/
	public static final Integer BANK_IS_CHECK_1 = 1;
	
	/****************** 分组字段 *********************/
	public static String GROUPBY_CHANNELID = "CHANNELID";
	public static String GROUPBY_MERCHTYPE = "MERCHTYPE";
	
	
	/****************** 对账账单处理状态 *********************/
	/**0未处理*/
	public static final Integer BILL_IS_DEAL_0 = 0;
	/**1已处理*/
	public static final Integer BILL_IS_DEAL_1 = 1;
	
	/****************** 对账账单处理方式 *********************/
	/**1退款*/
	public static final Integer BILL_DEAL_WAY_1 = 1;
	/**2冲正*/
	public static final Integer BILL_DEAL_WAY_2 = 2;
	/**3登账*/
	public static final Integer BILL_DEAL_WAY_3 = 3;
	
	/** 门诊 */
	public static final Integer HIS_TYPE_1 = 1;
	/** 住院 */
	public static final Integer HIS_TYPE_2 = 2;
	
	/** 缴费主类型 */
	public static final String HOSBIZTYPE = "HOSBIZTYPE";
	/** 门诊 */
	public static final String OUTPATIENT = "OUTPATIENT";
	/** 住院 */
	public static final String HOSPITALIZATION = "HOSPITALIZATION";
	
	/**
	 * 系统操作人
	 */
	public static final String SYSOPERATORID = "SYSTEM";
	/**
	 * 系统操作人
	 */
	public static final String SYSOPERATORNAME = "SYSTEM";
	
	/*********************健康之路**************************/
	/**
	 * 调用健康之路短信接口所需的handlerId
	 */
	public static final String JKZL_SMS_HANDLERID = "1011058";
	/**
	 * 健康之路端卡思特的渠道ID
	 */
	public static final String JKZL_KASITE_CLIENTID = "9000088";
	/**
	 * 健康之路接口地址
	 */
	public static final String JKZL_INTERFACE_URL = "http://service.yihu.com:8080/WSGW/rest";
	/**
	 * 健康之路开发线接口地址
	 */
	public static final String JKZL_INTERFACE_URL_DEBUG = "http://service.yihu.com:8085/WSGW/rest";
	
	/**
	 * 文件服务器(智付后台上传图片)
	 */
//	public static final String UPLOAD_URL = "http://f1.yihuimg.com/TFS/TFSServlet";
	public static final String UPLOAD_URL = "http://127.0.0.1:8670";
	
	/*********************健康之路**************************/
	
	/*********************二维码信息点用户类型**************************/
	
	/**
	 * 1mini付
	 */
	public static Integer QR_USAGETYPE_1 = new Integer(1);
	/**
	 * 2腕带付
	 */
	public static Integer QR_USAGETYPE_2 = new Integer(2);
	/**
	 * 3处方付
	 */
	public static Integer QR_USAGETYPE_3 = new Integer(3);
	/**
	 * 4卡面付
	 */
	public static Integer QR_USAGETYPE_4 = new Integer(4);
	
	/**
	 * 定时任务 入参key
	 */
	public static String SCHEDULER_JOB_PARAM_KEY = "JOB_PARAM_KEY";
	 /**
	  * 定时任务状态
     * 
     */
    public enum SchedulerStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        private SchedulerStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}
