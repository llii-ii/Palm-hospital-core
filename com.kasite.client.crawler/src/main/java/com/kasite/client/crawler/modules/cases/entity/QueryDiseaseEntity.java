package com.kasite.client.crawler.modules.cases.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: QueryDiseaseEntity
 * @author: lcz
 * @date: 2018年6月11日 下午7:44:02
 */
public class QueryDiseaseEntity {
	
	
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**对于门诊病人，此标识就是门诊病人的挂号号，对于住院病人，此标识就是住院病人的住院号，对于健康体检人，此标识就是体检号**/
	private String eventNo;
	/**患者就诊类型的的分类名称代码**/
	private String eventType;
	/**疾病代码**/
	private String diseaseCode;
	/**疾病名称**/
	private String diseaseName;
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getDiseaseCode() {
		return diseaseCode;
	}
	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	
	
	
}
