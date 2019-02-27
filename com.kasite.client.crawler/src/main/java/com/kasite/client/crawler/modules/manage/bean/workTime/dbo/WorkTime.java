package com.kasite.client.crawler.modules.manage.bean.workTime.dbo;

import java.util.Date;

/**
 * 作业时间数据库对象
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年8月7日 上午17:47:33
 */
public class WorkTime {

	private String id;
	private String hospital_id;
	private Date work_date;
	private Integer report_time;
	private Integer check_time;
	private Integer convert_time;
	private Integer collect_time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHospital_id() {
		return hospital_id;
	}
	public void setHospital_id(String hospital_id) {
		this.hospital_id = hospital_id;
	}
	public Integer getReport_time() {
		return report_time;
	}
	public void setReport_time(Integer report_time) {
		this.report_time = report_time;
	}
	public Integer getCheck_time() {
		return check_time;
	}
	public void setCheck_time(Integer check_time) {
		this.check_time = check_time;
	}
	public Integer getConvert_time() {
		return convert_time;
	}
	public void setConvert_time(Integer convert_time) {
		this.convert_time = convert_time;
	}
	public Integer getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(Integer collect_time) {
		this.collect_time = collect_time;
	}
	public Date getWork_date() {
		return work_date;
	}
	public void setWork_date(Date work_date) {
		this.work_date = work_date;
	}
	
	
	
	
	
}
