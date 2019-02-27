package com.kasite.core.serviceinterface.module.queue.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryLocalQueue
 * @author: lcz
 * @date: 2018年8月6日 上午10:09:15
 */
public class ReqQueryLocalQueue extends AbsReq {
	
	private String queryId;
	/**卡号*/
	private String cardNo; 
	/**卡*/
	private Integer cardType;
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	/**
	 * @Title: ReqQueryLocalQueue
	 * @Description: 
	 * @param msg
	 * @param queryId
	 * @param cardNo
	 * @param cardType
	 * @throws AbsHosException
	 */
	public ReqQueryLocalQueue(InterfaceMessage msg, String queryId, String cardNo, Integer cardType) throws AbsHosException {
		super(msg);
		this.queryId = queryId;
		this.cardNo = cardNo;
		this.cardType = cardType;
	} 
	/**
	 * @Title: ReqQueryLocalQueue
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryLocalQueue(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element data = root.element(KstHosConstant.DATA);
		if (data == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(data, "CardNo", false);
		this.cardType = XMLUtil.getInt(data, "CardType", false);
		this.queryId = XMLUtil.getString(data, "QueryId", false);
	}
	
}
