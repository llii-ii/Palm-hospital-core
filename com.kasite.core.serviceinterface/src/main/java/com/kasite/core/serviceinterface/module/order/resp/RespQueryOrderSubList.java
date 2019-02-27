package com.kasite.core.serviceinterface.module.order.resp;

import java.sql.Timestamp;
import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryOrderSubList extends AbsResp{
	
	private Long id;
	
 	private String orderId;
 
 	private String subHisOrderId;
 	
 	private String hisRegId;
 	
 	private Integer price;
 	
 	private String priceName;
 	
 	private Timestamp createTime;
 	
 	private Timestamp updateTime;
 	
 	private List<CommonPrescriptionItem> data_1;

	public List<CommonPrescriptionItem> getData_1() {
		return data_1;
	}

	public void setData_1(List<CommonPrescriptionItem> data_1) {
		this.data_1 = data_1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getHisRegId() {
		return hisRegId;
	}

	public void setHisRegId(String hisRegId) {
		this.hisRegId = hisRegId;
	}
 	
 	
}
