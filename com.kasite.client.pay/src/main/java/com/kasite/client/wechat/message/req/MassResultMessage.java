package com.kasite.client.wechat.message.req;

import java.util.Map;

/** 
 * 群发结果 
 * @author zgc
 */  
public class MassResultMessage extends BaseMessage<MassResultMessage> {  

	/**事件信息*/
	private String event;
	/**群发的消息ID*/
	private String msgID;
	/**群发的结果*/
	private String status;
	/**group_id下粉丝数；或者openid_list中的粉丝数*/
	private String totalCount;
	/**准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount*/
	private String filterCount;
	/**发送成功的粉丝数*/
	private String sentCount;
	/**发送失败的粉丝数*/
	private String errorCount;

	

	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getFilterCount() {
		return filterCount;
	}

	public void setFilterCount(String filterCount) {
		this.filterCount = filterCount;
	}

	public String getSentCount() {
		return sentCount;
	}

	public void setSentCount(String sentCount) {
		this.sentCount = sentCount;
	}

	public String getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}


	@Override
	public MassResultMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		
		this.event = map.get("Event");
		this.msgID = map.get("MsgID");
		this.status = map.get("Status");
		this.totalCount = map.get("TotalCount");
		this.filterCount = map.get("FilterCount");
		this.sentCount = map.get("SentCount");
		this.errorCount = map.get("ErrorCount");

		return this;
	}  
} 