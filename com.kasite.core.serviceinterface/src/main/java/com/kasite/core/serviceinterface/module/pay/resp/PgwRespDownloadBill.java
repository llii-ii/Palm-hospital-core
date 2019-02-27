package com.kasite.core.serviceinterface.module.pay.resp;

import java.util.List;

import com.kasite.core.serviceinterface.module.pay.bo.MchBill;
import com.yihu.hos.IRetCode;

/**
 * @author linjf
 * 支付网关-统一下单出参
 * 某些渠道的特殊出参字段，请注释清楚
 */
public class PgwRespDownloadBill {

	private IRetCode respCode;
	
	private String respMsg;
	
	private List<MchBill> mchBillList;
	
	
	
	public IRetCode getRespCode() {
		return respCode;
	}

	public void setRespCode(IRetCode respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public List<MchBill> getMchBillList() {
		return mchBillList;
	}

	public void setMchBillList(List<MchBill> mchBillList) {
		this.mchBillList = mchBillList;
	}
	
	
	
}
