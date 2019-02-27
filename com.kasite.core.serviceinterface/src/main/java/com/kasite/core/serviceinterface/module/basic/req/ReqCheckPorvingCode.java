package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqCheckPorvingaCode
 * @author: lcz
 * @date: 2018年7月23日 下午4:24:24
 */
public class ReqCheckPorvingCode extends AbsReq{
	
	public ReqCheckPorvingCode(InterfaceMessage msg, String pcId, String mobile, String provingCode)
			throws AbsHosException {
		super(msg);
		this.pCId = pcId;
		this.mobile = mobile;
		this.provingCode = provingCode;
	}
	private String pCId;
	private String mobile;
	private String provingCode;
	
	
	public String getpCId() {
		return pCId;
	}
	public void setpCId(String pCId) {
		this.pCId = pCId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvingCode() {
		return provingCode;
	}
	public void setProvingCode(String provingCode) {
		this.provingCode = provingCode;
	}
	
	
	
}
