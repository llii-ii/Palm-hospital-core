package com.kasite.core.elastic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.coreframework.util.ReflectionUtils;
import com.coreframework.util.StringUtil;

public class ElasticJSONUtil {


	public static Boolean getJsonBoolean(JSONObject json,
			String key, boolean isMust) throws ElasticParamException, Exception {
		boolean retval = false;
		Object obj = null;
		obj = json.get(key);
		if(null == obj){ 
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		try {
			retval = json.getBooleanValue(key);
		} catch (Exception e) {
			throw new ElasticParamException("从Json中解析数据.[json]对象时:" + key
					+ " 的值不是Boolean型.[json] = " + json.toString());
		}
		return retval;
	}
	public static Integer getJsonInt(com.alibaba.fastjson.JSONObject json, 
			String key, int defVal)throws ElasticParamException, Exception {
		return getJsonInt(json, key,false,defVal);
		
	}
	 /**
     * 
     * @param json
     * @param key
     * @param defaultVal 默认值（为空或者接点不存在时返回）
     * @return
     * @throws ElasticParamException
     */
    public static  Integer getJsonInt(JSONObject json,String key,Integer defaultVal) throws ElasticParamException, Exception {
		if(null == json){
			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = null;
		try{
			obj = json.get(key);
		}catch (Exception e) {
			obj = null;
		}
		if(StringUtil.isBlank(obj) || "null".equals(obj) || "".equals(obj.toString())){
			return defaultVal;
		}
		try{
			return json.getInteger(key);
		}catch (Exception e) {
			throw new ElasticParamException("从Json中解析数据.[json]对象时:"+ key +" 的值不是整型数.[json] = "+ json.toString());
		}
		
	}
	/**
	 * @param json 数据源
	 * @param key key
	 * @param isMust 是否必须存在如果不存在抛出异常
	 * @param defVal 默认值
	 * @throws Exception 
	 */
	public static Integer getJsonInt(JSONObject json, 
			String key, boolean isMust,int defVal)throws ElasticParamException, Exception {
		Integer retval = defVal;
		if (null == json) {
			throw new ElasticParamException("入参不能为空.");
		}
		Object obj = null;
		obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if ((null == obj  || "".equals(obj.toString()) || "null".equals(obj.toString())) && isMust) {
			throw new ElasticParamException("参数："+key+"不能为空.");
		}
		if ((null == obj  || "".equals(obj.toString()) || "null".equals(obj.toString())) && !isMust) {
			return retval;
		}
		if (null == obj || "".equals(obj.toString()) || "null".equals(obj.toString())) {
			return retval;
		}
		try{
			retval = Integer.parseInt(obj.toString());
		}catch (NumberFormatException e) {
			if(isMust){
				throw new ElasticParamException("从Json中解析数据.[json]对象中:" + key
						+ " 的参数类型不正确.[json] = " + json.toString());
			}
			retval = 0;
		}
		return retval;
	}

	public static Integer getJsonInt(JSONObject json, String key) throws ElasticParamException, Exception {
		return getJsonInt(json, key,false);
	}
	public static Double getJsonDouble(JSONObject json, String key) throws ElasticParamException, Exception {
		return getJsonDouble(json, key,false);
	}
	public static Double getJsonDouble(JSONObject json, String key,boolean isMust) throws ElasticParamException, Exception{
		Double retval = new Double(0);
		if (null == json) {
			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		if (null == obj || "".equals(obj.toString()) || "null".equals(obj.toString())) {
			return retval;
		}
		try{
			retval = Double.parseDouble(obj.toString());
		}catch (NumberFormatException e) {
			if(isMust){
				throw new ElasticParamException("从Json中解析数据.[json]对象中:" + key
						+ " 的值不是整数.[json] = " + json.toString());
			}
			retval = new Double(0);
		}
		return retval;
	}
	public static Integer getJsonInt(JSONObject json, String key,
			boolean isMust) throws ElasticParamException, Exception {
		Integer retval = 0;
		if (null == json) {
			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		if (null == obj || "".equals(obj.toString()) || "null".equals(obj.toString())) {
			
			return 0;
			
		}
		try{
			retval = Integer.parseInt(obj.toString());
		}catch (NumberFormatException e) {
			if(isMust){
				throw new ElasticParamException("从Json中解析数据.[json]对象中:" + key
						+ " 的值不是整数.[json] = " + json.toString());
			}
		}
		
		return retval;
		
	}
//	/**
//	 * 获取json 中的日期数据
//	 * @param json json对象
//	 * @param key key值
//	 * @param isMust 默认非必须 为空时返回 NULL
//	 * @return  返回 java.sql.Date 对象数据 
//	 * @throws ElasticParamException 为必须时并且为空或格式不正确时抛出参数异常。
//	 */
//	public static java.sql.Date getJsonDate(JSONObject json, String key) throws ElasticParamException, Exception {
//		return getJsonDate(json, key, false);
//	}
	/**
	 * 获取json 中的日期数据
	 * @param json json对象
	 * @param key key值
	 * @param isMust 是否必须  是必须 并且为空时抛出异常
	 * @return  返回 java.sql.Date 对象数据 
	 * @throws ElasticParamException 为必须时并且为空或格式不正确时抛出参数异常。
	 */
	public static java.sql.Date getJsonDate(JSONObject json, String key,
			boolean isMust) throws ElasticParamException, Exception {
		String retval = "";
		if (null == json) {
			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		if (null == obj || "".equals(obj.toString())) {
			return null;
		}
		retval = obj.toString();
		try {
			return new java.sql.Date(DateOper
					.parse(retval).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ElasticParamException("从Json中解析数据.[json]对象中 :" + key
					+ " 的值.[json] = " + json.toString() +" 不是合法的日期数据类型字符串");
		}
	}
//	/**
//	 * 获取json 中的日期数据
//	 * @param json json对象
//	 * @param key key值
//	 * @param isMust 是否必须  是必须 并且为空时抛出异常
//	 * @return  返回 java.sql.Date 对象数据 
//	 * @throws ElasticParamException 为必须时并且为空或格式不正确时抛出参数异常。
//	 */
//	public static java.sql.Date getJsonDate(JSONObject json, String key,
//			boolean isMust) throws ElasticParamException, Exception {
//		String retval = "";
//		if (null == json) {
//			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
//		}
//		Object obj = null;
//		try {
//			obj = json.get(key);
//			if(null == obj){
//				obj = getO(json, key);
//			}
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		if (null == obj && isMust) {
//			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
//					+ " 的值.[json] = " + json.toString());
//		}
//		if (null == obj || "".equals(obj.toString())) {
//			return null;
//		}
//		
//		try {
//			retval = json.getString(key);
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			return new java.sql.Date(DateOper
//					.parse(retval).getTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new ElasticParamException("从Json中解析数据.[json]对象中 :" + key
//					+ " 的值.[json] = " + json.toString() +" 不是合法的日期数据类型字符串");
//		}
//	}
//	public static Integer getJsonInt(JSONObject json, String key, boolean isMust)
//		throws ElasticParamException, Exception {
//		Integer retval = 0;
//		if (null == json) {
//			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
//		}
//		Object obj = null;
//		try {
//			obj = json.get(key);
//			if(null == obj){
//				obj = getO(json, key);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			obj = null;
//		}
//
//		if (null == obj && isMust) {
//
//			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
//					+ " 的值.[json] = " + json.toString());
//
//		}
//
//		if (null == obj || "".equals(obj.toString())) {
//
//			return 0;
//
//		}
//
//		try {
//
//			retval = json.getInt(key);
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			throw new ElasticParamException("从Json中解析数据.[json]对象时:" + key
//					+ " 的值不是整型数.[json] = " + json.toString());
//		}
//
//		return retval;
//
//	}

	public static String getJsonString(JSONObject json, String key)
		throws ElasticParamException, Exception {
		return getJsonString(json, key, false);
	}

	
	public static String getJsonString(JSONObject json, String key,
			boolean isMust) throws ElasticParamException, Exception {
		try {
			if (null == json) {
				throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
			}
			Object obj = null;
			obj = json.get(key);
			if(null == obj){
				obj = getO(json, key);
			}
			if ((null == obj || StringUtil.isBlank(obj+"")) && isMust) {
				throw new ElasticParamException("从Json中解析数据.[json]对象中没有:" + key
						+ " 的值.[json] = " + json.toString());
			}
			if (null == obj) {
				return null;
			}
			return obj.toString();
		} catch(ElasticParamException pe) {
			throw pe;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static  String getJsonString(JSONObject json,String key,
		boolean isMust,boolean isNull) throws ElasticParamException, Exception {
		
		if(null == json){
			
			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
			
		}
		Object obj = null;
		try{
			obj = json.get(key);
		}catch (Exception e) {
			obj = null;
		}
		
		if(null == obj && isMust){
			
			throw new ElasticParamException("从Json中解析数据.[json]对象中没有:"+ key +" 的值.[json] = "+ json.toString());
			
		}
		
		if(null == obj && isNull){
			
			return null;
			
		}else if(null == obj){
			return "";
		}
		
		return obj.toString();
		
	}
	
	
	
	
	public static Object getO(com.alibaba.fastjson.JSONObject json,String key){
		return json.get(key);
	}
	
	
	/**
	 * xml字符串转换成json字符串
	 * 
	 * @param xmlStr 字符串
	 * @return
	 * @throws Exception 
	 */
	public static String xmlToJson(String xmlStr) throws Exception {
		JSONObject jsonObj = new JSONObject();
		JSONArray array  = new JSONArray();
		Document doc = DocumentHelper.parseText(xmlStr);
		List<Element> ls = doc.getRootElement().elements();
		for (Element el : ls) {
			String key = el.getName();
			String val = el.getText();
			if("List".equals(key)){
				JSONObject js = new JSONObject();
				List<Element> element = el.elements("Entity");
				for (Element en : element) {
					Document document = DocumentHelper.parseText(en.asXML());
					List<Element> ls2 = document.getRootElement().elements();
					for (Element element2 : ls2) {
						js.put(element2.getName(), element2.getTextTrim());
					}
					array.add(js);
				}
				
			}else{
				jsonObj.put(key, val);
			}
		}
		jsonObj.put("Result", array.toString());
		return jsonObj.toString();
	}
	/**
	 * xml字符串转换成json字符串
	 * 
	 * @param xmlStr字符串
	 * @param removeKey 不需要的节点名称   多个以“|”分隔
	 * @return
	 * @throws Exception 
	 */
	public static String xmlToJson(String xmlStr,String removeKey) throws Exception {
		JSONObject jsonObj = new JSONObject();
		JSONArray array  = new JSONArray();
		Document doc = DocumentHelper.parseText(xmlStr);
		String[] str = null;
		if(!StringUtil.isBlank(removeKey)){
			str = removeKey.split("\\|");
		}
		List<Element> ls = doc.getRootElement().elements();
		for (Element el : ls) {
			boolean bool= false;
			String key = el.getName();
			String val = el.getText();
			if("List".equals(key)){
				JSONObject js = new JSONObject();
				List<Element> element = el.elements("Entity");
				for (Element en : element) {
					Document document = DocumentHelper.parseText(en.asXML());
					List<Element> ls2 = document.getRootElement().elements();
					for (Element element2 : ls2) {
						bool = false;
						if(str!=null){
							for(int i=0;i<str.length;i++){
								if(element2.getName().equals(str[i])){
									bool = true;
									continue;
								}
							}
							if(bool){
								continue;
							}
						}
						js.put(element2.getName(), element2.getTextTrim());
					}
					array.add(js);
				}
				
			}else{
				if(str!=null){
					for(int i=0;i<str.length;i++){
						if(key.equals(str[i])){
							bool = true;
							continue;
						}
					}
					if(bool){
						continue;
					}
				}
				jsonObj.put(key, val);
			}
		}
		jsonObj.put("Result", array.toString());
		return jsonObj.toString();
	}
	/**
	 * json字符串转换xml字符串
	 * @param jsonStr json字符串
	 * @return
	 * @throws Exception 
	 */
	public static String jsonToXml(String jsonStr) throws Exception {
		JSONObject json = JSONObject.parseObject(jsonStr);
		StringBuffer sb =  new StringBuffer();
		sb.append("<OutPut>");
		if(json.get("Result")!=null){
			Set<String> set = json.keySet();
			for (String key : set) {
				if(!"Result".equals(key)){
					sb.append("<" + key + ">");
					sb.append((json.get(key)!=null?json.get(key).toString():""));
					sb.append("</" + key + ">");
				}
			}
			if(json.get("Result")!=null){
				JSONArray array = JSONArray.parseArray(json.getString("Result"));
				if(!array.isEmpty()){
					sb.append("<List size=\"").append(array.size()).append("\">");
					for (int i=0;i<array.size();i++) {
						JSONObject js = array.getJSONObject(i);
						Set<String> set2 = js.keySet();
						sb.append("<Entity>");
						for (String key2 : set2) {
							sb.append("<" + key2 + ">");
							sb.append((js.get(key2)!=null?js.get(key2).toString():""));
							sb.append("</" + key2 + ">");
						}
						sb.append("</Entity>");
					}
					sb.append("</List>");
				}
			}
		}else{
			Set<String> set = json.keySet();
			for (String key : set) {
				if(!"Result".equals(key)){
					sb.append("<" + key + ">");
					sb.append((json.get(key)!=null?json.get(key).toString():""));
					sb.append("</" + key + ">");
				}
			}
		}
		sb.append("</OutPut>");
		return sb.toString();
	}
	/**
	 * json字符串转换xml字符串
	 * @param jsonStr json字符串
	 * @param removeKey 不需要的节点名称   多个以“|”分隔
	 * @return
	 * @throws Exception 
	 */
	public static String jsonToXml(String jsonStr,String removeKey) throws Exception {
		JSONObject json = JSONObject.parseObject(jsonStr);
		String[] str = null;
		if(!StringUtil.isBlank(removeKey)){
			str = removeKey.split("\\|");
		}
		boolean bol = false;
		StringBuffer sb =  new StringBuffer();
		sb.append("<OutPut>");
		if(json.get("Result")!=null){
			Set<String> set = json.keySet();
			for (String key : set) {
				bol = false;
				if(!"Result".equals(key)){
					if(str!=null){
						for(int i=0;i<str.length;i++){
							if(str[i].equals(key)){
								bol = true;
								continue;
							}
						}
						if(bol){
							continue;
						}
					}
					sb.append("<" + key + ">");
					sb.append((json.get(key)!=null?json.get(key).toString():""));
					sb.append("</" + key + ">");
				}
			}
			JSONArray array = JSONArray.parseArray(json.getString("Result"));
			if(array!=null && array.size()>0){
				sb.append("<List size=\"").append(array.size()).append("\">");
				for (int i=0;i<array.size();i++) {
					JSONObject js = array.getJSONObject(i);
					Set<String> set2 = js.keySet();
					sb.append("<Entity>");
					for (String key2 : set2) {
						bol = false;
						if(str!=null){
							for(int k=0;k<str.length;k++){
								if(str[k].equals(key2)){
									bol = true;
									continue;
								}
							}
							if(bol){
								continue;
							}
						}
						sb.append("<" + key2 + ">");
						sb.append((js.get(key2)!=null?js.get(key2).toString():""));
						sb.append("</" + key2 + ">");
					}
					sb.append("</Entity>");
				}
				sb.append("</List>");
			}
		}else{
			Set<String> set = json.keySet();
			for (String key : set) {
				if(!"Result".equals(key)){
					sb.append("<" + key + ">");
					sb.append((json.get(key)!=null?json.get(key).toString():""));
					sb.append("</" + key + ">");
				}
			}
		}
		sb.append("</OutPut>");
		return sb.toString();
	}
	/**
	 * 根据传入类型返回结果
	 * @param outType   需返回的类型			0: json  1: xml
	 * @param result    返回值
	 * @param nowType   当前传入结果类型		0: json  1: xml
	 * @return
	 */
	public static String retResult(int outType,String result,int nowType)throws Exception{
		if(outType==0 && nowType==1){			//xml字符串转化成json
			return xmlToJson(result);
		}else if(outType==1 && nowType==0){		//json字符串转化成xml
			return jsonToXml(result);
		}else{
			return result;
		}
	}
	/**
	 * 根据传入类型返回结果
	 * @param outType   需返回的类型			0: json  1: xml
	 * @param nowType   当前传入结果类型		0: json  1: xml
	 * @param removeKey 要删除的节点		
	 * @param result    返回值
	 * @return
	 */
	public static String retResult(int outType,String result,int nowType,String removeKey)throws Exception{
		if(StringUtil.isBlank(removeKey)){
			if(outType==0 && nowType==1){			//xml字符串转化成json
				return xmlToJson(result);
			}else if(outType==1 && nowType==0){		//json字符串转化成xml
				return jsonToXml(result);
			}else{
				return result;
			}
		}else{
			if(outType==0 && nowType==1){			//xml字符串转化成json
				return xmlToJson(result,removeKey);
			}else if(outType==1 && nowType==0){		//json字符串转化成xml
				return jsonToXml(result,removeKey);
			}else{
				return removeKey(outType,result,removeKey);
			}
		}
		
	}
	private static String removeKey(int outType,String result,String removeKey)throws Exception{
		if(outType==0 && !StringUtil.isBlank(removeKey)){
			String[] str = removeKey.split("\\|");
			JSONObject js = JSONObject.parseObject(result);
			for(int i=0;i<str.length;i++){
				js.remove(str[i]);
			}
			if(js.get("Result")!=null){
				JSONArray arr = js.getJSONArray("Result");
				if(arr!=null && arr.size()>0){
					for(int i=0;i<arr.size();i++){
						for(int k=0;k<str.length;k++){
							js.getJSONArray("Result").getJSONObject(i).remove(str[k]);
						}
					}
				}
			}
			return js.toString();
		}else if(outType==1 && !StringUtil.isBlank(removeKey)){
			Document doc = DocumentHelper.parseText(result);
			String[] str = removeKey.split("\\|");
			List<Element> ls = doc.getRootElement().elements();
			for (Element el : ls) {
				String key = el.getName();
				String val = el.getText();
				if("List".equals(key)){
					List<Element> element = el.elements("Entity");
					for (Element en : element) {
						List<Element> ls2 = en.elements();
						for (Element element2 : ls2) {
							if(str!=null){
								for(int i=0;i<str.length;i++){
									if(element2.getName().equals(str[i])){
										element2.getParent().remove(element2);
										continue;
									}
								}
							}
						}
					}
				}else{
					for(int i=0;i<str.length;i++){
						if(key.equals(str[i])){
							el.getParent().remove(el);
							continue;
						}
					}
				}
			}
			return doc.asXML();
		}else{
			return result;
		}
	}
	 public static Long getJsonLong(com.alibaba.fastjson.JSONObject json,String key,Long defaultVal) throws ElasticParamException, Exception {
	  		if(null == json){
	  			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
	  		}
	  		Object obj = null;
	  		try{
	  			obj = json.get(key);
	  		}catch (Exception e) {
	  			obj = null;
	  		}
	  		if(StringUtil.isBlank(obj) || "null".equals(obj) || "".equals(obj.toString())){
	  			return defaultVal;
	  		}
	  		try{
	  			return json.getLong(key);
	  		}catch (Exception e) {
	  			throw new ElasticParamException("从Json中解析数据.[json]对象时:"+ key +" 的值不是整型数.[json] = "+ json.toString());
	  		}
	  	}
	    public static Double getJsonDouble(com.alibaba.fastjson.JSONObject json,String key,Double defaultVal) throws ElasticParamException, Exception {
	  		if(null == json){
	  			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
	  		}
	  		Object obj = null;
	  		try{
	  			obj = json.get(key);
	  		}catch (Exception e) {
	  			obj = null;
	  		}
	  		if(StringUtil.isBlank(obj) || "null".equals(obj) || "".equals(obj.toString())){
	  			return defaultVal;
	  		}
	  		try{
	  			return json.getDouble(key);
	  		}catch (Exception e) {
	  			throw new ElasticParamException("从Json中解析数据.[json]对象时:"+ key +" 的值不是浮点数类型.[json] = "+ json.toString());
	  		}
	  	}
	    public static Float getJsonFloat(com.alibaba.fastjson.JSONObject json,String key,Float defaultVal) throws ElasticParamException, Exception {
	  		if(null == json){
	  			throw new ElasticParamException("从Json中解析数据.[json]对象为空.");
	  		}
	  		Object obj = null;
	  		try{
	  			obj = json.get(key);
	  		}catch (Exception e) {
	  			obj = null;
	  		}
	  		if(StringUtil.isBlank(obj) || "null".equals(obj) || "".equals(obj.toString())){
	  			return defaultVal;
	  		}
	  		try{
	  			return Float.parseFloat(json.getString(key));
	  		}catch (Exception e) {
	  			throw new ElasticParamException("从Json中解析数据.[json]对象时:"+ key +" 的值不是浮点数类型.[json] = "+ json.toString());
	  		}
	  	}
	
	
	public static JSONObject parse2JsonObj(String jsonStr) throws ElasticParamException, Exception {
		//将医院返回的锁号信息放入到 参数中
		if(StringUtil.isNotBlank(jsonStr)){
			try{
				String storestr = jsonStr;
				if(storestr.length() > 1){
					String s1 = storestr.substring(0, 1);
					if("\"".equals(s1)){
						storestr = storestr.substring(1, storestr.length()-1);
					}
				}
				return JSONObject.parseObject(storestr);
			}catch(Exception e){
				throw new ElasticParamException("Json 字符串有问题无法转成 JSONObject 对象： "+ jsonStr);
			}
		}
		return null;
	}
	
	public static JSONObject toJSONObject(Object obj){
		return JSONObject.parseObject(obj.toString());
    }
	    
    public static JSONArray toJSONArray(Object obj){
    	JSONArray array = new JSONArray();
    	if(obj instanceof List<?>){
    		List<?> list = (List<?>) obj;
    		for (Object object : list) {
    			array.add(JSONObject.toJSON(object));
			}
    	}else{
    		array.add(JSONObject.toJSON(obj));
    	}
		return array;
    }
    public static <T> T toBean(JSONObject json,Class<T> cls) throws Exception{
//    	return toBean(JSONObject.parseObject(jsonStr), cls);
    	return JSONObject.toJavaObject(json, cls);
    }
//    
//    public static <T> T toBean(com.alibaba.fastjson.JSONObject json,Class<T> cls) throws Exception{
//		T entity = cls.newInstance();
//		java.lang.reflect.Field[] fs = cls.getDeclaredFields();
//		for (java.lang.reflect.Field f : fs) {
//			String name = f.getName();
//			if (null == json || !json.containsKey(name)) {
//				continue;
//			}
//			Object obj = json.get(name);
//			// 过滤为null 类型数据，但不过滤空串
//			if (obj == null) {
//				continue;
//			} else if (JSONObject.class.isAssignableFrom(obj.getClass()) && ((JSONObject) obj).isEmpty()) {
//				continue;
//			}
//			// 将Json字符串转换为对象属性所属类型
//			if (String.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), obj.toString());
//			} else if (Integer.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), getJsonInt(json, name, null));
//			} else if (Timestamp.class.isAssignableFrom(f.getType())) {
//				try{
//					ReflectionUtils.setFieldValue(entity, f.getName(), Timestamp.valueOf(obj.toString()));
//				}catch (IllegalArgumentException e) {
//					ReflectionUtils.setFieldValue(entity, f.getName(), new Timestamp(DateOper.parse(obj.toString()).getTime()));
//				}
//			} else if (java.sql.Date.class.isAssignableFrom(f.getType())) {
//				try{
//					ReflectionUtils.setFieldValue(entity, f.getName(), java.sql.Date.valueOf(obj.toString()));
//				}catch (IllegalArgumentException e) {
//					ReflectionUtils.setFieldValue(entity, f.getName(), new java.sql.Date(DateOper.parse(obj.toString()).getTime()));
//				}
//			} else if (java.util.Date.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), DateOper.parse(obj.toString()));
//			} else if (Long.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), getJsonLong(json, name, null));
//			} else if (Double.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), getJsonDouble(json, name, null));
//			} else if (Float.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), getJsonFloat(json, name, null));
//			}  else if (Boolean.class.isAssignableFrom(f.getType())) {
//				ReflectionUtils.setFieldValue(entity, f.getName(), json.getBoolean(name));
//			}  else if(AbsElasticResponse.class.isAssignableFrom(f.getType())){
//				try{
//					String entityJson = json.getString(name);
//					parseJSONTokener2Object(entityJson, entity, f, 1);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			} else if(Collection.class.isAssignableFrom(f.getType())){
//				try{
//					String entityJson = json.getString(name);
//					parseJSONTokener2Object(entityJson, entity, f, 2);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			} else{
//				System.out.println( f.getType());
//			}
//		}
//		return entity;
// }
//	private static void parseJSONTokener2Object(String stroeJsonStr,Object entity,java.lang.reflect.Field f,int type) throws Exception{
//		JSONTokener tk = new JSONTokener(stroeJsonStr);
//		Object json = tk.nextValue();
//		if(type == 1){
//			if(json instanceof JSONObject){
//				 JSONObject storeJson = (JSONObject)json;
//				 Object entityObj = toBean(storeJson, f.getType());
//				 ReflectionUtils.setFieldValue(entity, f.getName(), entityObj);
//			}
//		}else {
//			if(json instanceof JSONArray){
//				JSONArray jsonArray = (JSONArray)json;
//				f.getType().getTypeParameters();
//				 Type genericType = f.getGenericType();
//				 if(genericType == null) return;    
//		        // 如果是泛型参数的类型     
//		        if(genericType instanceof ParameterizedType){     
//		            ParameterizedType pt = (ParameterizedType) genericType;  
//		            //得到泛型里的class类型对象    
//		            Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];   
//		            Object entityObj = toBeanList(jsonArray, genericClazz);
//					ReflectionUtils.setFieldValue(entity, f.getName(), entityObj);
//		        }     
//				
//			}
//		}
//	}
//   	public static <T> List<T> toBeanList(JSONArray array,Class<T> cls) throws Exception{
//		if(array==null || array.size()<=0){
//			return null;
//		}
//		List<T> ll = new Vector<T>();
//		for(int i=0;i<array.size();i++){
//			T t = toBean(array.getJSONObject(i), cls);
//			ll.add(t);
//		}
//		return ll;
//    }
}
