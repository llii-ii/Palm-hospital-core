package com.kasite.client.wechat.service;



import com.kasite.client.wechat.message.MessageContent;
import com.kasite.client.wechat.message.req.EventScanMessage;
import com.kasite.client.wechat.message.req.LinkMessage;
import com.kasite.client.wechat.message.req.MapMessage;
import com.kasite.client.wechat.message.req.MassResultMessage;
import com.kasite.client.wechat.message.req.NewsMessage;
import com.kasite.client.wechat.message.req.SubscribeMessage;
import com.kasite.client.wechat.message.req.TextMessage;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.exception.MsgListenerException;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;

/**
 * 消息事件服务
 * @author Administrator
 *
 */
public abstract class AbstractEventService extends AbstractEventListener
{
	private static final String EVENTKEY_11 = "11";
	private static final String EVENTKEY_12 = "12";
	@Override
	public String onMessage(MessageContent msgc) throws MsgListenerException {
		
		String msgType = msgc.map.get("MsgType");
		
		 // 文本消息  
		try {
	        if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_TEXT)) {
				TextMessage msg = msgc.parse(new TextMessage());
				return onMessage(msg);
	        }  
	        // 图片消息  
	        else if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_IMAGE)) {
	        	// TODO
	        }  
	        // 地理位置消息  
	        else if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_LOCATION)) {
	        	MapMessage msg = msgc.parse(new MapMessage());
	        	return onMessage(msg);
	        }  
	        // 链接消息  
	        else if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_LINK)) {
	           // TODO
	        }  
	        // 音频消息  
	        else if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_VOICE)) {
	        	// TODO
	        }  
	        // 事件推送  
	        else if (msgType.equals(WeiXinConstant.REQ_MESSAGE_TYPE_EVENT)) {  
	            //  事件类型  
	            String eventType = msgc.map.get("Event");  
	            //TODO 订阅  
	            if (eventType.equals(WeiXinConstant.EVENT_TYPE_SUBSCRIBE)) {
	            	SubscribeMessage msg = msgc.parse(new SubscribeMessage());
	            	return onMessage(msg);
	            }
	            //TODO 取消订阅  
	            else if (eventType.equals(WeiXinConstant.EVENT_TYPE_UNSUBSCRIBE)) {  
	            	SubscribeMessage msg = msgc.parse(new SubscribeMessage());
	            	return onMessage(msg);
	            }  
	            // 自定义菜单点击事件  
	            else if (eventType.equals(WeiXinConstant.EVENT_TYPE_CLICK)) {  
	            	String eventKey = msgc.map.get("EventKey");
	            	KasiteConfig.print(eventKey);
	            	if (eventKey.equals(EVENTKEY_11)) {
	            		//图文信息  
	            		NewsMessage msg = msgc.parse(new NewsMessage());
	            		return onMessage(msg);
	        		}else if (eventKey.equals(EVENTKEY_12)) {
	        			//图文信息  
	        			// 创建图文消息  
	        			NewsMessage msg = msgc.parse(new NewsMessage());
	            		return onMessage(msg);
	        		}
	            }else if (eventType.equals(WeiXinConstant.EVENT_TYPE_VIEW)) {
	            	
	        	}else if (eventType.equals(WeiXinConstant.MASSS_END_JOB_FINISH)) {
	            	//事件推送群发结果
	        		MassResultMessage msg = msgc.parse(new MassResultMessage());
            		return onMessage(msg);
	        	}else if (eventType.equals(WeiXinConstant.EVENT_SCAN)) {
	            	//扫描带参数二维码事件(已经关注公众号) 
	        		EventScanMessage msg = msgc.parse(new EventScanMessage());
	        		return onMessage(msg);
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(TextMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(NewsMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(MapMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(LinkMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(SubscribeMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(MassResultMessage reqMsg);
	/**
	 * onMessage
	 * @return
	 * @param reqMsg
	 */
	public abstract String onMessage(EventScanMessage reqMsg);
	
	
	@Override
	public void onExcption(MsgListenerException e) {
		e.printStackTrace();
	}
 
}
