package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:32:47 
 * TODO 查询消息回复请求对象
 */
public class ReqReplayMgr extends AbsReq {
	private String keyWord;
	private Integer replayType;
	private Integer state;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getReplayType() {
		return replayType;
	}

	public void setReplayType(Integer replayType) {
		this.replayType = replayType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public ReqReplayMgr(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.replayType = XMLUtil.getInt(dataEl, "ReplayType", false);
		this.state = XMLUtil.getInt(dataEl, "State", false);
		this.keyWord = XMLUtil.getString(dataEl, "KeyWord", true);
	}
}
