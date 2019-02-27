package com.kasite.server.admin.dingtalk;

/**
 * @desc : 企业应用接入时的常量定义
 * 
 * @author: shirayner
 * @date : 2017年9月27日 下午4:57:36
 */

public class Env {

	/**
	 * 企业应用接入秘钥相关 KST
	 */
	public static final Long AGENTID = 224478538L;
	public static final String AppKey = "dinghstc0twwbuquwtex";
	public static final String AppSecret = "LYCS-KZdQ1vsFbtxaGnKd50SaP0Y4kompB-KGp1lahC1xSZVrAtPvbtoR9X_dUp5";

//	public static final Long AGENTID = 223598748L;
//	public static final String AppKey = "dingnp94koepvr3dnnjd";
//	public static final String AppSecret = "YzfQ_1Y4r27MnP_VyFlg6QtGdThjMVORJC-rjGd5OQrWSkjTvOLRRzcouQehPh5S";
	
	/**
	 * DING API地址
	 */
	public static final String OAPI_HOST = "https://oapi.dingtalk.com";
	/**
	 * 企业应用后台地址，用户管理后台免登使用
	 */
	public static final String OA_BACKGROUND_URL = "";

	/**
	 * 企业通讯回调加密Token，注册事件回调接口时需要传递给钉钉服务器
	 */
	public static final String TOKEN = "";
	public static final String ENCODING_AES_KEY = "";

}
