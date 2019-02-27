package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**预约信息查询入参
 * @author lsq
 * version 1.0
 * 2017-7-10下午2:12:29
 */
public class ReqQueryRegInfo extends AbsReq{
	/**证件类型*/
	private String cardType;
	/**证件号*/
	private String cardNo;
	/**
	 * 用户成员
	 */
	private String memberId;
	/**身份证号码*/
	private String idCardNo;
	/**订单号*/
	private String orderId;
	/**就诊卡号*/
	private String clinicCard;
	/**就诊时段*/
	private String timeSlice;
	/**就诊日期开始*/
	private String startTime;
	/**就诊日期结束*/
	private String endTime;
	/**1-预约 2-当天挂号*/
	private Integer regFlag;


	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public String getTimeSlice() {
		return timeSlice;
	}
	public void setTimeSlice(String timeSlice) {
		this.timeSlice = timeSlice;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getRegFlag() {
		return regFlag;
	}
	public void setRegFlag(Integer regFlag) {
		this.regFlag = regFlag;
	}
	public ReqQueryRegInfo(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", false);
		this.orderId = XMLUtil.getString(ser, "OrderId", false);
		this.clinicCard = XMLUtil.getString(ser, "ClinicCard", false);
		this.timeSlice = XMLUtil.getString(ser, "TimeSlice", false);
		this.startTime = XMLUtil.getString(ser, "StartTime", false);
		this.endTime = XMLUtil.getString(ser, "EndTime", false);
		this.regFlag = XMLUtil.getInt(ser, "RegFlag", false);
	}
	/**
	 * @Title: ReqQueryRegInfo
	 * @Description: 
	 * @param msg
	 * @param cardType
	 * @param cardNo
	 * @param idCardNo
	 * @param orderId
	 * @param clinicCard
	 * @param timeSlice
	 * @param startTime
	 * @param endTime
	 * @param regFlag
	 * @throws AbsHosException
	 */
	public ReqQueryRegInfo(InterfaceMessage msg, String cardType, String cardNo, String idCardNo, String orderId, String clinicCard, String timeSlice, String startTime, String endTime, Integer regFlag) throws AbsHosException {
		super(msg);
		this.cardType = cardType;
		this.cardNo = cardNo;
		this.idCardNo = idCardNo;
		this.orderId = orderId;
		this.clinicCard = clinicCard;
		this.timeSlice = timeSlice;
		this.startTime = startTime;
		this.endTime = endTime;
		this.regFlag = regFlag;
	}
	
	
}
