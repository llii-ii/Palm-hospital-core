package com.kasite.core.serviceinterface.module.msg.resp;


import java.sql.Timestamp;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;
import com.kasite.core.common.resp.AbsResp;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespMsgSource extends AbsResp{
	private String sourceId;
	private String sourceName;
	private int state;
	
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
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
