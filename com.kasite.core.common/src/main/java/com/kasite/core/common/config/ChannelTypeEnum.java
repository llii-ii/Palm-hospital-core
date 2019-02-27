package com.kasite.core.common.config;
/**
 * 支付方式
 * 
 * 后续有新增其它支付渠道请在这里做扩展
 * 
 * 新增渠道的时候  KasiteConfig.getChannelType() getOauthCallBackUrl()  getPayCallBackUrl()方法需要扩展
 * 
 * 如果支付方式支持从云端配置则需要扩展：
 *  VerificationBuser.initWxAndZfbConfig() 方法
 * 如果支付方式支持扫码付则扩展接口：
 * 扩展： PayServiceImpl getPayQRCode()
 * 如果支付方式支持查询支付结果需要扩展接口：
 * 扩展： PayServiceImpl.queryMerchantOrder()
 * 如果支付方式支持退款接口调用：
 * 扩展： PayServiceImpl.refund()
 * 如果支付方式支持撤销接口调用：
 * 扩展： PayServiceImpl.revoke()
 * 当取消无效订单（如当面付）需要扩展：
 * 		MerchantOrderCheckThread
 * 支持扫码付请扩展接口
 * 		PayServiceImpl.sweepCodePay()
 * 统一下单接口请扩展：
 * 		PayServiceImpl.uniteOrder()
 * 新增支付方式需要做好测试，特别是支付退款部分 要做好避免短款问题。
 * @author daiyanshui
 *
 */
public enum ChannelTypeEnum {
	/**
	 * 微信
	 */
	wechat("微信"),
	/**
	 * 支付宝
	 */
	zfb("支付宝"),
	/**
	 * 银联
	 */
	unionpay("银联"),
	/**
	 * 威富通渠道
	 */
	swiftpass("威富通"),
	/**
	 * 企业微信渠道
	 * */
	qywechat("企业微信"),
	
	/**
	 * 招行一网通
	 */
	netpay("招行一网通"),
	
	/**
	 * 建行龙支付
	 */
	dragonpay("建行龙支付")
	;
	private String title;
	ChannelTypeEnum(String title){
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
}
