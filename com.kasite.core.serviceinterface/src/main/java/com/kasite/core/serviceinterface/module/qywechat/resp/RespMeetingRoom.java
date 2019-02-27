package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 会议室RESP
 * 
 * @author 無
 *
 */
public class RespMeetingRoom extends AbsResp {

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 会议室名称
	 */
	private String name;

	/**
	 * 可容纳人数
	 */
	private Integer accommodate;

	/**
	 * 地址
	 */
	private String place;

	/**
	 * 设备ID 多个用逗号分隔
	 */
	private String equipmentid;

	/**
	 * 状态 默认0=开放 1=停用
	 */
	private Integer status;

	/**
	 * 插入时间
	 */
	private String inserttime;

	/**
	 * 修改时间
	 */
	private String updatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAccommodate() {
		return accommodate;
	}

	public void setAccommodate(Integer accommodate) {
		this.accommodate = accommodate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEquipmentid() {
		return equipmentid;
	}

	public void setEquipmentid(String equipmentid) {
		this.equipmentid = equipmentid;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
