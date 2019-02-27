package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface RecipeType {
	static RecipeType create( Supplier< RecipeType > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "西药处方";
		case "2":return "中药处方";
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
