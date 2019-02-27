//package com.kasite.core.common.dao;
//
//import com.coreframework.db.TableEnum;
//
///**
// * ClassName: KstHosTableEnum
// *
// * @Description: TODO 数据库表枚举类
// * @author 無
// * @date 2018年4月24日 下午2:28:26
// */
///**
// * ClassName: KstHosTableEnum
// *
// * @Description: TODO
// * @author 無
// * @date 2018年4月24日 下午2:29:40
// */
//public enum KstHosTableEnum implements TableEnum {
//
//	/***************** 系统模块 ***********************/
//	/**
//	 * 渠道信息表
//	 */
//	SYS_CHANNELINFO,
//
//	/***************** 订单模块 ***********************/
//	/**
//	 * 订单信息表
//	 */
//	O_ORDER,
//	/**
//	 * 订单扩展表
//	 */
//	O_ORDER_EXTENSION,
//
//	/**
//	 * 订单回调记录表
//	 */
//	O_ORDERCALLBACK,
//
//	/**
//	 * 订单视图对象
//	 */
//	O_ORDER_VIEW,
//
//	/**
//	 * 支付记录表
//	 */
//	O_PAYORDER,
//	/**
//	 * 订单退费表
//	 */
//	O_REFUNDORDER,
//	/**
//	 * 订单完结状态表
//	 */
//	O_OVERORDER,
//	/**
//	 * 订单业务状态表
//	 */
//	O_BIZORDER,
//
//	/***************** 支付模块 ***********************/
//	/**
//	 * 订单业务状态表
//	 */
//	P_REFUNDMERCHANTORDER,
//	/**
//	 * 支付配置表
//	 */
//	P_CONFIG,
//
//	/**
//	 * 账单表
//	 */
//	P_BILL,
//
//	/***************** 基础模块 ***********************/
//	/**
//	 * 医院
//	 */
//	B_HOSPITAL,
//	/**
//	 * 成员基本信息表
//	 */
//	B_MEMBER,
//	/**
//	 * 成员患者信息表
//	 */
//	B_PATIENT,
//	/**
//	 * 基础科室
//	 */
//	B_DEPT,
//
//	/**
//	 * 基础医生
//	 */
//	B_DOCTOR,
//	/**
//	 * 基础-验证码表
//	 */
//	B_PROVINGCODE,
//	/**
//	 * 基础文章
//	 */
//	B_ARTICLE,
//	/***************** 报表模块 ***********************/
//	/**
//	 * 统计报表
//	 */
//	RF_REPORT,
//	/**
//	 * 统计每日汇总
//	 */
//	RF_REPORTDATE,
//	/**
//	 * 云报表
//	 */
//	RF_CLOUD_DATA,
//
//	/***************** 二维码模块 ***********************/
//	/**
//	 * 二维码信息点表
//	 */
//	QR_GUIDE,
//	/**
//	 * 信息点扫描记录表
//	 */
//	QR_GUIDESCANLOG,
//
//	/***************** BAT模块 ***********************/
//	/**
//	 * bat账户
//	 */
//	BAT_ACCOUNT,
//	/**
//	 * bat用户
//	 */
//	BAT_USER,
//	/**
//	 * bat用户分组
//	 */
//	BAT_USER_GROUP,
//
//	/***************** M消息模块 ***********************/
//
//	/**
//	 * 短信推送记录表
//	 */
//	M_MSGPUSH,
//
//	/**
//	 * 消息模版
//	 */
//	M_MSGTEMP,
//
//	/**
//	 * 自动回复
//	 */
//	M_AUTOREPLAY,
//	/**
//	 * 黑名单表--暂未启用
//	 */
//	M_BLACK_LIST,
//	/**
//	 * 排队信息表
//	 */
//	Q_QUEUEINFO,
//
//	/***************** SV_满意度调查模块 ***********************/
//	SV_SUBJECT,
//
//	SV_SAMPLE,
//
//	SV_SAMPLEANSWER,
//
//	SV_QUESTION,
//
//	SV_QUESTIONFLOW,
//
//	SV_QUESTIONITEM,
//
//	SV_SAMPLETRACK,
//
//	SV_SAMPLEPHONE,
//
//	/***************** YY_预约模块 ***********************/
//
//	/**
//	 * 锁号表
//	 */
//	YY_LOCK,
//	/**
//	 * 解锁表
//	 */
//	YY_UNLOCK,
//	/**
//	 * 预约限制
//	 */
//	YY_LIMIT,
//	/**
//	 * 预约流水
//	 */
//	YY_WATER,
//	/**
//	 * 预约规则
//	 */
//	YY_RULE,
//	/**
//	 * 预约取消
//	 */
//	YY_CANCEL,
//
//}
