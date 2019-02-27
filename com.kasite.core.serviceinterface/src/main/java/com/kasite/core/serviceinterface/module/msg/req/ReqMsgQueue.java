package com.kasite.core.serviceinterface.module.msg.req;

import java.text.ParseException;
import java.util.Date;

import org.dom4j.Element;

import com.coreframework.util.DateOper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author zwl 2018年11月13日 09:31:15 
 * TODO 消息队列请求对象
 */
public class ReqMsgQueue extends AbsReq{
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
	 * 关联订单号
	 */
	private String orderId;
	/**
	 * 操作人
	 */
	private String operatorId;
	/**
	 * 操作人
	 */
	private String operatorName;
	/**
	 * 医院id
	 */
	private String orgId;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @param msg
	 * @throws AbsHosException
	 * @throws ParseException 
	 */
	public ReqMsgQueue(InterfaceMessage msg) throws AbsHosException, ParseException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.msgType =  XMLUtil.getInt(dataEL, "MsgType", false);//1：模板 2文本
		this.modeId =  XMLUtil.getString(dataEL, "ModeId", false);//模板ID
		this.channelId = XMLUtil.getString(dataEL, "ChannelId", true);
		this.msgContent =  XMLUtil.getString(dataEL, "MsgContent", true);//
		this.channelName = XMLUtil.getString(dataEL, "ChannelName", false);
		this.modeType = XMLUtil.getString(dataEL, "ModeType", false);
		this.sceneName = XMLUtil.getString(dataEL, "SceneName", false);
		this.pushMemberId = XMLUtil.getString(dataEL, "PushMemberId", false);
		this.mobile = XMLUtil.getString(dataEL, "Mobile", false);
		this.cardNo = XMLUtil.getString(dataEL, "CardNo", false);
		this.cardType = XMLUtil.getInt(dataEL, "CardType", false);
		this.openId = XMLUtil.getString(dataEL, "OpenId", false);
		this.preSendTime = XMLUtil.getString(dataEL, "PreSendTime", false);
		this.operatorId = XMLUtil.getString(dataEL, "OperatorId", true);
		this.operatorName = XMLUtil.getString(dataEL, "OperatorName", false);
		this.orderId = XMLUtil.getString(dataEL, "OrderId", false);
		this.orgId = XMLUtil.getString(dataEL, "HosId", true);
		if(StringUtil.isBlank(this.modeId)&&StringUtil.isBlank(this.modeType)){//场景和模板id不能同时为空
			throw new RRException("ModeId和modeType不能同时为空");
		}
		if(StringUtil.isBlank(this.cardNo)&&StringUtil.isBlank(this.openId)){
			throw new RRException("cardNo和openId不能同时为空");
		}
	}
	/**
	 * @Title: ReqSendSms
	 * @Description: 
	 * @param msg
	 * @param moblie
	 * @param content
	 * @param channelId
	 * @param operatorId
	 * @param operatorName
	 * @throws AbsHosException
	 */
	public ReqMsgQueue(InterfaceMessage msg, String cardNo, int cardType, 
			String channelId, String channelName, String mobile,
			String modeId,String modeType,String msgContent,String openId,int msgType,String operatorId,
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
