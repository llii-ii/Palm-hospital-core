package com.kasite.client.yy.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**预约规则
 * @author lsq
 * version 1.0
 * 2017-7-7下午2:30:12
 */
@Table(name="YY_RULE")
public class YyRule extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private String ruleId;
	private String hosId;
	private Integer startDay;
	private Integer endDay;
	private String startTime;
	private String endTime;
	private String drawPoint;
	private String amTakeNum;
	private String pmTakeNum;
	private Integer breachDay;
	private Integer breachTimes;
	private Integer state;
	private String numberDesc;
	
	public String getHosId() {
		return hosId;
	}
	public Integer getStartDay() {
		return startDay;
	}
	public Integer getEndDay() {
		return endDay;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public String getDrawPoint() {
		return drawPoint;
	}
	public String getAmTakeNum() {
		return amTakeNum;
	}
	public String getPmTakeNum() {
		return pmTakeNum;
	}
	public Integer getBreachDay() {
		return breachDay;
	}
	public Integer getBreachTimes() {
		return breachTimes;
	}
	public Integer getState() {
		return state;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}
	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void setDrawPoint(String drawPoint) {
		this.drawPoint = drawPoint;
	}
	public void setAmTakeNum(String amTakeNum) {
		this.amTakeNum = amTakeNum;
	}
	public void setPmTakeNum(String pmTakeNum) {
		this.pmTakeNum = pmTakeNum;
	}
	public void setBreachDay(Integer breachDay) {
		this.breachDay = breachDay;
	}
	public void setBreachTimes(Integer breachTimes) {
		this.breachTimes = breachTimes;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getNumberDes() {
		return numberDesc;
	}
	public void setNumberDes(String numberDes) {
		this.numberDesc = numberDes;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
