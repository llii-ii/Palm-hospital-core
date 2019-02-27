package com.kasite.core.common.validator;

import java.util.EnumSet;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.kasite.core.common.exception.RRException;

public class CheckEnumValidator implements ConstraintValidator<CheckEnum, Integer> {
	private final static String DESNAME = "参数错误,Value=";
	private final static String CNAME = "|";
	CheckEnum ann;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
//        		if(isNotNull == false && (null == value ||"".equals(value))) {
//        			return true;
//        		}
		Class<? extends Enum> e = ann.inf();
		EnumSet es = EnumSet.allOf(e);
		boolean t = false;
		Iterator iterator = es.iterator();
		String des = null;
		while(iterator.hasNext()) {
			Object o = iterator.next();
			ICheckEnumInf i = null;
			if(o instanceof ICheckEnumInf) {
				i = (ICheckEnumInf) o;
				int v = i.getState();
				if(null != value && v == value) {
					t = true;
				}
			}else {
				throw new RRException("属性枚举不正确，请确认枚举有继承接口：com.kasite.core.common.validator.ICheckEnumInf ");
			}
			des = i.des();
		}
		if(!t) {
			String v = constraintValidatorContext.getDefaultConstraintMessageTemplate();
			v = v + DESNAME+value+CNAME+ des;
			throw new RRException(v);
		}
		return true;
    }

	@Override
	public void initialize(CheckEnum constraintAnnotation) {
		this.ann = constraintAnnotation;
	}

}