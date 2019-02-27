package com.kasite.core.serviceinterface.module.report.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetReportInfo
 * @author: lcz
 * @date: 2018年7月25日 下午4:59:01
 */
public class RespGetTjReportItemDetail extends AbsResp{

	private String itemDetailTitle;//检查项目名称
	private String resultVal;//结果值
	private String unit;//单位
	private String referenceValues;//参考值
	private String isNormal;//是否正常 -1 偏低  0正常  1偏高
	public String getItemDetailTitle() {
		return itemDetailTitle;
	}
	public void setItemDetailTitle(String itemDetailTitle) {
		this.itemDetailTitle = itemDetailTitle;
	}
	public String getResultVal() {
		return resultVal;
	}
	public void setResultVal(String resultVal) {
		this.resultVal = resultVal;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getReferenceValues() {
		return referenceValues;
	}
	public void setReferenceValues(String referenceValues) {
		this.referenceValues = referenceValues;
	}
	public String getIsNormal() {
		return isNormal;
	}
	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}
	
	
	
}
