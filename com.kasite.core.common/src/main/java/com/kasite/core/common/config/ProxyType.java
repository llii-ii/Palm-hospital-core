package com.kasite.core.common.config;
/**
 * 代理跳转地址类型
 * 目前分支付宝／微信
 * 
 * @author daiyanshui
 *
 */
public enum ProxyType {
	/**微信公众号相关地址*/
	wechat,
	/**微信支付相关地址*/
	wechatPay,
	/**支付宝相关地址*/
	zfb
}
