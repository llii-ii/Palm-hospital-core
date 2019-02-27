package com.kasite.core.serviceinterface.module.msg.resp;
import com.kasite.core.common.resp.AbsResp;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespMsgUserOpenId extends AbsResp{
	private int cardType;
	private int state;
	private String cardNo;
	private int openType;
	private String openId;
	private String id;

	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
