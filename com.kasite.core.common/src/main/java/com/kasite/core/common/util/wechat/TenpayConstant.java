package com.kasite.core.common.util.wechat;


/**
 * @author linjf TODO 腾迅支付全局变量
 */
public class TenpayConstant {
	/********************* 微信接口返回RETURN_CODE ********************/
	public static final String RETURN_CODE_SUCCESS = "SUCCESS";
	public static final String RETURN_CODE_FAIL = "FAIL";

	/********************* 微信接口参数名 ********************/
	public static final String RETURN_CODE = "return_code";
	public static final String RETURN_MSG = "return_msg";
	public static final String RESULT_CODE = "result_code";
	public static final String OUT_TRADE_NO = "out_trade_no";
	public static final String TOTAL_FEE = "total_fee";
	public static final String TRANSACTION_ID = "transaction_id";
	public static final String ERR_CODE = "err_code";
	public static final String ERR_CODE_DES = "err_code_des";

	/** 下载对账单 */
	public static final String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	/** 申请退款 */
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	/** 提交刷卡支付 */
	public static final String MICROPAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
	/** 查询订单 */
	public static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**撤销订单*/
	public static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
	/**关闭订单*/
	public static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	/** 查询退款订单 */
	public static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	/**
	 * 统一支付接口
	 */
	public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/********************* 微信接口错误代码 ********************/
	public static final String ERRCODE_SYSTEMERROR ="SYSTEMERROR";
	public static final String ERRCODE_BANKERROR ="BANKERROR";
	public static final String ERRCODE_AUTHCODEINVALID ="AUTH_CODE_INVALID";
	public static final String ERRCODE_ORDERNOTEXIST ="ORDERNOTEXIST";
	/**
	 * 无效transaction_id
	 */
	public static final String ERRCODE_INVALIDTRANSACTIONID ="INVALID_TRANSACTIONID";
	/**
	 * 参数错误
	 */
	public static final String ERRCODE_PARAMERROR ="PARAM_ERROR";
	/**
	 * 请使用post方法	
	 */
	public static final String ERRCODE_REQUIREPOSTMETHOD ="REQUIRE_POST_METHOD";
	/**
	 * 订单无法撤销	订单有7天的撤销有效期，过期将不能撤销	请检查需要撤销的订单是否超过可撤销有效期
	 */
	public static final String ERRCODE_REVERSEEXPIRE ="REVERSE_EXPIRE";
	/**
	 * 签名错误	
	 */
	public static final String ERRCODE_SIGNERROR ="SIGNERROR";
	/**
	 * 无效请求
	 */
	public static final String ERRCODE_INVALIDREQUEST ="INVALID_REQUEST";
	/**
	 * 订单错误；业务错误导致交易失败；请检查用户账号是否异常、被风控、是否符合规则限制等
	 */
	public static final String ERRCODE_TRADERROR ="TRADE_ERROR";
	/**
	 * 用户支付中
	 */
	public static final String ERRCODE_USERPAYING ="USERPAYING";
	/**
	 * 退款业务流程错误，需要商户触发重试来解决	并发情况下，业务被拒绝，商户重试即可解决	请不要更换商户退款单号，请使用相同参数再次调用API。
	 */
	public static final String ERRCODE_BIZERRNEEDRETRY ="BIZERR_NEED_RETRY";
	/**
	 * 订单已经超过退款期限	订单已经超过可退款的最大期限(支付后一年内可退款)	请选择其他方式自行退款
	 */
	public static final String ERRCODE_TRADEOVERDUE ="TRADE_OVERDUE";
	/**
	 * 业务错误	申请退款业务发生错误	该错误都会返回具体的错误原因，请根据实际返回做相应处理。
	 */
	public static final String ERRCODE_ERROR ="ERROR";
	/**
	 * 退款请求失败	用户帐号注销	此状态代表退款申请失败，商户可自行处理退款。
	 */
	public static final String ERRCODE_USERACCOUNTABNORMAL ="USER_ACCOUNT_ABNORMAL";
	/**
	 * 无效请求过多	连续错误请求数过多被系统短暂屏蔽	请检查业务是否正常，确认业务正常后请在1分钟后再来重试
	 */
	public static final String ERRCODE_INVALIDREQTOOMUCH ="INVALID_REQ_TOO_MUCH";
	/**
	 * 余额不足	商户可用退款余额不足	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
	 */
	public static final String ERRCODE_NOTENOUGH ="NOTENOUGH";
	/**
	 * XML格式错误	XML格式错误	请检查XML参数格式是否正确
	 */
	public static final String  ERRCODE_XMLFORMATERROR ="XML_FORMAT_ERROR";
	/**
	 * 频率限制	2个月之前的订单申请退款有频率限制	该笔退款未受理，请降低频率后重试
	 */
	public static final String  ERRCODE_FREQUENCYLIMITED ="FREQUENCY_LIMITED";
		
	
	
	/********************* 交易状态 ********************/
	public static final String TRADE_STATE_NAME ="TradeState";
	public static final String TRADE_STATE_USERPAYING ="USERPAYING";
	public static final String TRADE_STATE_SUCCESS ="SUCCESS";
	public static final String TRADE_STATE_REFUND ="REFUND";
	public static final String TRADE_STATE_NOTPAY ="NOTPAY";
	public static final String TRADE_STATE_CLOSED ="CLOSED";
	public static final String TRADE_STATE_REVOKED ="REVOKED";
	public static final String TRADE_STATE_PAYERROR ="PAYERROR";
	public static final String TRADE_STATE_PROCESSING ="PROCESSING";
	public static final String TRADE_STATE_CHANGE ="CHANGE";
	public static final String TRADE_STATE_REFUNDCLOSE ="REFUNDCLOSE";
	/**
	 * * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	 */
	public static final String DEVICE_INFO_WEB = "WEB";
	
	/**
	 * 货币默认类型 人民币
	 */
	public static final String FEE_TYPE_CNY = "CNY";
	
	/**
	 * 微信交易类型JSAPI
	 */
	public static final String TRADE_TYPE_JSAPI = "JSAPI";
	/**
	 *  微信交易类型NATIVE
	 */
	public static final String TRADE_TYPE_NATIVE = "NATIVE";
	/**
	 *  微信交易类型APP
	 */
	public static final String TRADE_TYPE_APP = "APP";
	/**
	 *  微信交易类型WAP
	 */
	public static final String TRADE_TYPE_WAP = "WAP";
	/**
	 *  微信交易类型MWEB   H5
	 */
	public static final String TRADE_TYPE_MWEB = "MWEB";
	
	/**
	 * utf-8编码
	 */
	public static final String CHARSET_UTF8 = "UTF-8";
	
	/**
	 * MD5签名
	 */
	public static final String SIGNTYPE_MD5 = "MD5";

//	/**公众号appid*/
//	public static String APP_ID = ""; 
//	/**商户号*/
//	public static String MCH_ID = "";
//	/**父APPID*/
//	public static String PARENT_APP_ID = "";
//	/**父商户号*/
//	public static String PARENT_MCH_ID = "";
//	/**商户密钥*/
//	public static String MCH_KEY = "";
//	/**证书地址*/
//	public static String CERT_PATH = "";
	
	/***
	 * 支付方式：0 现金充值 1 支付宝 2 银联 3 微信
	 */
	public static String CHARGETYPE_ALI = "1";
	public static String CHARGETYPE_WX = "3";
	/**
	 * 错误码
	 */
	public static String ERRCODE ="errCode";
	/**
	 * 查询订单次数
	 */
	public static int count = 6;
	
	/**
	 * 通知地址 是 接收微信支付异步通知回调地址
	 * 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	 */
//	public static String NOTIFY_URL = "";
//	
//	static{
//    	try{
//    		
//    		APP_ID = KasiteConfig.getTenPayAppId();
//    		MCH_ID = KasiteConfig.getTenPayMerchantId();
//    		PARENT_APP_ID = KasiteConfig.getTenPayParentAppId();
//    		PARENT_MCH_ID = KasiteConfig.getTenPayParentMerchantId();
//    		MCH_KEY = KasiteConfig.getTenPayMerchantKey();
//    		NOTIFY_URL = KasiteConfig.getTenPayPayNotifyUrl();
//    		CERT_PATH = KasiteConfig.getTenPayCertPath();
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
	

	
}
