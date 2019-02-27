package com.kasite.core.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;

/**
 * @author linjf
 * TODO
 */
public class ParamUtil {
	private static final String FILTER = "Filter";
	/**
	 * 数组转XML
	 * @param code
	 * @param tags
	 * @param values
	 * @return
	 */
	public static String getParam(String code, String[] tags, Object[] values) {
		if (tags != null) {
			if (tags.length != values.length) {
				return null;
			}
		}
		Document document = DocumentHelper.createDocument();
		Element req = document.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, code);
		Element data = req.addElement(KstHosConstant.DATA);
		for (int i = 0; i < tags.length; i++) {
//			if (FILTER.equals(tags[i])) {
//				Element filter = data.addElement("Filter");
//				Filter f = (Filter) values[i];
//				XMLUtil.addElement(filter, "Type", f.getType());
//				XMLUtil.addElement(filter, "V", f.getValue());
//			} else {
				XMLUtil.addElement(data, tags[i], values[i]);
//			}
		}
		return req.asXML();
	}
	
}
