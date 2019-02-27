package com.kasite.client.core.msg.bean.dbo;


import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/** 公众号自动回复设置
 * @author admin
 *
 */
@Table(name="M_AUTOREPLAY")
public class AutoReplay extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;
	/**
	 * '回复类型 1，关注回复，2关键字回复，3任意字回复',
	 */
	private Integer replayType;
	private String keyWord;
	private String replayMsg;
	/**
	 * '匹配规则0不匹配，1强匹配（=），2弱匹配（like）',
	 */
	private Integer matchRule;
	private Integer state;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getReplayType() {
		return replayType;
	}
	public void setReplayType(Integer replayType) {
		this.replayType = replayType;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getReplayMsg() {
		return replayMsg;
	}
	public void setReplayMsg(String replayMsg) {
		this.replayMsg = replayMsg;
	}
	public Integer getMatchRule() {
		return matchRule;
	}
	public void setMatchRule(Integer matchRule) {
		this.matchRule = matchRule;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	

}
