package com.yihu.hos.util;
import java.util.Iterator;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.config.KasiteConfig;
import com.yihu.hos.IApiModule;
/**
 * 通过接口api创建 请求参数AbsReq对象和返回参数AbsResp对象
 * @author Administrator
 *
 */
public class MakeReqAndRespBean {

	
	//<Req><Data><Name>张三</Name><Sex>0</Sex><Age>0</Age></Data></Req>
	
	
	public static void makeReq(String xml,IApiModule apiModule) throws Exception{
		String api = apiModule.getName();
		String[] tmp=api.split("\\.");
		String page = "package com.yihu.hos.bean;";
		StringBuffer clazzStr = new StringBuffer(page);
		clazzStr.append("\r\nimport org.dom4j.Element;");
		clazzStr.append("\r\nimport com.yihu.hos.exception.AbsHosException;");
		clazzStr.append("\r\nimport com.yihu.hos.exception.ParamException;");
		clazzStr.append("\r\nimport com.yihu.hos.util.XMLUtil;");
		clazzStr.append("\r\nimport com.yihu.wsgw.api.InterfaceMessage;");
		
		String reqClazzName = "Req"+tmp[2];

		clazzStr.append("\r\npublic class "+ reqClazzName +" extends AbsReq {");
		
		Element root = DocumentHelper.parseText(xml).getRootElement();
		Element data = root.element("Data");
		Iterator<Element> eles = data.elementIterator();
		StringBuffer setAndget = new StringBuffer();
		StringBuffer pr = new StringBuffer();
		while (eles.hasNext()) {
			Element e = (Element) eles.next();
			String propertyName = e.getName();
			clazzStr.append("\r\n\tprivate String "+ propertyName+";");
			
			setAndget.append("\r\n\tpublic String get"+ propertyName +"() {");
			setAndget.append("\r\n\t	return "+ propertyName +";");
			setAndget.append("\r\n\t}");
			
			pr.append("\r\n\t\tthis."+ propertyName +" = XMLUtil.getString(ser, \""+ propertyName +"\", false);");
		}
		setAndget.append("\r\n\tpublic "+ reqClazzName +"(InterfaceMessage msg) throws AbsHosException {");
		setAndget.append("\r\n\t\tsuper(msg);");
		setAndget.append("\r\n\t\tElement ser = root.element(\"Data\");");
		setAndget.append("\r\n\t\tif(ser==null){");
		setAndget.append("\r\n\t\t\tthrow new ParamException(\"传入参数中[Data]节点不能为空。\");");
		setAndget.append("\r\n\t\t}");
		setAndget.append(pr);
		setAndget.append("\r\n\t}");
		
		clazzStr.append(setAndget);
		clazzStr.append("\r\n}");
		System.out.println(clazzStr);
		
	}
	public static void makeResp(String xml,IApiModule apiModule) throws Exception{
		String api = apiModule.getName();
		String[] tmp=api.split("\\.");
		String page = "package com.yihu.hos.bean;";
		StringBuffer clazzStr = new StringBuffer(page);
		clazzStr.append("\r\nimport com.yihu.hos.bean.AbsDataSet_Data;");
		
		String reqClazzName = "Resp"+tmp[2];

		clazzStr.append("\r\npublic class "+ reqClazzName +" extends AbsDataSet_Data {");
		
		Element root = DocumentHelper.parseText(xml).getRootElement();
		Element data = root.element("Data");
		Iterator<Element> eles = data.elementIterator();
		StringBuffer setAndget = new StringBuffer();
		while (eles.hasNext()) {
			Element e = (Element) eles.next();
			String propertyName = e.getName();
			clazzStr.append("\r\n\tprivate String "+ propertyName+";");
			setAndget.append("\r\n\tpublic String get"+ propertyName +"() {");
			setAndget.append("\r\n\t	return "+ propertyName +";");
			setAndget.append("\r\n\t}");
		}
		clazzStr.append(setAndget);
		clazzStr.append("\r\n}");
		System.out.println(clazzStr);
		
	}
	
}
