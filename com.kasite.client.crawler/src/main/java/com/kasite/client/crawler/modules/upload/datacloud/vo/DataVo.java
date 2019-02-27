package com.kasite.client.crawler.modules.upload.datacloud.vo;

/**
 * 原始数据集合
 * @author daiyanshui
 *
 */
public class DataVo {
	private String inner_version;
	private String patient_id;
	private String event_no;
	private String org_code;
	private String event_time;
	private String create_date;
	private String code;
	
	public DataVo(String code,String create_date,String event_time,String patient_id) {
		this.code = code;
		this.create_date = create_date;
		this.event_time = event_time;
		this.patient_id = patient_id;
	}
//	public void addData(JSONObject obj) {
//		if(null == data) {
//			data = new JSONArray();
//		}
//		data.put(obj);
//	}
//	
//	public JSONArray getData() {
//		return data;
//	}
//
//	public void setData(JSONArray data) {
//		this.data = data;
//	}

	public String getInner_version() {
		return inner_version;
	}
	public void setInner_version(String inner_version) {
		this.inner_version = inner_version;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public String getEvent_no() {
		return event_no;
	}
	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
