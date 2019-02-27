//package com.kasite.core.common.dao.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.kasite.core.common.dao.impl.BaseDaoImpl.MatchType;
//import com.kasite.core.common.dao.impl.BaseDaoImpl.PropertyMustType;
//import com.kasite.core.common.dao.impl.BaseDaoImpl.PropertyType;
//import com.kasite.core.common.util.StringUtil;
//
//
//public class FilterVo {
//	private MatchType matchType;
//	private PropertyType propertyType;
//	private PropertyMustType propertyMustType;
//	private String fieldName;
//	private String mapKey;
//	
//	private static Map<String, FilterVo> map = new HashMap<String, FilterVo>();
//	private static final String SQLFORMAT_FILTER_TYPE = "!";
//	static{
//		MatchType[] mts = MatchType.values();
//		PropertyType[] pts = PropertyType.values();
//		PropertyMustType[] pmts = PropertyMustType.values();
//		for (MatchType matchType : mts) {
//			for (PropertyType propertyType : pts) {
//				for (PropertyMustType pmt : pmts) {
//					String filterStr = "_"+matchType.name()+SQLFORMAT_FILTER_TYPE+propertyType.name()+"_";
//					String filterStrMust = "_"+matchType.name()+SQLFORMAT_FILTER_TYPE+propertyType.name()+pmt.getTypeName()+"_";
//					map.put(filterStr, new FilterVo(matchType, propertyType, null));
//					map.put(filterStrMust, new FilterVo(matchType, propertyType, pmt));
//				}
//			}
//		}
//	}
//	
//	
//	public static FilterVo getFilterVo(String format_filter){
//		if(StringUtil.isNotBlank(format_filter)){
//			for (Map.Entry<String, FilterVo> m : map.entrySet()) {
//				String key = m.getKey();
//				if(format_filter.indexOf(key) > 0){
//					FilterVo vo = m.getValue();
//					String[] s = format_filter.split(key);
//					vo.setFieldName(s[0]);
//					vo.setMapKey(s[1]);
//					return vo;
//				}
//			}
//		}
//		return null;
//	}
//	
//	private FilterVo(MatchType matchType,PropertyType propertyType,PropertyMustType propertyMustType){
//		this.matchType = matchType;
//		this.propertyMustType = propertyMustType;
//		this.propertyType = propertyType;
//	}
//
//	public MatchType getMatchType() {
//		return matchType;
//	}
//
//	public void setMatchType(MatchType matchType) {
//		this.matchType = matchType;
//	}
//
//	public PropertyType getPropertyType() {
//		return propertyType;
//	}
//
//	public void setPropertyType(PropertyType propertyType) {
//		this.propertyType = propertyType;
//	}
//
//	public PropertyMustType getPropertyMustType() {
//		return propertyMustType;
//	}
//
//	public void setPropertyMustType(PropertyMustType propertyMustType) {
//		this.propertyMustType = propertyMustType;
//	}
//
//	public String getFieldName() {
//		return fieldName;
//	}
//
//	public void setFieldName(String fieldName) {
//		this.fieldName = fieldName;
//	}
//
//	public String getMapKey() {
//		return mapKey;
//	}
//
//	public void setMapKey(String mapKey) {
//		this.mapKey = mapKey;
//	}
//	
//}
