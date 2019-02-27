package com.kasite.core.serviceinterface.module.yy.req;

import java.io.Serializable;
import java.util.List;

public class ReqUpdateRuleAndLimitVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ruleId;
	
	private Integer startDay;
	
	private String startTime;
	
	private Integer endDay;
	
	private String endTime;
	
	private String amTakeNum;
	
	private String pmTakeNum;
	
	private Integer breachDay;
	
	private Integer breachTimes;
	
	private Integer state;
	
	private String drawPoint;
	
	private String transActionCode;
	
	private String limits_string;
	
	private String numberDes;
	
	private List<ReqYyLimit> limits;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAmTakeNum() {
		return amTakeNum;
	}

	public void setAmTakeNum(String amTakeNum) {
		this.amTakeNum = amTakeNum;
	}

	public String getPmTakeNum() {
		return pmTakeNum;
	}

	public void setPmTakeNum(String pmTakeNum) {
		this.pmTakeNum = pmTakeNum;
	}

	public Integer getBreachDay() {
		return breachDay;
	}

	public void setBreachDay(Integer breachDay) {
		this.breachDay = breachDay;
	}

	public Integer getBreachTimes() {
		return breachTimes;
	}

	public void setBreachTimes(Integer breachTimes) {
		this.breachTimes = breachTimes;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDrawPoint() {
		return drawPoint;
	}

	public void setDrawPoint(String drawPoint) {
		this.drawPoint = drawPoint;
	}

	public String getTransActionCode() {
		return transActionCode;
	}

	public void setTransActionCode(String transActionCode) {
		this.transActionCode = transActionCode;
	}

	public String getLimits_string() {
		return limits_string;
	}

	public void setLimits_string(String limits_string) {
		this.limits_string = limits_string;
	}

	public String getNumberDes() {
		return numberDes;
	}

	public void setNumberDes(String numberDes) {
		this.numberDes = numberDes;
	}

	public List<ReqYyLimit> getLimits() {
		return limits;
	}

	public void setLimits(List<ReqYyLimit> limits) {
		this.limits = limits;
	}
}
