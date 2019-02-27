package com.kasite.core.serviceinterface.module.qywechat.dbo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 会议室管理 This class was generated by MyBatis Generator. This class corresponds
 * to the database table QY_MEETING_ROOM
 */
@Table(name = "QY_MEETING_ROOM")
public class MeetingRoom implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;

	/**
	 * 会议室名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 可容纳人数
	 */
	@Column(name = "ACCOMMODATE")
	private Integer accommodate;

	/**
	 * 地址
	 */
	@Column(name = "PLACE")
	private String place;

	/**
	 * 设备ID 多个用逗号分隔
	 */
	@Column(name = "EQUIPMENTID")
	private String equipmentid;

	/**
	 * 状态 默认0=开放 1=停用
	 */
	@Column(name = "STATUS")
	private Integer status;

	/**
	 * 插入时间
	 */
	@Column(name = "INSERTTIME")
	private String inserttime;

	/**
	 * 修改时间
	 */
	@Column(name = "UPDATETIME")
	private String updatetime;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table QY_MEETING_ROOM
	 *
	 * @mbg.generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.ID
	 *
	 * @return the value of QY_MEETING_ROOM.ID
	 *
	 * @mbg.generated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.ID
	 *
	 * @param id the value for QY_MEETING_ROOM.ID
	 *
	 * @mbg.generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.NAME
	 *
	 * @return the value of QY_MEETING_ROOM.NAME
	 *
	 * @mbg.generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.NAME
	 *
	 * @param name the value for QY_MEETING_ROOM.NAME
	 *
	 * @mbg.generated
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.ACCOMMODATE
	 *
	 * @return the value of QY_MEETING_ROOM.ACCOMMODATE
	 *
	 * @mbg.generated
	 */
	public Integer getAccommodate() {
		return accommodate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.ACCOMMODATE
	 *
	 * @param accommodate the value for QY_MEETING_ROOM.ACCOMMODATE
	 *
	 * @mbg.generated
	 */
	public void setAccommodate(Integer accommodate) {
		this.accommodate = accommodate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.PLACE
	 *
	 * @return the value of QY_MEETING_ROOM.PLACE
	 *
	 * @mbg.generated
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.PLACE
	 *
	 * @param place the value for QY_MEETING_ROOM.PLACE
	 *
	 * @mbg.generated
	 */
	public void setPlace(String place) {
		this.place = place == null ? null : place.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.EQUIPMENTID
	 *
	 * @return the value of QY_MEETING_ROOM.EQUIPMENTID
	 *
	 * @mbg.generated
	 */
	public String getEquipmentid() {
		return equipmentid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.EQUIPMENTID
	 *
	 * @param equipmentid the value for QY_MEETING_ROOM.EQUIPMENTID
	 *
	 * @mbg.generated
	 */
	public void setEquipmentid(String equipmentid) {
		this.equipmentid = equipmentid == null ? null : equipmentid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.STATUS
	 *
	 * @return the value of QY_MEETING_ROOM.STATUS
	 *
	 * @mbg.generated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.STATUS
	 *
	 * @param status the value for QY_MEETING_ROOM.STATUS
	 *
	 * @mbg.generated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.INSERTTIME
	 *
	 * @return the value of QY_MEETING_ROOM.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public String getInserttime() {
		return inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.INSERTTIME
	 *
	 * @param inserttime the value for QY_MEETING_ROOM.INSERTTIME
	 *
	 * @mbg.generated
	 */
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column QY_MEETING_ROOM.UPDATETIME
	 *
	 * @return the value of QY_MEETING_ROOM.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column QY_MEETING_ROOM.UPDATETIME
	 *
	 * @param updatetime the value for QY_MEETING_ROOM.UPDATETIME
	 *
	 * @mbg.generated
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table QY_MEETING_ROOM
	 *
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", accommodate=").append(accommodate);
		sb.append(", place=").append(place);
		sb.append(", equipmentid=").append(equipmentid);
		sb.append(", status=").append(status);
		sb.append(", inserttime=").append(inserttime);
		sb.append(", updatetime=").append(updatetime);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}