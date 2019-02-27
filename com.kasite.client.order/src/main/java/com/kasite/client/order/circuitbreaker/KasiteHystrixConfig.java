package com.kasite.client.order.circuitbreaker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.kasite.core.common.constant.KstHosConstant;

/**
 * @author linjf
 * 断路器配置类
 * Hystrix的支持yml配置，目前暂时使用单例类来配置
 * 后期可以视情况统一回收到一个地方配置。
 */
public class KasiteHystrixConfig {

	private static ConcurrentMap<String,Map<String,String>> map = new ConcurrentHashMap<String,Map<String,String>>(16);
	
	public static void setValue(String key,String name,String value) {
		Map<String,String> proMap = map.get(key);
		proMap.put(name,value);
	}
	
	
	public static boolean getBooleanValue(String key,String name) {
		Map<String,String> proMap = map.get(key);
		String val = proMap.get(name);
		if( KstHosConstant.STRING_1.equals(val)
				|| KstHosConstant.TRUE_STR.equals(val)) {
			return true;
		}else {
			return false;
		}
	}
	
	static {
		//初始化配置
		String hisBizExecuteGroupKey =  KasiteHystrixCommandKey.HisBizExecuteGroup.name();
		Map<String,String> hisBizExecuteGroupMap = new HashMap<String,String>(16);
		hisBizExecuteGroupMap.put("CircuitBreakerEnabled", KstHosConstant.TRUE_STR);
		hisBizExecuteGroupMap.put("CircuitBreakerForceOpen",KstHosConstant.FALSE_STR);
		hisBizExecuteGroupMap.put("IsCircuitBreakerOpen",KstHosConstant.FALSE_STR);
		map.put(hisBizExecuteGroupKey, hisBizExecuteGroupMap);
	}
	
}
