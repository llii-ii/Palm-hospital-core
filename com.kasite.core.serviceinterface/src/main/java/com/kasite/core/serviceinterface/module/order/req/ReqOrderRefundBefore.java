package com.kasite.core.serviceinterface.module.order.req;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lq
 * @Description: API，订单取消
 * @version: V1.0  
 * 2017-7-11 下午13:58:57
 */
public class ReqOrderRefundBefore extends AbsReq{
	 public ReqOrderRefundBefore(InterfaceMessage msg,ReqOrderIsCancel reqOrderIsCancel) throws AbsHosException {
		super(msg);
		this.reqOrderIsCancel = reqOrderIsCancel;
	}
	private ReqOrderIsCancel reqOrderIsCancel;
	 public ReqOrderIsCancel getReqOrderIsCancel() {
		return reqOrderIsCancel;
	}
	 public void setReqOrderIsCancel(ReqOrderIsCancel reqOrderIsCancel) {
		this.reqOrderIsCancel = reqOrderIsCancel;
	}
}
