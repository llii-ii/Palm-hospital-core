/**
 * 
 */
package com.kasite.client.survey.bean.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-5 下午5:30:57
 */
@Table(name="SV_SAMPLE")
public class Sample extends BaseDbo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**样本id*/
	@Id
	@KeySql(useGeneratedKeys=true)
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
}

