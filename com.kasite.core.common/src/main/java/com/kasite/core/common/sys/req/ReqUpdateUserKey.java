package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 设置二级支付密码
 * 
 * @author zhaoy
 *
 */
public class ReqUpdateUserKey extends AbsReq {

	public ReqUpdateUserKey(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.userId = super.getOpenId();
			this.loginKey = getDataJs().getString("LoginKey");
			this.payKey = getDataJs().getString("PayKey");
		}
	}
	
	private String userId;
	private String loginKey;
	private String payKey;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	
	

}
