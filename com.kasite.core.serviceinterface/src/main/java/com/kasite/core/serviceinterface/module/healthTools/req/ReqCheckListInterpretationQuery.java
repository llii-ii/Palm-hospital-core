package com.kasite.core.serviceinterface.module.healthTools.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 检验单解读Query
 * 
 * @author 無
 *
 */
public class ReqCheckListInterpretationQuery extends AbsReq {
	public ReqCheckListInterpretationQuery(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.itemCode = XMLUtil.getString(dataEl, "itemCode", false);
		this.itemName = XMLUtil.getString(dataEl, "itemName", false);
		this.status = XMLUtil.getInt(dataEl, "status", false);
		this.orgCode = XMLUtil.getString(dataEl, "orgCode", false);
	}

	private String itemCode;
	private String itemName;
	private int status;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
}
