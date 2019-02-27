package com.yihu.hos.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @author Administrator
 * 
 */
@Retention (RetentionPolicy.RUNTIME )
@Target (ElementType.FIELD)
@Documented
public @interface RelativeXmlNode {
	public String BeanFiled(); //实体BEAN 中的filed
	public String XmlFiled(); //接口文档中返回参数XML对应的节点
	public Class clazz() default String.class; //

}
