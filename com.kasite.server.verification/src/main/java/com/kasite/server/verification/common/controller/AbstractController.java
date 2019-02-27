package com.kasite.server.verification.common.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.IPUtils;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.server.verification.module.sys.entity.SysUserEntity;


/**
 * Controller公共组件
 * 
 * @email 343675979@qq.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 获取当前操作用户对象
	 * @return
	 */
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}
	
	 /** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler  
    public R exp(HttpServletRequest request, Exception ex) {  
    	try{
	    	if(null != ex && ex.getStackTrace().length > 0){
	    		SysLogEntity sysLog = new SysLogEntity();
	        	//请求的方法名
	    		String className ="";
	    		String methodName ="";
	    		int line = 0;
	        	methodName = ex.getStackTrace()[0].getMethodName();
	        	className = ex.getStackTrace()[0].getClassName();
	        	line = ex.getStackTrace()[0].getLineNumber();
	        	sysLog.setMethod(className + "." + methodName + "("+line+")");
	        	sysLog.setIp(IPUtils.getIpAddr(request));
	        	String params = new Gson().toJson(request.getParameterMap());
	        	sysLog.setParams(params);
	        	sysLog.setOperation("系统异常");
	        	String exception = StringUtil.getExceptionStack(ex);
	        	if(StringUtil.isNotBlank(exception) && exception.length() > 200){
	        		exception = exception.substring(0, 200);
	        	}
	        	sysLog.setException(exception);
//	        	sysLog.setUsername(this.getUser().getUsername());
	    		sysLog.setTime(-1l);
	    		sysLog.setCreate_Date(new Date());
	    		//保存系统日志
//    			sysLogService.save(sysLog);
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("鉴权系统异常",e);
    	}
    	ex.printStackTrace();
    	if(ex instanceof RRException){
    		return R.error( ((RRException) ex).getCode(),ex.getMessage());
    	}
        return R.error(ex);
    }  
}
