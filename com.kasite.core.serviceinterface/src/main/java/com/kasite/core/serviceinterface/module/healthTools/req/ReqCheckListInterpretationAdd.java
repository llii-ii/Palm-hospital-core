package com.kasite.core.serviceinterface.module.healthTools.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 检验单解读Add
 * 
 * @author 無
 *
 */
public class ReqCheckListInterpretationAdd extends AbsReq {
	public ReqCheckListInterpretationAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.itemCode = XMLUtil.getString(dataEl, "itemCode", true);
		this.itemName = XMLUtil.getString(dataEl, "itemName", true);
		this.interpretation = XMLUtil.getString(dataEl, "interpretation", true);
		this.orgCode = XMLUtil.getString(dataEl, "orgCode", true);
	}

	private String itemCode;
	private String itemName;
	private String interpretation;
	private String orgCode;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
}
