package com.kasite.core.serviceinterface.module.basic.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryMatriStamp extends AbsResp {

	private String question;
	
	private List<RespQueryMatriItem> data;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<RespQueryMatriItem> getData() {
		return data;
	}

	public void setData(List<RespQueryMatriItem> data) {
		this.data = data;
	}
}
