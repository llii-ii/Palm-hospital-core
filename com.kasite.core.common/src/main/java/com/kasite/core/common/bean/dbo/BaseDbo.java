package com.kasite.core.common.bean.dbo;

import java.sql.Timestamp;

/**
 * 
 * @className: BaseDbo
 * @author: lcz
 * @date: 2018年7月18日 下午6:26:01
 */
public class BaseDbo {
	
	/**新增时间**/
	private Timestamp createTime;
	/**最后更新时间**/
	private Timestamp updateTime;
	/**操作人ID**/
	private String operatorId;
	/**操作人名称**/
	private String operatorName;
	
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
	
	
	
}
