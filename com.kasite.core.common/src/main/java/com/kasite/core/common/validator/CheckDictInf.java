package com.kasite.core.common.validator;

public interface CheckDictInf {

	/**获取字典值*/
	boolean checkValue(String version, String type,String code,CheckDict dict);
}
