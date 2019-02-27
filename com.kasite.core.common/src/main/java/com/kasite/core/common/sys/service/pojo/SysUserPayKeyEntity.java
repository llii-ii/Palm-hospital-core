package com.kasite.core.common.sys.service.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 二级支付密码
 * 
 * @author zhaoy
 *
 */
@Table(name="SYS_USER_PAYKEY") 
public class SysUserPayKeyEntity {

	/**
	 * 用户ID
	 */
	@Id
	@KeySql(useGeneratedKeys=true)
	private String userId;
	
	/**
	 * 支付密码
	 */
	private String payKey;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPayKey() {
		return payKey;
	}
	
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	
}
