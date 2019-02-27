package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author zwl 2018年11月13日 09:31:15 
 * TODO 消息队列请求对象
 */
public class ReqMsgCenterMainCount extends AbsReq{
	/**
	 * 类型  modeType：按场景  operatorId：按来源    channelId：按渠道
	 */
	private String type;
	private String modeType;
	private String operatorId;
	private String channelId;
	private String startTime;//开始时间  yyyy-mm-dd
	private String endTime;
	private String orgId;
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqMsgCenterMainCount(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.type =  XMLUtil.getString(dataEL, "type",true);//1：模板 2文本
		this.modeType =  XMLUtil.getString(dataEL, "modeType", false);//模板ID
		this.channelId = XMLUtil.getString(dataEL, "ChannelId", false);
		this.operatorId = XMLUtil.getString(dataEL, "operatorId", false);
		this.modeType = XMLUtil.getString(dataEL, "ModeType", false);
		this.startTime = XMLUtil.getString(dataEL, "startTime", true);
		this.endTime = XMLUtil.getString(dataEL, "endTime", true);
		this.orgId = XMLUtil.getString(dataEL, "OrgId", true);
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	public ReqMsgCenterMainCount(InterfaceMessage msg, String moblie, String content, 
			String channelId, String operatorId, String operatorName,
			String signName,String templateCode,String templateParam) throws AbsHosException {
		super(msg);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	


	
	
}
