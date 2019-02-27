package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;


public class HisQuerySubOrderPrescriptionList extends AbsResp{
	
	@NotBlank(message="his子订单[subHisOrderId]  不能为空", groups = {AddGroup.class})
	private String subHisOrderId;
	
	@CheckCurrency(message="费用[price]  不能为空", groups = {AddGroup.class})
	private Integer price;
	
	@NotBlank(message="费用名称[priceName]  不能为空", groups = {AddGroup.class})
	private String priceName;

	public String getSubHisOrderId() {
		return subHisOrderId;
	}

	public void setSubHisOrderId(String subHisOrderId) {
		this.subHisOrderId = subHisOrderId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}
	
	
	
}
