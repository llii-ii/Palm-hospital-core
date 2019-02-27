package com.kasite.client.survey.bean.dto;

import java.io.Serializable;

/**
 * 答案详情
 * 
 * @author zhaoy
 *
 */
public class SampleInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String itemId;
	
	private String otherAnswer;
	
	private String itemCount;
	
	private String ifAddBlank;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOtherAnswer() {
		return otherAnswer;
	}

	public void setOtherAnswer(String otherAnswer) {
		this.otherAnswer = otherAnswer;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getIfAddBlank() {
		return ifAddBlank;
	}

	public void setIfAddBlank(String ifAddBlank) {
		this.ifAddBlank = ifAddBlank;
	}
	
}
