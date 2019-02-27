package com.kasite.client.core.msg.bean.dbo;


import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
@Table(name="M_MSGSCENE_OPENID")
public class MsgOpenIdScene{
	@Id
	private String id;
	private String openId;
	private String sceneName;
	private String modeType;
	private Integer state;
	private String hosId;
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}
