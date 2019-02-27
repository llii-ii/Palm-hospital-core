package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryAnswer extends AbsResp {

	private String oAnswer;
	
	private String answer;

	public String getoAnswer() {
		return oAnswer;
	}

	public void setoAnswer(String oAnswer) {
		this.oAnswer = oAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
