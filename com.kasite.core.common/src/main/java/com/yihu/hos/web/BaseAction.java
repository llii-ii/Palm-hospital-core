package com.yihu.hos.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.kasite.core.common.config.KasiteConfig;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.yihu.hos.IRetCode;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.util.CookieTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public abstract class BaseAction<T extends Entity> extends BaseActionSupport<T> implements ModelDriven<T>, Preparable {
	private static final long serialVersionUID = 1L;
	public final static String EXCEPTIONPAGE = "exceptionPage";

	public String GetJson(Params params){
		String retVal = "{}";
		List<Params> ps = params.getParams();
		JSONObject json = new JSONObject();
		if(null != ps && ps.size() >0){
			for (Params param : ps) {
				String key = param.getKey();
				Object value = param.getValue();
				json.put(key, value);
			}
			retVal = json.toString();
		}
		return retVal; 
	}
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * �����֤
	 */
	public void userCheck(){
		JSONObject obj = new JSONObject();
		String respCode = IConstant.FAILCODE;
		String respMessage = IConstant.FAILSTR;
		try{
			String openid = request.getParameter("openid");
			String channelId = request.getParameter("channelId");
			String ext = request.getParameter("ext");
//			String desExt = "";
			String hospitalId = request.getParameter("hospitalId");
			if(StringUtils.isNotBlank(openid)){
				JSONObject resultObj = new JSONObject();
				if(ext != null){
//					DES des = new DES();
//					desExt = des.getEncString(ext);
					resultObj.put("ext", ext);
				}
				//��¼�ɹ�,����Ϣ���浽Cookie
				resultObj.put("channelId", channelId);
				resultObj.put("openid", openid);
				resultObj.put("hospitalId", hospitalId);
				KasiteConfig.print("--resultObj-->"+resultObj.toString());
				addLoginCookie(response, resultObj.toString());
				respCode = IConstant.SUCCESSCODE+"";
				respMessage = IConstant.SUCCESSSTR;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		obj.put("Code", respCode);
		obj.put("Message", respMessage);  
		rendText(obj.toString());
		return;
	}
	 /**
     * ��ӵ�¼��Ϣ��Cookie
     * @param response
     * @param cookieValue
     * @param day
     */
    public static void addLoginCookie(HttpServletResponse response,String cookieValue)   
    { 
    	String LOGIN = "DEFAULT";
    	IGetConfigElement getConfiUtil;
		try {
			getConfiUtil = InitUtil.getInstance().getGetConfiUtil();
	    	if(null != getConfiUtil){
	    		LOGIN = getConfiUtil.getConfigRootElement(IConstant.SysId);
	    	}else{
	    		KasiteConfig.print("ϵͳ�����ļ�Sys.xml ��û������SysId �ڵ� ���� ��ʼ����ʱ��δ�� IGetConfigElement ʵ������  InitUtil");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	CookieTool.addCookie(response, LOGIN, CookieTool.cookieEnCode(cookieValue), 1);
    }
    
    /**
     * ��õ�¼��Cookie
     * @param request
     * @return
     */
    public static String getLoginCookieValue(HttpServletRequest request) 
    {
    	String LOGIN = "DEFAULT";
    	IGetConfigElement getConfiUtil;
		try {
			getConfiUtil = InitUtil.getInstance().getGetConfiUtil();
	    	if(null != getConfiUtil){
	    		LOGIN = getConfiUtil.getConfigRootElement(IConstant.SysId);
	    	}else{
	    		KasiteConfig.print("ϵͳ�����ļ�Sys.xml ��û������SysId �ڵ� ���� ��ʼ����ʱ��δ�� IGetConfigElement ʵ������  InitUtil");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	String loginStr = CookieTool.getCookieValue(request, LOGIN);
		return CookieTool.cookieDecCode(loginStr);
    }
	/**
	 * ��õ�¼��Cookie��Ϣ
	 */
	public void getLoginCookie(){
		String loginStr = getLoginCookieValue(request);
		rendText(loginStr);
	}
	public JSONObject getErrorRet(IRetCode retCode){
		JSONObject result = new JSONObject();
		try {
			result.put("Code", retCode.getCode());
			result.put("Message", retCode.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public JSONObject GetErrorRet(IRetCode retCode){
		JSONObject result = new JSONObject();
		try {
			result.put("Code", retCode.getCode());
			result.put("Message", retCode.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public JSONObject GetErrorRet(IRetCode retCode,Exception e){
		JSONObject result = new JSONObject();
		try {
			result.put("Code", retCode.getCode());
			result.put("Message", retCode.getMessage() );
			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			e.printStackTrace(w);
			result.put("ExceptionMessage",sw.toString());
		} catch (Exception e11) {
			e11.printStackTrace();
		}
		return result;
	}
	public HttpServletRequest request = ServletActionContext.getRequest();  
	public HttpServletResponse response =ServletActionContext.getResponse();
	/**
	 * Code�ڵ�����
	 */
	private static final String CODE = "Code";
	/**
	 * Message�ڵ�����
	 */
	private static final String MESSAGE = "Message";

	public String execute()
	{
		return SUCCESS;  
	}
	/**
	 * �����Ĭ����ӦcontentType=application/json;charset=UTF-8
	 * @param value String ����
	 * @throws IOException
	 */
	public void write(String value) {
		this.write(value, "application/json;charset=UTF-8");
	}
	public void write(String value, String contentType) {
		response.setContentType(contentType);
		try {
			response.getWriter().write(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡHttpServletResponse�����÷��ر���
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(
				"com.opensymphony.xwork2.dispatcher.HttpServletResponse");
		response.setContentType("text/x-json;charset=UTF-8");
		return response;
	}
	/**
	 * �����ǰ��
	 * 
	 * @param value
	 */
	public void json(String value) {
		try {
			this.getResponse().getWriter().write(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ǰ��
	 * 
	 * @param value
	 */
	public void write(Integer code, String msg) {
		try {
			this.getResponse().getWriter().write("{\"Code\":" + code + ",\"Message\":\"" + msg + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����JSONObject:{Code:1,Message:''}
	 * @return net.sf.json.JSONObject
	 */
	public JSONObject jsonResult(){
		return this.jsonResult(1,"SUCCESS");
	}
	public JSONObject jsonResult(int code, String message){
		JSONObject  j=new JSONObject();
		j.put(CODE, code);
		j.put(MESSAGE, message);
		return j;
	}
	public JSONObject jsonResult(int code, String message, Object result){
		JSONObject  j=new JSONObject();
		j.put(CODE, code);
		j.put(MESSAGE, message);
		Object rs = result;
		if(result instanceof JSONObject 
				|| result instanceof JSONArray){
			rs = result.toString();
		}
		j.put("Result", rs);
		return j;
	}
	/**
	 * ȡ��HTTP������ֵΪ���ַ�����nullʱ����Ĭ��ֵ
	 * 
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public Integer getParameterToInt(String name) {
		String _obj = request.getParameter(name);
		if(_obj != null && _obj.length() > 0 && _obj.matches("\\d+")){
			return Integer.valueOf(_obj);
		}
		return null;
	}
}
