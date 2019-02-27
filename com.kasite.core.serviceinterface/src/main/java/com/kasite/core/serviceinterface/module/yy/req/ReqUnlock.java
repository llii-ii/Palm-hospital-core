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

/**释号入参
 * @author lsq
 * version 1.0
 * 2017-7-5下午4:56:56
 */
public class ReqUnlock extends AbsReq{
	/**订单号*/
	private String orderId;

	
	public ReqUnlock(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(ser, "OrderId", true);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
}
