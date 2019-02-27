package com.kasite.client.hospay.module.bill.util.wxpay;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.core.common.util.MD5Util;
import com.kasite.core.common.util.StringUtil;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cc
 * TODO
 */
@Component
public class TenPayUtil {

	/** 请求的参数 */
	private static SortedMap parameters = new TreeMap();

	@Autowired
    RequestHandlerParam requestHandlerParam;



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

	public SortedMap<String, String> initSortedMap(){
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		//如果存在父商户，则为服务商模式
		if(!StringUtil.isEmpty(requestHandlerParam.parentAppId)) {
			packageParams.put("appid", requestHandlerParam.parentAppId);
			packageParams.put("sub_mch_id", requestHandlerParam.merchantId);
			packageParams.put("mch_id", requestHandlerParam.parentMerchantId);
		}else {
			//如果不存在父商户，则为普通商户模式
			packageParams.put("appid", requestHandlerParam.appId);
			packageParams.put("mch_id", requestHandlerParam.merchantId);
		}
		return packageParams;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
				&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + requestHandlerParam.merchantKey);
		String sign = MD5Util.md5Encode(sb.toString(), Constant.DEF_ENCODING)
			.toUpperCase();
		System.out.println("packge签名:" + sign);
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

	public SortedMap<String, String> getReturnPackage(String nonceStr, String prepayId) {

		SortedMap<String, String> finalpackage = new TreeMap<String, String>();

		String packages = "prepay_id=" + prepayId;
		finalpackage.put("appId", requestHandlerParam.appId);
		finalpackage.put("timeStamp", getTimeStamp());
		finalpackage.put("nonceStr", nonceStr);
		finalpackage.put("package", packages);
		finalpackage.put("signType", Constant.SIGNTYPE_MD5);
		return finalpackage;
	}

	/**
	 * 获取授权模式 返回给前端js数据包
	 * 
	 * @param nonceStr
	 * @param prepayId
	 * @return
	 */
	public SortedMap<String, String> getReturnPackageByParent(String nonceStr, String prepayId) {

		SortedMap<String, String> finalpackage = new TreeMap<String, String>();

		String packages = "prepay_id=" + prepayId;
		finalpackage.put("appId", requestHandlerParam.parentAppId);
		finalpackage.put("timeStamp", getTimeStamp());
		finalpackage.put("nonceStr", nonceStr);
		finalpackage.put("package", packages);
		finalpackage.put("signType", Constant.SIGNTYPE_MD5);
		return finalpackage;
	}

	/**
	 *  当前时间
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	public static void main(String[] args) {
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			SortedMap<String, String> one = getPackageParams1("358178977", date, "闽A12345");
			String map = formatUrlMap(one,true,true);
			System.out.println(map);
			System.out.println(createSign1(one, "R6d915582wf95N12"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
	{
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try
		{
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
			{

				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
				{
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds)
			{
				if (StringUtils.isNotBlank(item.getKey()))
				{
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode)
					{
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower)
					{
						buf.append(key.toLowerCase() + "=" + val);
					} else
					{
						buf.append(key + "=" + val);
					}
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false)
			{
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e)
		{
			return null;
		}
		return buff;
	}

	public static SortedMap<String, String> getPackageParams1(String parkId, String time,String plate)
		throws Exception {

		SortedMap<String, String> packageParams = new TreeMap<String, String>();

		// 公众账号ID
		packageParams.put("jsapi_ticket", "123");
		packageParams.put("noncestr","123");
		packageParams.put("timestamp", "123");
		packageParams.put("url", "123");
		// 设备号

		return packageParams;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign1(SortedMap<String, String> packageParams, String merchantKey) {
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
		sb.append("secret=" + merchantKey);
		String sign = MD5Util.md5Encode(sb.toString(), Constant.DEF_ENCODING).toUpperCase();
		return sign;
	}

	/**
	 * 获取参数值
	 *
	 * @param parameter
	 *            参数名称
	 * @return String
	 */
	public static String getParameter(String parameter) {
		String s = (String) parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public static void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		parameters.put(parameter, v);
	}

	/**
	 * 根据XML解析成map集合并设值
	 * @param xmlContent
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static void doParse(String xmlContent) throws JDOMException, IOException {
		parameters.clear();
		//解析xml,得到map
		Map<String,String> m = TenpayXMLUtil.doXMLParse(xmlContent);

		//设置参数
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			setParameter(k, v);
		}
	}

	public boolean isValidWXSign() {
		String sign = createSign(parameters);
		System.out.println("sign===="+sign);
		String validSign = getParameter("sign").toUpperCase();
		System.out.println("ValidSign===="+validSign);
		return sign.equals(validSign);

	}

}
