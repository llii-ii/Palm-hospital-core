package com.kasite.client.wechat.message.resp;

import org.dom4j.Element;

/** 
 * 链接消息 
 *  
 * @author 
 */  
public class LinkMessage extends BaseMessage<LinkMessage> {  
	/** 消息标题*/
    private String title;  
    /** 消息描述*/
    private String description;  
    /** 消息链接*/
    private String url;  

  
    public String getTitle() {  
        return title;  
    }  
  
    public void setTitle(String title) {  
    	this.title = title;  
    }  
  
    public String getDescription() {  
        return this.description;  
    }  
  
    public void setDescription(String description) {  
    	this.description = description;  
    }  
  
    public String getUrl() {  
        return this.url;  
    }  
  
    public void setUrl(String url) {  
    	this.url = url;  
    }
    @Override
    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }
    
	@Override
	public String make(Element root) throws Exception {
		super.makeBasic(root);
		addNodeCDATA(root,"Title",title);
		addNodeCDATA(root,"Description",description);
		addNodeCDATA(root,"Url",url);
		return root.asXML();
	}  
} 