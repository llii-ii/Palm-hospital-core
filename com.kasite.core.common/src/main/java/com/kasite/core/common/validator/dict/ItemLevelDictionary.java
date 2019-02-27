package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface ItemLevelDictionary {
	static ItemLevelDictionary create( Supplier< ItemLevelDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "单方";
		case "2":return "复方";
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
