/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author lcy
 * 验证码出参类
 * @version 1.0
 * 2017-6-22 下午3:35:03
 */
public class RespGetprovingCode  extends AbsResp{

	private String pCId;
	
	public String getpCId() {
		return pCId;
	}
	public void setpCId(String pCId) {
		this.pCId = pCId;
	}
	public RespGetprovingCode() {
		super();
	}
	public RespGetprovingCode(String pcId) {
		super();
		this.pCId = pcId;
	}

}
