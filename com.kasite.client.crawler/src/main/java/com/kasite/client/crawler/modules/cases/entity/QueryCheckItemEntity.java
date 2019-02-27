package com.kasite.client.crawler.modules.cases.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: QueryCheckItemEntity
 * @author: lcz
 * @date: 2018年6月11日 下午7:44:44
 */
public class QueryCheckItemEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**对于门诊病人，此标识就是门诊病人的挂号号，对于住院病人，此标识就是住院病人的住院号，对于健康体检人，此标识就是体检号**/
	private String eventNo;
	/**患者就诊类型的的分类名称代码**/
	private String eventType;
	
	/**报告单号**/
	private String reportNo;
	/**项目代码**/
	private String itemNo;
	/**项目名称**/
	private String itemName;
	/**项目内容**/
	private String itemContent;
	/**英文/缩写**/
	private String englishAbbreviation;
	/**检验结果**/
	private String labResult;
	/**正常标志**/
	private String pnFlag;
	/**参考值**/
	private String reference;
	/**单位**/
	private String unit;
	/**微生物名称**/
	private String microorganismName;
	/**培养结果**/
	private String cultureResult;
	/**抗生素中文名称**/
	private String antibioticName;
	/**抗生素定性结果**/
	private String antibioticQualitativeResult;
	/**抗生素定量结果**/
	private String antibioticQuantitativeResult;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =false)
	/**最后修改时间 lastmodify **/
	private String lastmodify;
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
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemContent() {
		return itemContent;
	}
	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}
	public String getEnglishAbbreviation() {
		return englishAbbreviation;
	}
	public void setEnglishAbbreviation(String englishAbbreviation) {
		this.englishAbbreviation = englishAbbreviation;
	}
	public String getLabResult() {
		return labResult;
	}
	public void setLabResult(String labResult) {
		this.labResult = labResult;
	}
	public String getPnFlag() {
		return pnFlag;
	}
	public void setPnFlag(String pnFlag) {
		this.pnFlag = pnFlag;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMicroorganismName() {
		return microorganismName;
	}
	public void setMicroorganismName(String microorganismName) {
		this.microorganismName = microorganismName;
	}
	public String getCultureResult() {
		return cultureResult;
	}
	public void setCultureResult(String cultureResult) {
		this.cultureResult = cultureResult;
	}
	public String getAntibioticName() {
		return antibioticName;
	}
	public void setAntibioticName(String antibioticName) {
		this.antibioticName = antibioticName;
	}
	public String getAntibioticQualitativeResult() {
		return antibioticQualitativeResult;
	}
	public void setAntibioticQualitativeResult(String antibioticQualitativeResult) {
		this.antibioticQualitativeResult = antibioticQualitativeResult;
	}
	public String getAntibioticQuantitativeResult() {
		return antibioticQuantitativeResult;
	}
	public void setAntibioticQuantitativeResult(String antibioticQuantitativeResult) {
		this.antibioticQuantitativeResult = antibioticQuantitativeResult;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
	
	
}
