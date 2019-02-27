package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.dao.vo.AbsRespObject;

/**
 * 会议-设备管理RESP
 * 
 * @author 無
 *
 */
public class RespMeetingEquipment extends AbsRespObject {

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 设备名称
	 */
	private String name;

	/**
	 * 状态 默认0=正常 1=删除
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
