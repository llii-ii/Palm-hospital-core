package com.kasite.core.common.util.wechat;


/**
 * @author MECHREV
 */
public class Ticket {
	private long time;
	
	private String value;

	/**类型（config签名jsapi_ticket：jsapi，微信卡券api_ticket：wx_card）*/
	private String type;


	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
