package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;

import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 *  HIS业务处理handler返回参数
 */
public class RespPayEndBizOrderExecute extends AbsResp implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
	
	private BizDealState bizDealState;
	
	private String outBizOrderId;

	public BizDealState getBizDealState() {
		return bizDealState;
	}

	public void setBizDealState(BizDealState bizDealState) {
		this.bizDealState = bizDealState;
	}

	public String getOutBizOrderId() {
		return outBizOrderId;
	}

	public void setOutBizOrderId(String outBizOrderId) {
		this.outBizOrderId = outBizOrderId;
	}
	
	

}
