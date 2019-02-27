/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**挂号出参
 * @author lsq
 * version 1.0
 * 2017-7-6下午2:19:47
 */
public class HisOfferNumber extends AbsResp{
	/**锁号订单*/
	@NotBlank(message="锁号订单 hisOrderId 不能为空", groups = {AddGroup.class})
	private String hisOrderId;
	/**锁定的号数*/
	private Integer sqNo;
	/**调用HIS挂号的结果集 保存HIS返回结果集到表  O_ORDER_HISINFO 表中*/
	private String store;
	/**就诊位置*/
	private String position;
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
	public String getHisOrderId() {
		return hisOrderId;
	}
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	public Integer getSqNo() {
		return sqNo;
	}
	public void setSqNo(Integer sqNo) {
		this.sqNo = sqNo;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
