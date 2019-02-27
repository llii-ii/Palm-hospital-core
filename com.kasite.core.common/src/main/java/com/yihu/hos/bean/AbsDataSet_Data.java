package com.yihu.hos.bean;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.exception.ParamException;
import com.yihu.hos.util.ConfigBean;

public abstract class AbsDataSet_Data implements IRespElement  {
	
	/***
	 * 返回给前端的 标准的xml
	 */
	protected Element returnXml = DocumentHelper.createElement("Data");
	
	@Override
	public void setRespCode(String respCode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRespMessage(String respMessage) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setTransactionCode(String transactionCode) {
		// TODO Auto-generated method stub
		
	}
	public void setRoot(Element root) {
		this.root = root;
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
					// TODO: handle exception
					throw new ParamException("节点中的数据转成整形出错。Node = "+ node +" Service = "+ element.asXML());
				}
			}
		}
		return val;
	}
	
	private Element root;
	
	public Object parseData(Object req){
		return  this;
	}
	
	@Override
	public Element getElement() {
		return root;
	}
	@Override
	public void setElement(Element element) {
		this.root = element;
	}
	
	private ConfigBean cfg;
	@Override
	public void setCfg(ConfigBean cfg) {
		this.cfg = cfg;
	}

	public ConfigBean getCfg() {
		return cfg;
	}
	public Element getRespRootElement(){
		return returnXml;
	}
}
