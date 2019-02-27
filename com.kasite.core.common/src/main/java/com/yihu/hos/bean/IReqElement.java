package com.yihu.hos.bean;

import org.dom4j.Element;

public interface IReqElement {
	Element getRoot();
	void setRoot(Element element);
	void setCallHisReqModuleParam(String string);
	void parseData(Object obj);
}
