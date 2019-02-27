package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface IsSingleRemedy {
	static IsSingleRemedy create( Supplier< IsSingleRemedy > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "甲类";
		case "2":return "已类";
		case "3":return "丙类";
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
