package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface MedicalTypeDictionary {
	static MedicalTypeDictionary create( Supplier< MedicalTypeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
			case "0":return "普通门诊";
			case "1":return "生育门诊";
			case "2":return "住院";
			case "3":return "生育住院";
			case "4":return "急诊";
			case "5":return "特病门诊";
			case "6":return "特需门诊";
			case "7":return "专家门诊";
			case "8":return "体检";
			case "9":return "预防接种";
			case "10":return "其他";
			default:return null;
		}
	}
	/**对字典值进行校验*/
	default boolean validate(String value){
		if(null == getName(value)) {
			return false;
		}else {
			return true;
		}
	}
}
