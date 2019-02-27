package com.kasite.core.common.resp;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;

/**
 * 抽象返回值处理类
 * @className: AbsRespHandler
 * @author: lcz
 * @date: 2018年7月19日 下午6:21:28
 */
public class BaseRespHandler extends AbsRespHandler{
	
	
	public BaseRespHandler data(CommonResp<?> data) {
		super.setData(data);
		return this;
	}

	public BaseRespHandler config(ParseConfig cfg) {
		super.setCfg(cfg);
		return this;
	}

	@Override
	public Document parse() {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		addElement(resp, KstHosConstant.TRANSACTIONCODE, data.getTransactionCode());
		addElement(resp, KstHosConstant.RESPCODE, data.getCode());
		addElement(resp, KstHosConstant.RESPMESSAGE, data.getMessage());
//		this.data.setData(data);
//		this.data.setRoot(doc.getRootElement());
		return doc;
	}
	
	@Override
	public String parseString() {
		
		return this.parse().asXML();
	}
	/**
	 * 添加xml节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public void addElement(Element e,String name,Object value){
		if(StringUtil.isBlank(value)){
			e.addElement(name).addText("");
		}else{
			e.addElement(name).addText(value.toString());
		}
	}
	/**
	 * 添加xml节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public void addElement(Element e, String name, Integer value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.setText(Integer.toString(value));
		}
	}

	/**
	 * 添加CDATA 类型节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public void addCDATAElement(Element e, String name, String value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.addCDATA(value.trim());
		}
	}
	/**
	 * 添加CDATA 类型节点
	 * @param e
	 * @param name
	 * @param value
	 */
	public void addCDATAElement(Element e, String name, Integer value) {
		Element current = e.addElement(name);
		if (value != null) {
			current.addCDATA(value.toString());
		}
	}

	@Override
	public JSONObject parseJSON() {
		JSONObject respJs = new JSONObject();
		respJs.put(KstHosConstant.TRANSACTIONCODE, data.getTransactionCode());
		respJs.put(KstHosConstant.RESPCODE,  data.getCode());
		respJs.put(KstHosConstant.RESPMESSAGE,  data.getMessage());
		return respJs;
	}
}
