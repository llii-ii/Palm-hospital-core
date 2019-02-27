/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询基础科室入参
 * 
 * @author lsq version 1.0 2017-7-31上午11:56:42
 */
public class ReqSaveProvingCode extends AbsReq {
	public ReqSaveProvingCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}
	
	/**
	 * @Title: ReqSaveProvingCode
	 * @Description: 
	 * @param msg
	 * @param code
	 * @param mobile
	 * @throws AbsHosException
	 */
	public ReqSaveProvingCode(InterfaceMessage msg, String code, String mobile) throws AbsHosException {
		super(msg);
		this.code = code;
		this.mobile = mobile;
	}

	private String code;
	private String mobile;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
