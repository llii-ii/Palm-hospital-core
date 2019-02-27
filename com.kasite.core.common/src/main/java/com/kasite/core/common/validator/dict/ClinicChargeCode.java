package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface ClinicChargeCode {
	static ClinicChargeCode create( Supplier< ClinicChargeCode > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "01":return "西药";
		case "02":return "中成药";
		case "03":return "中草药";
		case "04":return "诊察费";
		case "05":return "检査费";
		case "06":return "化验费";
		case "07":return "放射费";
		case "08":return "治疗费";
		case "09":return "手术费";
		case "10":return "材料费";
		case "11":return "药事服务费";
		case "12":return "一般诊疗费";
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
