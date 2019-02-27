package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface ClinicCardTypeDictionary {
	static ClinicCardTypeDictionary create( Supplier< ClinicCardTypeDictionary > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "1":return "社会保障卡";
		case "2":return "医保卡";
		case "3":return "新农合";
		case "4":return "发行正式卡";
		case "5":return "发行临时卡";
		case "9":return "其他卡类别";


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
