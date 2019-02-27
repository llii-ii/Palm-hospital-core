package com.kasite.core.httpclient.http;


import java.util.List;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 解析参数中 需要传入头部的参数
 * @author daiyanshui
 *
 */
public abstract class IParseParamHeader {
	final static String HEADERXMLSTART = "<soapenvaddsoapheader>";
	final static String HEADERXMLEND = "</soapenvaddsoapheader>";
	
	/**
	 * 解析
	 * @param param 参数报文 非空
	 * @param map 请求对象入参  可以为空
	 * @return 返回处理后的请求参数
	 * @throws DocumentException 
	 */
	public String parseParam2Header(HttpEntityEnclosingRequestBase request,
			String soapXml) throws DocumentException{
		String retVal = soapXml;
		String param = soapXml;
		if (StringUtils.isNotBlank(param) && param.indexOf(HEADERXMLSTART) >= 0) {
			int index1 = param.indexOf(HEADERXMLSTART);
			int index2 = param.indexOf(HEADERXMLEND);
			String hxml = param.substring(index1,
					index2 + HEADERXMLEND.length());
			Document doc = DocumentHelper.parseText(hxml);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			for (Element element : elements) {
				String name = element.getName();
				String value = element.getText();
				if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
					request.setHeader(name, value);
					System.out.println("name:"+name+"|value:"+value);
				}
			}
			retVal = soapXml.replace(hxml, "");
		}
		return retVal;
	}
}
