package com.kasite.client.medicalCopy.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.core.common.config.KdniaoAuthConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

/**
 * 快递鸟api
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年9月29日 
 */
@Controller
@RequestMapping(value = "/kdniao")
public class KdniaoApi {
	
	@Autowired
	KdniaoAuthConfig kdniaoAuthConfig;
	
	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MCOPY);
	//private static String appKey = "802a4b9f-4a6e-45e7-9434-569a1cb9e778";  //AppKey
	//private static String EBusinessID = "1376799"; //商户ID 
	//private static String repUrl = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";//快递鸟正式地址
	
	
	/**
	 * 即时查询快递信息
	 * @param access_token 鉴权 （待确定）
	 * @param OrderCode 订单编号-可为空
	 * @param ShipperCode 快递公司编码-不可空
	 * @param LogisticCode 物流单号-不可空  
	 * */
	@RequestMapping(value = "/kdApiSearch.do")
	@ResponseBody
	public String kdApiSearch(String OrderCode,String ShipperCode,String LogisticCode) {
		
		String requestData= "{'OrderCode':'"+OrderCode+"','ShipperCode':'"+ShipperCode+"','LogisticCode':'"+LogisticCode+"'}";
		try {
			String dataSign = encrypt(requestData, kdniaoAuthConfig.getAuth().getAppKey(), "UTF-8");
			SoapResponseVo result = HttpRequestBus.create(kdniaoAuthConfig.getAuth().getRepUrl(), RequestType.post)
					.addHttpParam("RequestData", urlEncoder(requestData, "UTF-8"))
					.addHttpParam("EBusinessID", kdniaoAuthConfig.getAuth().getEBusinessID())
					.addHttpParam("RequestType", "1002")
					.addHttpParam("DataType", "2")
	        		.addHttpParam("DataSign", urlEncoder(dataSign, "UTF-8")).send();
			return result.getResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.Common.ERROR_THIRD_PARTY);
		}
	}
	
	
	/**
	 * 单号识别
	 * @param access_token 鉴权 （待确定）
	 * @param LogisticCode 物流单号-不可空  
	 * */
	@RequestMapping(value = "/orderDistinguish.do")
	@ResponseBody
	public String orderDistinguish(String LogisticCode) {
		String requestData= "{'LogisticCode':'"+LogisticCode+"'}";
		try {
			String dataSign = encrypt(requestData, kdniaoAuthConfig.getAuth().getAppKey(), "UTF-8");
			SoapResponseVo result = HttpRequestBus.create(kdniaoAuthConfig.getAuth().getRepUrl(), RequestType.post)
					.addHttpParam("RequestData", urlEncoder(requestData, "UTF-8"))
					.addHttpParam("EBusinessID", kdniaoAuthConfig.getAuth().getEBusinessID())
					.addHttpParam("RequestType", "2002")
					.addHttpParam("DataType", "2")
	        		.addHttpParam("DataSign", urlEncoder(dataSign, "UTF-8")).send();
			return result.getResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw LogUtil.throwRRException(e, RetCode.Common.ERROR_THIRD_PARTY);
		}
	}
	
	
	@SuppressWarnings("unused")
	private static String urlEncoder(String str, String charset) throws UnsupportedEncodingException{
		String result = URLEncoder.encode(str, charset);
		return result;
	}
	
	@SuppressWarnings("unused")
	private static String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
	{
		if (keyValue != null)
		{
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}
	
	@SuppressWarnings("unused")
	private static String MD5(String str, String charset) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(str.getBytes(charset));
	    byte[] result = md.digest();
	    StringBuffer sb = new StringBuffer(32);
	    for (int i = 0; i < result.length; i++) {
	        int val = result[i] & 0xff;
	        if (val <= 0xf) {
	            sb.append("0");
	        }
	        sb.append(Integer.toHexString(val));
	    }
	    return sb.toString().toLowerCase();
	}
	
	private static String base64(String str, String charset) throws UnsupportedEncodingException{
		String encoded = base64Encode(str.getBytes(charset));
		return encoded;    
	}
	
	   private static char[] base64EncodeChars = new char[] { 
		        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
		        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
		        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
		        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 
		        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
		        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
		        'w', 'x', 'y', 'z', '0', '1', '2', '3', 
		        '4', '5', '6', '7', '8', '9', '+', '/' }; 
			
		    public static String base64Encode(byte[] data) { 
		        StringBuffer sb = new StringBuffer(); 
		        int len = data.length; 
		        int i = 0; 
		        int b1, b2, b3; 
		        while (i < len) { 
		            b1 = data[i++] & 0xff; 
		            if (i == len) 
		            { 
		                sb.append(base64EncodeChars[b1 >>> 2]); 
		                sb.append(base64EncodeChars[(b1 & 0x3) << 4]); 
		                sb.append("=="); 
		                break; 
		            } 
		            b2 = data[i++] & 0xff; 
		            if (i == len) 
		            { 
		                sb.append(base64EncodeChars[b1 >>> 2]); 
		                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
		                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]); 
		                sb.append("="); 
		                break; 
		            } 
		            b3 = data[i++] & 0xff; 
		            sb.append(base64EncodeChars[b1 >>> 2]); 
		            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
		            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]); 
		            sb.append(base64EncodeChars[b3 & 0x3f]); 
		        } 
		        return sb.toString(); 
		    }
}




