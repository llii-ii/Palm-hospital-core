package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqCopyPurpose extends AbsReq{

	private String id;//复印内容id
	private String name;//复印内容名称
	private String sort;//排序
	private String state;//状态0删除1正常
	private String contentIds;//内容id多个以逗号隔开
	private String enableDel;//能否删除0-否1-能
	private String enableShow;//是否显示0-否1-是
	
	public ReqCopyPurpose(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.sort=XMLUtil.getString(ser, "sort", false);
		this.state=XMLUtil.getString(ser, "state", false);
		this.contentIds=XMLUtil.getString(ser, "contentIds", false);
		this.enableDel=XMLUtil.getString(ser, "enableDel", false);
		this.enableShow=XMLUtil.getString(ser, "enableShow", false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContentIds() {
		return contentIds;
	}

	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}

	public String getEnableDel() {
		return enableDel;
	}

	public void setEnableDel(String enableDel) {
		this.enableDel = enableDel;
	}

	public String getEnableShow() {
		return enableShow;
	}

	public void setEnableShow(String enableShow) {
		this.enableShow = enableShow;
	}
	
	

}
