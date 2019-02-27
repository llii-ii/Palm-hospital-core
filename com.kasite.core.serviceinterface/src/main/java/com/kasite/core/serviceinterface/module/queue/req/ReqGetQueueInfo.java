/**
 * 
 */
package com.kasite.core.serviceinterface.module.queue.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqGetQueueInfo extends AbsReq {

	/**卡号*/
	private String cardNo; 
	/**卡*/
	private int cardType; 



	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public ReqGetQueueInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);

		if (ser == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		// Element p = ser.element("Page");
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.cardType = XMLUtil.getInt(ser, "CardType", false);
		// this.page = new PageVo(p);
	}

}
