package com.kasite.client.crawler.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface HosParse {
	public String key(); //医院ID
	public String hospitalName();//接口名称
	public boolean isDebug();
}  

