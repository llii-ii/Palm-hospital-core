package com.kasite.client.crawler.modules.patient.entity;
import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckCase;
import com.kasite.core.common.validator.PrivateMode;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.common.validator.group.UpdateGroup;
/***
 * 人口学信息 实体对象
 * @author daiyanshui
 *	保存人口学信息相关属性。  新增和更新的时候必需要保证用户ID不能为空
 */
public class PatientEntity {
	@NotBlank(message="用户ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@EntityID
	private String patientId;
	@NotBlank(message="用户姓名不能为空", groups = {AddGroup.class})
	private String name;
	@NotBlank(message="用户证件类型不能为空", groups = {AddGroup.class})
	@CheckCase(value = PrivateMode.certType, message = "medicalType必须是字典值范围内的属性", groups= {AddGroup.class})	
	private String certType;
	@NotBlank(message="用户证件号码不能为空", groups = {AddGroup.class})
	private String certNum;
	@NotBlank(message="身份证号码不能为空", groups = {AddGroup.class})
	private String idCardId;
	@NotBlank(message="出生日期不能为空", groups = {AddGroup.class})
	private String birthday;
	@NotBlank(message="性别", groups = {AddGroup.class})
	private String gender;
	@NotBlank(message="民族", groups = {AddGroup.class})
	private String race;
	/**监护人姓名*/
	private String guardianName;
	/**监护人证件号码*/
	private String guardianIdNo;
	/**监护人证件类型*/
	private String guardianIdType;
	/**监护人性别*/
	private String guardianGender;
	/**监护人出生年月*/
	private String guardianBirthday;
	/**手机号码*/
	private String mobilePhone;
	/**社会保障号*/
	private String ssNum;
	/**过敏史*/
	private String allergies;
	/**个人史*/
	private String socialhistory;
	/**疾病史*/
	private String historyOfpastillness;
	/**现病史*/
	private String historyPresentIllness;
	/**既往史*/
	private String pastDiseaseHistory;
	/**家庭地址**/
	private String homeAddress;
	/**单位名称**/
	private String companyName;
	/**是否参加社保**/
	private String isInSocialSecurityFlg;
	/**何种社保**/
	private String socialSecurityNm;
	
	
	public String getSocialSecurityNm() {
		return socialSecurityNm;
	}
	public void setSocialSecurityNm(String socialSecurityNm) {
		this.socialSecurityNm = socialSecurityNm;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIsInSocialSecurityFlg() {
		return isInSocialSecurityFlg;
	}
	public void setIsInSocialSecurityFlg(String isInSocialSecurityFlg) {
		this.isInSocialSecurityFlg = isInSocialSecurityFlg;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNum() {
		return certNum;
	}
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getGuardianIdNo() {
		return guardianIdNo;
	}
	public void setGuardianIdNo(String guardianIdNo) {
		this.guardianIdNo = guardianIdNo;
	}
	public String getGuardianIdType() {
		return guardianIdType;
	}
	public void setGuardianIdType(String guardianIdType) {
		this.guardianIdType = guardianIdType;
	}
	public String getGuardianGender() {
		return guardianGender;
	}
	public void setGuardianGender(String guardianGender) {
		this.guardianGender = guardianGender;
	}
	public String getGuardianBirthday() {
		return guardianBirthday;
	}
	public void setGuardianBirthday(String guardianBirthday) {
		this.guardianBirthday = guardianBirthday;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getSsNum() {
		return ssNum;
	}
	public void setSsNum(String ssNum) {
		this.ssNum = ssNum;
	}
	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getSocialhistory() {
		return socialhistory;
	}
	public void setSocialhistory(String socialhistory) {
		this.socialhistory = socialhistory;
	}
	public String getHistoryOfpastillness() {
		return historyOfpastillness;
	}
	public void setHistoryOfpastillness(String historyOfpastillness) {
		this.historyOfpastillness = historyOfpastillness;
	}
	public String getHistoryPresentIllness() {
		return historyPresentIllness;
	}
	public void setHistoryPresentIllness(String historyPresentIllness) {
		this.historyPresentIllness = historyPresentIllness;
	}
	public String getPastDiseaseHistory() {
		return pastDiseaseHistory;
	}
	public void setPastDiseaseHistory(String pastDiseaseHistory) {
		this.pastDiseaseHistory = pastDiseaseHistory;
	}
	
	
//	public static void main(String[] args) {
//		PatientEntity t = new PatientEntity();
//		ValidatorUtils.validateEntity(t, UpdateGroup.class);
//	}
}
