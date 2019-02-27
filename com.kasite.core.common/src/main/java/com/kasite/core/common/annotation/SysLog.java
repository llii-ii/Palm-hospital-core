	package com.kasite.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
	/**
	 * 是否保存返回结果集日志  默认是保存，如果不保存结果集日志的话
	 * 会判断返回结果集中是否包含
	 * 就只会保存请求入参
	 * @return
	 */
	boolean isSaveResult() default true;
}
