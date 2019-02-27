package com.kasite.core.serviceinterface.module.basic.req;


import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 查询本地医院入参
 * 
 * @author mhd
 * @version 1.0 2017-7-17 下午3:44:01
 */
public class ReqQueryHospitalLocal extends AbsReq {
	/**
	 * 住院号
	 */
	private String hosId;

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public ReqQueryHospitalLocal(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType() == 0) {
			this.hosId = getDataJs().getString("HosId");
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			if (ser == null) {
				throw new ParamException("传入参数中[Data]节点不能为空。");
			}
			this.hosId = XMLUtil.getString(ser, "HosId", false,super.getHosId());
		}
	}

	/**
	 * @Title: ReqQueryHospitalLocal
	 * @Description: 
	 * @param msg
	 * @param hosId
	 * @throws AbsHosException
	 */
	public ReqQueryHospitalLocal(InterfaceMessage msg, String hosId) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		if(StringUtil.isEmpty(hosId)) {
			this.hosId = super.getHosId();
		}
	}
	
	
}
