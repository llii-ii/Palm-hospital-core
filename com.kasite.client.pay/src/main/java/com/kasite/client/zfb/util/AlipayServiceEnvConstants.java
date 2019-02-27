package com.kasite.client.zfb.util;
//package com.kasite.client.pay.util.alipay;
//
//import com.alipay.api.AlipayConstants;
//import com.kasite.core.common.config.KasiteConfig;
//
///**
// * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
// * 
// * @author taixu.zqq
// * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
// */
//public class AlipayServiceEnvConstants {
//
////	final private static String PM_PAYTYPE = "alipay"; 
////	final private static String PM_ALIPAY_PUBLIC_KEY = "alipayPublicKey"; 
////	final private static String PM_SIGN_CHARSET = "charset"; 
////	final private static String PM_APP_ID = "appId"; 
////	final private static String PM_PRIVATE_KEY = "appPrivateKey"; 
////	final private static String PM_PUBLIC_KEY = "appPublicKey"; 
////	final private static String PM_SELLER_EMAIL = ""; 
////	final private static String PM_CHARSET = "charset"; 
////	final private static String PM_SIGN_TYPE = "signType"; 
////	final private static String PM_NOTIFY_URL = "payNotifyUrl"; 
////	final private static String PM_SIGN_PARTNER = "signPartner"; 
//	
////	/**
////	 * 获取支付宝配置信息
////	 */
////	private static Map<String, String> configMap; 
////	static{
////		
////		//赋值r
////	    ALIPAY_PUBLIC_KEY = KasiteConfig.getAliPayPublicKey();
////	    SIGN_CHARSET      = KasiteConfig.getAliPayCharset();
////	    CHARSET           = KasiteConfig.getAliPayCharset();
////	    APP_ID            = KasiteConfig.getAliPayAppId();
////	    PRIVATE_KEY       = KasiteConfig.getAliPayAppPrivateKey();
////	    PUBLIC_KEY        = KasiteConfig.getAliPayAppPublicKey();                                                   
////	    SELLER_EMAIL 	  = "";
////	    SIGN_TYPE 	  = KasiteConfig.getAliPaySignType();
////	    NOTIFY_URL = KasiteConfig.getAliPayPayNotifyUrl();
////	    SIGN_PARTNER = KasiteConfig.getAliPaySignPartner();
////	}
////	
//    /**支付宝公钥-从支付宝服务窗获取*/
//	public static  String ALIPAY_PUBLIC_KEY ;
//    /**签名编码-视支付宝服务窗要求*/
//    public static  String SIGN_CHARSET      ;
//    /**字符编码-传递给支付宝的数据编码*/
//    public static  String CHARSET           ;
//    /** 服务窗appId  */
//    public static  String APP_ID            ;
//    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南
//    /** 应用私钥 */
//    public static  String PRIVATE_KEY       ;
//    /** 应用公钥 */
//    public static  String PUBLIC_KEY        ;                                                   
//    /** 收款支付宝账号，一般情况下收款账号就是签约账号  */
//    public static  String SELLER_EMAIL 	 ;
//
//    /**签名类型-视支付宝服务窗要求*/
//    public static String SIGN_TYPE         = AlipayConstants.SIGN_TYPE_RSA2;
//
//    public static final String PARTNER           = "";
//    
//    /**支付宝网关*/
//    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";
//
//    /**授权访问令牌的授权类型*/
//    public static final String GRANT_TYPE  = "authorization_code";
//	
//	/** 商户的私钥*/
//	public static final String KEY = "";
//	
//	/**
//	 * 服务器异步通知页面路径
//	 */
//	public static String  NOTIFY_URL ;
//	/** 补贴用*/
//	public static String  SIGN_PARTNER ;
//	//需http://格式的完整路径，不能加?id=123这类自定义参数
//
//	//页面跳转同步通知页面路径
////	public static final String  call_back_url = "";
//	//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
//
//	//操作中断返回地址
////	public static final String  merchant_url = "";	
//	
//	
//}
