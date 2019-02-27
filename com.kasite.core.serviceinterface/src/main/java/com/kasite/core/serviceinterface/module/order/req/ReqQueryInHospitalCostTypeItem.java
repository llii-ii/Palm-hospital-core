package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
    
public class ReqQueryInHospitalCostTypeItem extends AbsReq {

	private String expenseTypeCode;
	
	private String date;
	
	private String cardNo;
	
	private String cardType;
	
	private String memberId;

	public ReqQueryInHospitalCostTypeItem(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.expenseTypeCode = XMLUtil.getString(service, "ExpenseTypeCode", false);
		this.date = XMLUtil.getString(service, "Date", false);
		this.cardNo = XMLUtil.getString(service, "CardNo", false);
		this.cardType = XMLUtil.getString(service, "CardType", false);
		this.memberId = XMLUtil.getString(service, "MemberId", false); 
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getExpenseTypeCode() {
		return expenseTypeCode;
	}

	public void setExpenseTypeCode(String expenseTypeCode) {
		this.expenseTypeCode = expenseTypeCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
