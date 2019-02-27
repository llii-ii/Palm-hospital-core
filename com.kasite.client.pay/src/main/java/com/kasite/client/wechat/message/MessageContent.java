package com.kasite.client.wechat.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.client.wechat.message.req.TextMessage;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;



/**
 * 消息中心
 * @author Administrator
 *
 */
public class MessageContent {

	public Map<String,String> map;
	
	public Element root;
	
	public void init(AuthInfoVo vo,Document doc) throws Exception{
		KasiteConfig.print("----in--->"+doc.asXML());
		Element rt = doc.getRootElement();
		AuthInfoVo authInfo = vo;//KasiteConfig.getWeChatAuthInfo(configKey,doc);
		List<Element> list = rt.elements();
		map = new HashMap<String,String>(17);
		if(null != authInfo) {
			map.put("AuthInfo", authInfo.toString());
		}
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
	}
	
	public MessageContent(){}
//	
//	public MessageContent(String configKey,String xml)throws DocumentException {
//		init(configKey,DocumentHelper.parseText(xml));
//	}
//	
//	public MessageContent(String configKey,Document doc){
//		init(configKey,doc);
//	}
	
	public MessageContent(AuthInfoVo vo,Document doc) throws Exception{
//		// 从request中取得输入流  
//	    InputStream inputStream = req.getInputStream();
//	    // 读取输入流  
//	    SAXReader reader = new SAXReader();  
//	    init(reader.read(inputStream));
		init(vo, doc);
	}
	
	public <T> T parse(BodyParse<T> parse)throws Exception{
		return parse.parse(map);
	}
	
	public <T> String make(BodyBuilder<T> builder)throws Exception{
		Element rt = DocumentHelper.createElement("xml");
		return builder.make(rt);
	}
	
	
	public static void main(String[] args)throws Exception {
		
		String xml = "<xml><ToUserName><![CDATA[bbbbbbbbbb]]></ToUserName><Url>xxxxxxxxxx</Url><FromUserName><![CDATA[aaaaaaaaaa]]></FromUserName><CreateTime>1430296491</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[aaaaaa]]></Content><MsgId>1234567890123456</MsgId></xml>";
		
//		MessageContent d = new MessageContent(a);
//		TextMessage c = new TextMessage();
//		c.setContent("aaaa");
//		c.setCreateTime(new Date().getTime()+"");
//		c.setFromUserName("fromUserName");
//		c.setFuncFlag("1");
//		c.setMsgId("event");
//		c.setMsgType("msgType");
//		c.setToUserName("toUserName");
//		String xml = d.make(c);
//		
//		ConfigUtil.print(xml);
		
//		MessageContent d = new MessageContent(xml);
//		TextMessage c = d.parse(new TextMessage());
		
//		ConfigUtil.print(c.getContent());
//		ConfigUtil.print(c.getFromUserName());
//		ConfigUtil.print(c.getToUserName());
		
		TextMessage c = new TextMessage();
		
		c.setContent("aaaa");
		c.setCreateTime(System.currentTimeMillis()+"");
		c.setFromUserName("fromUserName");
		c.setFuncFlag("1");
		c.setMsgId("event");
		c.setMsgType("msgType");
		c.setToUserName("toUserName");
//		MessageContent m = new MessageContent("bbbbbbbbbb",xml);
//		ConfigUtil.print(m.make(c));
		
	}
}
