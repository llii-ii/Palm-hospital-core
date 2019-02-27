package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface ClientStatusDictionary {
	static ClientStatusDictionary create( Supplier< ClientStatusDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "未火化";
		case "2":return "已火化";
		case "3":return "未土/水葬";
		case "4":return "已土/水葬";
		case "5":return "宣告死亡;";
		case "6":return "未作伤残鉴定";
		case "7":return "已作伤残鉴定";
		case "8":return "尚未治疗准备中";
		case "9":return "门诊治疗中";
		case "10":return "住院治疗中";
		case "11":return "(阶段性)治疗结束";
		case "12":return "已报公安部门或法院判决";
		case "13":return "未报公安部门或法院判决";
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
