package com.kasite.core.log.warn;
///**
// * 
// */
//package com.yihu.warn;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.ConcurrentHashMap;
//
//import com.yihu.warn.vo.WarnConfigVo;
//
//public class WarnConfigCache {
//	private ConcurrentHashMap<String,Map<String,WarnConfigVo>> map;
//	private static final WarnConfigCache  instance=new WarnConfigCache();
//	
//	private WarnConfigCache(){
//		map=new ConcurrentHashMap<String,Map<String,WarnConfigVo>>();
//	}
//	public static WarnConfigCache  me(){
//		return instance;
//	}
//	public long getWarnObjectSize(){
//		return map.size();
//	}
//	public long getSize(){
//		long size = 0;
//		for(Entry<String,Map<String,WarnConfigVo>> entry : map.entrySet()){
//			Map<String,WarnConfigVo> cmp = entry.getValue();
//			size += cmp.size();
//		}
//		return size;
//	}
//	public void clear() {
//		map.clear();
//	}
//
//	public Map<String,WarnConfigVo> get(String key){
//		return map.get(key);
//	}
//	
//	public void refresh(List<WarnConfigVo> list) {
//		ConcurrentHashMap<String,Map<String,WarnConfigVo>> temp=new ConcurrentHashMap<String,Map<String,WarnConfigVo>>();
//		for(WarnConfigVo vo:list){
//			String key =vo.getWarnObject();
//			Map<String,WarnConfigVo> cmp = temp.get(key);
//			if(cmp==null){
//				cmp = new HashMap<String,WarnConfigVo>();
//			}
//			String warnType = vo.getWarnType();
//			cmp.put(warnType,vo);
//			temp.put(key, cmp);
//		}
//		map=temp;
//		
//	}
//	
//	
//}
