//package com.kasite.core.common.mq;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//
//import org.springframework.jms.core.MessageCreator;
//
//
///**
// * 
// * @className: TextMsgCreator
// * @author: lcz
// * @date: 2018年7月9日 上午11:36:12
// */
//public class TextMsgCreator implements MessageCreator{
//
//	private String message;
//	
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	public TextMsgCreator(String message) {
//		this.message = message;
//	}
//	
//	@Override
//	public Message createMessage(Session arg0) throws JMSException {
//		TextMessage textMessage = arg0.createTextMessage();
//        textMessage.setText(this.message);
//        return textMessage;
//	}
//	
//	
//	
//}
