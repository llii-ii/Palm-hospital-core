package com.kasite.core.common.config;
public enum WXPayEnum{
		channelType,
		wx_pay_key,
		/**
		 * 是否 父商户模式：  T 是  F 否
		 */
		is_parent_mode,
		wx_app_id,
		wx_mch_id,
		wx_mch_key,
		wx_cert,
		wx_cert_path,
		wx_parent_app_id,
		wx_parent_mch_id,
		wx_parent_mch_key,
		wx_parent_cert,
		wx_parent_cert_path,
		bank_name,//银行名称：中国银行
		bank_abs_name,//银行名英文缩写： ICBC
		bank_num,//银行卡号 1234567890123456789 
		pay_rule,//支付规则{"singleLimitStart":0,"singleLimitEnd":2000,"payTimeStart":"01:00","payTimeEnd":"22:30","creditCardsAccepted":"true","refundTimeStart":"01:00","refundTimeEnd":"22:00"}
//		wx_pay_notify_url,
		;
	}