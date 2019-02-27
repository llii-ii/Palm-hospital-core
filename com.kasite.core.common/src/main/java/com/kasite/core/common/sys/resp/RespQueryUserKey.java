package com.kasite.core.common.sys.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 查询二级支付密码
 * 
 * @author zhaoy
 *
 */
public class RespQueryUserKey extends AbsResp {

	private String userId;
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
