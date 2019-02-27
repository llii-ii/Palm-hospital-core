package com.kasite.client.crawler.modules.client;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;

import org.dom4j.Element;

import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.service.CommonMethodEnum;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

/**
 * 所有第三方调用抽象部分
 * 一般提供基础的方法封装。
 * @author daiyanshui
 *
 * @param <T>
 */
public abstract class AbsCommonClientCallService {

	public static final String CLASS_TYPE_NAME_STRING = "java.lang.String";
	public static final String CLASS_TYPE_NAME_INTEGER = "java.lang.Integer";
	
	
	public abstract HosClientConfig getHosClientConfig();
	
	public String getUrl() {
		return getHosClientConfig().getWsUrl();
	}
	
	public RequestType getReqType() {
		return getHosClientConfig().getReqType();
	}
	
	protected SoapResponseVo call(CommonMethodEnum method,String requestXml,String parinteId) {
		String name = method.getName();
//		switch (name) {
//		case "QueryClinicRegisetrInfo":
//			String result = "<?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><S:Body><ns2:QueryClinicRegisetrInfoResponse xmlns:ns2=\"http://webservice/\"><return><Response><Data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:clinicRegisetrInfo\"><clinicCard>FJ059510020000725105</clinicCard><clinicDate>2017-03-28</clinicDate><clinicNum></clinicNum><departmentId>0717030000</departmentId><departmentName>上肢科8</departmentName><doctorId>20051010075136</doctorId><doctorName>张细祥</doctorName><ghFree>0.00</ghFree><medicalExpenses>54524.00</medicalExpenses><medicalNum>2042222</medicalNum><medicalType>0</medicalType><patientId>710955</patientId><timeId></timeId></Data><Data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:clinicRegisetrInfo\"><clinicCard>FJ059510020000725105</clinicCard><clinicDate>2017-05-09</clinicDate><clinicNum></clinicNum><departmentId>0717030000</departmentId><departmentName>上肢科8</departmentName><doctorId>20051010075136</doctorId><doctorName>张细祥</doctorName><ghFree>0.00</ghFree><medicalExpenses>18780.00</medicalExpenses><medicalNum>2071724</medicalNum><medicalType>0</medicalType><patientId>710955</patientId><timeId></timeId></Data><Data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:clinicRegisetrInfo\"><clinicCard>FJ059510020000725105</clinicCard><clinicDate>2017-06-13</clinicDate><clinicNum></clinicNum><departmentId>0717030000</departmentId><departmentName>上肢科8</departmentName><doctorId>20051010075136</doctorId><doctorName>张细祥</doctorName><ghFree>0.00</ghFree><medicalExpenses>25876.00</medicalExpenses><medicalNum>2097104</medicalNum><medicalType>0</medicalType><patientId>710955</patientId><timeId></timeId></Data><Data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:clinicRegisetrInfo\"><clinicCard>FJ059510020000725105</clinicCard><clinicDate>2017-11-03</clinicDate><clinicNum></clinicNum><departmentId>0717030100</departmentId><departmentName>上肢科8</departmentName><doctorId>20051014114706</doctorId><doctorName>郑尤辉</doctorName><ghFree>0.00</ghFree><medicalExpenses>32900.00</medicalExpenses><medicalNum>2210085</medicalNum><medicalType>0</medicalType><patientId>710955</patientId><timeId></timeId></Data><Data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns2:clinicRegisetrInfo\"><clinicCard>FJ059510020000725105</clinicCard><clinicDate>2017-11-03</clinicDate><clinicNum></clinicNum><departmentId>0201000000</departmentId><departmentName>急诊科</departmentName><doctorId>20051010075145</doctorId><doctorName>叶新民</doctorName><ghFree>0.00</ghFree><medicalExpenses>83144.00</medicalExpenses><medicalNum>2210181</medicalNum><medicalType>0</medicalType><patientId>710955</patientId><timeId></timeId></Data><Message>成功</Message><Code>10000</Code><total>0</total></Response></return></ns2:QueryClinicRegisetrInfoResponse></S:Body></S:Envelope>" + 
//					"";
//			SoapResponseVo respVo = new SoapResponseVo();
//			respVo.setCode(200);
//			respVo.setResult(result);
//			return respVo;
//		default:
//			break;
//		}
		
		SoapResponseVo respVo = HttpRequestBus.create(getUrl(), getReqType())
				.setParam(requestXml)
				.send();
		
		
		return respVo;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parseVo2Obj(Object o,Element elemen) throws ParamException{
		Field[] fs = o.getClass().getDeclaredFields();
		for (Field f : fs) {
			String name = f.getName();
			String val = getElementStringValue(elemen, name, false);
			if(null != f && StringUtil.isNotBlank(val)){
				String fname = f.getType().getName();
				String sname = CLASS_TYPE_NAME_STRING;
				String iname = CLASS_TYPE_NAME_INTEGER;
				if(sname.equals(fname)){
					try{
						val = val.trim();
						com.coreframework.util.ReflectionUtils.setFieldValue(o, f.getName(), val);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(iname.equals(fname)){
					try{
						if(StringUtil.isNotBlank(val)){
							Float v = Float.parseFloat(val);
							com.coreframework.util.ReflectionUtils.setFieldValue(o, f.getName(), v.intValue());
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			CheckDate checkDate = f.getAnnotation(CheckDate.class);
			if(null != checkDate) {
				Object v = com.coreframework.util.ReflectionUtils.getFieldValue(o, f.getName());
				if(null != v && v instanceof String) {
					try {
						Date date = DateOper.parse(o.toString());
						String format = checkDate.format();
						String value = DateOper.formatDate(date, format);
						com.coreframework.util.ReflectionUtils.setFieldValue(o, f.getName(), value);
					} catch (ParseException e) {
					}
				}
			}
		}
		return o;
	}
	
	
	
	protected String getElementStringValue(Element element,String node,Boolean isMust) throws ParamException{
		
		String val = "";
		
		Element e = element.element(node);
		if(null == e && isMust == true){
			throw new ParamException(element.asXML() + " 节点中："+ node +"  为必传节点你传入的节点中没有找到该节点。");
		}
		if(null != e){
			return e.getTextTrim();
		}
		return val;
	}
	
	
	protected Integer getElementIntegerValue(Element element,String node,Boolean isMust) throws ParamException{
		
		Integer val = 0;
		
		Element e = element.element(node);
		if(null == e && isMust == true){
			throw new ParamException(element.asXML() + " 节点中："+ node +"  为必传节点你传入的节点中没有找到该节点。");
		}
		if(null != e){
			String v = e.getTextTrim();
			if(StringUtil.isNotBlank(v)){
				try{
					val = Integer.parseInt(v);
				}catch (NumberFormatException ex) {
					throw new ParamException("节点中的数据转成整形出错。Node = "+ node +" Service = "+ element.asXML());
				}
			}
		}
		return val;
	}
	
}
