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
public class ReqEditMsgScene extends AbsReq{

	private String sceneName;
	private String modeType;
	private Integer state;
	private String sceneId;
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
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
	/**
	 * 
	 * @Title: ReqSendTemplateMessage
	 * @Description: 
	 * @param msg
	 * @param wxKey
	 * @throws AbsHosException
	 */
	public ReqEditMsgScene(InterfaceMessage msg,String wxKey) throws AbsHosException {
		super(msg);
		this.wxKey = wxKey;
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeType = XMLUtil.getString(dataEl, "ModeType", false);
		this.sceneName = XMLUtil.getString(dataEl, "SceneName", false);
		this.sceneId = XMLUtil.getString(dataEl, "SceneId", true);
		this.state = XMLUtil.getInt(dataEl, "State", false);
	}
	public ReqEditMsgScene(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.modeType = XMLUtil.getString(dataEl, "modeType", false);
		this.sceneName = XMLUtil.getString(dataEl, "sceneName", false);
		this.sceneId = XMLUtil.getString(dataEl, "SceneId", true);
		this.state = XMLUtil.getInt(dataEl, "State", false);
	}
	private String wxKey;
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
}
