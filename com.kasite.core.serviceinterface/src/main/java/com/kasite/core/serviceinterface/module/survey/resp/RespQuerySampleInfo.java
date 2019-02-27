package com.kasite.core.serviceinterface.module.survey.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 满意度调查-统计-答案详情
 * 
 * @author zhaoy
 *
 */
public class RespQuerySampleInfo extends AbsResp {

	private Integer sampleId;

	private Integer subjectId;
	private String casehistoryId;
	private String userName;
	private Integer sex;
	private Integer age;
	private String moblie;
	private String deptName;
	private String doctorName;
	private Timestamp treatDate;
	private String treatres;
	private Integer status;
	private Integer trackflag;
	private Integer careUser;
	private String hospId;
	private String hospName;
	private Timestamp sendTime;

	/**其他的信息*/
	private String other;   

	/**电话或或者手机唯一标准符*/
	private String phoneOrPc;
	/**ip 地址*/
	private String ip;
	
	private Integer totalProperty;
	
	private Timestamp opertime;
	
	private String sampleName;
	
	public Integer getSampleId() {
		return sampleId;
	}
	
	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	
	public Integer getSubjectId() {
		return subjectId;
	}
	
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	
	public String getCasehistoryId() {
		return casehistoryId;
	}
	
	public void setCasehistoryId(String casehistoryId) {
		this.casehistoryId = casehistoryId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Integer getSex() {
		return sex;
	}
	
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getMoblie() {
		return moblie;
	}
	
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public Timestamp getTreatDate() {
		return treatDate;
	}
	
	public void setTreatDate(Timestamp treatDate) {
		this.treatDate = treatDate;
	}
	
	public String getTreatres() {
		return treatres;
	}
	
	public void setTreatres(String treatres) {
		this.treatres = treatres;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getTrackflag() {
		return trackflag;
	}
	
	public void setTrackflag(Integer trackflag) {
		this.trackflag = trackflag;
	}
	
	public Integer getCareUser() {
		return careUser;
	}
	
	public void setCareUser(Integer careUser) {
		this.careUser = careUser;
	}
	
	public String getHospId() {
		return hospId;
	}
	
	public void setHospId(String hospId) {
		this.hospId = hospId;
	}
	
	public String getHospName() {
		return hospName;
	}
	
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	
	public Timestamp getSendTime() {
		return sendTime;
	}
	
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getOther() {
		return other;
	}
	
	public void setOther(String other) {
		this.other = other;
	}
	
	public String getPhoneOrPc() {
		return phoneOrPc;
	}
	
	public void setPhoneOrPc(String phoneOrPc) {
		this.phoneOrPc = phoneOrPc;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getTotalProperty() {
		return totalProperty;
	}
	
	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

	public Timestamp getOpertime() {
		return opertime;
	}

	public void setOpertime(Timestamp opertime) {
		this.opertime = opertime;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	} 
	
}
