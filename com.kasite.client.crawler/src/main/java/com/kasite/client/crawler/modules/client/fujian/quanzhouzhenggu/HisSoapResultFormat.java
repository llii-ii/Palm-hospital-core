package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu;

import org.apache.commons.lang.StringEscapeUtils;

import com.kasite.core.httpclient.http.StringUtils;

public class HisSoapResultFormat {
	/**
	 * 格式化 SOAP 返回值
	 * */
	public static String formateSoapResp(String soapXml) {
		if (StringUtils.isNotBlank(soapXml)) {
			soapXml = StringEscapeUtils.unescapeXml(soapXml);
			if (StringUtils.isNotBlank(soapXml) && soapXml.contains("<return>")) {
				int begin = soapXml.indexOf("<return>");
				int end = soapXml.indexOf("</return>");
				soapXml = soapXml.substring(begin + 8, end);
			}
		}
		return soapXml;
	}
}
