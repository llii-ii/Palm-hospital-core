package com.kasite.core.common.util;

/**
 * 
 * @className: FormatUtils
 * @author: lcz
 * @date: 2018年9月17日 下午3:11:08
 */
public class FormatUtils {
	
	public static String mobileFormat(String mobile) {
		if(StringUtil.isBlank(mobile)) {
			return mobile;
		}
		return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
	}
	
	public static String idCardFormat(String idCardNo) {
		if(StringUtil.isBlank(idCardNo)) {
			return idCardNo;
		}
		return idCardNo.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2");
	}
	
	
	
	public static String getTimeName(Integer timeId) {
		if(timeId==null) {
			return "";
		}else if(timeId==0) {
			return "全天";
		}else if(timeId==1){
			return "上午";
		}else if(timeId==2){
			return "下午";
		}else if(timeId==3){
			return "晚间";
		}
		return "";
	}
}
