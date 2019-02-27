package com.kasite.core.common.validator;

import org.apache.commons.lang.StringUtils;

import com.kasite.core.common.exception.RRException;

/**
 * 数据校验
 * @author admin
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str) || "".equals(str.trim())) {
            throw new RRException(message);
        }
    }
    public static void isBlank(Object obj, String message) {
        if ( null == obj || "".equals(obj.toString().trim())) {
            throw new RRException(message);
        }
    }
    public static void isBlank(String str, String message,int code) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message,code);
        }
    }
    
    public static void isBlank2(String str,String paramName) {
    		switch(paramName) {
	    		case "appId":{
	    			isBlank(str, "appId 不能为空",20001);	
	    			break;
	    		}
	    		case "token":{
	    			isBlank(str, "token 不能为空",20002);	
	    			break;
	    		}
	    		case "orgCode":{
	    			isBlank(str, " 机构代码 不能为空",20003);	
	    			break;
	    		}
	    		case "appSecret":{
	    			isBlank(str, "appSecret 不能为空",20004);	
	    			break;
	    		}
	    		case "encrypt":{
	    			isBlank(str, "encrypt 不能为空",20005);	
	    			break;
	    		}
	    		default:{
	    			isBlank(str, "参数"+ paramName +" 不能为空",-14444);	
	    		}
    		}
    }
    
    

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}