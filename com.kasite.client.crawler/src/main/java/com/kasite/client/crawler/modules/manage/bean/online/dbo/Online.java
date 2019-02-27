package com.kasite.client.crawler.modules.manage.bean.online.dbo;

import java.util.Date;

/**
 * 设备在线情况数据库对象
 * 
 * @author 無
 * @version V1.0
 * @date 2018年6月21日 上午11:47:33
 */
public class Online {
	
	private String id; //编号
	private String name; //设备名称
	private String state; //当前状态
	private String folder; //文件夹
	private String url; //路径
	private Date insert_date; //插入时间
	private Date last_online_date; //最后一次上线时间
	private String belong;
	
	
	//查询条件
	private String municipal; //市
	private String county; //县区
	private String startTime; //开始时间
	private String endTime; //结束时间

	
	private Date online_break_date;
	private String equipment_id;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLast_online_date() {
		return last_online_date;
	}

	public void setLast_online_date(Date last_online_date) {
		this.last_online_date = last_online_date;
	}

	public Date getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(Date insert_date) {
		this.insert_date = insert_date;
	}

	public Date getOnline_break_date() {
		return online_break_date;
	}

	public void setOnline_break_date(Date online_break_date) {
		this.online_break_date = online_break_date;
	}

	public String getEquipment_id() {
		return equipment_id;
	}

	public void setEquipment_id(String equipment_id) {
		this.equipment_id = equipment_id;
	}

	public String getMunicipal() {
		return municipal;
	}

	public void setMunicipal(String municipal) {
		this.municipal = municipal;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	
}
