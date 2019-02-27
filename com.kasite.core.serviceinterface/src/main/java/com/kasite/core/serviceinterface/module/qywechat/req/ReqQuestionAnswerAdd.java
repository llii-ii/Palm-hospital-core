package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ增加问题答案
 * 
 * @author 無
 *
 */
public class ReqQuestionAnswerAdd extends AbsReq {
	public ReqQuestionAnswerAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.themeid = XMLUtil.getString(dataEl, "themeId", true);
		this.answerArr = XMLUtil.getString(dataEl, "answerArr", true);
	}

	/**
	 * 投票或问卷主题ID
	 */
	private String themeid;

	private String answerArr;

	public String getThemeid() {
		return themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public String getAnswerArr() {
		return answerArr;
	}

	public void setAnswerArr(String answerArr) {
		this.answerArr = answerArr;
	}
}
