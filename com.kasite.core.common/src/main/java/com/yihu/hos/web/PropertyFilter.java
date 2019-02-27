package com.yihu.hos.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.coreframework.util.Asserter;
import com.coreframework.util.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 与具体ORM实现无关的属性过滤条件封装类, 主要记录页面中简单的搜索过滤条件.
 * @author 戴燕水
 * 创建日期：2016-06-28 14:20:35
 * 描述：
 * 最后一次修改日期：2016-06-28 14:20:35
 */
public class PropertyFilter {

	/** 多个属性间OR关系的分隔符. */
	public static final String OR_SEPARATOR = "_OR_";

	/** 属性比较类型. 
	 * EQ 表示=
	 * LIKE 表示like
	 * LT 表示<
	 * GT 表示>
	 * LE 表示<=
	 * GE 表示>=
	 * */
	public enum MatchType {

		EQ, LIKE, LT, GT, LE, GE,OLE,OGE,LIKES,LIKEX,LIKEE,ORDERBY;
	}

	/** 属性数据类型. */
	public enum PropertyType {

		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);
		private Class<?> clazz;

		private PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}
	private MatchType matchType = null;
	private Object matchValue = null;
	private Class<?> propertyClass = null;
	private String[] propertyNames = null;

	public PropertyFilter() {
	}
	
	public void setMatchValue(Object matchValue) {
		this.matchValue = matchValue;
	}

	/**
	 * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表.
	 *                   eg. LIKES_NAME_OR_LOGIN_NAME
	 * @param value 待比较的值.
	 */
	public PropertyFilter(final String filterName, final String value) {

		String firstPart = StringUtils.substringBefore(filterName, "_");
		String matchTypeCode = StringUtils.substring(firstPart, 0, firstPart.length() - 1);
		String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1, firstPart.length());

		try {
			matchType = Enum.valueOf(MatchType.class, matchTypeCode);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
		}

		try {
			propertyClass = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
		}

		String propertyNameStr = StringUtils.substringAfter(filterName, "_");
		Asserter.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
		propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, PropertyFilter.OR_SEPARATOR);

		this.matchValue = ConvertUtils.convertStringToObject(value, propertyClass);
	}

	/**
	 * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 *
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request) {
		return buildFromHttpRequest(request, "filter");
	}
	/**
	 * 从JsonObject中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * @throws Exception 
	 *
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final JSONObject request) throws Exception {
		return buildFromHttpRequest(request, "filter");
	}
	/**
	 * 取得带相同前缀的Request Parameters.
	 *
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Asserter.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	/**
	 * 取得带相同前缀的Jsonobject Parameters.
	 *
	 * 返回的结果的Parameter名已去除前缀.
	 * @throws Exception 
	 */
	public static Map<String, Object> getParametersStartingWith(JSONObject request, String prefix) throws Exception {
		Asserter.notNull(request, "Request must not be null");
		Iterator<String> paramNames = request.keys();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
//				String[] values = request.getParameterValues(paramName);
//				if (values == null || values.length == 0) {
//					// Do nothing, no values found at all.
//				} else if (values.length > 1) {
//					params.put(unprefixed, values);
//				} else {
//					params.put(unprefixed, values[0]);
//				}
				if(request.containsKey(paramName)) {
					String value = request.getString(paramName);
					params.put(unprefixed, value);
				}
			}
		}
		return params;
	}
	/**
	 * 从JsonObject中创建PropertyFilter列表
	 * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
	 *
	 * eg.
	 * filter_EQS_name
	 * filter_LIKES_name_OR_email
	 * @throws Exception 
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final JSONObject request, final String filterPrefix) throws Exception {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

		//从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = getParametersStartingWith(request, filterPrefix + "_");

		//分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String filterName = entry.getKey();
			String value = (String) entry.getValue();
			//如果value值为空,则忽略此filter.
			if (StringUtils.isNotBlank(value)) {
				PropertyFilter filter = new PropertyFilter(filterName, value);
				filterList.add(filter);
			}
		}

		return filterList;
	}
	/**
	 * 从HttpRequest中创建PropertyFilter列表
	 * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
	 *
	 * eg.
	 * filter_EQS_name
	 * filter_LIKES_name_OR_email
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request, final String filterPrefix) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

		//从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = getParametersStartingWith(request, filterPrefix + "_");

		//分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String filterName = entry.getKey();
			String value = (String) entry.getValue();
			//如果value值为空,则忽略此filter.
			if (StringUtils.isNotBlank(value)) {
				PropertyFilter filter = new PropertyFilter(filterName, value);
				filterList.add(filter);
			}
		}

		return filterList;
	}

	/**
	 * 绑定PropertyFilter列表中的属性值到entity对象
	 */
	public static void buildToEntity(Object entity, List<PropertyFilter> filters) {
		try {
			Map<String, Object> map = BeanUtils.describe(entity);
			for (PropertyFilter filter : filters) {
				if (!filter.hasMultiProperties()) {
					if (map.containsKey(filter.getPropertyName())) {
						PropertyUtils.setProperty(entity, filter.getPropertyName(), filter.getMatchValue());
					}
				} else {
					for (String propName : filter.getPropertyNames()) {
						if (map.containsKey(propName)) {
							PropertyUtils.setProperty(entity, propName, filter.getMatchValue());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取比较值的类型.
	 */
	public Class<?> getPropertyClass() {
		return propertyClass;
	}

	/**
	 * 获取比较方式.
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * 获取比较值.
	 */
	public Object getMatchValue() {
		return matchValue;
	}

	/**
	 * 获取比较属性名称列表.
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * 获取唯一的比较属性名称.
	 */
	public String getPropertyName() {
		Asserter.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
		return propertyNames[0];
	}

	/**
	 * 是否比较多个属性.
	 */
	public boolean hasMultiProperties() {
		return (propertyNames.length > 1);
	}
}
