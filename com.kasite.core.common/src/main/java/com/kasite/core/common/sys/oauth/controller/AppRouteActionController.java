//package com.kasite.core.common.sys.oauth.controller;
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.coreframework.util.FileOper;
//import com.google.gson.Gson;
//import com.kasite.core.common.annotation.AuthIgnore;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.exception.RRException;
//import com.kasite.core.common.sys.verification.VerificationBuser;
//import com.kasite.core.common.util.R;
//import com.kasite.core.common.util.StringUtil;
//import com.yihu.wsgw.api.KasiteRouteParser;
//
//@RestController
//@RequestMapping("/route")
//public class AppRouteActionController {
//	
//	@AuthIgnore
//    @PostMapping("/updateNodeInfo")
//    R updateNodeInfo(String appName,String pName,String xmlContent) throws Exception {
//		String root = KasiteConfig.localConfigPath()+File.separator+ pName + VerificationBuser.ROUTENAMELASTNAME;
//		String filePath = root+File.separator+appName;
//		File f = new File(root);
//		if(!f.exists()) {
//			f.mkdirs();
//		}
//		FileOper.write(filePath, xmlContent, false);
//		return R.ok().put("Code", 1).put("Message", "修改成功");
//	}
//	
//	@AuthIgnore
//    @PostMapping("/xmlFormat")
//    R xmlFormat(String xmlContent) throws Exception {
//		String c = StringUtil.formatXML(xmlContent,"UTF-8");
//			JSONObject js = new JSONObject();
//			js.put("xmlContent", c);
//			return R.ok().put("Code", 1).put("Result", js);
//	}
//	
//	@AuthIgnore
//    @PostMapping("/getNodeInfo")
//    R getNodeInfo(String appName,String pName) throws Exception {
//		String root = KasiteConfig.localConfigPath()+File.separator;
//		File path = new File(root);
//		if(!(path.exists() && path.isDirectory())) {
//			logger.error("未找到配置文件目录："+ root);
//			throw new RRException("未找到配置文件目录："+ root);
//		}
//		File[] files=path.listFiles();
//		if(files==null){
//			throw new RRException("未找到配置文件");
//		}
//		String result = "";
//		for (File file : files) {
//			String fileName = file.getName();
//			if(fileName.indexOf(KasiteRouteParser.ROUTENAMELASTNAME) >= 0) {
//				String systype = fileName.substring(0,fileName.indexOf(KasiteRouteParser.ROUTENAMELASTNAME));
//				File[] fs = file.listFiles();
//				for(File f:fs){
//					if(f.isFile() && ( f.getName().toLowerCase().endsWith(".xml"))) {
//						String routeName = f.getName();
//						if(systype.equals(pName) && routeName.equals(appName)) {
//							byte[] data = Files.readAllBytes(f.toPath());
//							result = new String(data, StandardCharsets.UTF_8);
//						}
//					}
//				}
//			}
//		}
//		return R.ok().put("Code", 1).put("Result",result);
//	}
//
//	@AuthIgnore
//    @PostMapping("/queryRoute")
//    R queryRoute() throws Exception {
//		String root = KasiteConfig.localConfigPath()+File.separator;
//		File path = new File(root);
//		if(!(path.exists() && path.isDirectory())) {
//			logger.error("未找到配置文件目录："+ root);
//			throw new RRException("未找到配置文件目录："+ root);
//		}
//		File[] files=path.listFiles();
//		if(files==null){
//			throw new RRException("未找到配置文件");
//		}
//		/*
//		"routeid": 289,
//		"systype": "约医生",
//		"remark": "预约申请",
//		"systypeid": 16,
//		"routename": "AppointmentApply" 
//	 */
//		JSONArray array = new JSONArray();
//		for (File file : files) {
//			String fileName = file.getName();
//			if(fileName.indexOf(KasiteRouteParser.ROUTENAMELASTNAME) >= 0) {
//				String systype = fileName.substring(0,fileName.indexOf(KasiteRouteParser.ROUTENAMELASTNAME));
//				
//				File[] fs = file.listFiles();
//				for(File f:fs){
//					if(f.isFile() && ( f.getName().toLowerCase().endsWith(".xml"))) {
//						String routeName = f.getName();
//						JSONObject obj = new JSONObject();
//						obj.put("systype", systype);
//						obj.put("systypeid", systype);
//						obj.put("routename", routeName);
//						obj.put("routeid", routeName);
//						obj.put("remark", routeName);
//						array.add(obj);
//					}
//				}
//			}
//		}
//		
//		
//		return R.ok().put("Code", 1).put("Message", "").put("Result", array);
//	}
//	
//	
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//	 /** 基于@ExceptionHandler异常处理 */  
//	   @ExceptionHandler  
//	   public R exp(HttpServletRequest request, Exception ex) {  
//	   	try{
//		    	if(null != ex && ex.getStackTrace().length > 0){
//		    		StringBuffer sbf = new StringBuffer();
//		        	//请求的方法名
//		    		String className ="";
//		    		String methodName ="";
//		    		int line = 0;
//		        	methodName = ex.getStackTrace()[0].getMethodName();
//		        	className = ex.getStackTrace()[0].getClassName();
//		        	line = ex.getStackTrace()[0].getLineNumber();
//		        	String params = new Gson().toJson(request.getParameterMap());
//		        	String exception = StringUtil.getExceptionStack(ex);
//		        	if(StringUtil.isNotBlank(exception) && exception.length() > 200){
//		        		exception = exception.substring(0, 200);
//		        	}
//		        	sbf.append(className).append(".").append(methodName).append("(").append(line).append(")");
//		        	sbf.append("\r\n").append(params).append("\r\n").append(exception);
//		    		logger.error(sbf.toString());
//		    	}
//	   	}catch(Exception e){
//	   		e.printStackTrace();
//	   		logger.error("鉴权异常",e);
//	   	}
//	   	ex.printStackTrace();
//	   	if(ex instanceof RRException){
//	   		return R.error( ((RRException) ex).getCode(),ex.getMessage()).put("Code", -14444);
//	   	}
//	       return R.error(ex).put("Code", -14444);
//	   }  
//}
