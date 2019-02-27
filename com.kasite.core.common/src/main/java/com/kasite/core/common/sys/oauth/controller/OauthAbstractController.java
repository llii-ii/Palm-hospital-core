package com.kasite.core.common.sys.oauth.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;


/**
 */
public abstract class OauthAbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	 /** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler  
    public R exp(HttpServletRequest request, Exception ex) {  
    	try{
	    	if(null != ex && ex.getStackTrace().length > 0){
	    		StringBuffer sbf = new StringBuffer();
	        	//请求的方法名
	    		String className ="";
	    		String methodName ="";
	    		int line = 0;
	        	methodName = ex.getStackTrace()[0].getMethodName();
	        	className = ex.getStackTrace()[0].getClassName();
	        	line = ex.getStackTrace()[0].getLineNumber();
	        	String params = new Gson().toJson(request.getParameterMap());
	        	String exception = StringUtil.getExceptionStack(ex);
	        	if(StringUtil.isNotBlank(exception) && exception.length() > 200){
	        		exception = exception.substring(0, 200);
	        	}
	        	sbf.append(className).append(".").append(methodName).append("(").append(line).append(")");
	        	sbf.append("\r\n").append(params).append("\r\n").append(exception);
	    		logger.error(sbf.toString());
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("鉴权异常",e);
    	}
    	ex.printStackTrace();
    	if(ex instanceof RRException){
    		return R.error( ((RRException) ex).getCode(),ex.getMessage());
    	}
        return R.error(ex);
    }  
    
    // 字符串读取
    // 方法一
    public static String ReadAsChars(HttpServletRequest request)
    {
 
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    /**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  }
}
