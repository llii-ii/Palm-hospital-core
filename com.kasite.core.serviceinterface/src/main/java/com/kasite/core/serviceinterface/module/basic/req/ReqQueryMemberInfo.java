/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lcy
 * 查询用户列表入参类
 * @version 1.0 
 * 2017-7-4上午8:51:32
 */
public class ReqQueryMemberInfo extends AbsReq{
	/**
	 * 唯一ID
	 */
	private String memberId;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 卡类型
	 */
	private String cardType;
	
	/**
	 * 用户在渠道的唯一id
	 */
	private String opId;
	
	/**
	 * 是否返回所有用户信息不加密
	 */
	private boolean isReturnAllInfo;
	/**
	 * 是否默认就诊人  1是 
	 */
	private Integer isDefaultMember;
	

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
	 * @return the opId
	 */
	public String getOpId() {
		return opId;
	}
	/**
	 * @param opId the opId to set
	 */
	public void setOpId(String opId) {
		this.opId = opId;
	}
	/**
	 * @return the isReturnAllInfo
	 */
	public boolean isReturnAllInfo() {
		return isReturnAllInfo;
	}
	/**
	 * @param isReturnAllInfo the isReturnAllInfo to set
	 */
	public void setReturnAllInfo(boolean isReturnAllInfo) {
		this.isReturnAllInfo = isReturnAllInfo;
	}
	/**
	 * @return the isDefaultMember
	 */
	public Integer getIsDefaultMember() {
		return isDefaultMember;
	}
	/**
	 * @param isDefaultMember the isDefaultMember to set
	 */
	public void setIsDefaultMember(Integer isDefaultMember) {
		this.isDefaultMember = isDefaultMember;
	}
	
	public ReqQueryMemberInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.memberId = getDataJs().getString("MemberId");
			this.cardNo = getDataJs().getString("CardNo");
			this.cardType = getDataJs().getString("CardType");
			this.opId = getDataJs().getString("OpId");
			this.isDefaultMember = getDataJs().getInteger("IsDefaultMember");
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			this.memberId=XMLUtil.getString(ser, "MemberId", false);
			this.cardNo = XMLUtil.getString(ser, "CardNo", false);
			this.cardType = XMLUtil.getString(ser, "CardType", false);
			this.opId = XMLUtil.getString(ser, "OpId", false,super.getOpenId());
			this.isDefaultMember = XMLUtil.getInt(ser, "IsDefaultMember",null);
		}
		if(StringUtil.isBlank(this.memberId) && StringUtil.isBlank(this.cardNo)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"卡号和成员ID不能同时为空。");
		}
		if(StringUtil.isBlank(this.opId)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数OpId不能为空");
		}
		if(StringUtil.isBlank(this.cardType)) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数卡类型不能为空");
		}
	}
	/**
	 * 
	 * @param msg
	 * @param memberId
	 * @param opId
	 * @param isReturnAllInfo 是否不加密用户信息  true 不加密  false 加密
	 * @throws AbsHosException
	 */
	public ReqQueryMemberInfo(InterfaceMessage msg, String memberId,String cardNo, String cardType,String opId,boolean isReturnAllInfo) throws AbsHosException {
		super(msg);
		this.isReturnAllInfo = isReturnAllInfo;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.opId = opId;
		this.memberId = memberId;
	}
}
