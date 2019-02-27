package com.kasite.core.common.sys.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqUpdateUser
 * @author: lcz
 * @date: 2018年8月29日 下午5:08:09
 */
public class ReqUpdateUserPassword extends AbsReq {

	private String username;
	private String oldPassword;
	private String newPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @Title: ReqUpdateUser
	 * @Description:
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqUpdateUserPassword(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.oldPassword = getDataJs().getString("OldPassword");
			this.newPassword = getDataJs().getString("NewPassword");
			this.username = getDataJs().getString("Username");
			if(StringUtil.isBlank(this.oldPassword)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"旧密码不能为空。");
			}
			if(StringUtil.isBlank(this.newPassword)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"新密码不能为空。");
			}
			if(StringUtil.isBlank(this.username)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"用户名不能为空。");
			}
		}
	}

}
