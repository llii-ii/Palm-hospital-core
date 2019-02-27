package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * "<Req><TransactionCode>6010</TransactionCode><Data><CardType>1</CardType><CardNo>A2131091FFFF8115FFFFFFFFFFFFFFFFFFFFFFFFFFD27600000400FFFFFFFFFF</CardNo><IdCardId/><Mobile/><Name/><McardNo/></Data></Req>"
 * @className: ReqCheckEntityCard
 * @author: daiys
 * @date: 2018年10月25日 下午1:45:50
 */
public class ReqCheckEntityCard extends AbsReq{

	private String cardType;
	private String cardNo;
	private String patientName;
	
	
	public String getPatientName() {
		return patientName;
	}


	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public ReqCheckEntityCard(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		this.cardNo = XMLUtil.getString(getData(), "CardNo", true);
		this.cardType = XMLUtil.getString(getData(), "CardType", true);
		this.patientName = XMLUtil.getString(getData(), "Name", false);
	}
	
	
}
