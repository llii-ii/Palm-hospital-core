package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 会议-设备管理req
 * 
 * @author 無
 *
 */
public class ReqMeetingEquipment extends AbsReq {

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
	private String insertTime;

	/**
	 * 修改时间
	 */
	private String updateTime;

	public ReqMeetingEquipment(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(dataEl, "id", false);
		this.name = XMLUtil.getString(dataEl, "name", false);
		this.insertTime = XMLUtil.getString(dataEl, "insertTime", false);
		this.updateTime = XMLUtil.getString(dataEl, "updateTime", false);
		this.status = XMLUtil.getInt(dataEl, "status", false);
	}

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

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
