package com.coreframework.util;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.beanutils.PropertyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	private static boolean isDebug = false;

	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static String obj2JsonString(Object object) {
		return net.sf.json.JSONObject.fromObject(object).toString();
	}

	public static String array2JsonString(Collection array) {
		return JSONArray.fromObject(array).toString();
	}

	private static Map<String, Object> encodeObject2Map(Object entity, String[] fields) {
		Map rowMap = new HashMap();
		String[] arrayOfString;
		int j = (arrayOfString = fields).length;
		for (int i = 0; i < j; i++) {
			String column = arrayOfString[i];
			Object value = null;
			try {
				value = PropertyUtils.getProperty(entity, column);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (value != null) {
				rowMap.put(column, value);
			} else {
				rowMap.put(column, "");
			}
		}
		return rowMap;
	}

	public static String encodeObject2Json(Object pObject, String[] fields) {
		return encodeObject2Json(pObject, null, fields);
	}

	public static String encodeObject2Json(Object pObject, String pFormatString, String[] fields) {
		String jsonString = "[]";
		if (!StringUtil.isBlank(pObject)) {
			JsonConfig cfg = new JsonConfig();
			if (StringUtil.isNotBlank(pFormatString)) {
				cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessorImpl(pFormatString));
				cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(pFormatString));
				cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(pFormatString));
			}
			if ((fields != null) && (fields.length > 0)) {
				if ((pObject instanceof Collection)) {
					List jsonMapList = new ArrayList();
					Collection list = (Collection) pObject;
					for (Object entity : list) {
						Map rowMap = encodeObject2Map(entity, fields);

						jsonMapList.add(rowMap);
					}
					JSONArray jsonObj = JSONArray.fromObject(jsonMapList, cfg);
					jsonString = jsonObj.toString();
				} else {
					Map rowMap = encodeObject2Map(pObject, fields);
					net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(rowMap, cfg);
					jsonString = jsonObject.toString();
				}
			} else if ((pObject instanceof ArrayList)) {
				JSONArray jsonArray = JSONArray.fromObject(pObject, cfg);
				jsonString = jsonArray.toString();
			} else {
				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(pObject, cfg);
				jsonString = jsonObject.toString();
			}
		}
		if (isDebug) {
			System.out.println("���������������JSON������������:\n" + jsonString);
		}
		return jsonString;
	}

	public static String encodeJson2PageJson(String jsonString, Integer totalCount) {
		StringBuilder gridBuffer = new StringBuilder();
		gridBuffer.append("{\"total\":").append(totalCount).append(",\"rows\":").append(jsonString).append("}");
		if (isDebug) {
			System.out.println("������������JSON������������:\n" + gridBuffer.toString());
		}
		return gridBuffer.toString();
	}

	public static String encodeList2PageJson(List list, Integer totalCount, String dataFormat, String[] fields) {
		StringBuilder gridBuffer = new StringBuilder();
		String subJsonString = encodeObject2Json(list, dataFormat, fields);
		gridBuffer.append("{\"total\":").append(totalCount).append(",\"rows\":").append(subJsonString).append("}");
		return gridBuffer.toString();
	}

	public static <T> T parseJson2Entity(Class<T> entityClass, String jsonString) {
		if (StringUtil.isBlank(jsonString)) {
			return (T) EntityUtil.getNewEntityObject(entityClass);
		}
		JSONObject json = JSON.parseObject(jsonString);
		T entity = json.toJavaObject(entityClass);
		Field[] fs = entityClass.getDeclaredFields();
		Field[] arrayOfField1;
		int j = (arrayOfField1 = fs).length;
		for (int i = 0; i < j; i++) {
			Field f = arrayOfField1[i];
			String name = f.getName();
			if ((f.getType().getName().indexOf("Queue") >= 0) || (f.getType().getName().indexOf("SortedSet") >= 0)
					|| (f.getType().getName().indexOf("Deque") >= 0) || (f.getType().getName().indexOf("List") >= 0)
					|| (f.getType().getName().indexOf("Set") >= 0)) {
				String jsonglist = json.getString(name);
				Collection list = parseArrayJson2List(f.getDeclaringClass(), jsonglist);
				try {
					ReflectionUtils.setFieldValue(entity, f.getName(), list);
				} catch (Exception localException) {
				}
			}
		}
		return entity;
	}

	public static <T> List<T> parseJson2List(Class<T> entityClass, String jsonString) {
		List list = new ArrayList();
		net.sf.json.JSONObject jbJsonObject = net.sf.json.JSONObject.fromObject(jsonString);
		Iterator iterator = jbJsonObject.keySet().iterator();
		while (iterator.hasNext()) {
			Object entity = parseJson2Entity(entityClass, jbJsonObject.getString(iterator.next().toString()));
			list.add(entity);
		}
		return list;
	}

	public static <T> List<T> parseArrayJson2List(Class<T> entityClass, String jsonString) {
		List list = new ArrayList();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		for (int i = 0; i < jsonArray.size(); i++) {
			net.sf.json.JSONObject object = (net.sf.json.JSONObject) jsonArray.get(i);
			Object entity = net.sf.json.JSONObject.toBean(object, entityClass);
			list.add(entity);
		}
		return list;
	}

	public static List<Map> parseArrayJson2MapList(String jsonString) {
		List list = new ArrayList();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		for (int i = 0; i < jsonArray.size(); i++) {
			list.add(parseJson2Map(jsonArray.getString(i)));
		}
		return list;
	}

	public static Map parseJson2Map(String jsonString) {
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();

		Map valueMap = new HashMap();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = String.valueOf(jsonObject.get(key));
			valueMap.put(key, value);
		}
		return valueMap;
	}

	public static Object[] parseJson2ObjectArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	public static String[] parseJson2StringArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}

	public static Long[] parseJson2LongArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = Long.valueOf(jsonArray.getLong(i));
		}
		return longArray;
	}

	public static Integer[] parseJson2IntegerArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = Integer.valueOf(jsonArray.getInt(i));
		}
		return integerArray;
	}

	public static java.util.Date[] parseJson2DateArray(String jsonString, String DataFormat) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		java.util.Date[] dateArray = new java.util.Date[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			String dateString = jsonArray.getString(i);
			java.util.Date date = DateOper.parseDateFormat(dateString, DataFormat);
			dateArray[i] = date;
		}
		return dateArray;
	}

	public static Double[] parseJson2DoubleArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Double[] doubleArray = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			doubleArray[i] = Double.valueOf(jsonArray.getDouble(i));
		}
		return doubleArray;
	}

	public static Boolean getJsonBoolean(net.sf.json.JSONObject json, String key, boolean isMust) throws Exception {
		boolean retval = false;

		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {
			throw new Exception(
					"���Json���������������.[json]���������������:" + key + " ������.[json] = " + json.toString());
		}
		try {
			retval = json.getBoolean(key);
		} catch (Exception e) {
			throw new Exception("���Json���������������.[json]���������:" + key + " ������������Boolean���.[json] = "
					+ json.toString());
		}
		return Boolean.valueOf(retval);
	}

	public static Boolean getJsonBoolean(JSONObject json, String key, boolean isMust) throws Exception {
		boolean retval = false;

		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {
			throw new Exception(
					"���Json���������������.[json]���������������:" + key + " ������.[json] = " + json.toString());
		}
		try {
			retval = json.getBoolean(key);
		} catch (Exception e) {

			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		return Boolean.valueOf(retval);
	}

	public static Integer getJsonInt(net.sf.json.JSONObject json, String key, boolean isMust) throws Exception {
		Integer retval = Integer.valueOf(0);
		if (json == null) {
			throw new Exception("JSON 为空不能解析");
		}
		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {

			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		if ((obj == null) || ("".equals(obj.toString()))) {
			return Integer.valueOf(0);
		}
		try {
			retval = Integer.valueOf(json.getInt(key));
		} catch (Exception e) {
			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		return retval;
	}

	public static Integer getJsonInt(JSONObject json, String key, boolean isMust) throws Exception {
		Integer retval = Integer.valueOf(0);
		if (json == null) {
			throw new Exception("JSON 为空不能解析");
		}
		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {

			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		if ((obj == null) || ("".equals(obj.toString()))) {
			return Integer.valueOf(0);
		}
		try {
			retval = Integer.valueOf(json.getIntValue(key));
		} catch (Exception e) {
			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		return retval;
	}

	public static String getJsonString(net.sf.json.JSONObject json, String key) throws Exception {
		return getJsonString(json, key, false);
	}

	public static String getJsonString(JSONObject json, String key) throws Exception {
		return getJsonString(json, key, false);
	}

	public static String getJsonString(net.sf.json.JSONObject json, String key, boolean isMust) throws Exception {
		if (json == null) {
			throw new Exception("JSON 为空不能解析");
		}
		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {
			throw new Exception("JSON中不存在 Key:" + key + " 的值。[json] = " + json.toString());
		}
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	public static String getJsonString(JSONObject json, String key, boolean isMust) throws Exception {
		if (json == null) {
			throw new Exception("JSON 为空不能解析");
		}
		Object obj = null;
		try {
			obj = json.get(key);
		} catch (Exception e) {
			obj = null;
		}
		if ((obj == null) && (isMust)) {
			throw new Exception("从Json 中解析.[json]对象没有值:" + key + " [json] = " + json.toString());
		}
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}
}
