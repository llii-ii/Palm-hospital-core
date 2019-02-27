package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQueryOperationInfo extends AbsReq{

	private String hospitalId;
	private String orgCode;
	
	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public ReqQueryOperationInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.hospitalId=XMLUtil.getString(ser, "hospitalId", true);
		this.orgCode=XMLUtil.getString(ser, "orgCode", true);
	}

}
