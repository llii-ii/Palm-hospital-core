package com.kasite.core.common.util.wechat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.xml.XMLSerializer;

/**
 * @author linjf
 * TODO
 */
public class TenpayUtil {

	/**
	 * 把对象转换成字符串
	 * 
	 * @param obj
	 * @return String 转换成字符串,若对象为null,则返回空字符串.
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 把对象转换为int数值.
	 * 
	 * @param obj
	 *            包含数字的对象.
	 * @return int 转换后的数值,对不能转换的对象返回0。
	 */
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null) {
				a = Integer.parseInt(obj.toString());
			}
		} catch (Exception e) {

		}
		return a;
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 获取当前日期 yyyyMMdd
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double start = 0.1;
		double random = Math.random();
		if (random < start) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 获取编码字符集
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	public static String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {

		if (null == request || null == response) {
			return "gbk";
		}

		String enc = request.getCharacterEncoding();
		if (null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}

		if (null == enc || "".equals(enc)) {
			enc = "gbk";
		}

		return enc;
	}

	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * 
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if (null == date) {
			return 0;
		}

		return date.getTime() / 1000;
	}

	/**
	 * 时间转换成字符串
	 * 
	 * @param date
	 *            时间
	 * @param formatType
	 *            格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}

	/**
	 * 取出一个指定长度大小为32随机字符串.
	 * 
	 * @return 返回生成的随机数。
	 */
	public static String getNonceStr() {
		int length = 32;
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int size = chars.length();
		String noceStr = "";
		for (int i = 0; i < length; i++) {
			double b = Math.random();
			noceStr += chars.charAt((int) (b * size));
		}
		return noceStr;
	}

	/**
	 * 服务商模式统一下单入参封装
	 * 
	 * @param openid
	 * @param outTradeNo
	 * @param productPrice
	 * @param body
	 * @param remoteIp
	 * @param isLimitCredit
	 * @param tradeType
	 * @param configMap
	 * @return
	 * @throws Exception
	 */
	public static SortedMap<String, String> getPackageParamsByParent(String clientId,String configKey,String openId, String outTradeNo,
			Integer productPrice, String body, String remoteIp, Integer isLimitCredit, String tradeType,String token) throws Exception {

		SortedMap<String, String> packageParams = new TreeMap<String, String>();

		String wx_parent_app_id = KasiteConfig.getWxPay(WXPayEnum.wx_parent_app_id, configKey);
		String wx_parent_mch_id = KasiteConfig.getWxPay(WXPayEnum.wx_parent_mch_id, configKey);
		String wx_mch_id = KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey);
		String wx_app_id = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey);
		//父公众账号ID
		packageParams.put("appid", wx_parent_app_id);
		//父商户号
		packageParams.put("mch_id", wx_parent_mch_id);
		//子商户号
		packageParams.put("sub_mch_id", wx_mch_id);
		//子商户号
		packageParams.put("sub_appid", wx_app_id);
		//设备号
		packageParams.put("device_info", TenpayConstant.DEVICE_INFO_WEB);
		//随机字符串
		packageParams.put("nonce_str", getNonceStr());
		//商品描述
		packageParams.put("body", body);
		//商户订单号
		packageParams.put("out_trade_no", outTradeNo);
		//货币类型
		packageParams.put("fee_type", TenpayConstant.FEE_TYPE_CNY);
		//总金额（单位：分）
		packageParams.put("total_fee", productPrice + "");

		//终端IP
		packageParams.put("spbill_create_ip", remoteIp);
		//接收微信支付异步通知回调地址 //
		packageParams.put("notify_url", KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.wechat, configKey, clientId, openId, token, outTradeNo));
		//交易类型
		packageParams.put("trade_type", tradeType);
		//商品ID waterid
		packageParams.put("product_id", outTradeNo);
		if(!StringUtil.isEmpty(openId)) {
			//用户标识
			packageParams.put("sub_openid", openId);
		}
		if (null != isLimitCredit && isLimitCredit.equals(1)) {
			//限制使用信用卡
			packageParams.put("limit_pay", "no_credit");
		}
		return packageParams;
	}

	/**
	 * 普通商户模式统一下单入参封装
	 * 
	 * @param openid
	 * @param outTradeNo
	 * @param productPrice
	 * @param body
	 * @param remoteIp
	 * @param isLimitCredit
	 * @param tradeType
	 * @param configMap
	 * @return
	 * @throws Exception
	 */
	public static SortedMap<String, String> getPackageParams(String clientId,String configKey,String openId, String outTradeNo, Integer productPrice,
			String body, String remoteIp, Integer isLimitCredit, String tradeType,String token)
			throws Exception {
		String wx_mch_id = KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey);
		String wx_app_id = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey);
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();

		// 公众账号ID
		packageParams.put("appid", wx_app_id);
		// 商户号
		packageParams.put("mch_id", wx_mch_id);
		// 设备号
		packageParams.put("device_info", TenpayConstant.DEVICE_INFO_WEB);
		// 随机字符串
		packageParams.put("nonce_str", getNonceStr());
		// 商品描述
		packageParams.put("body", body);
		// 商户订单号
		packageParams.put("out_trade_no", outTradeNo);
		// 货币类型
		packageParams.put("fee_type", TenpayConstant.FEE_TYPE_CNY);
		// 总金额（单位：分）
		packageParams.put("total_fee", productPrice + "");

		// 终端IP
		packageParams.put("spbill_create_ip", remoteIp);
		// 接收微信支付异步通知回调地址 "/wxPay/{clientId}/{configKey}/{openId}/{token}/payNotify.do";
		packageParams.put("notify_url", KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.wechat, configKey, clientId, openId, token,outTradeNo));
		// 交易类型
		packageParams.put("trade_type", tradeType);
		// 商品ID waterid
		packageParams.put("product_id", outTradeNo);

		if(!StringUtil.isEmpty(openId)) {
			//用户标识
			packageParams.put("openid", openId);

		}
		if (isLimitCredit == 1) {
			//限制使用信用卡
			packageParams.put("limit_pay", "no_credit");

		}
		return packageParams;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams, String merchantKey) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + merchantKey);
		String sign = MD5Util.md5Encode(sb.toString(), TenpayConstant.CHARSET_UTF8).toUpperCase();
		return sign;
	}

	/**
	 * 将入参封装成XML
	 * 
	 * @param packageParams
	 * @param sign
	 * @return
	 */
	public static String setMapToXml(SortedMap<String, String> packageParams, String sign) {

		Set<String> set = packageParams.keySet();
		StringBuffer xml = new StringBuffer("<xml>");
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String key = iterator.next().toString();
			if (!StringUtil.isEmpty(packageParams.get(key))) {
				xml.append("<" + key + ">").append(packageParams.get(key)).append("</" + key + ">");
			}
		}
		return xml.append("<sign>").append(sign).append("</sign></xml>").toString();
	}

	/**
	 * XML字符串转成Json字符串
	 * 
	 * @param xml
	 * @return
	 */
	public static String xml2JSON(String xml) {
		if (StringUtil.isEmpty(xml)) {
			return "";
		} else {
			return new XMLSerializer().read(xml).toString();
		}
	}

	public static SortedMap<String, String> getReturnPackage(String app_id,String nonceStr, String prepayId) {
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String packages = "prepay_id=" + prepayId;
		finalpackage.put("appId", app_id);
		finalpackage.put("timeStamp", getTimeStamp());
		finalpackage.put("nonceStr", nonceStr);
		finalpackage.put("package", packages);
		finalpackage.put("signType", TenpayConstant.SIGNTYPE_MD5);
		return finalpackage;
	}
//
//	/**
//	 * 获取授权模式 返回给前端js数据包
//	 * 
//	 * @param nonceStr
//	 * @param prepayId
//	 * @return
//	 */
//	public static SortedMap<String, String> getReturnPackageByParent(String nonceStr, String prepayId) {
//		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
//		String packages = "prepay_id=" + prepayId;
//		finalpackage.put("appId", TenpayConstant.PARENT_APP_ID);
//		finalpackage.put("timeStamp", getTimeStamp());
//		finalpackage.put("nonceStr", nonceStr);
//		finalpackage.put("package", packages);
//		finalpackage.put("signType", TenpayConstant.SIGNTYPE_MD5);
//		return finalpackage;
//	}

	/**
	 *  当前时间
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

}
