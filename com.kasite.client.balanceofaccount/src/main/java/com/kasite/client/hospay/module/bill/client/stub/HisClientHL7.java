package com.kasite.client.hospay.module.bill.client.stub;

import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.core.common.util.CommonUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wufadongxjj
 * @Description: 调用易联众集成平台开发商接口说明书 V:2017-05-17
 * @version: V1.0 2018-3-01
 */
@Component
public class HisClientHL7 {
	private final static Logger logger = LoggerFactory.getLogger(HisClientHL7.class);

	/**
	 * 在初始化spring容器时为静态变量赋值
	 */
	public static String hisUrl;

	@Value("${HisConfig.url}")
	public String url;

	@PostConstruct
	public void init(){
		hisUrl = url;
	}
	/**
	 * 连接超时时间
	 */
	private static int ConnectionTimeout = 60000;
	/**
	 * 最大超时时间
	 */
	private static int SOTIMEOUT = 60000;
	/**
	 * 正式his地址192.168.99.252:8081 测试地址:192.168.99.195:8081
	 */
//	private static String hisUrl = "http://192.168.99.252:8081/HISHL7Service.asmx";
	/**外网测试地址*/
//	private static String hisUrl = "http://61.131.10.205:9091/HISHL7Service.asmx";
	/**
	 * his基础信息地址 测试：http://192.168.99.195:8082   正式：http://192.168.99.252:8082
	 */
	private static String hisBaseUrl = "http://192.168.99.195:8082/CommonPort.asmx";
	/**外网测试地址*/
//	private static String hisBaseUrl = "http://61.131.10.205:9091/CommonPort.asmx/";
	/**
	 * 发送系统名称，由集成平台统一指定
	 */
	private static String sysName = "APP";
	/**
	 * 发送设备名称，可空
	 */
	private static String modeName = "健康之路";
	/**
	 * HIS医院ID
	 */
	private static String hosId = "222142";
	/**
	 * 拼装soap消息
	 * 
	 * @param param
	 * @return
	 */
	public static String getSoapContent(String param) {
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ylz=\"http://ylzinfo.com/\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<ylz:HISHL7Interface>");
		sb.append("<!--Optional:-->");
		sb.append("<ylz:hl7Message><![CDATA[" + param + "]]></ylz:hl7Message>");
		sb.append("</ylz:HISHL7Interface>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return sb.toString();
	}
	/**
	 * 拼装basic soap消息
	 * 
	 * @param param
	 * @return
	 */
	public static String getSoapContentBasic(String param) {
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<tem:GetYLZHisInterface>");
		sb.append("<!--Optional:-->");
		sb.append("<tem:xmlData><![CDATA[" + param + "]]></tem:xmlData>");
		sb.append("</tem:GetYLZHisInterface>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return sb.toString();
	}
	
	/**
	 * 调用HIS方法
	 * 
	 * @param param
	 * @return
	 */
	public String callHis(String param) throws Exception {
		return call(hisUrl, param,true);
	}
	/**
	 * call方法
	 * 
	 * @param url
	 * @param param
	 * @param isFilter 是否过滤soap节点
	 * @return
	 */
	public static String call (String url, String param, boolean isFilter) throws Exception {
		long begin = System.currentTimeMillis();
		StringBuilder logSbf = new StringBuilder();
		logSbf.append("\r\n请求参数:\r\n").append(param);
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		String respParam = null;
		int statusCode = 0;
		String soapParam;
		try {
			if(hisBaseUrl.equals(url)){
				soapParam = getSoapContentBasic(param);
			}else {
				soapParam = getSoapContent(param);
			}
			RequestEntity requestEntity = new StringRequestEntity(soapParam, "text/xml", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(ConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOTIMEOUT);
			statusCode = httpClient.executeMethod(postMethod);
			byte[] responseBody = postMethod.getResponseBody();
			respParam = new String(responseBody, "UTF-8");
			respParam = respParam.replace("&lt;", "<");
			respParam = respParam.replace("&gt;", ">");
			//基础信息接口不管是否200 都返回消息
			if (statusCode != Constant.HTTPCODE || !hisBaseUrl.equals(url)) {
				throw new Exception("调用HIS接口异常,code:"+statusCode+",结果集："+respParam);
			}

			logSbf.append("\r\n响应状态码:").append(statusCode);
			logSbf.append("\r\n响应时间:").append(System.currentTimeMillis() - begin).append("毫秒");
			/*过滤soap无用节点*/
			if(isFilter){
				if(!StringUtil.isBlank(respParam) && respParam.contains("GetYLZHisInterfaceResult")){
					respParam = respParam.substring(respParam.indexOf("<GetYLZHisInterfaceResult>")+26, 
							respParam.indexOf("</GetYLZHisInterfaceResult>"));
				}
				if(!StringUtil.isBlank(respParam) && respParam.contains("HISHL7InterfaceResult")){
					respParam = respParam.substring(respParam.indexOf("<HISHL7InterfaceResult>")+23, 
							respParam.indexOf("</HISHL7InterfaceResult>"));
				}
			}
			logSbf.append("\r\n响应内容:\r\n").append(respParam);
		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
			logger.info("接口返回信息:{}",logSbf.toString());
		}
		return respParam;
	}

	/**
	 * 获取当前日期
	 * 
	 * @param formatter
	 *            格式
	 * @return
	 */
	public static String getNowDateStr(String formatter) {
		String nowDateStr = null;
		try {
			nowDateStr = DateOper.getNow(formatter);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return nowDateStr;
	}

	/**
	 * 获取当前日期 yyyyMMddHHmmss.SSS
	 * 
	 * @return
	 */
	public static String getNowDateStr() {
		String nowDateStr = null;
		try {
			nowDateStr = DateOper.getNow("yyyyMMddHHmmss.SSS");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return nowDateStr;
	}

	/**
	 * 字符串日期格式转换
	 * 
	 * @param date
	 * @param fromMatter
	 *            从XX格式
	 * @param toMatter
	 *            到XX格式
	 * @return
	 */
	public static String strToDateFormat(String date, String fromMatter, String toMatter) {
		SimpleDateFormat formatter = new SimpleDateFormat(fromMatter);
		Date newDate;
		try {
			if(!StringUtil.isBlank(date)){
				newDate = formatter.parse(date);
				formatter = new SimpleDateFormat(toMatter);
				return formatter.format(newDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取天数间隔
	 * 
	 * @param stime
	 * @param etime
	 * @return
	 */
	public static int getBetweenDays(String stime, String etime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date sdate = null;
		Date eDate = null;
		try {
			sdate = df.parse(stime);
			eDate = df.parse(etime);
			// 天数间隔
			long betweendays = (long) ((eDate.getTime() - sdate.getTime()) / (1000 * 60 * 60 * 24));
			// 前后+1天
			return Integer.parseInt(betweendays + "") + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取HIS账单
	 * @param reqParam
	 * @return
	 */
	public List<String[]> queryAllBills(String reqParam) throws Exception {

		String result = callHis(reqParam);
		List<String[]> list = new ArrayList<>();
		if (!StringUtil.isBlank(result)) {
			String[] results = result.split("\n");
			for (int i = 0; i < results.length; i++) {
				if (results[i].startsWith("RDT")) {
					list.add(results[i].split("\\|"));
				}
			}
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		String nowDateStr = getNowDateStr();

		//6.4.9.	获取对账信息   1=门诊   3=住院
		String reqParam = "MSH|^~\\&|APP|健康之路|HIS|接收设备名称|" + nowDateStr + "||QBP^Q13^QBP_Q13|QRY_Reconciliation-" + nowDateStr.replace(".", "") + "|P|2.7\n"
				+ "QPD|Q13^QRY_Reconciliation^HL7|QR0001|20180928|20180928|1|\n"
				+ "RDF|12|单据号^NM^50~姓名^ST^50~缴费金额^NM^50~支付方式^ST^20~出票单位^ST^50~出票开户银行^ST^50~票据流水号^NM^50~操作日期^DMT^50~操作时间^DMT^50~操作员^ST^50~病人余额^NM^50~机构名称^ST^50\n"
				+ "RCP|I|页数^PG&每页记录数";
		HisClientHL7 hisClientHL7 = new HisClientHL7();
		String result = hisClientHL7.callHis(reqParam);
		System.out.println(result);
//		HisClientHL7 his = new HisClientHL7();
//		String startDate = DateOper.addDate(DateOper.getNow("yyyyMMdd"), -1);
//        String endDate = DateOper.addDate(DateOper.getNow("yyyyMMdd"), -1);
//
//		Map<String, String> map = new HashMap<>(16);
//		map.put("startDate",startDate);
//		map.put("endDate",endDate);
//		map.put("type", "1");
//		List<String[]> list = his.queryAllBills(map);
//		for (String[] all : list) {
//			System.out.println();
//			System.out.println(all[1]);
//			System.out.println(all[13]);
//		}

		//科室
//		 reqParam = "<Root><TransactionCode>30003</TransactionCode><Data><HosId>222142</HosId><DeptCode></DeptCode><DeptName></DeptName><DeptType>2</DeptType></Data></Root>";
//		 String result = callHisBasic(reqParam);
		 //医生
//		 reqParam = "<Root><TransactionCode>30004</TransactionCode><Data><HosId>222142</HosId><DeptCode>495</DeptCode><DeptName></DeptName><DeptType>2</DeptType></Data></Root>";
//		 String result = callHisBasic(reqParam);
			
	}
}
