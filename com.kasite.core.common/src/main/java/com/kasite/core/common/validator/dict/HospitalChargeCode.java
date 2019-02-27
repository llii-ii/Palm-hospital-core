package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface HospitalChargeCode {
	static HospitalChargeCode create( Supplier< HospitalChargeCode > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "01":return "西药";
		case "02":return "中药";
		case "03":return "检验";
		case "04":return "特检";
		case "05":return "治疗";
		case "06":return "放射";
		case "07":return "手术";
		case "08":return "输血";
		case "09":return "床位";
		case "10":return "护理";
		case "11":return "材料费";
		case "12":return "药事服务费";
		case "13":return "一般诊疗费";
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
