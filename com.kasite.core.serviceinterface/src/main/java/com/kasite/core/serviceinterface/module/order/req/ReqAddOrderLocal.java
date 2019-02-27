package com.kasite.core.serviceinterface.module.order.req;

import java.util.List;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * @author linjianfa
 * @Description: 新增订单API入参
 * @version: V1.0  
 * 2017-7-4 下午8:27:45
 */
public class ReqAddOrderLocal extends AbsReq{

	private String orderId;
	private String prescNo;
	private Integer payMoney;
	private Integer totalMoney;
	private String priceName;
	private String cardNo;
	private String cardType;
	private String operatorId;
	private String operatorName;
	private String serviceId;
	private Integer isOnlinePay;
	private Integer eqptType;
	/**
	 * 订单失效时间： 挂号的时候如果锁号成功，但是未支付有个锁号有效期 10分钟。
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	private String endDate;
	private String orderMemo;
	//信息点相关信息。通过这个id可以从信息点表查询信息  支付场景下信息点相关信息保存在 qr_guide 表
	private String guideOrderId;
	/**用户信息数据**/
	private String memberId;
	private String memberName;
	private Integer sex;
	private String address;
	private String birthdate;
	private String idCardNo;
	private Integer isChildren;
	private String mobile;
	private String hisMemberId;
	/**
	 * 如果有第三方的渠道ID创建的订单 则保存第三方的渠道id  如MiniPay
	 * 如果不传则默认是当前操作人所在的渠道
	 */
	private String channelId;
	private String hosId;
	/**
	 * 是否校验就诊卡
	 * 目前仅限当面付使用，其它场景不支持 也不支持前端调用新增订单的时候传入此参数
	 * */
	private String isCheckCardNo;
	
	public String getIsCheckCardNo() {
		return isCheckCardNo;
	}
	public void setIsCheckCardNo(String isCheckCardNo) {
		this.isCheckCardNo = isCheckCardNo;
	}
	public String getHosId() {
		if(StringUtil.isBlank(hosId)) {
			try {
				hosId = super.getHosId();
			} catch (AbsHosException e) {
				e.printStackTrace();
			}
		}
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) { 
		this.orderId = orderId;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public Integer getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}
	public Integer getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getPriceName() {
		return priceName;
	}
	public void setPriceName(String priceName) {
		this.priceName = priceName;
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
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public Integer getIsOnlinePay() {
		return isOnlinePay;
	}
	public void setIsOnlinePay(Integer isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}
	
	public Integer getEqptType() {
		return eqptType;
	}
	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}
	
	 
	public String getGuideOrderId() {
		return guideOrderId;
	}
	public void setGuideOrderId(String guideOrderId) {
		this.guideOrderId = guideOrderId;
	}
	//	
//	public ReqOrderExtension getReqOrderExtension() {
//		return reqOrderExtension;
//	}
//	public void setReqOrderExtension(ReqOrderExtension reqOrderExtension) {
//		this.reqOrderExtension = reqOrderExtension;
//	}
	public String getOrderMemo() {
		return orderMemo;
	}
	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}
	public ReqAddOrderLocal(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.memberId = XMLUtil.getString(service, "MemberId", false,50);
		this.orderId =  XMLUtil.getString(service, "OrderId", false,32);
		this.prescNo =  XMLUtil.getString(service, "PrescNo", false,50);
		this.payMoney = XMLUtil.getInt(service, "PayMoney", true);
		this.totalMoney = XMLUtil.getInt(service, "TotalMoney", true);
		this.priceName = XMLUtil.getString(service, "PriceName", false,20);
		this.cardNo = XMLUtil.getString(service, "CardNo", false,50);
		this.cardType = XMLUtil.getString(service, "CardType", false,10);		
		this.operatorId = XMLUtil.getString(service, "OperatorId", false,super.getOpenId(),50);
		this.operatorName = XMLUtil.getString(service, "OperatorName", false,super.getOperatorName(),50);
		this.serviceId = XMLUtil.getString(service, "ServiceId", true,10);
		this.isOnlinePay = XMLUtil.getInt(service, "IsOnlinePay", true);
		this.eqptType = XMLUtil.getInt(service, "EqptType", true);
		this.hisMemberId = XMLUtil.getString(service, "HisMemberId", false);
		this.endDate = XMLUtil.getString(service, "EndDate", false);
		
		this.channelId =  XMLUtil.getString(service, "ChannelId", false);
		if(StringUtil.isNotBlank(channelId)) {
			String isOpen = KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, channelId);
			String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, channelId);
			if("false".equals(isOpen)) {
				throw new RRException("渠道:"+clientName+" 未开放【新增订单】请联系管理员。");
			}
		}
		this.hosId = XMLUtil.getString(service, "HosId", false,super.getHosId());
		/**
		 * 信息点相关信息ID 有传后续有需要从此获取没有传不用获取信息
		 */
		this.guideOrderId = XMLUtil.getString(service, "GuideOrderId", false);
		/**
		 * 获取病人相关信息
		 */
		this.memberName = XMLUtil.getString(service, "MemberName", false);
		this.sex = XMLUtil.getInt(service, "Sex", false);
		if(KstHosConstant.I0.equals(sex)) {
			String sexStr = XMLUtil.getString(service, "Sex", false);
			if(StringUtil.isNotBlank(sexStr)) {
				if("男".equals(sexStr)) {
					sex = KstHosConstant.I1;
				}else if("女".equals(sexStr)) {
					sex = KstHosConstant.I2;
				}
			}
		}
		this.idCardNo = XMLUtil.getString(service, "IdCardNo", false);
		this.mobile = XMLUtil.getString(service, "Mobile", false);
		this.isChildren = XMLUtil.getInt(service, "IsChildren", false);
		this.birthdate = XMLUtil.getString(service, "Birthdate", false);
		this.address = XMLUtil.getString(service, "Address", false);

		Element data = service.element(KstHosConstant.DATA_1);
		if(data!=null){
			@SuppressWarnings("unchecked")
			List<Element> ls = data.elements();
			JSONObject json = new JSONObject();
			for (Element e : ls) {
				String name = e.getName();
				json.put(name,e.getText());
			}
			this.orderMemo = json.toJSONString();
		}
		EqptTypeEnum[] ems = EqptTypeEnum.values();
		boolean isT = false;
		for (EqptTypeEnum e : ems) {
			if(eqptType == e.getCode()) {
				isT = true;
				break;
			}
		}
		if(!isT) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"参数 EqptType 设备类型不对，请确认设备类型。EqptType="+eqptType);
		}
		
	}
	/**
	 * @Title: ReqAddOrderLocal
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param prescNo
	 * @param payMoney
	 * @param totalMoney
	 * @param priceName
	 * @param orderMemo
	 * @param cardNo
	 * @param cardType
	 * @param operatorId
	 * @param operatorName
	 * @param serviceId
	 * @param isOnlinePay
	 * @param eqptType
	 * @param merchantType
	 * @param configKey
	 * @param reqOrderExtension
	 * @throws AbsHosException
	 */
	public ReqAddOrderLocal(InterfaceMessage msg, 
			String orderId, String prescNo, Integer payMoney, Integer totalMoney, 
			String priceName,String cardNo, String cardType, String operatorId, 
			String operatorName, String serviceId, Integer isOnlinePay, Integer eqptType,
			JSONObject orderMemo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.prescNo = prescNo;
		this.payMoney = payMoney;
		this.totalMoney = totalMoney;
		this.priceName = priceName;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.serviceId = serviceId;
		this.isOnlinePay = isOnlinePay;
		this.eqptType = eqptType;
		if(null != orderMemo) {
			this.orderMemo = orderMemo.toJSONString();
		}
		try {
			hosId = super.getHosId();
		} catch (AbsHosException e) {
			e.printStackTrace();
		}
	}

	public ReqAddOrderLocal(InterfaceMessage msg, 
			String orderId, String prescNo, Integer payMoney, Integer totalMoney, 
			String priceName,String cardNo, String cardType, String operatorId, 
			String operatorName, String serviceId, Integer isOnlinePay, Integer eqptType,String hisMemberId,
			JSONObject orderMemo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.prescNo = prescNo;
		this.payMoney = payMoney;
		this.totalMoney = totalMoney;
		this.priceName = priceName;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.serviceId = serviceId;
		this.isOnlinePay = isOnlinePay;
		this.eqptType = eqptType;
		if(null != orderMemo) {
			this.orderMemo = orderMemo.toJSONString();
		}
		this.hisMemberId = hisMemberId;
		try {
			hosId = super.getHosId();
		} catch (AbsHosException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param msg
	 * @param orderId
	 * @param prescNo
	 * @param payMoney
	 * @param totalMoney
	 * @param priceName
	 * @param cardNo
	 * @param cardType
	 * @param operatorId
	 * @param operatorName
	 * @param serviceId
	 * @param isOnlinePay
	 * @param eqptType
	 * @param orderMemo
	 * @param memberId
	 * @throws AbsHosException
	 */
	public ReqAddOrderLocal(InterfaceMessage msg, String orderId,String prescNo, Integer payMoney, Integer totalMoney,
			String priceName, String cardNo, String cardType, String operatorId, String operatorName, String serviceId,
			Integer isOnlinePay, Integer eqptType, String memberId,String hisMemberId,JSONObject orderMemo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.prescNo = prescNo;
		this.payMoney = payMoney;
		this.totalMoney = totalMoney;
		this.priceName = priceName;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.serviceId = serviceId;
		this.isOnlinePay = isOnlinePay;
		this.eqptType = eqptType;
		if(null != orderMemo) {
			this.orderMemo = orderMemo.toJSONString();
		}
		this.memberId = memberId;
		this.hisMemberId = hisMemberId;
		try {
			hosId = super.getHosId();
		} catch (AbsHosException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
