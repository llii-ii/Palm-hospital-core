package com.kasite.core.serviceinterface.module.basic.cache;

import java.util.List;

import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;

/**
 * 
 * @className: IDictLocalCache
 * @author: lcz
 * @date: 2018年8月6日 下午3:00:50
 */
public interface IDictLocalCache {
	/**
	 * 获取字典信息，首次获取直接查询库，然后存入map，后续map中存在则直接获取
	 * @Description: 
	 * @param dictType
	 * @return
	 */
	List<Dictionary> get(String dictType);
	/**
	 * 根据字典类型及key获取字典信息
	 * @Description: 
	 * @param dictType
	 * @param keyword
	 * @return
	 */
	Dictionary get(String dictType,String keyword);
	/**
	 * 根据字典类型及key，获取字段对应的值
	 * @Description: 
	 * @param dictType
	 * @param keyword
	 * @return
	 */
	String getValue(String dictType,String keyword);
	/**
	 * 刷新缓存数据
	 * @Description:
	 */
	void refresh();
	
	/**
	 * 加载数据
	 * @Description:
	 */
	void load();
}
