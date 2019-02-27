package com.kasite.core.serviceinterface.module.msg.resp;


import java.sql.Timestamp;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;
import com.kasite.core.common.resp.AbsResp;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespMsgQueueList extends AbsResp{
	/**
	 * 模板ID
	 */
	private String modeId;
	/**
	 * 推送渠道
	 */
	private String channelName;
	/**
	 * 推送渠道ID 100123：微信 100125 支付宝  sms： 短信
	 */
	private String channelId;
	/**
	 * 消息内容
	 */
	private String msgContent;
	/**
	 * 消息类型1：模板 2文本
	 */
	private int msgType;
	
	/**
	 * 场景类型id
	 */
	private String modeType;
	/**
	 * 场景名称
	 */
	private String sceneName;
	/**
	 * 推送成员id
	 */
	private String pushMemberId;
	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 卡类型 
	 */
	private int cardType;
	/**
	 * openid
	 */
	private String openId;
	/**
	 * 电话
	 */
	private String mobile;
	/**
	 * 预发送时间，如果预发送时间比当前时间小，则马上发送，如果大于当前时间，则等到时间了再发送
	 */
	private String preSendTime;
	/**
	 * 总发送条数
	 */
	private int totalNum;
	/**
	 * 未发送条数
	 */
	private int unSendNum;
	/**
	 * 发送成功条数
	 */
	private int successNum;
	/**
	 * 发送失败条数
	 */
	private int failNum;
	
	private int isRead;
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getUnSendNum() {
		return unSendNum;
	}
	public void setUnSendNum(int unSendNum) {
		this.unSendNum = unSendNum;
	}
	public int getSuccessNum() {
		return successNum;
	}
	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}
	public int getFailNum() {
		return failNum;
	}
	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}
	private String sendTime;
	private int sendCount;
	private String pushResult;
	private String createTime;
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 关联订单号
	 */
	private String orderId;
	/**
	 * 操作人
	 */
	private String operatorId;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 操作人
	 */
	private String msgId;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	private String operatorName;
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public String getPushResult() {
		return pushResult;
	}
	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
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
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
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
	public String getPreSendTime() {
		return preSendTime;
	}
	public void setPreSendTime(String preSendTime) {
		this.preSendTime = preSendTime;
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
	
	
	
}
