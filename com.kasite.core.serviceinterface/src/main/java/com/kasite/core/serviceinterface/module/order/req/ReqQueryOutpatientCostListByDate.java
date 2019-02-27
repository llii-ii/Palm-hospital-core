/**
 * 
 */
package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-11 下午6:31:37
 */
public class ReqQueryOutpatientCostListByDate extends AbsReq{

	private PageVo page;
	/**卡号*/
	private String cardNo;      
	/**卡*/
	private String cardType;     
	/**开始时间*/
	private String beginDate;  
	/**结束时间*/
	private String endDate;     
	
	private String memberId;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Override
	public PageVo getPage() {
		return page;
	}

	@Override
	public void setPage(PageVo page) {
		this.page = page;
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public ReqQueryOutpatientCostListByDate(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element p = ser.element("Page");
		this.cardNo=XMLUtil.getString(ser, "CardNo", false );
		this.cardType=XMLUtil.getString(ser, "CardType", false);
		String beginDate=XMLUtil.getString(ser, "BeginDate", false );
		String endDate = XMLUtil.getString(ser, "EndDate", false);
		this.memberId =  XMLUtil.getString(ser, "MemberId", false);
		this.beginDate=beginDate;
		this.endDate=endDate;
		this.page=new PageVo(p);
	}
	
}
