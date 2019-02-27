package com.kasite.client.crawler.modules.clinic.entity;
import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckCase;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.PrivateMode;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.common.validator.group.UpdateGroup;

/***
 * 门诊挂号信息
 * @author daiyanshui
 *	门诊挂号信息
 */
public class ClinicRegWaterEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**门（急）诊号 clinicNum **/
	@NotBlank(message="门（急）诊号 clinicNum 不能为空", groups = {AddGroup.class})
	private String clinicNum;
	/**就诊流水号 medicalNum **/
	@NotBlank(message="就诊流水号 medicalNum 不能为空", groups = {AddGroup.class})
	@EntityID
	private String medicalNum;
	/**就诊方式 medicalType **/
	@NotBlank(message="就诊方式 medicalType 不能为空", groups = {AddGroup.class})
	@CheckCase(value = PrivateMode.medicalType, message = "就诊方式必须是字典值范围内的属性", 
	groups= {AddGroup.class,UpdateGroup.class})	
	private String medicalType;
	@CheckDate(message="就诊日期时间 clinicDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**就诊日期时间 clinicDate **/
	@NotBlank(message="就诊日期时间 clinicDate 不能为空", groups = {AddGroup.class})
	private String clinicDate;
	/**就诊时段 timeId **/
	private String timeId;
	/**科室 ID departmentId **/
	private String departmentId;
	/**科室名称 departmentName **/
	@NotBlank(message="科室名称 departmentName 不能为空", groups = {AddGroup.class})
	private String departmentName;
	/**医生ID doctorId **/
	private String doctorId;
	/**医生姓名 doctorName **/
	@NotBlank(message="医生姓名 doctorName 不能为空", groups = {AddGroup.class})
	private String doctorName;
	@CheckCurrency(message="诊疗费 medicalExpenses 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**诊疗费 medicalExpenses **/
	private Integer medicalExpenses;
	/**就诊卡号 clinicCard **/
	private String clinicCard;
	@CheckCurrency(message="挂号费 ghFree 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**挂号费 ghFree **/
	private Integer ghFree;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class})
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	/**经办人**/
	private String updateBy;
	/**经办日期**/
	private String updateDate;
	
	
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getClinicDate() {
		return clinicDate;
	}
	public void setClinicDate(String clinicDate) {
		this.clinicDate = clinicDate;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Integer getMedicalExpenses() {
		return medicalExpenses;
	}
	public void setMedicalExpenses(Integer medicalExpenses) {
		this.medicalExpenses = medicalExpenses;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public Integer getGhFree() {
		return ghFree;
	}
	public void setGhFree(Integer ghFree) {
		this.ghFree = ghFree;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}


	  
//	public static void main(String[] args) {
//		RegWaterEntity t = new RegWaterEntity();
//		t.setClinicCard("1");
//		t.setPatientId("1");
//		t.setDepartmentName("1");
//		t.setDoctorName("1");
//		t.setClinicDate("1");
//		t.setMedicalType("12");
//		t.setGhFree("123");
//		t.setClinicCard("11");
//		t.setClinicNum("1");
//		t.setMedicalNum("xx");
//		t.setMedicalExpenses("123");
//		ValidatorUtils.validateEntity(t,UpdateGroup.class);
//	}
}
