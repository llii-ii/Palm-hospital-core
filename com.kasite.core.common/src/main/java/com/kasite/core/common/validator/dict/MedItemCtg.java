package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface MedItemCtg {
	static MedItemCtg create( Supplier< MedItemCtg > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "01":return "药品 ";
		case "02":return "诊疗 ";
		case "03":return "医用耗材 ";
		case "04":return "其他 ";
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
