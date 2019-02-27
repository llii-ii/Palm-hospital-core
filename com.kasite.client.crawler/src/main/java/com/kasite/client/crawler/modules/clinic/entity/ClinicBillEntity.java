package com.kasite.client.crawler.modules.clinic.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.config.CheckDictBuser;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.group.AddGroup;

public class ClinicBillEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**就诊流水号 **/
	private String medicalNum;
	/**病例ID clinicCaseId **/
	@NotBlank(message="病例ID clinicCaseId 不能为空", groups = {AddGroup.class})
	private String clinicCaseId;
	/**门（急）诊号 clinicNum **/
	@NotBlank(message="门（急）诊号 clinicNum 不能为空", groups = {AddGroup.class})
	private String clinicNum;
	/**单据号 receiptNum **/
	@NotBlank(message="单据号 receiptNum 不能为空", groups = {AddGroup.class})
	private String receiptNum;
	/**发票号 invoiceNum **/
	@NotBlank(message="发票号 invoiceNum 不能为空", groups = {AddGroup.class})
	private String invoiceNum;
	@CheckDict(inf=CheckDictBuser.class,type="settlementType",isNotNull = true,message="结算方式 “settlementType” 字典值不合法",groups=AddGroup.class)
	/**结算方式 settlementType **/
	private String settlementType;
	@CheckDict(inf=CheckDictBuser.class,type="billType",isNotNull = true,message="账单类型 “billType” 字典值不合法",groups=AddGroup.class)
	/**账单类型 billType **/
	private String billType;
	@CheckDate(message="票据产生日期 invoiceDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**票据产生日期 invoiceDate **/
	private String invoiceDate;
	@CheckDate(message="发票打印日期 printDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**发票打印日期 printDate **/
	private String printDate;
	@CheckCurrency(message="发票总金额 invoiceAmount 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**发票总金额 invoiceAmount **/
	private Integer invoiceAmount;
	/**结算序号 settlementSeqNo **/
	private String settlementSeqNo;
	/**结算批次 settlementTimes **/
	private String settlementTimes;
	/**医保流水号 medicalLnsuranceNo **/
	private String medicalLnsuranceNo;
	@CheckCurrency(message="商业保险支付金额 commercialInsurancePay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**商业保险支付金额 commercialInsurancePay **/
	private Integer commercialInsurancePay;
	@CheckCurrency(message="个人账户支付金额 selfAccountPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**个人账户支付金额 selfAccountPay **/
	private Integer selfAccountPay;
	@CheckCurrency(message="个人支付金额 selfCashPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**个人支付金额 selfCashPay **/
	private Integer selfCashPay;
	@CheckCurrency(message="自费费用 selfFee 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**自费费用 selfFee **/
	private Integer selfFee;
	@CheckCurrency(message="自付一金额 selfPayA 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**自付一金额 selfPayA **/
	private Integer selfPayA;
	@CheckCurrency(message="自付二金额 selfPayB 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**自付二金额 selfPayB **/
	private Integer selfPayB;
	@CheckCurrency(message="医保余额不足自付金额 selfPayInsufficient 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**医保余额不足自付金额 selfPayInsufficient **/
	private Integer selfPayInsufficient;
	@CheckCurrency(message="超限额自付金额 selfPayAboveLimit 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**超限额自付金额 selfPayAboveLimit **/
	private Integer selfPayAboveLimit;
	@CheckCurrency(message="不足起付线自付金额 selfPayInit 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**不足起付线自付金额 selfPayInit **/
	private Integer selfPayInit;
	@CheckCurrency(message="少儿起付线到 5000 部 分比例自付金额 selfPayChildA 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿起付线到 5000 部 分比例自付金额 selfPayChildA **/
	private Integer selfPayChildA;
	@CheckCurrency(message="少儿 5000 到 10000部分比例自付金额 selfPayChildB 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿 5000 到 10000部分比例自付金额 selfPayChildB **/
	private Integer selfPayChildB;
	@CheckCurrency(message="少儿 10000 以上部分 比例自付金额 selfPayChildC 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿 10000 以上部分 比例自付金额 selfPayChildC **/
	private Integer selfPayChildC;
	@CheckCurrency(message="医保基金支付总金额 miFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**医保基金支付总金额 miFundPay **/
	private Integer miFundPay;
	@CheckCurrency(message="基本医保统筹基金支 付金额 basicFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**基本医保统筹基金支 付金额 basicFundPay **/
	private Integer basicFundPay;
	@CheckCurrency(message="大额医疗互助基金支付金额  largeFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**大额医疗互助基金支付金额  largeFundPay **/
	private Integer largeFundPay;
	@CheckCurrency(message="其他基金支付总金额 otherFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**其他基金支付总金额 otherFundPay **/
	private Integer otherFundPay;
	@CheckCurrency(message="地方补充医保基金支付金额 localFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**地方补充医保基金支付金额 localFundPay **/
	private Integer localFundPay;
	@CheckCurrency(message="地方补充医保基金正常部分支付金额 localFundNormalPartPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**地方补充医保基金正常部分支付金额 localFundNormalPartPay **/
	private Integer localFundNormalPartPay;
	@CheckCurrency(message="地方补充医保基金超出限额补偿金额 localFundOverPartPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**地方补充医保基金超出限额补偿金额 localFundOverPartPay **/
	private Integer localFundOverPartPay;
	@CheckCurrency(message="离退休医保基金支付金额 retiredMiFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**离退休医保基金支付金额 retiredMiFundPay **/
	private Integer retiredMiFundPay;
	@CheckCurrency(message="公务员医疗补助支付金额 servantPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**公务员医疗补助支付金额 servantPay **/
	private Integer servantPay;
	@CheckCurrency(message="家属统筹医疗基金支付金额 familyCoFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**家属统筹医疗基金支付金额 familyCoFundPay **/
	private Integer familyCoFundPay;
	@CheckCurrency(message="工伤保险基金支付金额 injuryInsuranceFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**工伤保险基金支付金额 injuryInsuranceFundPay **/
	private Integer injuryInsuranceFundPay;
	@CheckCurrency(message="企业补充医保基金支付金额 enterpriseFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**企业补充医保基金支付金额 enterpriseFundPay **/
	private Integer enterpriseFundPay;
	@CheckCurrency(message="农村医保基金支付金额 ruralFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**农村医保基金支付金额 ruralFundPay **/
	private Integer ruralFundPay;
	@CheckCurrency(message="少儿起付线到 5000 部分基金支付金额 childFundPayA 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿起付线到 5000 部分基金支付金额 childFundPayA **/
	private Integer childFundPayA;
	@CheckCurrency(message="少儿 5000 到 10000 部分基金支付金额 childFundPayB 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿 5000 到 10000 部分基金支付金额 childFundPayB **/
	private Integer childFundPayB;
	@CheckCurrency(message="少儿 10000 以上部分 基金支付金额 childFundPayC 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**少儿 10000 以上部分 基金支付金额 childFundPayC **/
	private Integer childFundPayC;
	@CheckCurrency(message="生育医保基金支付金额 childbirthFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**生育医保基金支付金额 childbirthFundPay **/
	private Integer childbirthFundPay;
	@CheckCurrency(message="离退休人员补助基金支付金额 retiredAidFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**离退休人员补助基金支付金额 retiredAidFundPay **/
	private Integer retiredAidFundPay;
	@CheckCurrency(message="残疾军人补助基金支付金额 disabledSoldierAidFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**残疾军人补助基金支付金额 disabledSoldierAidFundPay **/
	private Integer disabledSoldierAidFundPay;
	@CheckCurrency(message="统筹单位支付金额 coCorpPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**统筹单位支付金额 coCorpPay **/
	private Integer coCorpPay;
	@CheckCurrency(message="困难救助基金支出 difficultAidFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**困难救助基金支出 difficultAidFundPay **/
	private Integer difficultAidFundPay;
	@CheckCurrency(message="劳模基金支出 laborModelFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**劳模基金支出 laborModelFundPay **/
	private Integer laborModelFundPay;
	@CheckCurrency(message="保健基金支出 healthFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**保健基金支出 healthFundPay **/
	private Integer healthFundPay;
	@CheckCurrency(message="建国前老工人基金支出 oldWorkerFundPay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**建国前老工人基金支出 oldWorkerFundPay **/
	private Integer oldWorkerFundPay;
	@CheckCurrency(message="记帐前基本医保统筹 基金可用限额 basicFundAvailableBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐前基本医保统筹 基金可用限额 basicFundAvailableBefore **/
	private Integer basicFundAvailableBefore;
	@CheckCurrency(message="记帐前地方补充医保基金可用限额 localFundAvailableBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐前地方补充医保基金可用限额 localFundAvailableBefore **/
	private Integer localFundAvailableBefore;
	@CheckCurrency(message="记帐前个人帐户余额 selfAccountBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐前个人帐户余额 selfAccountBefore **/
	private Integer selfAccountBefore;
	@CheckCurrency(message="记帐后基本医保统筹 基金可用限额 basicFundAvailableAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐后基本医保统筹 基金可用限额 basicFundAvailableAfter **/
	private Integer basicFundAvailableAfter;
	@CheckCurrency(message="记帐后地方补充医保 基金可用限额 localFundAvailableAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐后地方补充医保 基金可用限额 localFundAvailableAfter **/
	private Integer localFundAvailableAfter;
	@CheckCurrency(message="记帐后个人帐户余额 selfAccountAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐后个人帐户余额 selfAccountAfter **/
	private Integer selfAccountAfter;
	@CheckCurrency(message="记账前自付段余额 SelfPaySectorBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记账前自付段余额 SelfPaySectorBefore **/
	private Integer SelfPaySectorBefore;
	@CheckCurrency(message="记账后自付段余额 selfPaySectorAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记账后自付段余额 selfPaySectorAfter **/
	private Integer selfPaySectorAfter;
	@CheckCurrency(message="记帐前劳务工合作医 疗基金可用限额 laborFundAvailableBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐前劳务工合作医 疗基金可用限额 laborFundAvailableBefore **/
	private Integer laborFundAvailableBefore;
	@CheckCurrency(message="记帐后劳务工合作医 疗基金可用限额 laborFundAvailableAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐后劳务工合作医 疗基金可用限额 laborFundAvailableAfter **/
	private Integer laborFundAvailableAfter;
	@CheckCurrency(message="记帐前少儿基本医疗 统筹可用限额 childFundAvailableBefore 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐前少儿基本医疗 统筹可用限额 childFundAvailableBefore **/
	private Integer childFundAvailableBefore;
	@CheckCurrency(message="记帐后少儿基本医疗 统筹可用限额 childFundAvailableAfter 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**记帐后少儿基本医疗 统筹可用限额 childFundAvailableAfter **/
	private Integer childFundAvailableAfter;
	@CheckCurrency(message="医院承担费用  hospitalCost 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**医院承担费用  hospitalCost **/
	private Integer hospitalCost;
	@CheckCurrency(message="本次大病合规费用 seriousDiseasePay 数据格式不正确", groups = {AddGroup.class} , isNotNull =false)
	/**本次大病合规费用 seriousDiseasePay **/
	private Integer seriousDiseasePay;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class})
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	
	
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
	public String getClinicCaseId() {
		return clinicCaseId;
	}
	public void setClinicCaseId(String clinicCaseId) {
		this.clinicCaseId = clinicCaseId;
	}
	public String getClinicNum() {
		return clinicNum;
	}
	public void setClinicNum(String clinicNum) {
		this.clinicNum = clinicNum;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public Integer getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Integer invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getSettlementSeqNo() {
		return settlementSeqNo;
	}
	public void setSettlementSeqNo(String settlementSeqNo) {
		this.settlementSeqNo = settlementSeqNo;
	}
	public String getSettlementTimes() {
		return settlementTimes;
	}
	public void setSettlementTimes(String settlementTimes) {
		this.settlementTimes = settlementTimes;
	}
	public String getMedicalLnsuranceNo() {
		return medicalLnsuranceNo;
	}
	public void setMedicalLnsuranceNo(String medicalLnsuranceNo) {
		this.medicalLnsuranceNo = medicalLnsuranceNo;
	}
	public Integer getCommercialInsurancePay() {
		return commercialInsurancePay;
	}
	public void setCommercialInsurancePay(Integer commercialInsurancePay) {
		this.commercialInsurancePay = commercialInsurancePay;
	}
	public Integer getSelfAccountPay() {
		return selfAccountPay;
	}
	public void setSelfAccountPay(Integer selfAccountPay) {
		this.selfAccountPay = selfAccountPay;
	}
	public Integer getSelfCashPay() {
		return selfCashPay;
	}
	public void setSelfCashPay(Integer selfCashPay) {
		this.selfCashPay = selfCashPay;
	}
	public Integer getSelfFee() {
		return selfFee;
	}
	public void setSelfFee(Integer selfFee) {
		this.selfFee = selfFee;
	}
	public Integer getSelfPayA() {
		return selfPayA;
	}
	public void setSelfPayA(Integer selfPayA) {
		this.selfPayA = selfPayA;
	}
	public Integer getSelfPayB() {
		return selfPayB;
	}
	public void setSelfPayB(Integer selfPayB) {
		this.selfPayB = selfPayB;
	}
	public Integer getSelfPayInsufficient() {
		return selfPayInsufficient;
	}
	public void setSelfPayInsufficient(Integer selfPayInsufficient) {
		this.selfPayInsufficient = selfPayInsufficient;
	}
	public Integer getSelfPayAboveLimit() {
		return selfPayAboveLimit;
	}
	public void setSelfPayAboveLimit(Integer selfPayAboveLimit) {
		this.selfPayAboveLimit = selfPayAboveLimit;
	}
	public Integer getSelfPayInit() {
		return selfPayInit;
	}
	public void setSelfPayInit(Integer selfPayInit) {
		this.selfPayInit = selfPayInit;
	}
	public Integer getSelfPayChildA() {
		return selfPayChildA;
	}
	public void setSelfPayChildA(Integer selfPayChildA) {
		this.selfPayChildA = selfPayChildA;
	}
	public Integer getSelfPayChildB() {
		return selfPayChildB;
	}
	public void setSelfPayChildB(Integer selfPayChildB) {
		this.selfPayChildB = selfPayChildB;
	}
	public Integer getSelfPayChildC() {
		return selfPayChildC;
	}
	public void setSelfPayChildC(Integer selfPayChildC) {
		this.selfPayChildC = selfPayChildC;
	}
	public Integer getMiFundPay() {
		return miFundPay;
	}
	public void setMiFundPay(Integer miFundPay) {
		this.miFundPay = miFundPay;
	}
	public Integer getBasicFundPay() {
		return basicFundPay;
	}
	public void setBasicFundPay(Integer basicFundPay) {
		this.basicFundPay = basicFundPay;
	}
	public Integer getLargeFundPay() {
		return largeFundPay;
	}
	public void setLargeFundPay(Integer largeFundPay) {
		this.largeFundPay = largeFundPay;
	}
	public Integer getOtherFundPay() {
		return otherFundPay;
	}
	public void setOtherFundPay(Integer otherFundPay) {
		this.otherFundPay = otherFundPay;
	}
	public Integer getLocalFundPay() {
		return localFundPay;
	}
	public void setLocalFundPay(Integer localFundPay) {
		this.localFundPay = localFundPay;
	}
	public Integer getLocalFundNormalPartPay() {
		return localFundNormalPartPay;
	}
	public void setLocalFundNormalPartPay(Integer localFundNormalPartPay) {
		this.localFundNormalPartPay = localFundNormalPartPay;
	}
	public Integer getLocalFundOverPartPay() {
		return localFundOverPartPay;
	}
	public void setLocalFundOverPartPay(Integer localFundOverPartPay) {
		this.localFundOverPartPay = localFundOverPartPay;
	}
	public Integer getRetiredMiFundPay() {
		return retiredMiFundPay;
	}
	public void setRetiredMiFundPay(Integer retiredMiFundPay) {
		this.retiredMiFundPay = retiredMiFundPay;
	}
	public Integer getServantPay() {
		return servantPay;
	}
	public void setServantPay(Integer servantPay) {
		this.servantPay = servantPay;
	}
	public Integer getFamilyCoFundPay() {
		return familyCoFundPay;
	}
	public void setFamilyCoFundPay(Integer familyCoFundPay) {
		this.familyCoFundPay = familyCoFundPay;
	}
	public Integer getInjuryInsuranceFundPay() {
		return injuryInsuranceFundPay;
	}
	public void setInjuryInsuranceFundPay(Integer injuryInsuranceFundPay) {
		this.injuryInsuranceFundPay = injuryInsuranceFundPay;
	}
	public Integer getEnterpriseFundPay() {
		return enterpriseFundPay;
	}
	public void setEnterpriseFundPay(Integer enterpriseFundPay) {
		this.enterpriseFundPay = enterpriseFundPay;
	}
	public Integer getRuralFundPay() {
		return ruralFundPay;
	}
	public void setRuralFundPay(Integer ruralFundPay) {
		this.ruralFundPay = ruralFundPay;
	}
	public Integer getChildFundPayA() {
		return childFundPayA;
	}
	public void setChildFundPayA(Integer childFundPayA) {
		this.childFundPayA = childFundPayA;
	}
	public Integer getChildFundPayB() {
		return childFundPayB;
	}
	public void setChildFundPayB(Integer childFundPayB) {
		this.childFundPayB = childFundPayB;
	}
	public Integer getChildFundPayC() {
		return childFundPayC;
	}
	public void setChildFundPayC(Integer childFundPayC) {
		this.childFundPayC = childFundPayC;
	}
	public Integer getChildbirthFundPay() {
		return childbirthFundPay;
	}
	public void setChildbirthFundPay(Integer childbirthFundPay) {
		this.childbirthFundPay = childbirthFundPay;
	}
	public Integer getRetiredAidFundPay() {
		return retiredAidFundPay;
	}
	public void setRetiredAidFundPay(Integer retiredAidFundPay) {
		this.retiredAidFundPay = retiredAidFundPay;
	}
	public Integer getDisabledSoldierAidFundPay() {
		return disabledSoldierAidFundPay;
	}
	public void setDisabledSoldierAidFundPay(Integer disabledSoldierAidFundPay) {
		this.disabledSoldierAidFundPay = disabledSoldierAidFundPay;
	}
	public Integer getCoCorpPay() {
		return coCorpPay;
	}
	public void setCoCorpPay(Integer coCorpPay) {
		this.coCorpPay = coCorpPay;
	}
	public Integer getDifficultAidFundPay() {
		return difficultAidFundPay;
	}
	public void setDifficultAidFundPay(Integer difficultAidFundPay) {
		this.difficultAidFundPay = difficultAidFundPay;
	}
	public Integer getLaborModelFundPay() {
		return laborModelFundPay;
	}
	public void setLaborModelFundPay(Integer laborModelFundPay) {
		this.laborModelFundPay = laborModelFundPay;
	}
	public Integer getHealthFundPay() {
		return healthFundPay;
	}
	public void setHealthFundPay(Integer healthFundPay) {
		this.healthFundPay = healthFundPay;
	}
	public Integer getOldWorkerFundPay() {
		return oldWorkerFundPay;
	}
	public void setOldWorkerFundPay(Integer oldWorkerFundPay) {
		this.oldWorkerFundPay = oldWorkerFundPay;
	}
	public Integer getBasicFundAvailableBefore() {
		return basicFundAvailableBefore;
	}
	public void setBasicFundAvailableBefore(Integer basicFundAvailableBefore) {
		this.basicFundAvailableBefore = basicFundAvailableBefore;
	}
	public Integer getLocalFundAvailableBefore() {
		return localFundAvailableBefore;
	}
	public void setLocalFundAvailableBefore(Integer localFundAvailableBefore) {
		this.localFundAvailableBefore = localFundAvailableBefore;
	}
	public Integer getSelfAccountBefore() {
		return selfAccountBefore;
	}
	public void setSelfAccountBefore(Integer selfAccountBefore) {
		this.selfAccountBefore = selfAccountBefore;
	}
	public Integer getBasicFundAvailableAfter() {
		return basicFundAvailableAfter;
	}
	public void setBasicFundAvailableAfter(Integer basicFundAvailableAfter) {
		this.basicFundAvailableAfter = basicFundAvailableAfter;
	}
	public Integer getLocalFundAvailableAfter() {
		return localFundAvailableAfter;
	}
	public void setLocalFundAvailableAfter(Integer localFundAvailableAfter) {
		this.localFundAvailableAfter = localFundAvailableAfter;
	}
	public Integer getSelfAccountAfter() {
		return selfAccountAfter;
	}
	public void setSelfAccountAfter(Integer selfAccountAfter) {
		this.selfAccountAfter = selfAccountAfter;
	}
	public Integer getSelfPaySectorBefore() {
		return SelfPaySectorBefore;
	}
	public void setSelfPaySectorBefore(Integer selfPaySectorBefore) {
		SelfPaySectorBefore = selfPaySectorBefore;
	}
	public Integer getSelfPaySectorAfter() {
		return selfPaySectorAfter;
	}
	public void setSelfPaySectorAfter(Integer selfPaySectorAfter) {
		this.selfPaySectorAfter = selfPaySectorAfter;
	}
	public Integer getLaborFundAvailableBefore() {
		return laborFundAvailableBefore;
	}
	public void setLaborFundAvailableBefore(Integer laborFundAvailableBefore) {
		this.laborFundAvailableBefore = laborFundAvailableBefore;
	}
	public Integer getLaborFundAvailableAfter() {
		return laborFundAvailableAfter;
	}
	public void setLaborFundAvailableAfter(Integer laborFundAvailableAfter) {
		this.laborFundAvailableAfter = laborFundAvailableAfter;
	}
	public Integer getChildFundAvailableBefore() {
		return childFundAvailableBefore;
	}
	public void setChildFundAvailableBefore(Integer childFundAvailableBefore) {
		this.childFundAvailableBefore = childFundAvailableBefore;
	}
	public Integer getChildFundAvailableAfter() {
		return childFundAvailableAfter;
	}
	public void setChildFundAvailableAfter(Integer childFundAvailableAfter) {
		this.childFundAvailableAfter = childFundAvailableAfter;
	}
	public Integer getHospitalCost() {
		return hospitalCost;
	}
	public void setHospitalCost(Integer hospitalCost) {
		this.hospitalCost = hospitalCost;
	}
	public Integer getSeriousDiseasePay() {
		return seriousDiseasePay;
	}
	public void setSeriousDiseasePay(Integer seriousDiseasePay) {
		this.seriousDiseasePay = seriousDiseasePay;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}


	
}
