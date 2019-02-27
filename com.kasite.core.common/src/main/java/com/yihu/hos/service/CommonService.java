package com.yihu.hos.service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.kasite.core.common.util.DateOper;
import com.yihu.hos.DModule;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.exception.CommonServiceException;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;
/**
 * 
 * 业务抽象类
 * @author Administrator
 *
 */
public abstract class CommonService extends CommonServiceReturnMessage implements ICommonService{
	/**
	 * 用于存放事件
	 */
	private Map<String, IEvent> eventMap = null;
	
	@Override
	public IEvent getEvent(String methodName) {
		if( null != methodName){
			return eventMap.get(methodName);
		}else{
			return null;
		}
	}
	/**
	 * 属性字段标示
	 */
	private static final String PROPERTY = "property";
	/**
	 * 属性字段name
	 */
	private static final String NAME = "name";
	/**
	 * method 节点下  属性字段event
	 */
	private static final String EVENT = "eventClass";
	
	/**
	 * 方法参数节点
	 */
	private static final String METHOD = "Method";
	
	/**
	 * 存储配置文件中
	 * Method 节点数据  key = [name]   
	 */
	private Map<String, String> methodMap;
	
	/**
	 * 取出存储配置文件中
	 * @param key Method 节点中数据  key = [name]   
	 * @return 返回 Method 中的值
	 */
	protected String getMethodValue(String key) {
		return methodMap.get(key);
	}
	/**
	 * 
	 */
	protected Map<String,String> propertymap;
	/**
	 * 
	 * @param param 请求参数
	 */
	private void initParam2(JSONObject param){
//		Set<String> keys = propertymap.keySet();
//		for (String key : keys) {
//			String value = propertymap.get(key);
//			param.put(key, value);
//		}
		param.putAll(propertymap);
		
	}
	/**
	 * 
	 * @param param 请求参数
	 */
	private void initParam(Element param){
		if(null != param){
			Iterator<Element> eles = param.elementIterator();
			while (eles.hasNext()) {
				Element e = (Element) eles.next();
				propertymap.put(e.getName(), e.getTextTrim());
			}
		}
		
	}
	
	public CommonService() {
		
	}
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * @param obj 需要赋值的对象
	 * @param fieldName 赋值的属性名称
	 * @param value 值
	 * @throws AbsHosException 参数异常 
	 */
	private static void setFieldValue(final Object obj, final String fieldName, final String value)
		throws AbsHosException {
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("在类中没有找到这个属性字段 [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			if(String.class.equals(field.getType())){
				field.set(obj, value);
			}else if(Integer.class.equals(field.getType())){
				Integer v = Integer.parseInt(value);
				field.set(obj, v);
			}else if(Boolean.class.equals(field.getType())){
				Boolean b = Boolean.valueOf(value);
				field.set(obj, b);
			}else if(java.util.Date.class.equals(field.getType())){
				java.util.Date d = DateOper.parse(value);
				field.set(obj, d);
			}else if(java.sql.Date.class.equals(field.getType())){
				java.sql.Date d = new java.sql.Date(DateOper.parse(value).getTime());
				field.set(obj, d);
			}else if(java.sql.Timestamp.class.equals(field.getType())){
				java.sql.Timestamp d = new java.sql.Timestamp(DateOper.parse(value).getTime());
				field.set(obj, d);
			}else if(Float.class.equals(field.getType())){
				Float f = Float.valueOf(value);
				field.set(obj, f);
			}else if(Long.class.equals(field.getType())){
				Long l = Long.valueOf(value);
				field.set(obj, l);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new CommonServiceException(" 属性字段 [" + fieldName + "] " +
					"的值有问题 无法转成 java.util.Date 对象 [" + obj + "]");
		}
	}

	public static String trace(StackTraceElement[] e) {
		boolean doNext = false;
		for (StackTraceElement s : e) {
			if (doNext) {
				return s.getMethodName();
			}
			doNext = s.getMethodName().equals("getStackTrace");
		}
		return null;
	}
	
	
	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 *
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 
	 * 
	 * 
	 * {name:'张三',age:12}
	 * <name>{name}</name>
	 * <xml>
	 * 
	 * 	<req>
	 * 	<name><[!DATA[{name}]]</name>
	 * 	<age>{age}</age>
	 * </req>
	 * 
	 * </xml>
	 * 
	 * "<root><hosp QueueNo=\"" + {queueno} + "\"/></root>" ;
	 * 
	 * 
	 * @param obj 需要赋值的对象
	 * @param fieldName 赋值的属性名称
	 * 
	 * @return 获取对象的DeclaredField
	 */
	private static Field getAccessibleField(final Object obj, final String fieldName) {
		for (Class<?> superClass = obj.getClass(); 
			superClass != Object.class; 
			superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	
	
	
	public DModule getModule() {
		return module;
	}

	/**
	 * Module配置信息
	 */
	protected DModule module;
	
	@Override
	public void setModule(DModule module) {
		this.module = module;
	}
	/**
	 * 测试表示
	 */
	protected boolean debug;
	@Override
	public void setDebug(boolean isDebug) {
		this.debug = isDebug;
	}

	
	/**
	 * 调用医院端接口异常返回
	 * @param reqParam 请求参数
	 * @param retStr 返回参数
	 * @return Code = -1446
	 */
	protected String getReqHosClientErrorMessage(String reqParam,String retStr) {
		return getReqHosClientErrorMessage(module, reqParam,retStr);
	}
	
	/**
	 * 调用医院端接口异常返回
	 * @param reqParam 请求参数
	 * @return Code = -1445
	 */
	protected String getReqHosClientErrorMessage(String reqParam){
		return getReqHosClientErrorMessage(module, reqParam);
	}
	/**
	 * 调用医院端接口异常返回
	 * @param e 异常信息
	 * @param msg 请求参数
	 * @return Code = -1445
	 */
	protected String getReqParamException(AbsHosException e,InterfaceMessage msg) {
		return getReqParamException(module, e, msg);
	}
	/**
	 * 业务异常
	 * @param e 异常信息
	 * @param msg 请求参数
	 * @return Code = -1445
	 */
	protected String getReqRRException(AbsHosException e,InterfaceMessage msg) {
		return getReqRRException(module, e, msg);
	}
	 
	@Override
	public String testConn() {
		return getNotOveMethod(module);
	}
	@Override
	public String testConn(InterfaceMessage msg) {
		return "1";
	}
	
	/*
	 * Code节点名称
	 */
	private static final String RESPCODE = "RespCode";
	/**
	 * Message节点名称
	 */
	private static final String MESSAGE = "RespMessage";
	
	/**
	 * Data节点名称
	 */
	private static final String DATA = "Data";
	
	public JSONObject jsonResult(int code, String message){
		JSONObject  j=new JSONObject();
		j.put(RESPCODE, code);
		j.put(MESSAGE, message);
		return j;
	}
	
	public JSONObject jsonData(int code, String message, Object result){
		JSONObject  j=new JSONObject();
		j.put(RESPCODE, code);
		j.put(MESSAGE, message);
		Object rs = result;
		if(result instanceof JSONObject 
				|| result instanceof JSONArray){
			rs = result.toString();
		}
		j.put(DATA, rs);
		return j;
	}
	
}
