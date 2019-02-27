package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:29:59
 * TODO 查询自动回复请求对象
 */
public class ReqQueryAutoReplayByFollow extends AbsReq {
	private String keyWord;
	//应用ID
	private String appId;
	//回复类型 1，关注回复，2关键字回复，3任意字回复
	private Integer replayType;
	private Integer state;
	
	public String getKeyWord() {
		return keyWord;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public ReqQueryAutoReplayByFollow(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(null != root) {
			Element dataEl = root.element(KstHosConstant.DATA);
			this.replayType = XMLUtil.getInt(dataEl, "ReplayType", true);
			this.state = XMLUtil.getInt(dataEl, "State", false);
		}
	}
}
