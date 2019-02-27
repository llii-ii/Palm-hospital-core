package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryAllTemplateList
 * @author: zwl
 * @date: 2018年8月3日 上午11:32:22
 */
public class ReqAddMsgScene extends AbsReq{

	private String sceneName;
	private String modeType;
	private String orgId;
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}

	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqAddMsgScene(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeType = XMLUtil.getString(dataEl, "modeType", true);
		this.sceneName = XMLUtil.getString(dataEl, "sceneName", true);
		this.orgId =  XMLUtil.getString(dataEl, "OrgId", true);
	}
	public ReqAddMsgScene(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeType = XMLUtil.getString(dataEl, "modeType", true);
		this.sceneName = XMLUtil.getString(dataEl, "sceneName", true);
		this.orgId =  XMLUtil.getString(dataEl, "OrgId", true);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
