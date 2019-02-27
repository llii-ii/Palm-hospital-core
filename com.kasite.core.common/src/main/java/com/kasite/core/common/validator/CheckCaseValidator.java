package com.kasite.core.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.kasite.core.common.validator.dict.BillTypeDictionary;
import com.kasite.core.common.validator.dict.CertTypeDictionary;
import com.kasite.core.common.validator.dict.ClientStatusDictionary;
import com.kasite.core.common.validator.dict.ClinicCardTypeDictionary;
import com.kasite.core.common.validator.dict.ClinicChargeCode;
import com.kasite.core.common.validator.dict.DiagnosisTypeDictionary;
import com.kasite.core.common.validator.dict.GenderDictionary;
import com.kasite.core.common.validator.dict.HospitalChargeCode;
import com.kasite.core.common.validator.dict.IDC10;
import com.kasite.core.common.validator.dict.IsSingleRemedy;
import com.kasite.core.common.validator.dict.ItemLevelDictionary;
import com.kasite.core.common.validator.dict.MedItemCtg;
import com.kasite.core.common.validator.dict.MedicalTypeDictionary;
import com.kasite.core.common.validator.dict.PayFeeTypeDictionary;
import com.kasite.core.common.validator.dict.Race;
import com.kasite.core.common.validator.dict.RecipeType;
import com.kasite.core.common.validator.dict.ResultCodeDictionary;
import com.kasite.core.common.validator.dict.impl.BillTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.CertTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.ClientStatusDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.ClinicCardTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.ClinicChargeCodeImpl;
import com.kasite.core.common.validator.dict.impl.DiagnosisTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.GenderDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.HospitalChargeCodeImpl;
import com.kasite.core.common.validator.dict.impl.IDC10Impl;
import com.kasite.core.common.validator.dict.impl.IsSingleRemedyImpl;
import com.kasite.core.common.validator.dict.impl.ItemLevelDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.MedItemCtgImpl;
import com.kasite.core.common.validator.dict.impl.MedicalTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.PayFeeTypeDictionaryImpl;
import com.kasite.core.common.validator.dict.impl.RaceImpl;
import com.kasite.core.common.validator.dict.impl.RecipeTypeImpl;
import com.kasite.core.common.validator.dict.impl.ResultCodeDictionaryImpl;

public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
    private PrivateMode caseMode;
    @Autowired
    MedicalTypeDictionary medicalType;
    @Autowired
    CertTypeDictionary certType;
    @Autowired
    MedItemCtg medItemCtg;
    @Autowired 
    ClinicChargeCode clinicChargeCode;
    @Autowired 
    ClientStatusDictionary clientStatus;
    @Autowired
    Race race;
    @Autowired
    ClinicCardTypeDictionary clinicCardType;
    @Autowired
    BillTypeDictionary billType;
    @Autowired
    ResultCodeDictionary resultCode;
    @Autowired
    DiagnosisTypeDictionary diagnosisTypeDictionary;
    @Autowired
    MedicalTypeDictionary medicalTypeDictionary;
    @Autowired
    PayFeeTypeDictionary payFeeTypeDictionary;
    @Autowired
    GenderDictionary genderDictionary;
    @Autowired
    CertTypeDictionary certTypeDictionary;
    @Autowired
    HospitalChargeCode hospitalChargeCode;
    @Autowired
    RecipeType recipeType;
    @Autowired
    IDC10 idc10;
    @Autowired
    ItemLevelDictionary itemLevel;
    @Autowired
    IsSingleRemedy isSingleRemedy;
    
    public void initialize(CheckCase checkCase) {
        this.caseMode = checkCase.value();
    }
    
    private void defaultDict() {
    	
    		if(null == medicalType) {
    			medicalType = MedicalTypeDictionary.create(MedicalTypeDictionaryImpl::new);
    		}
    		if(null == certType) {
    			certType = CertTypeDictionary.create(CertTypeDictionaryImpl::new);
    		}
    		if(null == medItemCtg) {
    			medItemCtg = MedItemCtg.create(MedItemCtgImpl::new);
    		}
    		if(null == clinicChargeCode) {
    			clinicChargeCode = ClinicChargeCode.create(ClinicChargeCodeImpl::new);
    		}
    		if(null == clientStatus) {
    			clientStatus = ClientStatusDictionary.create(ClientStatusDictionaryImpl::new);
    		}
    		
    		if(null == payFeeTypeDictionary) {
    			payFeeTypeDictionary = PayFeeTypeDictionary.create(PayFeeTypeDictionaryImpl::new);
    		}
    		
    		if(null == race) {
    			race = Race.create(RaceImpl::new);
    		}
    		if(null == clinicCardType) {
    			clinicCardType = ClinicCardTypeDictionary.create(ClinicCardTypeDictionaryImpl::new);
    		}
    		if(null == billType) {
    			billType = BillTypeDictionary.create(BillTypeDictionaryImpl::new);
    		}
    		if(null == resultCode) {
    			resultCode = ResultCodeDictionary.create(ResultCodeDictionaryImpl::new);
    		}
    		if(null == diagnosisTypeDictionary) {
    			diagnosisTypeDictionary = DiagnosisTypeDictionary.create(DiagnosisTypeDictionaryImpl::new);
    		}
    		
    		if(null == medicalTypeDictionary) {
    			medicalTypeDictionary = MedicalTypeDictionary.create(MedicalTypeDictionaryImpl::new);
    		}
    		if(null == genderDictionary) {
    			genderDictionary = GenderDictionary.create(GenderDictionaryImpl::new);
    		}
    		if(null == certTypeDictionary) {
    			certTypeDictionary = CertTypeDictionary.create(CertTypeDictionaryImpl::new);
    		}
    		if(null == hospitalChargeCode) {
    			hospitalChargeCode = HospitalChargeCode.create(HospitalChargeCodeImpl::new);
    		}
    		if(null == recipeType) {
    			recipeType = RecipeType.create(RecipeTypeImpl::new);
    		}
    		if(null == idc10) {
    			idc10 = IDC10.create(IDC10Impl::new);
    		}
    		if(null == itemLevel) {
    			itemLevel = ItemLevelDictionary.create(ItemLevelDictionaryImpl::new);
    		}
    		if(null == isSingleRemedy) {
    			isSingleRemedy = IsSingleRemedy.create(IsSingleRemedyImpl::new);
    		}
    		
    }
    
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    		/**
    		 * 当没有个性化验证的时候初始化默认的验证接口
    		 */
    		defaultDict();
        if (value == null) {
            return true;
        }
        switch (caseMode) {
		case certType:
			return certType.validate(value);
		case medicalType:
			return medicalType.validate(value);
		case medItemCtg:
			return medItemCtg.validate(value);
		case billType:
			return billType.validate(value);
		case clientStatus:
			return clientStatus.validate(value);
		case clinicCardType:
			return clinicCardType.validate(value);
		case clinicChargeCode:
			return clinicChargeCode.validate(value);
		case diagnosisType:
			return diagnosisTypeDictionary.validate(value);
		case gender:
			return genderDictionary.validate(value);
		case hospitalChargeCode:
			return hospitalChargeCode.validate(value);
		case payFeeType:
			return payFeeTypeDictionary.validate(value);
		case race:
			return race.validate(value);
		case resultCode:
			return resultCode.validate(value);
		default:
			return false;
		}
    }
}