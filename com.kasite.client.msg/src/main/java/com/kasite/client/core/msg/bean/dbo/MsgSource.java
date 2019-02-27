package com.kasite.client.core.msg.bean.dbo;


import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
@Table(name="M_MSGSOURCE")
public class MsgSource extends BaseDbo{
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
	@Id
	private String sourceId;
	private String sourceName;
	private String hosId;
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	private Integer state;
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}
