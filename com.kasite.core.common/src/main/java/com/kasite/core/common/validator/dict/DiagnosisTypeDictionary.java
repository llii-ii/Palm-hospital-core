package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface DiagnosisTypeDictionary {
	static DiagnosisTypeDictionary create( Supplier< DiagnosisTypeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "出院诊断";
		case "10":return "并发症诊断";
		case "11":return "院内感染诊断";
		case "12":return "主要诊断";
		case "13":return "次要诊断";
		case "2":return "门诊诊断";
		case "3":return "入院初步诊断";
		case "4":return "术前诊断";
		case "5":return "术后诊断";
		case "6":return "尸检诊断";
		case "7":return "放射诊断";
		case "8":return "超声诊断";
		case "9":return "病理诊断";
		case "99":return "其他";

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
