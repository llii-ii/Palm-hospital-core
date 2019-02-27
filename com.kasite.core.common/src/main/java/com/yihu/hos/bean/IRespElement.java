package com.yihu.hos.bean;
import org.dom4j.Element;

import com.yihu.hos.util.ConfigBean;
public interface IRespElement{
	Element getElement();
	void setElement(Element element);
	Element getRespRootElement();
	Object parseData(Object req);
	void setCfg(ConfigBean cfg); 
	void setTransactionCode(String transactionCode);
	void setRespMessage(String respMessage);
	void setRespCode(String respCode);
}
