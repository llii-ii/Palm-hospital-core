package com.kasite.core.serviceinterface.module.msg.req;

import java.sql.Timestamp;

import javax.persistence.Id;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: ReqQueryAllTemplateList
 * @author: zwl
 * @date: 2018年8月3日 上午11:32:22
 */
public class ReqEditMsgTemp extends AbsReq{

	private String modeId;
	private String modeContent;
	private String userHos;
	private String useDept;
	private String ifUseAllDept;
	private String useChannel;
	private String ifUseAllChannel;
	private String useTime;
	private String ifUseAllTime;
	private String modeType;
	private String modeUrl;
	private Timestamp begin;
	private Timestamp end;
	private String state;
	private String msgType;
	private String pushMode;
	private String msgTempName;
	private String operatorId;
	private String operatorName;
	
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
	public String getMsgTempName() {
		return msgTempName;
	}
	public void setMsgTempName(String msgTempName) {
		this.msgTempName = msgTempName;
	}
	private String templateId;
	
	public String getModeId() {
		return modeId;
	}
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}
	public String getModeContent() {
		return modeContent;
	}
	public void setModeContent(String modeContent) {
		this.modeContent = modeContent;
	}
	public String getUserHos() {
		return userHos;
	}
	public void setUserHos(String userHos) {
		this.userHos = userHos;
	}
	public String getUseDept() {
		return useDept;
	}
	public void setUseDept(String useDept) {
		this.useDept = useDept;
	}
	public String getIfUseAllDept() {
		return ifUseAllDept;
	}
	public void setIfUseAllDept(String ifUseAllDept) {
		this.ifUseAllDept = ifUseAllDept;
	}
	public String getUseChannel() {
		return useChannel;
	}
	public void setUseChannel(String useChannel) {
		this.useChannel = useChannel;
	}
	public String getIfUseAllChannel() {
		return ifUseAllChannel;
	}
	public void setIfUseAllChannel(String ifUseAllChannel) {
		this.ifUseAllChannel = ifUseAllChannel;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getIfUseAllTime() {
		return ifUseAllTime;
	}
	public void setIfUseAllTime(String ifUseAllTime) {
		this.ifUseAllTime = ifUseAllTime;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	public String getModeUrl() {
		return modeUrl;
	}
	public void setModeUrl(String modeUrl) {
		this.modeUrl = modeUrl;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getPushMode() {
		return pushMode;
	}
	public void setPushMode(String pushMode) {
		this.pushMode = pushMode;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqEditMsgTemp(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeId = XMLUtil.getString(dataEl, "modeId", true);
		this.useChannel = XMLUtil.getString(dataEl, "useChannel", false);
		this.modeType = XMLUtil.getString(dataEl, "modeType", false);
		this.modeUrl = XMLUtil.getString(dataEl, "modeUrl", false);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", false);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", false);
		this.state = XMLUtil.getString(dataEl, "state", false);
		this.modeContent = XMLUtil.getString(dataEl, "modeContent", false);
		this.msgType = XMLUtil.getString(dataEl, "msgType", false);
		this.pushMode = XMLUtil.getString(dataEl, "pushMode", false);
		this.templateId = XMLUtil.getString(dataEl, "templateId", false);
		this.msgTempName = XMLUtil.getString(dataEl, "msgTempName", false);
		
	}
	public ReqEditMsgTemp(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeId = XMLUtil.getString(dataEl, "modeId", true);
		this.useChannel = XMLUtil.getString(dataEl, "useChannel", false);
		this.modeType = XMLUtil.getString(dataEl, "modeType", false);
		this.modeUrl = XMLUtil.getString(dataEl, "modeUrl", false);
		this.operatorId = XMLUtil.getString(dataEl, "operatorId", false);
		this.operatorName = XMLUtil.getString(dataEl, "operatorName", false);
		this.state = XMLUtil.getString(dataEl, "state", false);
		this.modeContent = XMLUtil.getString(dataEl, "modeContent", false);
		this.msgType = XMLUtil.getString(dataEl, "msgType", false);
		this.pushMode = XMLUtil.getString(dataEl, "pushMode", false);
		this.templateId = XMLUtil.getString(dataEl, "templateId", false);
		this.msgTempName = XMLUtil.getString(dataEl, "msgTempName", false);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
