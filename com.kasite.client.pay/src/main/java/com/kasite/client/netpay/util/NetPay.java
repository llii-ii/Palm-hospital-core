package com.kasite.client.netpay.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.netpay.constants.NetPayConstant;
import com.kasite.client.zfb.util.Base64;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.NetPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.StringUtil;


public class NetPay {

	/**
	 * 对参数签名：
	 * 对reqData所有请求参数按从a到z的字典顺序排列，如果首字母相同，按第二个字母排列，以此类推。排序完成后按将所有键值对以“&”符号拼接。
	 * 拼接完成后再加上商户密钥。示例：param1=value1&param2=value2&...&paramN=valueN&secretKey
	 * 
	 * @param reqDataMap
	 *            请求参数
	 * @param secretKey
	 *            商户密钥
	 */
	public static String sign(Map<String, String> reqDataMap, String secretKey) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = reqDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			buffer.append(k + "=" + v + "&");
		}
		buffer.append(secretKey);// 商户密钥

		try {
			// 创建加密对象
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			// 传入要加密的字符串,按指定的字符集将字符串转换为字节流
			messageDigest.update(buffer.toString().getBytes(Util.CHARSET));
			byte byteBuffer[] = messageDigest.digest();

			// 將 byte转换为16进制string
			StringBuffer strHexString = new StringBuffer();
			for (int i = 0; i < byteBuffer.length; i++) {
				String hex = Integer.toHexString(0xff & byteBuffer[i]);
				if (hex.length() == 1) {
					strHexString.append('0');
				}
				strHexString.append(hex);
			}
			return strHexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String signMap(Map<String,Object> reqDataMap, String secretKey) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = reqDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			buffer.append(k + "=" + v + "&");
		}
		buffer.append(secretKey);// 商户密钥

		try {
			// 创建加密对象
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			// 传入要加密的字符串,按指定的字符集将字符串转换为字节流
			messageDigest.update(buffer.toString().getBytes(Util.CHARSET));
			byte byteBuffer[] = messageDigest.digest();

			// 將 byte转换为16进制string
			StringBuffer strHexString = new StringBuffer();
			for (int i = 0; i < byteBuffer.length; i++) {
				String hex = Integer.toHexString(0xff & byteBuffer[i]);
				if (hex.length() == 1) {
					strHexString.append('0');
				}
				strHexString.append(hex);
			}
			return strHexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean signMapRSA(Map<String, Object> reqDataMap, String verifySign, String publicKey) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = reqDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			buffer.append(k + "=" + v + "&");
		}
		//去掉最后一个&
		String signStr = buffer.toString();
		signStr=signStr.substring(0, signStr.length()-1);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance("SHA1WithRSA");

			signature.initVerify(pubKey);
			signature.update(signStr.getBytes(NetPayConstant.CHARSET_UTF8));

			boolean verify = signature.verify(Base64.decode(verifySign));
			return verify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static String getPubKey(String configKey,boolean refresh) throws Exception {
		String pubKey = null;
		if( NetPayConstant.publicKeyMap.containsKey(configKey) && !refresh) {
			pubKey = NetPayConstant.publicKeyMap.get(configKey);
		}else {
			SortedMap<String,String> signParam =  new TreeMap<String, String>();
			signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
			signParam.put("txCode", NetPayConstant.TXCODE_FBPK);
			signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, configKey));
			signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, configKey));
			String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey,configKey));//sIJ7bZAVetd1p1cE\
			JSONObject inParam = new JSONObject();
			inParam.put("version", NetPayConstant.VSERSION_1_0);
			inParam.put("charset", NetPayConstant.CHARSET_UTF8);
			inParam.put("sign", sign);
			inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
			inParam.put("reqData", JSONObject.toJSON(signParam));
			
			StringBuffer param = new StringBuffer();
			param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
			String resp = HttpsClientUtils.httpsPost(null,ApiModule.NetPay.NETPAY_DOBUSINESS, NetPayConstant.URL_DOBUSINESS, param.toString());
			if( !StringUtil.isEmpty(resp)) {
				JSONObject retJson = JSONObject.parseObject(resp);
				JSONObject rspDataJson = retJson.getJSONObject("rspData");
				if( rspDataJson != null ) {
					if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
						pubKey = rspDataJson.getString("fbPubKey");
					}
				}
			}
		}
		return pubKey;
	}
	
	
//	public static void testRefund() throws Exception {
//		SortedMap<String,String> signParam =  new TreeMap<String, String>();
//		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
//		signParam.put("branchNo","0595" );//分行号，4位数字
//		signParam.put("merchantNo", "000013");//商户号，6位数字
//		signParam.put("date", "20190129");
//		signParam.put("orderNo", "6F54D2AF9FF74015B9EF71213BDF1B15");
//		signParam.put("refundSerialNo", "6F54D2AF9FF74015B9EF71213BDF1B11");
//		signParam.put("amount","0.01");
//		//signParam.put("desc","测试退费");
//		
//		String sign = NetPay.sign(signParam, "sIJ7bZAVetd1p1cE");//sIJ7bZAVetd1p1cE
//		
//		JSONObject inParam = new JSONObject();
//		inParam.put("version", NetPayConstant.VSERSION_1_0);
//		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
//		inParam.put("sign", sign);
//		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
//		inParam.put("reqData", JSONObject.toJSON(signParam));
//		
//		StringBuffer param = new StringBuffer();
//		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
//		
//		String resp = HttpsClientUtils.httpsPost(ApiModule.NetPay.NETPAY_DOREFUND, NetPayConstant.URL_DOREFUND, param.toString());
//		System.out.println(resp);
//	}
//	
//	public static void testQueryOrder() throws Exception {
//		SortedMap<String,String> signParam =  new TreeMap<String, String>();
//		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
//		signParam.put("branchNo","0595" );//分行号，4位数字
//		signParam.put("merchantNo", "000013");//商户号，6位数字
//		signParam.put("date", "20190129");
////		signParam.put("type", NetPayConstant.TYPE_B);//B：按商户订单日期和订单号查询；
////		signParam.put("orderNo", "6F54D2AF9FF74015B9EF71213BDF1B15");//B：按商户订单日期和订单号查询；
//		signParam.put("type", NetPayConstant.TYPE_A);//A：按银行订单流水号查询
//		signParam.put("bankSerialNo", "19212923300000000010");//A：按银行订单流水号查询
//		
//		String sign = NetPay.sign(signParam, "sIJ7bZAVetd1p1cE");//sIJ7bZAVetd1p1cE
//		
//		JSONObject inParam = new JSONObject();
//		inParam.put("version", NetPayConstant.VSERSION_1_0);
//		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
//		inParam.put("sign", sign);
//		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
//		inParam.put("reqData", JSONObject.toJSON(signParam));
//		
//		StringBuffer param = new StringBuffer();
//		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
//		
//		String resp = HttpsClientUtils.httpsPost(ApiModule.NetPay.NETPAY_DOREFUND, NetPayConstant.URL_QUERYSINGLEORDER, param.toString());
//		System.out.println(resp);
//	}
//	
//	public static void testQueryRefundOrder() throws Exception {
//		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = new PgwRespQueryRefundOrder();
//		SortedMap<String,String> signParam =  new TreeMap<String, String>();
//		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
//		signParam.put("branchNo","0595" );//分行号，4位数字
//		signParam.put("merchantNo", "000013");//商户号，6位数字
//		signParam.put("date", "20190129");
//		//signParam.put("type",NetPayConstant.TYPE_A);
//		//signParam.put("bankSerialNo","19212996900000000010");
//			
//		signParam.put("type",NetPayConstant.TYPE_B);//B：按商户订单号+商户退款流水号查单笔
//		signParam.put("merchantSerialNo","6F54D2AF9FF74015B9EF");
//		signParam.put("orderNo","6F54D2AF9FF74015B9EF71213BDF1B15");
//		String sign = NetPay.sign(signParam, "sIJ7bZAVetd1p1cE");//sIJ7bZAVetd1p1cE
//		JSONObject inParam = new JSONObject();
//		inParam.put("version", NetPayConstant.VSERSION_1_0);
//		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
//		inParam.put("sign", sign);
//		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
//		inParam.put("reqData", JSONObject.toJSON(signParam));
//		
//		StringBuffer param = new StringBuffer();
//		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
//		
//		String resp = HttpsClientUtils.httpsPost(ApiModule.NetPay.NETPAY_QUERYSETTLEDREFUND, NetPayConstant.URL_QUERYSETTLEDREFUND, param.toString());
//		JSONObject retJson = JSONObject.parseObject(resp);
//		JSONObject rspDataJson = retJson.getJSONObject("rspData");
//		if( rspDataJson != null ) {
//			if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
//				Integer dataCount = rspDataJson.getInteger("dataCount"); 
//				if( dataCount!=null && dataCount.intValue()==1) {
//					//目前接口只查询一笔退款订单号
//					pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
//					pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
//					String dataList = rspDataJson.getString("dataList"); 
//					String[] bills = dataList.split("\\r\\n");
//					String billObj = bills[1];
//					String[] values = billObj.split(",`");
//					String orderStatus = values[6]; 
//					pgwRespQueryRefundOrder.setRefundId(values[4]);
//					pgwRespQueryRefundOrder.setRefundOrderId(values[5]);
//					pgwRespQueryRefundOrder.setRefundPrice(StringUtil.yuanChangeFenInt(values[8]));
//					pgwRespQueryRefundOrder.setRefundTime(values[12]+values[13]);//yyyyMMdd+HHmmss
//					switch (orderStatus) {
//					case NetPayConstant.ORDERSTATUS_210:
//						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
//						break;
//					case NetPayConstant.ORDERSTATUS_219:
//						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
//						break;
//					case NetPayConstant.ORDERSTATUS_240:
//						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
//						break;
//					case NetPayConstant.ORDERSTATUS_249:
//						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
//						break;
//					default:
//						//TODO 待完善
//						pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I7);
//						break;
//					}
//				}}}
//	}
//	public static void main(String[] args) {
//		try {
//			testQueryRefundOrder();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
