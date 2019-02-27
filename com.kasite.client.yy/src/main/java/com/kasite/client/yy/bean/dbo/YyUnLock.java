/**
 * 
 */
package com.kasite.client.yy.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**数据库实体类(释号)
 * @author lsq
 * version 1.0
 * 2017-7-5下午5:29:13
 */
@Table(name="YY_UNLOCK")
public class YyUnLock extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private String orderId;
	private String remark;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
