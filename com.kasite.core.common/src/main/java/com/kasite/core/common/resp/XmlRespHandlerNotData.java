package com.kasite.core.common.resp;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;

/**
 * xml格式返回值处理
 * @className: XmlRespHandler
 * @author: lcz
 * @date: 2018年7月19日 下午6:19:43
 */
public class XmlRespHandlerNotData extends XmlRespHandler{
	
	@Override
	public Document parse() {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		addElement(resp, KstHosConstant.TRANSACTIONCODE, data.getTransactionCode());
		addElement(resp, KstHosConstant.RESPCODE, data.getCode());
		addElement(resp, KstHosConstant.RESPMESSAGE, data.getMessage());
		if(data!=null) {
			if(data.getPIndex()!=null && data.getPSize()!=null && data.getPSize()>0) {
				Element page = resp.addElement(KstHosConstant.PAGE);
				addElement(page, KstHosConstant.PINDEX, data.getPIndex());
				addElement(page, KstHosConstant.PSIZE, data.getPSize());
				addElement(page, KstHosConstant.PCOUNT, data.getPCount());
			}
			if(data.getList()!=null && data.getList().size()>0) {
				if(data.getList().size() == 1) {
					objToElement(KstHosConstant.RESP,resp, data.getList().get(0));
				}else {
					for (Object obj : data.getList()) {
						objToElement(KstHosConstant.DATA,resp.addElement(KstHosConstant.DATA), obj);
					}
				}
			}
		}
		return doc;
	}
	
	@Override
	public String parseString() {
		return parse().asXML();
	}

}
