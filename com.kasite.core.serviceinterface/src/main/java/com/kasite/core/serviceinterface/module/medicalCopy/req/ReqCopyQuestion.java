package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqCopyQuestion extends AbsReq{


	private String id;
	private String question;//字段标题（多个以逗号隔开）
	private String answer;//排序
	private String state;//状态0删除1未发布2已发布


	
	
	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getQuestion() {
		return question;
	}




	public void setQuestion(String question) {
		this.question = question;
	}




	public String getAnswer() {
		return answer;
	}




	public void setAnswer(String answer) {
		this.answer = answer;
	}




	public String getState() {
		return state;
	}




	public void setState(String state) {
		this.state = state;
	}




	public ReqCopyQuestion(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.question=XMLUtil.getString(ser, "question", false);
		this.answer=XMLUtil.getString(ser, "answer", false);
		this.state=XMLUtil.getString(ser, "state", false);
		
	}
	
}
