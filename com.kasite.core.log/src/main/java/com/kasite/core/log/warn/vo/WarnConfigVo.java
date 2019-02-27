package com.kasite.core.log.warn.vo;

/**
 * 
 */



public class WarnConfigVo {
	public String warnObject;
	public String warnType;
	public int warnValue;
	
	public String getWarnObject() {
		return warnObject;
	}
	public void setWarnObject(String warnObject) {
		this.warnObject = warnObject;
	}
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	public int getWarnValue() {
		return warnValue;
	}
	public void setWarnValue(int warnValue) {
		this.warnValue = warnValue;
	}
	
	
	@Override
	public String toString() {
		return "WarnConfigVo [warnObject=" + warnObject + ", warnType="
				+ warnType + ", warnValue=" + warnValue + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if(this == obj){
			return true;
		}
		if(obj instanceof WarnConfigVo){
			WarnConfigVo vo = (WarnConfigVo)obj;
			if(vo.warnObject.equals(this.warnObject) && vo.warnType.equals(this.warnType) && vo.warnValue==this.warnValue){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		if(this.warnValue != 0){
			return this.warnObject.hashCode() * this.warnType.hashCode() * this.warnValue;
		}
		return this.warnObject.hashCode() * this.warnType.hashCode();
	}
	
}
