package com.yihu.hos.bean;

import org.dom4j.Element;

import com.yihu.hos.util.ConfigBean;

/**
 * 
 * @className: RespElement
 * @author: lcz
 * @date: 2018年7月10日 下午3:43:27
 */
public abstract class RespElement implements IRespElement{

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setElement(Element element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element getRespRootElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parseData(Object req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCfg(ConfigBean cfg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransactionCode(String transactionCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRespMessage(String respMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRespCode(String respCode) {
		// TODO Auto-generated method stub
		
	}
	
}
