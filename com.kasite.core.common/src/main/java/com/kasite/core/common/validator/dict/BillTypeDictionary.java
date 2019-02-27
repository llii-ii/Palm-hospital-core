package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface BillTypeDictionary {
	static BillTypeDictionary create( Supplier< BillTypeDictionary > supplier ) {
		 return supplier.get();
	}
	 
	default String getName(String value) {
		switch (value) {
		case "01":return "非社保";
		case "02":return "社保";
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
