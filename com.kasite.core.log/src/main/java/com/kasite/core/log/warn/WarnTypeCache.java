package com.kasite.core.log.warn;

/**
 * 
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kasite.core.log.warn.vo.WarnTypeVo;

public class WarnTypeCache {
	private Map<String,WarnTypeVo> map;
	private static final WarnTypeCache  instance=new WarnTypeCache();
	
	private WarnTypeCache(){
		map=new HashMap<String,WarnTypeVo>();
	}
	public static WarnTypeCache  me(){
		return instance;
	}
	public long getSize(){
		return map.size();
	}
	public void refresh(List<WarnTypeVo> voList){
		Map<String,WarnTypeVo> map2=new HashMap<String,WarnTypeVo>();
		for(WarnTypeVo vo:voList){
			map2.put(vo.getWarnType(), vo);
		}
		map=map2;
	}
	
	public WarnTypeVo get(String warnType){
		return map.get(warnType);
	}
	
	public Map<String,WarnTypeVo> getMap(){
		return map;
	}
	
}
