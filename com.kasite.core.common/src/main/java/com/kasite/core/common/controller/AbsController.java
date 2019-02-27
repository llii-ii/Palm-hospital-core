//package com.kasite.core.common.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import com.google.gson.Gson;
//import com.kasite.core.common.exception.RRException;
//import com.kasite.core.common.util.R;
//import com.kasite.core.common.util.StringUtil;
//
//public abstract class AbsController {
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//	private static final String ERROR_404= "/pageError.html?errorcode=";
//	@ExceptionHandler
//	public R exp(HttpServletRequest request, Exception ex) {
//		try {
//			if (null != ex && ex.getStackTrace().length > 0) {
//				StringBuffer sbf = new StringBuffer();
//				// 请求的方法名
//				String className = "";
//				String methodName = "";
//				int line = 0;
//				methodName = ex.getStackTrace()[0].getMethodName();
//				className = ex.getStackTrace()[0].getClassName();
//				line = ex.getStackTrace()[0].getLineNumber();
//				String params = new Gson().toJson(request.getParameterMap());
//				String exception = StringUtil.getExceptionStack(ex);
//				if (StringUtil.isNotBlank(exception)
//						&& exception.length() > 200) {
//					exception = exception.substring(0, 200);
//				}
//				sbf.append(className).append(".").append(methodName)
//						.append("(").append(line).append(")");
//				sbf.append("\r\n").append(params).append("\r\n")
//						.append(exception);
//				logger.error(sbf.toString());
//				//TODO 保存异常到数据库  如果是 -14444 或者是未定义到 异常则跳转到异常页面
//				
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("鉴权异常", e);
//		}
//		ex.printStackTrace();
//		if (ex instanceof RRException) {
//			return R.error(((RRException) ex).getCode(), ex.getMessage()).put(
//					"Code", -14444);
//		}
//		return R.error(ex).put("Code", -14444);
//	}
//}
