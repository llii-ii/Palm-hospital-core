package com.kasite.client.basic.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.basic.dao.IDictionaryMapper;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.module.basic.cache.IDictLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;

/**
 * 
 * @className: DictLocalCache
 * @author: lcz
 * @date: 2018年8月6日 下午3:01:26
 */
@Component
public class DictLocalCache implements IDictLocalCache{
 
	
	
	private Map<String, Map<String, Dictionary>> dictCacheMap;
	
	
	@PostConstruct
	public void init() {
		load();
	}
	
	/**
	 * 字典Map为空时，进行一次初始化
	 */
	private void initDictCache() {
		if(dictCacheMap==null || dictCacheMap.isEmpty()) {
			load();
		}
	}
	
	@Override
	public List<Dictionary> get(String dictType) {
		initDictCache();
		Map<String, Dictionary> map = dictCacheMap.get(dictType);
		if(map==null || map.isEmpty()) {
			return new ArrayList<Dictionary>();
		}
		return new ArrayList<Dictionary>(map.values());
	}

	@Override
	public Dictionary get(String dictType, String keyword) {
		initDictCache();
		Map<String, Dictionary> map = dictCacheMap.get(dictType);
		if(map==null || map.isEmpty()) {
			return null;
		}
		return map.get(keyword);
	}



	@Override
	public String getValue(String dictType, String keyword) {
		initDictCache();
		Map<String, Dictionary> map = dictCacheMap.get(dictType);
		if(map==null || map.isEmpty() || !map.containsKey(keyword)) {
			return null;
		}
		return map.get(keyword).getValue();
	}



	@Override
	public void refresh() {
		if(!dictCacheMap.isEmpty()) {
			dictCacheMap.clear();
		}
	}

	@Override
	public void load() {
		synchronized (this) {
			if(dictCacheMap==null) {
				dictCacheMap = new ConcurrentHashMap<String, Map<String, Dictionary>>();
			}else {
				dictCacheMap.clear();
			}
			Dictionary query = new Dictionary();
			query.setStatus(1);
			IDictionaryMapper dictionaryMapper = SpringContextUtil.getBean(IDictionaryMapper.class);
			if(null != dictionaryMapper) {
				List<Dictionary> list = dictionaryMapper.select(query);
				if(list.size()>0) {
					Map<String, List<Dictionary>> map = new ConcurrentHashMap<String, List<Dictionary>>();
					for (Dictionary dict : list) {
						List<Dictionary> dicList = map.get(dict.getDicType());
						if(dicList!=null) {
							map.get(dict.getDicType()).add(dict);
						}else {
							dicList = new Vector<Dictionary>();
							dicList.add(dict);
							map.put(dict.getDicType(), dicList);
						}
					}
					for (Entry<String, List<Dictionary>> entry : map.entrySet()) {
						Map<String, Dictionary> dictMap = new ConcurrentHashMap<String, Dictionary>();
						for (Dictionary dict : entry.getValue()) {
							dictMap.put(dict.getKeyword(), dict);
						}
						dictCacheMap.put(entry.getKey(), dictMap);
					}
				}
			}
		}
	}

}
