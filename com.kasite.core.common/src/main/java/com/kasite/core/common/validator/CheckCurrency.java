package com.kasite.core.common.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 判断金额是否正常
 * 金额不能为空 并且金额不能小于 0
 * 传入的值只能是Integer 我们内部的所有金额部分都是单位（分）
 * 
 * @author daiyanshui
 */
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckCurrencyValidator.class)
@Documented
public @interface CheckCurrency {
	
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
