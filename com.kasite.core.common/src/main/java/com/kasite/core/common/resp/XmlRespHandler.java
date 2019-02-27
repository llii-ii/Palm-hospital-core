package com.kasite.core.common.resp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.coreframework.util.Time;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.ReflectionUtils;

/**
 * xml格式返回值处理
 * @className: XmlRespHandler
 * @author: lcz
 * @date: 2018年7月19日 下午6:19:43
 */
public class XmlRespHandler extends BaseRespHandler{
	
	@Override
	public Document parse() {
		Document doc = DocumentHelper.createDocument();
		Element resp = doc.addElement(KstHosConstant.RESP);
		addElement(resp, KstHosConstant.TRANSACTIONCODE, data.getTransactionCode());
		addElement(resp, KstHosConstant.RESPCODE, data.getCode());
		addElement(resp, KstHosConstant.RESPMESSAGE, data.getMessage());
		if(data!=null) {
			if(data.getPIndex()!=null && data.getPSize()!=null && data.getPSize()>0) {
				Element page = resp.addElement(KstHosConstant.PAGE);
				addElement(page, KstHosConstant.PINDEX, data.getPIndex());
				addElement(page, KstHosConstant.PSIZE, data.getPSize());
				addElement(page, KstHosConstant.PCOUNT, data.getPCount());
			}
			if(data.getList()!=null && data.getList().size()>0) {
				for (Object obj : data.getList()) {
					objToElement(KstHosConstant.DATA,resp.addElement(KstHosConstant.DATA), obj);
				}
			}
		}
//		this.data.setDoc(respdoc);
//		this.data.setRoot(doc.getRootElement());
		return doc;
	}
	
	
	
	@Override
	public String parseString() {
		return this.parse().asXML();
	}



	public void jsonObjectToElement(Element el,JSONObject json){
		for (Entry<String, Object> entry : json.entrySet()) {
			String keyName = entry.getKey();
			if(cfg.isUppercase()) {
				keyName = uppercase(keyName);
			}
			Object val = entry.getValue();
			objToElement(keyName,el.addElement(keyName), val);
		}
	}
	
	public void jsonArrayToElement(Element el,JSONArray array,String keyName){
		for (int i=0;i<array.size();i++) {
			jsonObjectToElement(el.addElement(keyName), array.getJSONObject(i));
		}
	}
	public void mapToElementByApiKey(Element el,Map<ApiKey,Object> map){
		Set<Entry<ApiKey, Object>> set = map.entrySet();
		for (Entry<ApiKey, Object> entry : set) {
			ApiKey key = entry.getKey();
			objToElement(key.getName(),el.addElement(key.getName()), entry.getValue());
		}
	}
	public void mapToElement(Element el,Map<Object,Object> map){
		Set<Entry<Object, Object>> set = map.entrySet();
		for (Entry<Object, Object> entry : set) {
			String keyName = null;
			Object key = entry.getKey();
			if(ApiKey.class.isAssignableFrom(key.getClass())) {
				keyName = ((ApiKey)key).getName();
			}else {
				keyName = key.toString();
			}
			objToElement(keyName,el.addElement(keyName), entry.getValue());
		}
	}
	public void listToElement(Element el,List<?> list,String keyName){
		for (Object object : list) {
			objToElement(keyName,el.addElement(keyName), object);
		}
	}
	@SuppressWarnings("unchecked")
	public void objToElement(String parentKey,Element el,Object obj){
		if(obj==null) {
			el.addText("");
			return;
		}
		if(obj instanceof RespMap) {
			RespMap m = (RespMap) obj;
			obj = m.getMap();
		}
		if(JSONObject.class.isAssignableFrom(obj.getClass())) {
			jsonObjectToElement(el, (JSONObject)obj);
		}else if(Map.class.isAssignableFrom(obj.getClass())) {
			reflexALL(parentKey, el,obj);
			Map<Object, Object> map = (Map<Object, Object>)obj;
			if(map.keySet().size()>0) {
				mapToElement(el, map);
			}
		}else if(String.class.isAssignableFrom(obj.getClass())){
			if(isContainIllegalCharacters(obj.toString())) {
				el.addCDATA(obj.toString());
			}else {
				el.addText(obj.toString());
			}
		}else if(Integer.class.isAssignableFrom(obj.getClass())
					|| Long.class.isAssignableFrom(obj.getClass())
					|| Float.class.isAssignableFrom(obj.getClass())
					|| Double.class.isAssignableFrom(obj.getClass())
					|| Boolean.class.isAssignableFrom(obj.getClass())
				){
			el.addText(obj.toString());
		}else if(java.sql.Date.class.isAssignableFrom(obj.getClass())){
			SimpleDateFormat sdf = new SimpleDateFormat(Time.DBDATE);
			el.addText(sdf.format(obj));
		}else if(Timestamp.class.isAssignableFrom(obj.getClass())
				|| Date.class.isAssignableFrom(obj.getClass())){
			SimpleDateFormat sdf = new SimpleDateFormat(Time.DOTSCHEDULER_DATE2);
			el.addText(sdf.format(obj));
		}else {
			reflexALL(parentKey, el,obj);
		}
	}
	/**
	 * 反射传入class类的属性
	 * @Description: 
	 * @param parentKey
	 * @param el
	 * @param obj
	 * @param clazz
	 */
	public void reflex(String parentKey,Element el,Object obj,Class<?> clazz) {
		Field[] ff = clazz.getDeclaredFields();
		for (Field field : ff) {
			int mod = field.getModifiers();
			//过滤静态及final类型的属性
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
			String name = field.getName();
			//过滤返回节点
			boolean b = filterColumns(clazz, parentKey,name);
			if(!b) {
				continue;
			}
			
			String keyName = field.getName();
			if(cfg.isUppercase()) {
				keyName = uppercase(keyName);
			}
			Object oo = ReflectionUtils.getFieldValue(obj, name);
			if(oo==null){
				el.addElement(keyName).addText("");
			}else if(JSONArray.class.isAssignableFrom(oo.getClass())) {
				jsonArrayToElement(el, (JSONArray)oo,keyName);
			}else if(List.class.isAssignableFrom(oo.getClass())) {
				listToElement(el, (List<?>)oo,keyName);
			}else{
				objToElement(keyName,el.addElement(keyName), oo);
			}
		}
	}
	/**
	 * 反射对象的属性，包括父类属性
	 * @Description: 
	 * @param parentKey
	 * @param el
	 * @param obj
	 */
	public void reflexALL(String parentKey,Element el,Object obj) {
		Class<?> clazz = obj.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			reflex(parentKey, el, obj, clazz);
		}
	}
	/**
	 * 返回字段过滤
	 * @Description: 
	 * @return
	 */
	public boolean filterColumns(Class<?> clazz,String parentKey,String name) {
		if(StringUtil.isNotBlank(cfg.getColumns())) {
			String[] filter = cfg.getColumns().split("\\|");
			for (String ff : filter) {
				String[] arrays = ff.split("=");
				if(parentKey.equalsIgnoreCase(arrays[0])) {
					String[] columns = arrays[1].split(",");
					for (String column : columns) {
						if(name.equalsIgnoreCase(column)) {
							return true;
						}
					}
					return false;
				}
			}
		}
		
		if(StringUtil.isNotBlank(cfg.getExcludeColumns())) {
			String[] filter = cfg.getExcludeColumns().split("\\|");
			for (String ff : filter) {
				String[] arrays = ff.split("=");
				if(parentKey.equalsIgnoreCase(arrays[0])) {
					String[] columns = arrays[1].split(",");
					for (String column : columns) {
						if(name.equalsIgnoreCase(column)) {
							return false;
						}
					}
					return true;
				}
			}
		}
		//过滤Map类型父类特有的属性
		if(Map.class.isAssignableFrom(clazz)) {
			if("table".equals(name) || "entrySet".equals(name) 
					|| "threshold".equals(name) || "keySet".equals(name) 
					|| "values".equals(name) 
					|| "size".equals(name) || "modCount".equals(name) ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否包含XML非法字符
	 * @param str
	 * @return
	 */
	public boolean isContainIllegalCharacters(String str) {
		if(StringUtil.isBlank(str)) {
			return false;
		}else if(str.contains("&") || str.contains("<") || str.contains(">")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 首字母大写
	 * @Description: 
	 * @param name
	 * @return
	 */
	private String uppercase(String name) {
		return name.substring(0,1).toUpperCase()+name.substring(1);
	}
}
