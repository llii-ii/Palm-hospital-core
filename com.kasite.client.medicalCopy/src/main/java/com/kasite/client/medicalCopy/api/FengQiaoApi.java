package com.kasite.client.medicalCopy.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.FengQiaoAuthConfig;
import com.kasite.core.common.config.KdniaoAuthConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.FengQiaoOrderVo;

/**
 * 枫桥api
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年9月29日 
 */
@Controller
@RequestMapping(value = "/fengqiao")
public class FengQiaoApi {
	
	@Autowired
	FengQiaoAuthConfig fengQiaoAuthConfig;
	
	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MCOPY);
	//private static String appKey = "802a4b9f-4a6e-45e7-9434-569a1cb9e778";  //AppKey
	//private static String EBusinessID = "1376799"; //商户ID 
	//private static String repUrl = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";//快递鸟正式地址
	
	
	/**
	 * 即时查询快递信息
	 * @param trackingNumber 快递单号
	 * */
	@RequestMapping(value = "/fengqiaoApiSearch.do")
	@ResponseBody
	public String fengqiaoApiSearch(String trackingNumber) {
		String reqParam = "<Body><funCode>Mu003</funCode><dataset><row>"
				+ "<RouteRequest tracking_type='1' method_type='1' tracking_number='"+trackingNumber+"'/>"
				+"</Body>";
		String result = call(reqParam, "RouteService");
		result = result.replace("&lt;", "<");
		result = result.replace("&gt;", ">");
		result = result.replace("&apos;", "'");
		/**过滤soap无用节点*/
		if(!StringUtil.isBlank(result) && result.indexOf("return")!=-1){
			result = result.substring(result.indexOf("<return>")+8, result.indexOf("</return>"));
		}
		return result;		
	}
	
	/**
	 * 快递下单
	 * @param fengQiaoOrderVo 快递单
	 * */
	@RequestMapping(value = "/downloadOrder.do")
	@ResponseBody
	public String downloadOrder(FengQiaoOrderVo fengQiaoOrderVo) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderid", fengQiaoOrderVo.getOrderId());
		paramMap.put("express_type", fengQiaoOrderVo.getExpressType());
		paramMap.put("j_province", fengQiaoOrderVo.getjProvince());
		paramMap.put("j_city", fengQiaoOrderVo.getjCity());
		paramMap.put("j_contact", fengQiaoOrderVo.getjContact());
		paramMap.put("j_tel", fengQiaoOrderVo.getjTel());
		paramMap.put("j_address", fengQiaoOrderVo.getjAddress());
		paramMap.put("d_province", fengQiaoOrderVo.getdProvince());
		paramMap.put("d_city", fengQiaoOrderVo.getdCity());
		paramMap.put("d_contact", fengQiaoOrderVo.getdContact());
		paramMap.put("d_tel", fengQiaoOrderVo.getdTel());
		paramMap.put("d_address", fengQiaoOrderVo.getdAddress());
		paramMap.put("custid", fengQiaoAuthConfig.getAuth().getCustid());
		String reqParam = "<Body><Order orderid=\"{orderid}\" express_type='{express_type}' "
				+ "j_province='{j_province}' j_city='{j_city}' j_contact='{j_contact}' j_tel='{j_tel}' j_address='{j_address}' "
				+ "d_province='{d_province}' d_city='{d_city}' d_contact='{d_contact}' d_tel='{d_tel}' d_address='{d_address}' "
				+ "parcel_quantity='1' pay_method='2' custid ='{custid}' cargo='复印文件' ></Order>"			
				+"</Body>";
		reqParam = CommonUtil.formateReqParam(reqParam, paramMap);
		String result = call(reqParam, "OrderService");
		result = result.replace("&lt;", "<");
		result = result.replace("&gt;", ">");
		result = result.replace("&apos;", "'");
		/**过滤soap无用节点*/
		if(!StringUtil.isBlank(result) && result.indexOf("return")!=-1){
			result = result.substring(result.indexOf("<return>")+8, result.indexOf("</return>"));
		}
		return result;		
	}
	
	
	
	
	/**
	 * 调用HIS接口
	 * @param hisUrl
	 * @param method
	 * @param param
	 * @param vo
	 * @return
	 */
	public String call(String param,String service) {
		String hisResp = "";
		String head = fengQiaoAuthConfig.getAuth().getCustomerCode()+","+fengQiaoAuthConfig.getAuth().getCheckCode();
		String reqUrl = fengQiaoAuthConfig.getAuth().getRepUrl();
		try {
			HttpRequstBusSender sender = HttpRequestBus.create(reqUrl, RequestType.post);
			sender.contentType("text/xml");
			sender.setParam(getSoapContent(param,service,head));
			SoapResponseVo resp = sender.send();
			hisResp = resp.getResult();
		} catch (Exception e) {
			e.printStackTrace();LogUtil.error(log, e);
		}
		return hisResp;
	}
	
	/**
	 * 拼装soap消息
	 * 
	 * @param param
	 * @return
	 */
	
	public static String getSoapContent(String param,String service,String head) {
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.expressservice.integration.sf.com/\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("ser:sfexpressService");
		sb.append("<!--Optional:-->");
		sb.append("<arg0><![CDATA[" + "<Request service=\""+service+"\" lang=\"zh-CN\">"+"<Head>"+head+"</Head>"+
		param + "</Request>]]></arg0>");
		sb.append("</ser:sfexpressService>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return sb.toString();
	}
	
}




