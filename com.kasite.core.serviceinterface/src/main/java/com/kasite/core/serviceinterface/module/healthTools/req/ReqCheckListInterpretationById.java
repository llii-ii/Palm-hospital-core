package com.kasite.core.serviceinterface.module.healthTools.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 检验单解读QueryById
 * 
 * @author 無
 *
 */
public class ReqCheckListInterpretationById extends AbsReq {
	public ReqCheckListInterpretationById(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(dataEl, "id", true);
		this.orgCode = XMLUtil.getString(dataEl, "orgCode", true);
	}

	private Long id;
	private String orgCode;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
