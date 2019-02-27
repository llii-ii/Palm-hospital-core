package com.kasite.core.serviceinterface.module.msg.resp;


import java.sql.Timestamp;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;
import com.kasite.core.common.resp.AbsResp;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespMsgScene extends AbsResp{
	private String sceneId;
	private String sceneName;
	private String modeType;
	private int state;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	/**新增时间**/
	private Timestamp createTime;
	/**最后更新时间**/
	private Timestamp updateTime;
	
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
