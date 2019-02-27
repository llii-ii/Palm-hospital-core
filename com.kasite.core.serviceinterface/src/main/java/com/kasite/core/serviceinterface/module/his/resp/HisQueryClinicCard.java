package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: HisQueryClinicCard
 * @author: lcz
 * @date: 2018年10月12日 下午6:09:14
 */
public class HisQueryClinicCard extends AbsResp{
	/**his患者ID**/
	private String hisPatientId;
	/**患者名称**/
	private String patientName;
	/**性别**/
	private String sex;
	/**就诊卡号**/
	@NotBlank(message="就诊卡号 clinicCard 不能为空", groups = {AddGroup.class})
	private String clinicCard;
	/**状态  1有效   -1无效**/
	private String status;
	private String idCardId;
	private String mobile;
	private Integer age;
	
	
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the clinicCard
	 */
	public String getClinicCard() {
		return clinicCard;
	}
	/**
	 * @param clinicCard the clinicCard to set
	 */
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	/**
	 * @return the hisPatientId
	 */
	public String getHisPatientId() {
		return hisPatientId;
	}
	/**
	 * @param hisPatientId the hisPatientId to set
	 */
	public void setHisPatientId(String hisPatientId) {
		this.hisPatientId = hisPatientId;
	}
	/**
	 * @return the patientName
	 */
	public String getPatientName() {
		return patientName;
	}
	/**
	 * @param patientName the patientName to set
	 */
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
