package com.kasite.client.wechat.message.resp;

import org.dom4j.Element;

/** 
 * 文本消息 
 * @author 
 */  
public class TextMessage extends BaseMessage<TextMessage> {  
	/** 消息内容*/
    private String content;  
  
  
    public String getContent() {  
        return content;  
    }  
  
    public void setContent(String content) {  
        this.content = content;  
    }
    @Override
    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }
    
	@Override
	public String make(Element root) throws Exception {
		super.makeBasic(root);
		addNodeCDATA(root,"Content",content);
		return root.asXML();
	}  
}  
