package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 创建处方付二维码请求头
 * @author linjf
 * TODO
 */
public class ReqPrescQrValidateBefore extends AbsReq {
	
	private String guideId;
	
	private String idCardNo;
	
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqPrescQrValidateBefore(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.idCardNo = XMLUtil.getString(dataEl, "IdCardNo", true);
		this.guideId = XMLUtil.getString(dataEl, "GuideId", true);
	}


	public String getGuideId() {
		return guideId;
	}


	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}


	public String getIdCardNo() {
		return idCardNo;
	}


	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
}
