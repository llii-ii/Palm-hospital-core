package com.kasite.core.serviceinterface.module.his.req;

import java.util.Map;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqHisMemberAutoUnbind extends AbsReq{

	private Map<String, String> paramMap;
	private String memberId;
	private String cardType;
	private String cardNo;
	
	public ReqHisMemberAutoUnbind(InterfaceMessage msg,String memberId,String cardType,String cardNo,
			Map<String, String> paramMap) throws AbsHosException {
		super(msg);
		this.paramMap = paramMap;
		this.memberId = memberId;
		this.cardType = cardType;
		this.cardNo = cardNo;
	}




	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}




	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}




	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}




	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}




	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}




	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}




	public Map<String, String> getParamMap() {
		return paramMap;
	}



	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

}
