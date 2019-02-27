package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface ResultCodeDictionary {
	static ResultCodeDictionary create( Supplier< ResultCodeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "治愈";
		case "2":return "好转";
		case "3":return "稳定";
		case "4":return "恶化";
		case "5":return "死亡";
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
