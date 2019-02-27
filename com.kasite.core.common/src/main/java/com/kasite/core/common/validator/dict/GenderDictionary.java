package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface GenderDictionary {
	static GenderDictionary create( Supplier< GenderDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "0":return "未知的性别";
		case "1":return "男性";
		case "2":return "女性";
		case "9":return "未说明的性别";
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
