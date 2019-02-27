package com.kasite.core.serviceinterface.module.rf.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 运营状况-趋势走向数据响应实体类
 * 
 * @author zhaoy
 *
 */
public class RespQueryStdReport extends AbsResp {

	private Double total;
	
	private String rQ;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getrQ() {
		return rQ;
	}

	public void setrQ(String rQ) {
		this.rQ = rQ;
	}
	
}
