package com.kasite.core.serviceinterface.module.healthTools.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 检验单解读Update
 * 
 * @author 無
 *
 */
public class ReqCheckListInterpretationUpdate extends AbsReq {
	public ReqCheckListInterpretationUpdate(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(dataEl, "id", true);
		this.itemCode = XMLUtil.getString(dataEl, "itemCode", false);
		this.itemName = XMLUtil.getString(dataEl, "itemName", false);
		this.interpretation = XMLUtil.getString(dataEl, "interpretation", false);
		this.orgCode = XMLUtil.getString(dataEl, "orgCode", false);
		this.status = XMLUtil.getInt(dataEl, "status", false);
	}

	private Long id;
	private String itemCode;
	private String itemName;
	private String interpretation;
	private int status;
	private String orgCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
