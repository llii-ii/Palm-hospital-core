/**
 * 
 */
package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-12 上午11:51:34
 */
public class ReqQueryInHospitalCostType extends AbsReq{

	/**卡号*/
	private String cardNo;      
	/**卡*/
	private String cardType;       
	/**开始时间*/
	private String beginDate;   
	/**结束时间*/
	private String endDate;     
	
	private String memberId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public ReqQueryInHospitalCostType(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo=XMLUtil.getString(ser, "CardNo", false );
		this.cardType=XMLUtil.getString(ser, "CardType", false);
		this.beginDate=XMLUtil.getString(ser, "BeginDate", false );
		this.endDate = XMLUtil.getString(ser, "EndDate", false);
		this.memberId = XMLUtil.getString(ser, "MemberId", false);
	}

}
