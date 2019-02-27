package com.kasite.core.common.config;
public enum NetPayEnum{
	configKey,//配置configKey
	name,
	branchNo,//分行号，4位数字
	merchantNo,//商户号，6位数字
	secretKey,//商户号密钥
	operatorNo,//商户结账系统的操作员号
	pwd,//操作员登录密码。使用encrypType算法加密后的密码注意：加密后的密文必须转换为16进制字符串表示
	;
	}