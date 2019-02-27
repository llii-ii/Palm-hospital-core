/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**取消预约出参
 * @author lsq
 * version 1.0
 * 2017-7-7下午3:00:04
 */
public class HisYYCancel  extends AbsResp{
	/**退号的时候第三方返回的结果集*/
	private String store;
	/**返回码*/
	private String respCode;
	/**返回信息*/
	private String respMessage;

	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
}
