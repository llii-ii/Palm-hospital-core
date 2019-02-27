package com.kasite.core.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.coreframework.util.DateOper;

public class CheckDateValidator implements ConstraintValidator<CheckDate, String> { 
	private CheckDate ann;
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    			boolean isNotNull = ann.isNotNull();
        		if(null == value && isNotNull) {
        			return false;
        		}
        		if(null != value) {
        			try {
        				DateOper.parse(value);
        			}catch (Exception e) {
        				return false;
        			}
        			return true;
        		}
    			return true;
    }

	@Override
	public void initialize(CheckDate constraintAnnotation) {
		this.ann = constraintAnnotation;
	}
}