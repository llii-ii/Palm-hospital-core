package com.kasite.core.common.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 渠道权限控制
 * 入参数必须包含：CommonReq 或者 InterfaceMessage
 * @author daiyanshui
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientIdAuthApi {
	/**
	 * 接口API名称，用于与配置表中的配置的api对应
	 * @return
	 */
    String api();
    
}