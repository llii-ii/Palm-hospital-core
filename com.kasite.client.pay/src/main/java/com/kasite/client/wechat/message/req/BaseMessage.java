package com.kasite.client.wechat.message.req;

import java.util.Map;

import com.kasite.client.wechat.message.BodyParse;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;

/**
 * @author MECHREV
 */
public abstract class BaseMessage<T> implements BodyParse<T>  {

	/**
	 * 哪个微信号请求进来的 wx_appid
	 */
	private AuthInfoVo authInfo;
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

	public AuthInfoVo getAuthInfo() {
		return authInfo;
	}

	public String getToUserName() {  
        return this.toUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        this.toUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return this.fromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
    	this.fromUserName = fromUserName;  
    }  
  
    public String getMsgType() {  
        return this.msgType;  
    }  
  
    public void setMsgType(String msgType) {  
        this.msgType = msgType;  
    }  
  
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getFuncFlag() {
		return this.funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

	@Override
	public void parseBasic(Map<String, String> map) throws Exception {
	    this.toUserName = map.get("ToUserName");
	    this.fromUserName = map.get("FromUserName");
	    this.createTime = map.get("CreateTime");
	    this.msgType = map.get("MsgType");
	    this.msgId = map.get("MsgId");
	    this.funcFlag = map.get("FuncFlag");
	    this.authInfo = KasiteConfig.getAuthInfo(map.get("AuthInfo"));
	}

	
	
}
