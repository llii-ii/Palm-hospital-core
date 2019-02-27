package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 危急值req
 * 
 * @author 無
 *
 */
public class ReqWarnValue extends AbsReq {
	/**
	 * 主键ID 自增
	 */
	private Long id;
	/**
	 * lis系统报告单号
	 */
	private String lisOrderid;
	/**
	 * 病区
	 */
	private String ward;
	/**
	 * 病床
	 */
	private String bedNo;
	/**
	 * 住院号
	 */
	private int inHospitalNo;
	/**
	 * 患者姓名
	 */
	private String name;
	/**
	 * 检查项目
	 */
	private String checkIteam;
	/**
	 * 参考值
	 */
	private String reference;
	/**
	 * 结果
	 */
	private String result;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 主治医生工号 多个逗号分隔
	 */
	private String doctorCode;
	/**
	 * 状态 0=未读 1=已读
	 */
	private String status;
	/**
	 * 插入时间
	 */
	private String insertTime;
	/**
	 * 修改时间
	 */
	private String updateTime;

	public ReqWarnValue(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(ser, "id", true);
		this.lisOrderid = XMLUtil.getString(ser, "lisOrderid", false);
		this.ward = XMLUtil.getString(ser, "ward", false);
		this.bedNo = XMLUtil.getString(ser, "bedNo", false);
		this.inHospitalNo = XMLUtil.getInt(ser, "inHospitalNo", false);
		this.name = XMLUtil.getString(ser, "name", false);
		this.checkIteam = XMLUtil.getString(ser, "checkIteam", false);
		this.result = XMLUtil.getString(ser, "result", false);
		this.reference = XMLUtil.getString(ser, "reference", false);
		this.remark = XMLUtil.getString(ser, "remark", false);
		this.doctorCode = XMLUtil.getString(ser, "doctorCode", false);
		this.status = XMLUtil.getString(ser, "status", false);
		this.insertTime = XMLUtil.getString(ser, "insertTime", false);
		this.updateTime = XMLUtil.getString(ser, "updateTime", false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLisOrderid() {
		return lisOrderid;
	}

	public void setLisOrderid(String lisOrderid) {
		this.lisOrderid = lisOrderid;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public int getInHospitalNo() {
		return inHospitalNo;
	}

	public void setInHospitalNo(int inHospitalNo) {
		this.inHospitalNo = inHospitalNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheckIteam() {
		return checkIteam;
	}

	public void setCheckIteam(String checkIteam) {
		this.checkIteam = checkIteam;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ReqWarnValue [id=" + id + ", lisOrderid=" + lisOrderid + ", ward=" + ward + ", bedNo=" + bedNo
				+ ", inHospitalNo=" + inHospitalNo + ", name=" + name + ", checkIteam=" + checkIteam + ", reference="
				+ reference + ", remark=" + remark + ", doctorCode=" + doctorCode + ", status=" + status
				+ ", insertTime=" + insertTime + ", updateTime=" + updateTime + "]";
	}

}
