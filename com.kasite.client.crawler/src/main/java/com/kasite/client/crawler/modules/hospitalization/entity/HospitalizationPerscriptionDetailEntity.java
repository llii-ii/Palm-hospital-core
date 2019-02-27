package com.kasite.client.crawler.modules.hospitalization.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.config.CheckDictBuser;
import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.group.AddGroup;

public class HospitalizationPerscriptionDetailEntity {
	/**就诊流水号**/
	private String medicalNum;
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**住院号 inHospitalNum **/
	@NotBlank(message="住院号 inHospitalNum 不能为空", groups = {AddGroup.class})
	private String inHospitalNum;
	/**单据号 receiptNum **/
	@NotBlank(message="单据号 receiptNum 不能为空", groups = {AddGroup.class})
	private String receiptNum;
	@CheckDict(inf=CheckDictBuser.class,type="hospitalChargeCode",isNotNull = true,message="住院费用分类代码 “hospitalChargeCode” 字典值不合法",groups=AddGroup.class)
	/**处方流水号 recipeSerialNum **/
	@NotBlank(message="处方流水号 recipeSerialNum 不能为空", groups = {AddGroup.class})
	@EntityID
	private String recipeSerialNum;
	/**住院费用分类代码 hospitalChargeCode **/
	private String hospitalChargeCode;
	/**住院费用分类名称 hospitalChargeName **/
	@NotBlank(message="住院费用分类名称 hospitalChargeName 不能为空", groups = {AddGroup.class})
	private String hospitalChargeName;
	/**医保收费项目编码 ssItemCode **/
	@NotBlank(message="医保收费项目编码 ssItemCode 不能为空", groups = {AddGroup.class})
	private String ssItemCode;
	/**医保收费项目名称 ssItemName **/
	@NotBlank(message="医保收费项目名称 ssItemName 不能为空", groups = {AddGroup.class})
	private String ssItemName;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="扣费时间 eventTime 数据格式不正确", groups = {AddGroup.class}, isNotNull =true)
	/**扣费时间 eventTime **/
	private String eventTime;
	@CheckDict(inf=CheckDictBuser.class,type="medItemCtg",isNotNull = true,message="医疗项目类别 “medItemCtg” 字典值不合法",groups=AddGroup.class)
	/**医疗项目类别 medItemCtg **/
	private String medItemCtg;
	/**项目代码 itemCode **/
	@NotBlank(message="项目代码 itemCode 不能为空", groups = {AddGroup.class})
	private String itemCode;
	/**项目名称 itemName **/
	@NotBlank(message="项目名称 itemName 不能为空", groups = {AddGroup.class})
	private String itemName;
	@CheckCurrency(message="单价 price 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**单价 price **/
	private Integer price;
	/**数量 size **/
	@NotBlank(message="数量 size 不能为空", groups = {AddGroup.class})
	private String size;
	@CheckCurrency(message="金额 amount 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**金额 amount **/
	private Integer amount;
	/**规格 spec **/
	private String spec;
	/**单位 unit **/
	private String unit;
	/**自付比例 selfPayRatio **/
	@NotBlank(message="自付比例 selfPayRatio 不能为空", groups = {AddGroup.class})
	private String selfPayRatio;
	@CheckCurrency(message="自付金额 selfPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**自付金额 selfPay **/
	private Integer selfPay;
	@CheckCurrency(message="自费金额 selfFee 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**自费金额 selfFee **/
	private Integer selfFee;
	/**合规标识 compliantSign **/
	@NotBlank(message="合规标识 compliantSign 不能为空", groups = {AddGroup.class})
	private String compliantSign;
	@CheckCurrency(message="合规金额 compliantPrice 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**合规金额 compliantPrice **/
	private Integer compliantPrice;
	@CheckDict(inf=CheckDictBuser.class,type="SettlementType",isNotNull = true,message="费用支付代码 “SettlementType” 字典值不合法",groups=AddGroup.class)
	/**费用支付代码 SettlementType **/
	private String SettlementType;
	/**经办人 updateBy **/
	@NotBlank(message="经办人 updateBy 不能为空", groups = {AddGroup.class})
	private String updateBy;
	/**项目序号 itemNo **/
	@NotBlank(message="项目序号 itemNo 不能为空", groups = {AddGroup.class})
	private String itemNo;
	@CheckDict(inf=CheckDictBuser.class,type="itemLevel",isNotNull = true,message="项目等级 “itemLevel” 字典值不合法",groups=AddGroup.class)
	/**项目等级 itemLevel **/
	private String itemLevel;
	/**单次用量 singleUseSize **/
	private String singleUseSize;
	/**最小剂量单位 minDosageUnit **/
	private String minDosageUnit;
	/**每天次数 everyDaySize **/
	private String everyDaySize;
	/**用药天数 useDaySize **/
	private String useDaySize;
	@CheckDict(inf=CheckDictBuser.class,type="isSingleRemedy",isNotNull = false,message="单复方标志 “isSingleRemedy” 字典值不合法",groups=AddGroup.class)
	/**单复方标志 isSingleRemedy **/
	private String isSingleRemedy;
	@CheckDict(inf=CheckDictBuser.class,type="isPrescription",isNotNull = false,message="是否处方标识 “isPrescription” 字典值不合法",groups=AddGroup.class)
	/**是否处方标识 isPrescription **/
	private String isPrescription;
	/**中草药贴数 herbalSize **/
	private String herbalSize;
	@CheckDict(inf=CheckDictBuser.class,type="drugDeliverType",isNotNull = false,message="给药方式 “drugDeliverType” 字典值不合法",groups=AddGroup.class)
	/**给药方式 drugDeliverType **/
	private String drugDeliverType;
	/**适应症或主治功能 doctorFunction **/
	private String doctorFunction;
	/**禁忌 taboo **/
	private String taboo;
	@CheckDict(inf=CheckDictBuser.class,type="isSeriousDrugs",isNotNull = false,message="限制用药标识 “isSeriousDrugs” 字典值不合法",groups=AddGroup.class)
	/**限制用药标识 isSeriousDrugs **/
	private String isSeriousDrugs;
	/**限制用药 seriousDrugs **/
	private String seriousDrugs;
	/**基药标识 drugFlag **/
	private String drugFlag;
	/**国药准字号 chineseMedicineNo **/
	private String chineseMedicineNo;
	/**注册证号 registerNo **/
	private String registerNo;
	/**品牌 brand **/
	private String brand;
	/**项目英文名 projectEnglishName **/
	private String projectEnglishName;
	/**保内保外 insurantInnerOut **/
	private String insurantInnerOut;
	/**生产厂家 manufacturer **/
	private String manufacturer;
	@CheckCurrency(message="医保限价 medlimitedPrice 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**医保限价 medlimitedPrice **/
	private Integer medlimitedPrice;
	/**检查提示 examinationHint **/
	private String examinationHint;
	/**正常参考值 referenceRanges **/
	private String referenceRanges;
	/**检查结果 examinationResult **/
	private String examinationResult;
	@CheckDict(inf=CheckDictBuser.class,type="extraRecipeFlg",isNotNull = false,message="外带处方标志 “extraRecipeFlg” 字典值不合法",groups=AddGroup.class)
	/**外带处方标志 extraRecipeFlg **/
	private String extraRecipeFlg;
	@CheckCurrency(message="医院负担费用 hosBearMoney 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**医院负担费用 hosBearMoney **/
	private Integer hosBearMoney;
	/**成分 constituent **/
	private String constituent;
	/**执行科室名称 departmentName **/
	@NotBlank(message="执行科室名称 departmentName 不能为空", groups = {AddGroup.class})
	private String departmentName;
	/**执行科室代码 departmentCode **/
	private String departmentCode;
	/**医生ID doctorId **/
	private String doctorId;
	/**处方医生姓名 doctorName **/
	@NotBlank(message="处方医生姓名 doctorName 不能为空", groups = {AddGroup.class})
	private String doctorName;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =false)
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	/**处方日期**/
	private String recipeDate;
	
	
	public String getRecipeDate() {
		return recipeDate;
	}
	public void setRecipeDate(String recipeDate) {
		this.recipeDate = recipeDate;
	}
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getInHospitalNum() {
		return inHospitalNum;
	}
	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getHospitalChargeCode() {
		return hospitalChargeCode;
	}
	public void setHospitalChargeCode(String hospitalChargeCode) {
		this.hospitalChargeCode = hospitalChargeCode;
	}
	public String getHospitalChargeName() {
		return hospitalChargeName;
	}
	public void setHospitalChargeName(String hospitalChargeName) {
		this.hospitalChargeName = hospitalChargeName;
	}
	public String getSsItemCode() {
		return ssItemCode;
	}
	public void setSsItemCode(String ssItemCode) {
		this.ssItemCode = ssItemCode;
	}
	public String getSsItemName() {
		return ssItemName;
	}
	public void setSsItemName(String ssItemName) {
		this.ssItemName = ssItemName;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getMedItemCtg() {
		return medItemCtg;
	}
	public void setMedItemCtg(String medItemCtg) {
		this.medItemCtg = medItemCtg;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSelfPayRatio() {
		return selfPayRatio;
	}
	public void setSelfPayRatio(String selfPayRatio) {
		this.selfPayRatio = selfPayRatio;
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
	public String getCompliantSign() {
		return compliantSign;
	}
	public void setCompliantSign(String compliantSign) {
		this.compliantSign = compliantSign;
	}
	public Integer getCompliantPrice() {
		return compliantPrice;
	}
	public void setCompliantPrice(Integer compliantPrice) {
		this.compliantPrice = compliantPrice;
	}
	public String getSettlementType() {
		return SettlementType;
	}
	public void setSettlementType(String settlementType) {
		SettlementType = settlementType;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemLevel() {
		return itemLevel;
	}
	public void setItemLevel(String itemLevel) {
		this.itemLevel = itemLevel;
	}
	public String getSingleUseSize() {
		return singleUseSize;
	}
	public void setSingleUseSize(String singleUseSize) {
		this.singleUseSize = singleUseSize;
	}
	public String getMinDosageUnit() {
		return minDosageUnit;
	}
	public void setMinDosageUnit(String minDosageUnit) {
		this.minDosageUnit = minDosageUnit;
	}
	public String getEveryDaySize() {
		return everyDaySize;
	}
	public void setEveryDaySize(String everyDaySize) {
		this.everyDaySize = everyDaySize;
	}
	public String getUseDaySize() {
		return useDaySize;
	}
	public void setUseDaySize(String useDaySize) {
		this.useDaySize = useDaySize;
	}
	public String getIsSingleRemedy() {
		return isSingleRemedy;
	}
	public void setIsSingleRemedy(String isSingleRemedy) {
		this.isSingleRemedy = isSingleRemedy;
	}
	public String getIsPrescription() {
		return isPrescription;
	}
	public void setIsPrescription(String isPrescription) {
		this.isPrescription = isPrescription;
	}
	public String getHerbalSize() {
		return herbalSize;
	}
	public void setHerbalSize(String herbalSize) {
		this.herbalSize = herbalSize;
	}
	public String getDrugDeliverType() {
		return drugDeliverType;
	}
	public void setDrugDeliverType(String drugDeliverType) {
		this.drugDeliverType = drugDeliverType;
	}
	public String getDoctorFunction() {
		return doctorFunction;
	}
	public void setDoctorFunction(String doctorFunction) {
		this.doctorFunction = doctorFunction;
	}
	public String getTaboo() {
		return taboo;
	}
	public void setTaboo(String taboo) {
		this.taboo = taboo;
	}
	public String getIsSeriousDrugs() {
		return isSeriousDrugs;
	}
	public void setIsSeriousDrugs(String isSeriousDrugs) {
		this.isSeriousDrugs = isSeriousDrugs;
	}
	public String getSeriousDrugs() {
		return seriousDrugs;
	}
	public void setSeriousDrugs(String seriousDrugs) {
		this.seriousDrugs = seriousDrugs;
	}
	public String getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getChineseMedicineNo() {
		return chineseMedicineNo;
	}
	public void setChineseMedicineNo(String chineseMedicineNo) {
		this.chineseMedicineNo = chineseMedicineNo;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getProjectEnglishName() {
		return projectEnglishName;
	}
	public void setProjectEnglishName(String projectEnglishName) {
		this.projectEnglishName = projectEnglishName;
	}
	public String getInsurantInnerOut() {
		return insurantInnerOut;
	}
	public void setInsurantInnerOut(String insurantInnerOut) {
		this.insurantInnerOut = insurantInnerOut;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getMedlimitedPrice() {
		return medlimitedPrice;
	}
	public void setMedlimitedPrice(Integer medlimitedPrice) {
		this.medlimitedPrice = medlimitedPrice;
	}
	public String getExaminationHint() {
		return examinationHint;
	}
	public void setExaminationHint(String examinationHint) {
		this.examinationHint = examinationHint;
	}
	public String getReferenceRanges() {
		return referenceRanges;
	}
	public void setReferenceRanges(String referenceRanges) {
		this.referenceRanges = referenceRanges;
	}
	public String getExaminationResult() {
		return examinationResult;
	}
	public void setExaminationResult(String examinationResult) {
		this.examinationResult = examinationResult;
	}
	public String getExtraRecipeFlg() {
		return extraRecipeFlg;
	}
	public void setExtraRecipeFlg(String extraRecipeFlg) {
		this.extraRecipeFlg = extraRecipeFlg;
	}
	public Integer getHosBearMoney() {
		return hosBearMoney;
	}
	public void setHosBearMoney(Integer hosBearMoney) {
		this.hosBearMoney = hosBearMoney;
	}
	public String getConstituent() {
		return constituent;
	}
	public void setConstituent(String constituent) {
		this.constituent = constituent;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
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
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
	public String getRecipeSerialNum() {
		return recipeSerialNum;
	}
	public void setRecipeSerialNum(String recipeSerialNum) {
		this.recipeSerialNum = recipeSerialNum;
	}
	
}
