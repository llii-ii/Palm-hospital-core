package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface PayFeeTypeDictionary {
	
	static PayFeeTypeDictionary create( Supplier< PayFeeTypeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "01":return "城镇职工基本医疗保险";
		case "02":return "城镇居民基本医疗保险";
		case "03":return "新型农村合作医疗";
		case "04":return "贫困救助";
		case "05":return "商业医疗保险";
		case "06":return "全公费";
		case "07":return "全自费";
		case "08":return "其他社会保险";
		case "99":return "其他";
		case "98":return "深圳基本医保一档(综合医保)";
		case "97":return "深圳基本医保二档(住院医保)";
		case "96":return "深圳基本医保三档(农民工/劳务工医保)";
		case "95":return "少儿医疗保险";
		case "94":return "少儿医保(自费)";
		case "93":return "学生医疗保险";
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
