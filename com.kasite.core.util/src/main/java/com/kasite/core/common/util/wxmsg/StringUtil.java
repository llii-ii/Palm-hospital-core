package com.kasite.core.common.util.wxmsg;

import java.util.List;
import java.util.Map;

/**
 * 字符串公函
 * 
 * @author daiys
 * @since 2012-07-24
 */
@SuppressWarnings("rawtypes")
public class StringUtil {


	// String is null or not
	public static boolean isEmpty(String str) {
		if ("".equals(str) || "null".equals(str) || "NULL".equals(str) || str == null || "undefined".equals(str))
			return true;
		if ("".equals(str.trim()))
			return true;
		return false;
	}

	// List is null or not

	public static boolean isEmpty(List list) {
		return isBlank(list);
	}

	// Map is null or not
	public static boolean isEmpty(Map map) {
		if (map == null || map.size() == 0)
			return true;
		return false;
	}
	/**
	 * 判断对象是否为空！(null,"", "null")
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return isBlank((String) obj);
		} else {
			return isBlank(obj.toString());
		}
	}
	// public static boolean isEmpty(Object obj) {
	// return isBlank(obj);
	// }
	// public static boolean isNotEmpty(Object obj) {
	// return isNotBlank(obj);
	// }
	public static final String BLANK = ""; 
	public static final String NULL = "null";
	/**
	 * 判断对象是否为空！(null,"", "null")
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isBlank(String value) {
		if (value == null || value.length() == 0) {
			return true;
		} else if (BLANK.equals(value) || NULL.equals(value)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否非空！(null,"", "null")
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}

}
