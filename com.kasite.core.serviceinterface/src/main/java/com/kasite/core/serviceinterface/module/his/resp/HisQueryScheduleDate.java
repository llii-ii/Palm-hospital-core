/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**取消预约出参
 * @author lsq
 * version 1.0
 * 2017-7-7下午3:00:04
 */
public class HisQueryScheduleDate  extends AbsResp{
	private String bookDate;//可预约日期
	private String weekDay;//星期
	private String haveNo;////是否可预约 1：可预约

	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getHaveNo() {
		return haveNo;
	}
	public void setHaveNo(String haveNo) {
		this.haveNo = haveNo;
	}
	/**返回信息*/
	private String respCode;
	private String respMessage;

	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
}
