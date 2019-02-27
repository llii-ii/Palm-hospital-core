package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface IDC10 {
	static IDC10 create( Supplier< IDC10 > supplier ) {
		 return supplier.get();
	}
	String getName(String value);
	/**对字典值进行校验*/
	default boolean validate(String value){
		if(null == getName(value)) {
			return false;
		}else {
			return true;
		}
	}
}
