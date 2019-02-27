package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqCopyContent extends AbsReq{


	private String id;//复印内容id
	private String name;//复印内容名称
	private String sort;//排序
	private String type;//必选类型:1慢病申报2继续治疗3.报销4.自留(多个以逗号隔开）
	private String isDefault;//是否默认 0否1是
	private String enableDel;//能否删除0-否1-能
	private String enableShow;//是否显示0-否1-是
	private String purposeId;//复印用途id


	
	
	public ReqCopyContent(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.sort=XMLUtil.getString(ser, "sort", false);
		this.type=XMLUtil.getString(ser, "type", false);
		this.isDefault=XMLUtil.getString(ser, "isDefault", false);
		this.enableDel=XMLUtil.getString(ser, "enableDel", false);
		this.enableShow=XMLUtil.getString(ser, "enableShow", false);
		this.purposeId=XMLUtil.getString(ser, "purposeId", false);
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




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public String getIsDefault() {
		return isDefault;
	}




	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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




	public String getPurposeId() {
		return purposeId;
	}




	public void setPurposeId(String purposeId) {
		this.purposeId = purposeId;
	}


	
}
