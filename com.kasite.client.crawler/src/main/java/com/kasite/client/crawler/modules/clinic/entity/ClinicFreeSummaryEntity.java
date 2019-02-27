package com.kasite.client.crawler.modules.clinic.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

public class ClinicFreeSummaryEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**门（急）诊号 clinicNum **/
	@NotBlank(message="门（急）诊号 clinicNum 不能为空")
	private String clinicNum;
	/**就诊流水号 medicalNum **/
	@NotBlank(message="就诊流水号 medicalNum 不能为空")
	private String medicalNum;
	/**单据号 receiptNum **/
	@NotBlank(message="单据号 receiptNum 不能为空", groups = {AddGroup.class})
	@EntityID
	private String receiptNum;
	/**门诊费用金额 fee **/
	@NotBlank(message="门诊费用金额 fee 不能为空", groups = {AddGroup.class})
	private String fee;
	@CheckCurrency(message="社保扣除金额 insuranceDeduct 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**社保扣除金额 insuranceDeduct **/
	private Integer insuranceDeduct;
	@CheckCurrency(message="起付线 underwayCriterion 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**起付线 underwayCriterion **/
	private Integer underwayCriterion;
	@CheckCurrency(message="基本医疗赔付金额 baseInsurance 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**基本医疗赔付金额 baseInsurance **/
	private Integer baseInsurance;
	@CheckCurrency(message="补充医疗赔付金额 complementarityInsurance 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**补充医疗赔付金额 complementarityInsurance **/
	private Integer complementarityInsurance;
	@CheckCurrency(message="自付金额 selfPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**自付金额 selfPay **/
	private Integer selfPay;
	@CheckCurrency(message="自费金额 selfFee 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**自费金额 selfFee **/
	private Integer selfFee;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class})
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	/**经办人**/
	private String updateBy;
	
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getClinicNum() {
		return clinicNum;
	}
	public void setClinicNum(String clinicNum) {
		this.clinicNum = clinicNum;
	}
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public Integer getInsuranceDeduct() {
		return insuranceDeduct;
	}
	public void setInsuranceDeduct(Integer insuranceDeduct) {
		this.insuranceDeduct = insuranceDeduct;
	}
	public Integer getUnderwayCriterion() {
		return underwayCriterion;
	}
	public void setUnderwayCriterion(Integer underwayCriterion) {
		this.underwayCriterion = underwayCriterion;
	}
	public Integer getBaseInsurance() {
		return baseInsurance;
	}
	public void setBaseInsurance(Integer baseInsurance) {
		this.baseInsurance = baseInsurance;
	}
	public Integer getComplementarityInsurance() {
		return complementarityInsurance;
	}
	public void setComplementarityInsurance(Integer complementarityInsurance) {
		this.complementarityInsurance = complementarityInsurance;
	}
	public Integer getSelfPay() {
		return selfPay;
	}
	public void setSelfPay(Integer selfPay) {
		this.selfPay = selfPay;
	}
	public Integer getSelfFee() {
		return selfFee;
	}
	public void setSelfFee(Integer selfFee) {
		this.selfFee = selfFee;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}




	
}
