

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package com.kasite.client.zfb.constants;

/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
/**
 * @author linjianfa
 * @Description: TODO(用一句话描述该文件做什么) 
 * @version: V1.0  
 * 2017年10月25日 下午2:08:35
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝生活号详情页面获取*/
//	public static  String ALIPAY_PUBLIC_KEY = "";
    
    /**签名编码-视支付宝服务窗要求*/
//    public static  String SIGN_CHARSET      = "UTF-8";

    /**字符编码-传递给支付宝的数据编码*/
//    public static  String CHARSET           = "UTF-8";

    /**签名类型-视支付宝服务窗要求*/
//    public static  String SIGN_TYPE         = "";
    
    /**开发者账号PID*/
//    public static  String PARTNER           = "";
    
    /**签约服务商PID，用于返佣*/
//    public static  String SIGNPARTNER           = "";
    
    /**
     *  服务窗appId
     * TODO !!!! 注：该appId必须设为开发者自己的生活号id  
     */
//    public static  String APP_ID            = "";

    /**
     * TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
     */
//    public static  String PRIVATE_KEY       = "";
    
    /**
     *  TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
     */
//    public static  String PUBLIC_KEY        = "";
    /**支付宝网关*/
    public static  String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

    /**授权访问令牌的授权类型*/
    public static  String GRANT_TYPE        = "authorization_code";
    
    /** 支付宝oauth2.0 地址 **/
    public static final String AUTH_URL     ="https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    /** 支付宝生活号 保存到本地cookie的字段名 **/
    //public static final String OPENID = "userId";
 
 	/**
 	 * RSA2 签名类型
 	 */
 	public static String SIGNTYPE_RSA2 = "RSA2";
 	
 	public static String SIGNTYPE_RSA = "RSA";

	
	/**
	 * 性别男
	 */
	public static String GENDER_M = "m";
	
	/**
	 * 性别女
	 */
	public static String GENDER_F = "f";
	
//    static{
//    	
//    	try{
//    		ALIPAY_PUBLIC_KEY = KasiteConfig.getAliPayPublicKey();
//    		SIGN_TYPE = KasiteConfig.getAliPaySignType();
//    		PARTNER = KasiteConfig.getAliPayPartner();
//    		APP_ID = KasiteConfig.getAliPayAppId();
//    		PRIVATE_KEY = KasiteConfig.getAliPayAppPrivateKey();
//    		PUBLIC_KEY = KasiteConfig.getAliPayAppPublicKey();
//    		SIGNPARTNER = KasiteConfig.getAliPaySignPartner();
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    	
//    }
    
    
    
}