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
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-26 下午2:17:11 查询医嘱订单详情入参
 **/
public class ReqQueryOrderPrescriptionInfo extends AbsReq {
	/**
	 * 用户成员唯一ID
	 */
	private String memberId;
	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	private String cardType;
	/** 订单id */
	private String hisOrderId;
	/** 处方单号 */
	private String prescNo;
	/**
	 * 自定义参数节点，内容不要超过200个字符串
	 */
	private JSONObject data_1;

	public ReqQueryOrderPrescriptionInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(service, "CardNo", false);
		this.cardType = XMLUtil.getString(service, "CardType", false);
		this.hisOrderId = XMLUtil.getString(service, "HisOrderId", false);
		this.prescNo = XMLUtil.getString(service, "PrescNo", false);
		this.memberId = XMLUtil.getString(service, "MemberId", false);
		if( StringUtil.isEmpty(hisOrderId) && StringUtil.isEmpty(prescNo)) {
			throw new ParamException("传入参数中[HisOrderId][PrescNo]节点不能同时为空。");
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

	public JSONObject getData_1() {
		return data_1;
	}

	public void setData_1(JSONObject data_1) {
		this.data_1 = data_1;
	}
	
	

}
