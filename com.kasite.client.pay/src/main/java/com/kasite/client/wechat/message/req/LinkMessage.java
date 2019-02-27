package com.kasite.client.wechat.message.req;

import java.util.Map;

/** 
 * 链接消息 
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
        return description;  
    }  
  
    public void setDescription(String description) {  
    	this.description = description;  
    }  
  
    public String getUrl() {  
        return url;  
    }  
  
    public void setUrl(String url) {  
    	this.url = url;  
    }
    
	@Override
	public LinkMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		this.title = map.get("Title");
		this.description = map.get("Description");
		this.url = map.get("Url");
		return this;
	}  
} 