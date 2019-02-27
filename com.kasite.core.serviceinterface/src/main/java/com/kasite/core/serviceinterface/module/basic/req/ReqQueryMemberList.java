/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
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
public class ReqQueryMemberList extends AbsReq{
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
	 * 渠道Id
	 */
	private String channelId;
	
	/**
	 * 住院号
	 */
	private String hospitalNo;
	
	private String memberName;
	
	private String memberNameLike;
	
	private String idCardNo;
	
	private String hosId;
	/**
	 * 是否返回所有用户信息不加密
	 */
	private boolean isReturnAllInfo;
	
	private Integer isDefaultMember;
	
	private String hisMemberId;
	
	
	
	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
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

	public boolean isReturnAllInfo() {
		return isReturnAllInfo;
	}

	public void setReturnAllInfo(boolean isReturnAllInfo) {
		this.isReturnAllInfo = isReturnAllInfo;
	}

	public String getMemberNameLike() {
		return memberNameLike;
	}

	public void setMemberNameLike(String memberNameLike) {
		this.memberNameLike = memberNameLike;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

	public ReqQueryMemberList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.memberId = getDataJs().getString("MemberId");
			this.cardNo = getDataJs().getString("CardNo");
			this.cardType = getDataJs().getString("CardType");
			this.opId = getDataJs().getString("OpId");
			this.channelId = getDataJs().getString("ChannelId");
			this.hospitalNo = getDataJs().getString("HospitalNo");
			this.memberName = getDataJs().getString("MemberName");
			this.memberNameLike = getDataJs().getString("MemberNameLike");
			this.idCardNo = getDataJs().getString("IdCardNo");
			this.hosId = getDataJs().getString("HosId");
			this.hisMemberId = getDataJs().getString("HisMemberId");
			this.isDefaultMember = getDataJs().getInteger("IsDefaultMember");
		}else {
			Element ser = root.element(KstHosConstant.DATA);
			this.memberId=XMLUtil.getString(ser, "MemberId", false);
			this.hisMemberId=XMLUtil.getString(ser, "HisMemberId", false);
			this.cardNo = XMLUtil.getString(ser, "CardNo", false);
			this.cardType = XMLUtil.getString(ser, "CardType", false);
			this.opId = XMLUtil.getString(ser, "OpId", false,super.getOpenId());
			this.channelId= XMLUtil.getString(ser, "ChannelId", false,super.getClientId());
			this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", false);
			this.memberName=XMLUtil.getString(ser, "MemberName", false);
			this.memberNameLike=XMLUtil.getString(ser, "MemberNameLike", false);
			this.idCardNo=XMLUtil.getString(ser, "IdCardNo", false);
			this.hosId = XMLUtil.getString(ser, "HosId", false);
			this.isDefaultMember = XMLUtil.getInt(ser, "IsDefaultMember",null);
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
	public ReqQueryMemberList(InterfaceMessage msg, String memberId,String cardNo, String cardType,String opId) throws AbsHosException {
		super(msg);
		this.isReturnAllInfo = true;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.opId = opId;
		this.memberId = memberId;
	}
	/**
	 * 
	 * @param msg
	 * @param memberId
	 * @param cardNo
	 * @param cardType
	 * @param opId
	 * @param channelId
	 * @param hospitalNo
	 * @param memberName
	 * @param idCardNo
	 * @param isReturnAllInfo 是否不加密用户信息  true 不加密  false 加密
	 * @throws AbsHosException
	 */
	public ReqQueryMemberList(InterfaceMessage msg, String memberId, String cardNo, String cardType, String opId,
			String channelId, String hospitalNo,String memberName,String idCardNo,boolean isReturnAllInfo) throws AbsHosException {
		super(msg);
		this.memberId = memberId;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.opId = opId;
		this.channelId = channelId;
		this.hospitalNo = hospitalNo;
		this.memberName = memberName;
		this.idCardNo = idCardNo;
		this.isReturnAllInfo = isReturnAllInfo;
	}
	
	public ReqQueryMemberList(InterfaceMessage msg, String memberId, String cardNo, String cardType, String opId,
			String channelId, String hospitalNo, String memberName, String memberNameLike, String idCardNo,
			String hosId, boolean isReturnAllInfo, Integer isDefaultMember, String hisMemberId) throws AbsHosException {
		super(msg);
		this.memberId = memberId;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.opId = opId;
		this.channelId = channelId;
		this.hospitalNo = hospitalNo;
		this.memberName = memberName;
		this.memberNameLike = memberNameLike;
		this.idCardNo = idCardNo;
		this.hosId = hosId;
		this.isReturnAllInfo = isReturnAllInfo;
		this.isDefaultMember = isDefaultMember;
		this.hisMemberId = hisMemberId;
	}

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

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
