package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqGetAppointReceiptInfo extends AbsReq{

	private String hisKey;
	

	public String getHisKey() {
		return hisKey;
	}

	public void setHisKey(String hisKey) {
		this.hisKey = hisKey;
	}
	public ReqGetAppointReceiptInfo(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.hisKey = XMLUtil.getString(ser, "HisKey", true);
	}


	
	
	
	
}
