package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 投票、问卷Query
 * 
 * @author 無
 *
 */
public class ReqVoteQuestionQuery extends AbsReq {
	public ReqVoteQuestionQuery(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.status = XMLUtil.getInt(dataEl, "status", false, null);
		this.title = XMLUtil.getString(dataEl, "title", false);
		this.startTimeFrom = XMLUtil.getString(dataEl, "startTimeFrom", false);
		this.startTimeTo = XMLUtil.getString(dataEl, "startTimeTo", false);
		this.endTimeFrom = XMLUtil.getString(dataEl, "endTimeFrom", false);
		this.endTimeTo = XMLUtil.getString(dataEl, "endTimeTo", false);
		this.themeType = XMLUtil.getInt(dataEl, "themeType", false);
		this.tag = XMLUtil.getString(dataEl, "tag", false);
		this.power = XMLUtil.getString(dataEl, "power", false);
	}

	private String power;
	private String tag;
	private Integer status;
	private String title;
	private String startTimeFrom;
	private String startTimeTo;
	private String endTimeFrom;
	private String endTimeTo;
	private Integer themeType;

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getThemeType() {
		return themeType;
	}

	public void setThemeType(Integer themeType) {
		this.themeType = themeType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartTimeFrom() {
		return startTimeFrom;
	}

	public void setStartTimeFrom(String startTimeFrom) {
		this.startTimeFrom = startTimeFrom;
	}

	public String getStartTimeTo() {
		return startTimeTo;
	}

	public void setStartTimeTo(String startTimeTo) {
		this.startTimeTo = startTimeTo;
	}

	public String getEndTimeFrom() {
		return endTimeFrom;
	}

	public void setEndTimeFrom(String endTimeFrom) {
		this.endTimeFrom = endTimeFrom;
	}

	public String getEndTimeTo() {
		return endTimeTo;
	}

	public void setEndTimeTo(String endTimeTo) {
		this.endTimeTo = endTimeTo;
	}

}
