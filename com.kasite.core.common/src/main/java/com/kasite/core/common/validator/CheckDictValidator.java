package com.kasite.core.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coreframework.util.ReflectionUtils;

public class CheckDictValidator implements ConstraintValidator<CheckDict, String> {
	private static CheckDictInf inf;
	CheckDict ann;
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    			boolean isNotNull = ann.isNotNull();
        		if(null == value && isNotNull) {
        			return false;
        		}
        		if(isNotNull == false && (null == value ||"".equals(value))) {
        			return true;
        		}
        		Class<? extends CheckDictInf> clazz = ann.inf();
        		if(inf == null) { 
        			inf = ReflectionUtils.newInstance(clazz);
        		}
        		String type = ann.type();
        		String version = ann.version();
        		boolean ret = inf.checkValue(version,type,value,ann);
        		return ret;
        		
    }

	@Override
	public void initialize(CheckDict constraintAnnotation) {
		this.ann = constraintAnnotation;
	}
}