package com.kasite.core.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

/**
 * 
 * @className: BeanCopyUtils
 * @author: lcz
 * @date: 2018年7月23日 上午10:22:56
 */
public class BeanCopyUtils {

	private static Map<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

	public  static <T>  T copyProperties(Object source, T target,Converter converter) {
		boolean useConverter = false;
		String convert = null;
		if(converter!=null) {
			useConverter = true;
			convert = converter.getClass().getName();
		}
		String beanKey = generateKey(source.getClass().getName(), target.getClass().getName(),convert);
		BeanCopier copier = null;
		if (!beanCopierMap.containsKey(beanKey)) {
			copier = BeanCopier.create(source.getClass(), target.getClass(), useConverter);
			beanCopierMap.put(beanKey, copier);
		} else {
			copier = beanCopierMap.get(beanKey);
		}
		copier.copy(source, target, converter);
		return target;
	}

	private static String generateKey(String source, String target,String convert) {
		return source + "_"+target+"_"+convert;
	}
}
