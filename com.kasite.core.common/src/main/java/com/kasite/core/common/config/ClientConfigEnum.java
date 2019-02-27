package com.kasite.core.common.config;
/**
 * 渠道相关的配置信息 所有的key都是枚举在这里
 * final static String ORDERMODEL = "kasite.appConfig.{0}.IsOnlinePay_ORDERTYPE_{1}";
 * kasite.appConfig.【clientId】.IsOnlinePay_ORDERTYPE_【serviceId】
 * 判断这个渠道的某个业务是否开通线上支付  serviceId 业务ID
 * 
 * final static String ISSAVERESULT = "kasite.appConfig.{0}.CallHisResult_{1}";
 * kasite.appConfig.【clientId】.CallHisResult_【hisMethodName:在ApiModule 中  his方法的枚举类中】
 * 判断渠道对应的调用HIS的接口是否需要记录返回结果集 默认查询类返回 成功的时候都是不记录日志的。
 * 
 * final static String CLIENTCONFIG_APPINFO = "kasite.appConfig.{0}.{1}.{2}.{3}";// 保存渠道对应appId相关信息 指这个渠道下的应用ID信息
 * kasite.appConfig.【clientId】.【clientSecret：类型】.【AppId 应用ID】.【应用对应信息key】
 * 渠道对应的应用ID相关信息的配置
 * 
 * @author daiyanshui
 */
public enum ClientConfigEnum {
	/**
	 * 微信入口配置key选项
	 */
	WeChatConfigKey(false),
	/**
	 * 企业微信入口配置key选项
	 */
	QyWeChatConfigKey(false),
	/**
	 * 表示指定渠道的微信支付配置信息
	 */
	WxPayConfigKey(true),
	/**
	 * 表示指定渠道的支付宝配置信息
	 */
	ZfbConfigKey(true),
	/**
	 * 表示指定渠道的银联支付配置信息
	 */
	UnionPayConfigKey(true),
	/**
	 * 表示指定渠道的招行一网通支付配置信息
	 */
	NetPayConfigKey(true),
	/**
	 * 表示指定渠道的威富通支付配置信息
	 */
	SwiftpassConfigKey(true),
	/**
	 * 该渠道是否开通，默认=true开通
	 */
	isOpen(false),
	/**
	 * 渠道名称
	 */
	clientName(false),
	/**
	 * 渠道应用AppId 支持字符串配置  KASITE-CLIENT-ANDROID-MINIPay.59,KASITE-CLIENT-ANDROID-MINIPay.60,
	 */
	clientAppId(false),
	/**
	 * 渠道是否开放无卡预约 默认false
	 * 是否开放门诊无卡预约  true 开放，false 不开放 默认false
	 */
	isOpenYYClinicCard(false),
	/**
	 * 是否显示挂号费 1 不显示  0或空则显示
	 */
	lockShowFee(false),
	//下列 3个是提供给渠道应用使用的 ===================start
	/**
	 * 渠道应用密钥
	 */
	clientSecret(false),
	/**
	 * 渠道应用私钥
	 */
	clientPrivateKey(false),
	/**
	 * 渠道应用AppName
	 */
	clientAppName(false),
	
	/**
	 * 渠道指定 configKey 关闭的api列表
	 */
	apiConfig(false),
	/**
	 * 允许访问的IP白名单
	 */
	ipWhiteList(false),
	/**
	 * 支付成功后 个性化跳转页面地址
	 */
	paySuccessToUrl(false),
	//渠道应用属性 ====================================end
	;
	/**
	 * 该渠道是否可进行支付
	 */
	private boolean isPayChannel = false;
	/**
	 * 是否为支付渠道  如果是支付渠道 则返回true 否则返回false 
	 * 
	 * @return
	 */
	public boolean isPayChannel() {
		return this.isPayChannel;
	}
	ClientConfigEnum(boolean isPayChannel){
		this.isPayChannel = isPayChannel;
	}
}
