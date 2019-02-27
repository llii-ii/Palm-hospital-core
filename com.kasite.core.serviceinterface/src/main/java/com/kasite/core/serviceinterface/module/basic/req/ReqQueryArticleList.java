package com.kasite.core.serviceinterface.module.basic.req;


import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 文章入参类
 * 
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-20 下午5:59:42
 **/
public class ReqQueryArticleList extends AbsReq {
	private String startDate;
	private String endDate;
	private String hosID;
	private String typeClass;
	private Integer status;
	private String id;
	private Integer isHead;
	private String title;

	public ReqQueryArticleList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hosID = XMLUtil.getString(ser, "HosId", false,super.getHosId());
		this.typeClass = XMLUtil.getString(ser, "TypeClass", false);
		this.status = XMLUtil.getInt(ser, "Status", false);
		this.id = XMLUtil.getString(ser, "ID", false);
	}
	
	public ReqQueryArticleList(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.startDate = getDataJs().getString("StartDate");
			this.endDate = getDataJs().getString("EndDate");
			this.hosID = super.getHosId();
			this.id = getDataJs().getString("ArticleId");
			this.title = getDataJs().getString("Title");
			this.typeClass = getDataJs().getString("TypeClass");
			this.status = getDataJs().getInteger("Status");
			this.isHead = getDataJs().getInteger("IsHead");
		}
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getHosID() {
		return hosID;
	}

	public void setHosID(String hosID) {
		this.hosID = hosID;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getIsHead() {
		return isHead;
	}

	public void setIsHead(Integer isHead) {
		this.isHead = isHead;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
