package com.kasite.core.serviceinterface.module.rf.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 智付后台-运营状况请求入参
 * 
 * @author zhaoy
 *
 */
public class ReqQueryBillRf extends AbsReq{

	public ReqQueryBillRf(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.time = getDataJs().getInteger("Time");
			this.type = getDataJs().getString("Type");
		}
	}
	/**
	 * 0-代表月 1-日
	 */
	private Integer time;
	/**
	 * 各种渠道场景
	 */
	private String type;
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
