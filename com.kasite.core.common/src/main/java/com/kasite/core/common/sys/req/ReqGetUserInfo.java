package com.kasite.core.common.sys.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryUserList
 * @author: lcz
 * @date: 2018年8月28日 下午8:16:11
 */
public class ReqGetUserInfo extends AbsReq {
	private Long id;
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * @Title: ReqQueryUserList
	 * @Description:
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetUserInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.id = getDataJs().getLong("Id");
			this.username = getDataJs().getString("Username");
			if((this.id==null || this.id<=0) && StringUtil.isBlank(this.username)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"查询条件不能为空。");
			}
		}
	}

}
