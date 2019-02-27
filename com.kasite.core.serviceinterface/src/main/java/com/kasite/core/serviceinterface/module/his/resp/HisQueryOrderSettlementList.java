package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;

/**
 *@author caiyouhong
 *@version 1.0 
 *@time 2017-7-26 下午2:32:30 
 **/
public class HisQueryOrderSettlementList  extends AbsResp{
	
	/**
	 * 处方时间（yyyy-MM-dd HH:mm:ss）
	 */
	@NotBlank(message="处方时间[prescTime]不能为空", groups = {AddGroup.class})
	private String prescTime;
	
	/**
	 * 处方金额（分）
	 */
	@CheckCurrency(message="处方金额[price]不能为空", groups = {AddGroup.class})
	private Integer price;
	
	/**
	 * 是否结算0未结算1已结算
	 */
	@CheckCurrency(message="是否已结算[isSettlement]不能为空", groups = {AddGroup.class})
	private Integer isSettlement;
	
	/**
	 * His处方订单号
	 */
	@NotBlank(message="his订单号[hisOrderId]不能为空", groups = {AddGroup.class})
	private String hisOrderId;
	
	/**
	 * 处方号
	 */
	private String prescNo;
	
	/**
	 * 处方类型名称
	 */
	@NotBlank(message=" 处方类型名称[prescType]不能为空", groups = {AddGroup.class})
	private String prescType;
	
	/**
	 * 处方医生
	 */
	@NotBlank(message=" 处方医生[doctorName]不能为空", groups = {AddGroup.class})
	private String doctorName;
	
	/**
	 * 处方医生所属科室
	 */
	@NotBlank(message="处方医生所属科室[deptName]不能为空", groups = {AddGroup.class})
	private String deptName;

	public String getPrescTime() {
		return prescTime;
	}

	public void setPrescTime(String prescTime) {
		this.prescTime = prescTime;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Integer isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getPrescType() {
		return prescType;
	}

	public void setPrescType(String prescType) {
		this.prescType = prescType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

		

}
