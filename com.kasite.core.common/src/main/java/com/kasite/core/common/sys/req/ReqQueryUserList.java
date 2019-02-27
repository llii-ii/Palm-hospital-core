package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryUserList
 * @author: lcz
 * @date: 2018年8月28日 下午8:16:11
 */
public class ReqQueryUserList extends AbsReq {
	private Long id;
	private String username;
	private String mobile;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @Title: ReqQueryUserList
	 * @Description:
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryUserList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.id = getDataJs().getLong("Id");
			this.username = getDataJs().getString("Username");
			this.mobile = getDataJs().getString("Mobile");
		}
	}

}
