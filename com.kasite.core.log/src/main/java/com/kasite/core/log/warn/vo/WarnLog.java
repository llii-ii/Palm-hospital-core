package com.kasite.core.log.warn.vo;

/**
 * 
 */

import java.util.List;

/**
 * @author Administrator
 *
 */
public class WarnLog {
	/**
	 * json:
	 * {
	 *  warnObject:告警对象
	 * 	warnType:告警指标/类型
	 * 	desc:事件文本描述（直接作为短信下发）
	 * 	insertTime:告警时间System.currentTimeMillis()
	 * 	limit:告警阀值
	 *  value:当前值
	 *  warnByList:[{通知列表
	 *  		mobile:手机号码
	 *  		email:邮件
	 *  		}]
	 *  appId:应用ID
	 * }
	 */
	public String warnObject;//告警对象
	public String warnType;//告警指标/类型
	public String desc;//事件文本描述（直接作为短信下发）
	public long insertTime;//告警时间System.currentTimeMillis()
	public long limit;//告警阀值
	public long value;//当前值
	public List<WarnBy> warnByList;//通知列表
	public String appId = "WSGW";//应用ID
	private String key;//对应的redis的key
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWarnObject() {
		return warnObject;
	}
	public void setWarnObject(String warnObject) {
		this.warnObject = warnObject;
	}
	public List<WarnBy> getWarnByList() {
		return warnByList;
	}
	public void setWarnByList(List<WarnBy> warnByList) {
		this.warnByList = warnByList;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(long insertTime) {
		this.insertTime = insertTime;
	}
	public long getLimit() {
		return limit;
	}
	public void setLimit(long limit) {
		this.limit = limit;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	
}
