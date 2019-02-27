package com.yihu.hos.util;
import java.text.ParseException;
import java.util.Iterator;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.DateOper;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.exception.JsonUtilException;

import net.sf.json.JSONObject;

/**
 * 常用的Json操作工具类
 * 
 * @author Administrator
 * 
 */
public class JSONUtil {

	public static Boolean getJsonBoolean(net.sf.json.JSONObject json,
			String key, boolean isMust) throws AbsHosException {
		boolean retval = false;
		Object obj = null;
		obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		try {
			retval = json.getBoolean(key);
		} catch (Exception e) {
			throw new JsonUtilException("从Json中解析数据.[json]对象时:" + key
					+ " 的值不是Boolean型.[json] = " + json.toString());
		}
		return retval;
	}
//	public static Boolean getJsonBoolean(JSONObject json, String key,
//			boolean isMust)  throws AbsHosException {
//		boolean retval = false;
//		Object obj = null;
//		try {
//			obj = json.get(key);
//		} catch (JSONException e1) {
//			obj = null;
//		}
//		if (null == obj && isMust) {
//			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
//					+ " 的值.[json] = " + json.toString());
//		}
//		try {
//			retval = json.getBoolean(key);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			throw new JsonUtilException("从Json中解析数据.[json]对象时:" + key + " "
//					+ "的值不是Boolean型.[json] = " + json.toString());
//		}
//		return retval;
//	}
	/**
	 * @param json 数据源
	 * @param key key
	 * @param isMust 是否必须存在如果不存在抛出异常
	 * @param defVal 默认值
	 */
	public static Integer getJsonInt(net.sf.json.JSONObject json, 
			String key, boolean isMust,int defVal) throws AbsHosException {
		Integer retval = defVal;
		if (null == json) {
			throw new JsonUtilException("入参不能为空.");
		}
		Object obj = null;
		obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if ((null == obj  || "".equals(obj.toString())) && isMust) {
			throw new JsonUtilException("参数："+key+"不能为空.");
		}
		if ((null == obj  || "".equals(obj.toString())) && !isMust) {
			return retval;
		}
		if (null == obj || "".equals(obj.toString())) {
			return retval;
		}
		try{
			retval = Integer.parseInt(obj.toString());
		}catch (NumberFormatException e) {
			if(isMust){
				throw new JsonUtilException("从Json中解析数据.[json]对象中:" + key
						+ " 的参数类型不正确.[json] = " + json.toString());
			}
			retval = 0;
		}
		return retval;
	}
	
	public static Integer getJsonInt(net.sf.json.JSONObject json, String key,
			boolean isMust)  throws AbsHosException {
		Integer retval = 0;
		if (null == json) {
			throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
					+ " 的值.[json] = " + json.toString());
		}
		if (null == obj || "".equals(obj.toString())) {
			
			return 0;
			
		}
		try{
			retval = Integer.parseInt(obj.toString());
		}catch (NumberFormatException e) {
			if(isMust){
				throw new JsonUtilException("从Json中解析数据.[json]对象中:" + key
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
//	 * @ throws AbsHosException 为必须时并且为空或格式不正确时抛出参数异常。
//	 */
//	public static java.sql.Date getJsonDate(net.sf.json.JSONObject json, String key)  throws AbsHosException {
//		return getJsonDate(json, key, false);
//	}
	/**
	 * 获取json 中的日期数据
	 * @param json json对象
	 * @param key key值
	 * @param isMust 是否必须  是必须 并且为空时抛出异常
	 * @return  返回 java.sql.Date 对象数据 
	 * @ throws AbsHosException 为必须时并且为空或格式不正确时抛出参数异常。
	 */
	public static java.sql.Date getJsonDate(net.sf.json.JSONObject json, String key,
			boolean isMust)  throws AbsHosException {
		String retval = "";
		if (null == json) {
			throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
		}
		Object obj = json.get(key);
		if(null == obj){
			obj = getO(json, key);
		}
		if (null == obj && isMust) {
			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
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
			throw new JsonUtilException("从Json中解析数据.[json]对象中 :" + key
					+ " 的值.[json] = " + json.toString() +" 不是合法的日期数据类型字符串");
		}
	}
//	/**
//	 * 获取json 中的日期数据
//	 * @param json json对象
//	 * @param key key值
//	 * @param isMust 是否必须  是必须 并且为空时抛出异常
//	 * @return  返回 java.sql.Date 对象数据 
//	 * @ throws AbsHosException 为必须时并且为空或格式不正确时抛出参数异常。
//	 */
//	public static java.sql.Date getJsonDate(JSONObject json, String key,
//			boolean isMust)  throws AbsHosException {
//		String retval = "";
//		if (null == json) {
//			throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
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
//			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
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
//			throw new JsonUtilException("从Json中解析数据.[json]对象中 :" + key
//					+ " 的值.[json] = " + json.toString() +" 不是合法的日期数据类型字符串");
//		}
//	}
//	public static Integer getJsonInt(JSONObject json, String key, boolean isMust)
//		 throws AbsHosException {
//		Integer retval = 0;
//		if (null == json) {
//			throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
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
//			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
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
//			throw new JsonUtilException("从Json中解析数据.[json]对象时:" + key
//					+ " 的值不是整型数.[json] = " + json.toString());
//		}
//
//		return retval;
//
//	}

	public static String getJsonString(JSONObject json, String key)
		 throws AbsHosException {
		return getJsonString(json, key, false);
	}

//	public static String getJsonString(net.sf.json.JSONObject json, String key)
//		 throws AbsHosException {
//		return getJsonString(json, key, false);
//	}
	
	public static String getJsonString(JSONObject json, String key,
			boolean isMust)  throws AbsHosException {
		try {
			if (null == json) {
				throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
			}
			Object obj = null;
			
			obj = json.get(key);
			if(null == obj){
				obj = getO(json, key);
			}
			if (null == obj && isMust) {
				throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
						+ " 的值.[json] = " + json.toString());
			}
			if (null == obj) {
				return null;
			}
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getJsonString(com.alibaba.fastjson.JSONObject json, String key)
			 throws AbsHosException {
			return getJsonString(json, key, false);
		}

//		public static String getJsonString(net.sf.json.JSONObject json, String key)
//			 throws AbsHosException {
//			return getJsonString(json, key, false);
//		}
		
		public static String getJsonString(com.alibaba.fastjson.JSONObject json, String key,
				boolean isMust)  throws AbsHosException {
			try {
				if (null == json) {
					throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
				}
				Object obj = null;
				
				obj = json.get(key);
				if(null == obj){
					obj = getO(json, key);
				}
				if (null == obj && isMust) {
					throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
							+ " 的值.[json] = " + json.toString());
				}
				if (null == obj) {
					return null;
				}
				return obj.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	
	public static Object getO(JSONObject json,String key){
		Object obj = null;
		if(null == obj){
			Iterator<String> keys = json.keys();
			while(keys.hasNext()){
				String k = keys.next();
				if(k.toUpperCase().equals(key.toUpperCase())){
					obj = json.get(k);
				}
			}
		}
		return obj;
	}
	
	public static Object getO(com.alibaba.fastjson.JSONObject json,String key){
		Object obj = null;
		if(null == obj){
			Iterator<String> keys = json.keySet().iterator();
			while(keys.hasNext()){
				String k = keys.next();
				if(k.toUpperCase().equals(key.toUpperCase())){
					obj = json.get(k);
				}
			}
		}
		return obj;
	}
	
//	public static Object getO(net.sf.json.JSONObject json,String key) {
//		Object obj = null;
//		if(null == obj){
//			Iterator<String> keys = json.keys();
//			while(keys.hasNext()){
//				String k = keys.next();
//				if(k.toUpperCase().equals(key.toUpperCase())){
//					obj = json.get(k);
//				}
//			}
//		}
//		return obj;
//	}
	
//	public static String getJsonString(net.sf.json.JSONObject json, String key,
//			boolean isMust)  throws AbsHosException {
//		if (null == json) {
//			throw new JsonUtilException("从Json中解析数据.[json]对象为空.");
//		}
//		Object obj = null;
//		obj = json.get(key);
//		if(null == obj){
//			obj = getO(json, key);
//		}
//		if (null == obj && isMust) {
//			throw new JsonUtilException("从Json中解析数据.[json]对象中没有:" + key
//					+ " 的值.[json] = " + json.toString());
//		}
//		if (null == obj) {
//			return null;
//		}
//		return obj.toString();
//	}
	
	 /**
	   * 得到格式化json数据  退格用\t 换行用\r
	   */
	  public static String format(String jsonStr) {
	    int level = 0;
	    StringBuffer jsonForMatStr = new StringBuffer();
	    for(int i=0;i<jsonStr.length();i++){
	      char c = jsonStr.charAt(i);
	      if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
	        jsonForMatStr.append(getLevelStr(level));
	      }
	      switch (c) {
	      case '{': 
	      case '[':
	        jsonForMatStr.append(c+"\n");
	        level++;
	        break;
	      case ',': 
	        jsonForMatStr.append(c+"\n");
	        break;
	      case '}':
	      case ']':
	        jsonForMatStr.append("\n");
	        level--;
	        jsonForMatStr.append(getLevelStr(level));
	        jsonForMatStr.append(c);
	        break;
	      default:
	        jsonForMatStr.append(c);
	        break;
	      }
	    }
	    
	    return jsonForMatStr.toString();

	  }
	  
	  private static String getLevelStr(int level){
	    StringBuffer levelStr = new StringBuffer();
	    for(int levelI = 0;levelI<level ; levelI++){
	      levelStr.append("\t");
	    }
	    return levelStr.toString();
	  }
	  public static void main(String[] args) {
		    String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		    String fotmatStr = format(jsonStr);
//				fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
//				fotmatStr = fotmatStr.replaceAll("\t", "    ");
		    KasiteConfig.print(fotmatStr);
		  }
}
