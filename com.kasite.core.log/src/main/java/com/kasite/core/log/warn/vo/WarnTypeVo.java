package com.kasite.core.log.warn.vo;

/**
 * 
 */

/**
 * 
 * @author zhangzz
 * @company yihu.com
 * 2015-4-29上午10:30:17
 */
public class WarnTypeVo {
	public String warnType;//类别/指标
	public String warnName;//名称
	public int lifeType;//生命周期类型：1日2分钟
	public String warnMsg;//告警通知信息
	public int doAction;//告警判断操作
	
	
	public int getDoAction() {
		return doAction;
	}
	public void setDoAction(int doAction) {
		this.doAction = doAction;
	}
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	public String getWarnName() {
		return warnName;
	}
	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}
	public String getWarnMsg() {
		return warnMsg;
	}
	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}
	public int getLifeType() {
		return lifeType;
	}
	public void setLifeType(int lifeType) {
		this.lifeType = lifeType;
	}
	
}
