package com.kasite.client.wechat.message.resp;

import org.dom4j.Element;

import com.kasite.client.wechat.message.BodyBuilder;


/**
 * @author MECHREV
 */
public abstract class BaseMessage<T> implements BodyBuilder<T>  {

	/** 开发者微信号*/
    private String toUserName;  
    /** 发送方帐号（一个OpenID）*/
    private String fromUserName;  
    /** 消息创建时间 （整型）*/
    private String createTime;  
    /** 消息类型（text/image/location/link）*/
    private String msgType;  
    /** 消息id，64位整型*/
    private String msgId;  
    /** 位0x0001被标志时，星标刚收到的消息*/
    private String funcFlag;

    
    public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

	public String getToUserName() {  
        return this.toUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
    	this. toUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return this.fromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
    	this.fromUserName = fromUserName;  
    }  
  
    public String getCreateTime() {  
        return createTime;  
    }  
  
    public void setCreateTime(String createTime) {  
        this.createTime = createTime;  
    }  
  
    public String getMsgType() {  
        return this.msgType;  
    }  
  
    public void setMsgType(String msgType) {  
    	this.msgType = msgType;  
    }  
  
    public String getMsgId() {  
        return this.msgId;  
    }  
  
    public void setMsgId(String msgId) {  
    	this.msgId = msgId;  
    }

    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }
    
	@Override
	public void makeBasic(Element root) throws Exception {
		addNodeCDATA(root,"ToUserName", toUserName);
		addNodeCDATA(root,"FromUserName",fromUserName);
		addNodeCDATA(root,"CreateTime",createTime);
		addNodeCDATA(root,"MsgType",msgType);
		addNodeCDATA(root,"MsgId",msgId);
		addNodeCDATA(root,"FuncFlag",funcFlag);
	}  
	
	
}
