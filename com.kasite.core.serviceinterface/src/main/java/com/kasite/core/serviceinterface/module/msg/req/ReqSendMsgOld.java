package com.kasite.core.serviceinterface.module.msg.req;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:33:15 
 * TODO 消息推送请求对象
 */
public class ReqSendMsgOld extends AbsReq{
	private String orderId;
	private String modeType;
	private String channelId;
	private String cardNo;
	private String cardType;
	private String mobile;
	private String operatorId;
	private String operatorName;
	private String openId;
	private String memberId;
	private String channelName;
	private Element data1;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public ReqSendMsgOld(InterfaceMessage msg,String orderId,String modeType,String channelId,String cardNo,
			String cardType,String mobile,String operatorId,String operatorName,String openId,Element data1) throws AbsHosException {
		super(msg);
		this.orderId =  orderId;
		this.modeType =  modeType;
		this.channelId = channelId;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.mobile = mobile;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.openId = openId;
		this.data1 = data1;
		Document doc = this.toParamXml();
		super.doc = doc;
		super.root = doc.getRootElement();
		super.__DATA__ = super.root.element(IConstant.DATA);
	}
	public ReqSendMsgOld(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEL, "OrderId", false);
		this.modeType =  XMLUtil.getString(dataEL, "ModeType", false);
		this.channelId = XMLUtil.getString(dataEL, "ChannelId", false);
		this.cardNo = XMLUtil.getString(dataEL, "CardNo", false);
		this.cardType = XMLUtil.getString(dataEL, "CardType", false);
		this.mobile = XMLUtil.getString(dataEL, "Mobile", false);
		this.operatorId = XMLUtil.getString(dataEL, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEL, "OperatorName", false,super.getOperatorName());
		this.openId = XMLUtil.getString(dataEL, "OpenId", false);
		Element data1 = dataEL.element("Data_1");
		this.data1 =data1;
	}
	
	
	public Document toParamXml() {
		super.doc = DocumentHelper.createDocument();
		super.root = super.doc.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(super.root, KstHosConstant.TRANSACTIONCODE, KstHosConstant.DEFAULTTRAN);
		Element  data = super.root.addElement(KstHosConstant.DATA);
		XMLUtil.addElement(data, "OrderId", this.orderId);
		XMLUtil.addElement(data, "ModeType", this.modeType);
		XMLUtil.addElement(data, "ChannelId", this.channelId);
		XMLUtil.addElement(data, "CardNo", this.cardNo);
		XMLUtil.addElement(data, "CardType", this.cardType);
		XMLUtil.addElement(data, "Mobile", this.mobile);
		XMLUtil.addElement(data, "OperatorId", this.operatorId);
		XMLUtil.addElement(data, "OperatorName", this.operatorName);
		XMLUtil.addElement(data, "OpenId", this.openId);
		if(this.data1!=null) {
			data.add(this.data1);
		}
		return super.doc;
	}
}
