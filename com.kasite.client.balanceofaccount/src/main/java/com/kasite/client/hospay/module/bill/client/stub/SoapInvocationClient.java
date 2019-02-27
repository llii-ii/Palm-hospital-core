package com.kasite.client.hospay.module.bill.client.stub;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.core.httpclient.http.HttpRequest;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 
 * @author cc
 * TODO soap调用实现
 */
@Component
public class SoapInvocationClient {

	private static final Logger logger = LoggerFactory.getLogger(SoapInvocationClient.class);

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
	 *
	 * @param soapActionHeader
	 * @param soapEnv
	 * @return
	 */
	public String soapInvocation(String soapActionHeader,String soapEnv) throws Exception {
		logger.info("HIS账单接口地址为:{}",hisUrl);
		String url = "";
		String soapXml =
				"<soapenvaddurl>"+hisUrl+"</soapenvaddurl>"
				+"<soapenvaddsoapheader><SOAPAction><![CDATA["+soapActionHeader+"]]></SOAPAction></soapenvaddsoapheader>"
				+soapEnv;
		SoapResponseVo vo = HttpRequestBus.create(url, RequestType.soap1).setParam(soapXml).send();
		String resp = HttpRequest.formateSoapResp(vo.getResult());
		if (Constant.HTTPCODE.equals(vo.getCode())) {
			logger.info("调用成功CODE:{}",vo.getCode());
			return resp;
		}
		logger.info("调用HIS接口异常,code：{},结果集：{}",vo.getCode(),vo.getResult());
		throw new Exception("调用HIS接口异常,code:"+vo.getCode()+",结果集："+vo.getResult());
	}
}
