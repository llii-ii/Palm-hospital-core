package com.kasite.client.wechat.message.req;

import java.util.Map;

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

    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }

	@Override
	public TextMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		this.content = map.get("Content");
		return this;
	}
	
}  
