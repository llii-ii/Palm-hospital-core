package com.kasite.client.crawler.modules.manage.bean.report.dbo;

import java.util.Date;

/**
 * 数据占比数据库对象
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月25日 上午11:47:33
 */
public class Report {
	
	private String id; 
	private String hospital_id;
	private String hospital_name;
	private String business_id;
	private String business_name;
	private Date date;
	private Integer report_true;
	private Integer report_false;
	private Integer check_num;
	private Integer convert_num;	
	
	
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
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getReport_true() {
		return report_true;
	}
	public void setReport_true(Integer report_true) {
		this.report_true = report_true;
	}
	public Integer getReport_false() {
		return report_false;
	}
	public void setReport_false(Integer report_false) {
		this.report_false = report_false;
	}
	public Integer getCheck_num() {
		return check_num;
	}
	public void setCheck_num(Integer check_num) {
		this.check_num = check_num;
	}
	public Integer getConvert_num() {
		return convert_num;
	}
	public void setConvert_num(Integer convert_num) {
		this.convert_num = convert_num;
	}
	@Override
	public String toString() {
		return "Report [id=" + id + ", hospital_id=" + hospital_id + ", hospital_name=" + hospital_name
				+ ", business_id=" + business_id + ", business_name=" + business_name + ", date=" + date
				+ ", report_true=" + report_true + ", report_false=" + report_false + ", check_num=" + check_num
				+ ", convert_num=" + convert_num + "]";
	}
	

	
	
	
}
