package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: HisMemberAutoUnbind
 * @author: lcz
 * @date: 2018年9月14日 下午2:45:12
 */
public class HisMemberAutoUnbind  extends AbsResp{
	/**返回码*/
	private String respCode;
	/**返回信息*/
	private String respMessage;
	
	private Boolean hasUser;
	
	
	
	/**
	 * @return the hasUser
	 */
	public Boolean getHasUser() {
		return hasUser;
	}
	/**
	 * @param hasUser the hasUser to set
	 */
	public void setHasUser(Boolean hasUser) {
		this.hasUser = hasUser;
	}
	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * @return the respMessage
	 */
	public String getRespMessage() {
		return respMessage;
	}
	/**
	 * @param respMessage the respMessage to set
	 */
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	
	
}
