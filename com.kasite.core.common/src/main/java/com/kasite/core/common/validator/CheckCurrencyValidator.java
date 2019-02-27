package com.kasite.core.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCurrencyValidator implements ConstraintValidator<CheckCurrency, Integer> {
	CheckCurrency currecy;
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
		if(null == value) {
			return false;
		}
		if(null != value && value < 0) {
			return false;
		}
		return true; 
    }

	@Override
	public void initialize(CheckCurrency constraintAnnotation) {
		this.currecy = constraintAnnotation;
	}

}