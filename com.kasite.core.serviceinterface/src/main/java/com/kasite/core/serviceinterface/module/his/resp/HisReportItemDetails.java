package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class HisReportItemDetails  extends AbsResp{
	
	
	private String itemDetalsCode;
	private String itemNo;
	private String itemDetailsName;
	private String resultValue;
	private String unit;
	private String referenceValues;
	private String isNormal;
	private String germName;
	private String exFlag;
	private String range;
	private String words;
	private String growStatus;
	private String seFlag;
	public String getSeFlag() {
		return seFlag;
	}
	public void setSeFlag(String seFlag) {
		this.seFlag = seFlag;
	}
	public String getGermName() {
		return germName;
	}
	public void setGermName(String germName) {
		this.germName = germName;
	}
	public String getExFlag() {
		return exFlag;
	}
	public void setExFlag(String exFlag) {
		this.exFlag = exFlag;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getGrowStatus() {
		return growStatus;
	}
	public void setGrowStatus(String growStatus) {
		this.growStatus = growStatus;
	}
	

	public String getIsNormal() {
		return isNormal;
	}
	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}
	public String getItemDetalsCode() {
		return itemDetalsCode;
	}
	public String getItemNo() {
		return itemNo;
	}
	public String getItemDetailsName() {
		return itemDetailsName;
	}
	public String getResultValue() {
		return resultValue;
	}
	public String getUnit() {
		return unit;
	}
	public String getReferenceValues() {
		return referenceValues;
	}
	public void setItemDetalsCode(String itemDetalsCode) {
		this.itemDetalsCode = itemDetalsCode;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public void setItemDetailsName(String itemDetailsName) {
		this.itemDetailsName = itemDetailsName;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setReferenceValues(String referenceValues) {
		this.referenceValues = referenceValues;
	}
	
	
	
	
	
}
