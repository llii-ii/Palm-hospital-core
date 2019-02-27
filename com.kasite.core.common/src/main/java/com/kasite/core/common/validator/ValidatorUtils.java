package com.kasite.core.common.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.LogUtil;


/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author admin

 * @date 2017-03-15 10:50
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws RRException  校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws RRException {
    	Set<ConstraintViolation<Object>> constraintViolations = null;
    	try {
    		constraintViolations = validator.validate(object, groups);
    	}catch (Exception e) {
    		LogUtil.throwRRException(e, RetCode.Common.ERROR_PARAM);
		}
        if (null != constraintViolations && !constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
//            		String name = constraint.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            		constraint.getPropertyPath().forEach((v)->{
            			msg.append(v.getName()).append("||");//KasiteConfig.print(v.getName());
            		});
                msg.append(constraint.getMessage()).append("||").append("value=").append(constraint.getInvalidValue());
            }
            throw new RRException(msg.toString());
        }
        
    }
    /**
     * 校验对象
     * @param object        待校验对象
     * @throws RRException  校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object)
            throws RRException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder(); 
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new RRException(msg.toString());
        }
    }
}
