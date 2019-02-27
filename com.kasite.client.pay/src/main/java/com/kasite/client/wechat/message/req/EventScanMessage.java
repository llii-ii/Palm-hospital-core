package com.kasite.client.wechat.message.req;

import java.util.Map;

import org.dom4j.Element;

/** 
 * 已关注扫描 
 * @author zgc
 */  
public class EventScanMessage extends BaseMessage<EventScanMessage> {  
    /**事件类型，SCAN*/
    private String event; 
    /**事件KEY值,二维码的参数值或者二维码scene_id*/
    private String eventKey;
    /**二维码的ticket，可用来换取二维码图片*/
    private String ticket; 


    public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }

	@Override
	public EventScanMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		this.event = map.get("Event");
		this.eventKey = map.get("EventKey");
		this.ticket = map.get("Ticket");
		return this;
	}
	
}  
