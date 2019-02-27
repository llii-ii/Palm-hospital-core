package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: HisMemberAutoUnbind
 * @author: lcz
 * @date: 2018年9月14日 下午2:45:12
 */
public class HisAddMember  extends AbsResp{
	/**返回码*/
	private String respCode;
	/**返回信息*/
	private String respMessage;
	/**
	 * 用户就诊卡
	 */
	private String cardNo;
	/**
	 * 新增的卡类型
	 */
	private String cardType;
	/**
	 * HIS内部用户唯一ID
	 */
	private String hisMemberId;
	
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
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
