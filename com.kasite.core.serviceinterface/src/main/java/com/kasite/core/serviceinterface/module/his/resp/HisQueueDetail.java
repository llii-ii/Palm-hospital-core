package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class HisQueueDetail  extends AbsResp{
	/**卡类型*/
	private String cardType;
	/**卡号*/
	private String cardNo;
	/**手机号码*/
	private String phoneNo;
	/**病人名字*/
	private String pateientName;
	/**当前号序*/
	private String nowNo;
	/**就诊日期*/
	private String registerDate;
	/**就诊地点*/
	private String location;

	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	/**就诊时段（0、上午 1、下午）*/
	private String timeId;

	public String getNowNo() {
		return nowNo;
	}
	public void setNowNo(String nowNo) {
		this.nowNo = nowNo;
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
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPateientName() {
		return pateientName;
	}
	public void setPateientName(String pateientName) {
		this.pateientName = pateientName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctoName() {
		return doctoName;
	}
	public void setDoctoName(String doctoName) {
		this.doctoName = doctoName;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getMaxNo() {
		return maxNo;
	}
	public void setMaxNo(String maxNo) {
		this.maxNo = maxNo;
	}
	public String getNextNo() {
		return nextNo;
	}
	public void setNextNo(String nextNo) {
		this.nextNo = nextNo;
	}
	public String getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}
	/**科室名字*/
	private String deptName;
	/**医生名字*/
	private String doctoName;
	/**患者号数*/
	private String no;
	/**最大号序*/
	private String maxNo;
	/**下个号序*/
	private String nextNo;
	/**队列号*/
	private String queueNo;

	

	
}
