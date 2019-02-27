package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface CertTypeDictionary {
	static CertTypeDictionary create( Supplier< CertTypeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
			case "01":return "居民身份证";
			case "02":return "居民户口簿";
			case "03":return "护照";
			case "04":return "军官证";
			case "05":return "驾驶证";
			case "06":return "港澳居民来往内地通行证";
			case "07":return "台湾居民来往内地通行证";
			case "99":return "其他法定有效证件";
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
