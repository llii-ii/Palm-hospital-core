package com.kasite.core.serviceinterface.module.his.resp;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;

public class HisQueryOrderReceiptList extends AbsResp{

	/**
	 * 收据名称
	 */
	@NotBlank(message="收据名称[receiptName]  不能为空", groups = {AddGroup.class})
	private String receiptName;
	
	/**
	 * 结算状态1已结算0未结算
	 */
	@NotBlank(message="结算状态[isSettlement]  不能为空", groups = {AddGroup.class})
	private Integer isSettlement;
	
	/**
	 * 收据时间,yyy-MM-dd hh:mm:ss
	 */
	@NotBlank(message="收据时间[receiptTime]  不能为空", groups = {AddGroup.class})
	private String receiptTime;
	
	/**
	 * 收据总金额
	 */
	@CheckCurrency(message="收据总金额[totalPrice]  不能为空", groups = {AddGroup.class})
	private Integer totalPrice;
	
	/**
	 * 收据号
	 */
	@NotBlank(message="收据号[receiptNo]  不能为空", groups = {AddGroup.class})
	private String receiptNo;
	/**
	 * 挂号id
	 */
	private String regId;
	
	
	private List<CommonPrescriptionItem> itemList;
	

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	
	

	public Integer getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Integer isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public List<CommonPrescriptionItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<CommonPrescriptionItem> itemList) {
		this.itemList = itemList;
	}
	
	
}
