package com.kasite.core.serviceinterface.module.order.req;

import java.util.List;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqAddOrderPrescription extends AbsReq{

	private String hisOrderId;
	
	private String prescNo;
	
	private String hosId;
	
	private Integer payMoney;
	
	private Integer totalMoney;
	
	private String operatorId;
	
	private String operatorName;
	
	private Integer eqptType;
	
	private String oldOrderId;
	
	private String memberId;
	
	private String cardNo;
	
	private String cardType;
	
	/**
	 * 自定义参数节点，内容不要超过200个字符串
	 */
	private JSONObject data_1;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddOrderPrescription(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.hisOrderId =  XMLUtil.getString(service, "HisOrderId", false);
		this.prescNo =  XMLUtil.getString(service, "PrescNo", false);
		this.payMoney = XMLUtil.getInt(service, "PayMoney", true);
		this.totalMoney = XMLUtil.getInt(service, "TotalMoney", true);		
		this.operatorId = XMLUtil.getString(service, "OperatorId", true,super.getOpenId());
		this.operatorName = XMLUtil.getString(service, "OperatorName", true,super.getOperatorName());
		this.eqptType = XMLUtil.getInt(service, "EqptType", true);
		this.oldOrderId = XMLUtil.getString(service, "OldOrderId", false);
		this.memberId = XMLUtil.getString(service, "MemberId", false);
		this.cardNo = XMLUtil.getString(service, "CardNo", false);
		this.cardType = XMLUtil.getString(service, "CardType", false);
		if( StringUtil.isEmpty(hisOrderId) && StringUtil.isEmpty(prescNo)) {
			throw new ParamException("[HisOrderId][PrescNo]不能同时为空！");
		}
		Element data = service.element(KstHosConstant.DATA_1);
		if(data!=null){
			@SuppressWarnings("unchecked")
			List<Element> ls = data.elements();
			JSONObject json = new JSONObject();
			for (Element e : ls) {
				String name = e.getName();
				json.put(name,e.getText());
			}
			this.data_1=json;
		}
		
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

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
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

	public Integer getEqptType() {
		return eqptType;
	}

	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public JSONObject getData_1() {
		return data_1;
	}

	public void setData_1(JSONObject data_1) {
		this.data_1 = data_1;
	}
	
	
}
