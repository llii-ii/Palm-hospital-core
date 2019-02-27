package com.kasite.client.survey.bean.dto;

import java.io.Serializable;

public class SampleAnswerVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String itemId;
	
	private String answer;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
