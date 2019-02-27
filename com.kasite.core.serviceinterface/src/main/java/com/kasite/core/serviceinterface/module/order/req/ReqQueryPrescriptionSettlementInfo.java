package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqQueryPrescriptionSettlementInfo extends AbsReq{

	
	
	/**
	 * 处方号
	 */
	private String prescNo;
	
	/**
	 * his订单号
	 */
	private String hisOrderId;
	
	/**
	 * 用户ID
	 */
	private String memberId;
	

	public ReqQueryPrescriptionSettlementInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		if (dataEl == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}

		this.memberId = XMLUtil.getString(dataEl, "MemberId", false);
		this.prescNo = XMLUtil.getString(dataEl, "PrescNo", false);
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	
	

}
