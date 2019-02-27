/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-12 下午5:18:28
 */
public class HisQueryOutpatientCostList extends AbsResp{
 	/**清单日期*/
	@NotBlank(message="日期 date 不能为空", groups = {AddGroup.class})
 	private String date;
	/**金额*/
	@CheckCurrency(message="费用 fee 不能为空", groups = {AddGroup.class})
	private Integer fee;      
	/**医生*/
	private String doctor;      
	/**科室*/
	private String dept;        
	/**科室位置*/
	private String deptStation; 
	
	
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptStation() {
		return deptStation;
	}
	public void setDeptStation(String deptStation) {
		this.deptStation = deptStation;
	}

}
