/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**挂号入参
 * @author lsq
 * version 1.0
 * 2017-7-6下午1:59:38
 */
public class ReqAddOrderAndBookService extends AbsReq {
	/**订单号*/
	private String orderId;
	/**操作人id(微信Openid)*/
	private String operatorId;
	/**操作人姓名*/
	private String operatorName;
	
	private String cardNo;
	private String cardType;
	/**用户信息数据**/
	private String memberId;
	
	public ReqAddOrderAndBookService(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(ser, "OrderId", true);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.memberId = XMLUtil.getString(ser, "MemberId", true);
		this.operatorId = XMLUtil.getString(ser, "OperatorId", false,super.getOpenId());;
		this.operatorName = XMLUtil.getString(ser, "OperatorName", false,super.getOperatorName());
	}




	public ReqAddOrderAndBookService(InterfaceMessage msg, String orderId, String operatorId, String operatorName,
			String cardNo, String cardType, String memberId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.cardNo = cardNo;
		this.cardType = cardType;
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
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	
}
