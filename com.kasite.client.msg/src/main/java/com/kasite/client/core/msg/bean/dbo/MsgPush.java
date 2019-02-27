package com.kasite.client.core.msg.bean.dbo;



import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf 2017年11月14日 17:34:28 
 * TODO 消息推送对象
 */
@Table(name="M_MSGPUSH")
public class MsgPush  {
	@Id
	@KeySql(useGeneratedKeys=true)
	private String msgPushId;
	private String modeId;
	private String msgContent;
	private String modeType;
	private String msgType;
	private String msgNo;
	private String pushChannelName;
	private String pushChannelId;
	private String pushMemberId;
	private String pushResult;
	private String orderId;
	private Timestamp begin;
	private Timestamp end;
	private String operator;
	private String operatorName;
	private String mobile;
	private Integer state;
	private Integer num;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMsgPushId() {
		return msgPushId;
	}
	public void setMsgPushId(String msgPushId) {
		this.msgPushId = msgPushId;
	}
	public String getModeId() {
		return modeId;
	}
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}
	
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgNo() {
		return msgNo;
	}
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	public String getPushChannelName() {
		return pushChannelName;
	}
	public void setPushChannelName(String pushChannelName) {
		this.pushChannelName = pushChannelName;
	}
	public String getPushChannelId() {
		return pushChannelId;
	}
	public void setPushChannelId(String pushChannelId) {
		this.pushChannelId = pushChannelId;
	}
	public String getPushMemberId() {
		return pushMemberId;
	}
	public void setPushMemberId(String pushMemberId) {
		this.pushMemberId = pushMemberId;
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
	
	public Timestamp getBegin() {
		return begin;
	}
	public void setBegin(Timestamp begin) {
		this.begin = begin;
	}
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
}
