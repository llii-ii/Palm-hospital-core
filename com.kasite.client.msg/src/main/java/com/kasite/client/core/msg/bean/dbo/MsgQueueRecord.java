package com.kasite.client.core.msg.bean.dbo;


import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
@Table(name="M_MSGQUEUE_RECORD")
public class MsgQueueRecord extends BaseDbo{
	@Id
	private String msgId;
	private String modeId;
	private String channelName;
	private String channelId;
	private String msgContent;
	private Integer msgType;
	private String modeType;
	private String sceneName;
	private String pushMemberId;
	private String cardNo;
	private Integer cardType;
	private String openId;
	private String mobile;
	private Timestamp preSendTime;
	private Timestamp sendTime;
	private Integer sendCount;
	private String pushResult;
	private String orderId;
	private String operatorId;
	private String operatorName;
	private Integer state;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String pId;
	private String hosId;
	private Integer isRead;
	public MsgQueueRecord(MsgQueue vo){
		this.msgId = vo.getMsgId();
		this.modeId = vo.getModeId();
		this.channelName = vo.getChannelName();
		this.channelId = vo.getChannelId();
		this.msgContent = vo.getMsgContent();
		this.msgType = vo.getMsgType();
		this.modeType = vo.getModelType();
		this.sceneName = vo.getSceneName();
		this.pushMemberId = vo.getPushMemberId();
		this.cardNo = vo.getCardNo();
		this.cardType = vo.getCardType();
		this.openId = vo.getOpenId();
		this.mobile = vo.getMobile();
		this.preSendTime = vo.getPreSendTime();
		this.sendTime = vo.getSendTime();
		this.sendCount = vo.getSendCount();
		this.pushResult = vo.getPushResult();
		this.orderId = vo.getOrderId();
		this.operatorId = vo.getOperatorId();
		this.operatorName = vo.getOperatorName();
		this.state = vo.getState();
		this.createTime = vo.getCreateTime();
		this.updateTime = vo.getUpdateTime();
		this.pId = vo.getpId();
		this.hosId = vo.getHosId();
	}
	public MsgQueueRecord(){
		super();
	}
	
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(String modeType) {
		this.modeType = modeType;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getModeId() {
		return modeId;
	}
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public String getModelType() {
		return modeType;
	}
	public void setModelType(String modeType) {
		this.modeType = modeType;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public String getPushMemberId() {
		return pushMemberId;
	}
	public void setPushMemberId(String pushMemberId) {
		this.pushMemberId = pushMemberId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Timestamp getPreSendTime() {
		return preSendTime;
	}
	public void setPreSendTime(Timestamp preSendTime) {
		this.preSendTime = preSendTime;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public String getPushResult() {
		return pushResult;
	}
	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
