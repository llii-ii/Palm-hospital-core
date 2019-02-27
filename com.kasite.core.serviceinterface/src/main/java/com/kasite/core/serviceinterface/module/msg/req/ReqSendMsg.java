package com.kasite.core.serviceinterface.module.msg.req;


import java.sql.Timestamp;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:33:15 
 * TODO 消息推送请求对象
 */
public class ReqSendMsg extends AbsReq{
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
	private String preSendTime;
	private Timestamp sendTime;
	private Integer sendCount;
	private String pushResult;
	private String orderId;
	private String operatorId;
	private String operatorName;
	private Integer state;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String orgId;
	private Element data1;
	
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(String modeType) {
		this.modeType = modeType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Element getData1() {
		return data1;
	}

	public void setData1(Element data1) {
		this.data1 = data1;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getPreSendTime() {
		return preSendTime;
	}

	public void setPreSendTime(String preSendTime) {
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
	public ReqSendMsg(InterfaceMessage msg, String cardNo, Integer cardType, 
			String channelId, String channelName, String mobile,
			String modeId,String modeType,String msgContent,String openId,Integer msgType,String operatorId,
			String operatorName,String orderId,String preSendTime,String pushMemberId,String sceneName) throws AbsHosException {
		super(msg);
		this.cardNo=cardNo;
		this.cardType=cardType;
		this.channelId=channelId;
		this.channelName=channelName;
		this.mobile=mobile;
		this.modeId=modeId;
		this.modeType=modeType;
		this.msgContent=msgContent;
		this.openId=openId;
		this.msgType=msgType;
		this.operatorId=operatorId;
		this.operatorName=operatorName;
		this.orderId=orderId;
		this.preSendTime=preSendTime;
		this.pushMemberId=pushMemberId;
		this.sceneName=sceneName;
	}
	public ReqSendMsg(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.cardNo=XMLUtil.getString(dataEL, "CardNo", false);
		this.cardType=XMLUtil.getInt(dataEL, "CardType", false);
		this.channelId=XMLUtil.getString(dataEL, "ChannelId", false);
		this.channelName=XMLUtil.getString(dataEL, "ChannelName", false);
		this.mobile=XMLUtil.getString(dataEL, "Mobile", false);
		this.modeId=XMLUtil.getString(dataEL, "ModeId", false);
		this.modeType=XMLUtil.getString(dataEL, "ModeType", false);
		this.msgContent=XMLUtil.getString(dataEL, "MsgContent", false);
		this.openId=XMLUtil.getString(dataEL, "OpenId", false);
		this.msgType=XMLUtil.getInt(dataEL, "MsgType", false);
		this.operatorId = XMLUtil.getString(dataEL, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEL, "OperatorName", false,super.getOperatorName());
		this.orderId=XMLUtil.getString(dataEL, "OrderId", false);
		this.preSendTime=XMLUtil.getString(dataEL, "PreSendTime", false);
		this.pushMemberId=XMLUtil.getString(dataEL, "PushMemberId", false);
		this.sceneName=XMLUtil.getString(dataEL, "SceneName", false);
		this.orgId=XMLUtil.getString(dataEL, "OrgId", false);
	}
}
