package com.kasite.core.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Set;
/**
 * 反射工具类.
 *     提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class等Util函数.
 * 
 * @author 戴燕水
 * 创建日期：2011-1-8 13:51:07
 * 描述：
 * 最后一次修改日期：2011-1-8 13:51:07
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ReflectionUtils {

	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static void main(String[] args) {
		
		
		
		
		
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("没有找到这个属性[" + fieldName + "]在该对象中: [" + obj.getClass().getName() + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 *
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				
			}
		}
		return null;
	}

	/**
	 * 对于被cglib AOP过的对象, 取得真实的Class类型.
	 */
	public static Class<?> getUserClass(Class<?> clazz) {
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况.
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (NoSuchMethodException e) {//NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 *
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static Object getFieldValue(Class<?> classObj, Object obj, String fieldName) {
		try {
			Field field = classObj.getDeclaredField(fieldName);

			field.setAccessible(true);

			Object value = field.get(obj);

			if (value == null) {
				String className = field.getType().getName();
				Class clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
				value = clazz.newInstance();	//当类没有缺省构造函数时，抛出InstantiationException异常
			}

			return value;

		} catch (Exception e) {
			return null;
		}
	}

	public static Object newInstance(String className, String p1) {
		try {
			Class<?> classObject = Class.forName(className);

			Constructor<?> classConstructor = classObject.getConstructor(
					new Class[]{String.class});

			Object[] arguments = new Object[]{p1};

			return classConstructor.newInstance(arguments);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object newInstance(String className) {
		return ReflectionHelper.newObject(className);
	}
	
	public static <T> T newInstance(Class<T> clazz) {
		return (T) ReflectionHelper.newObject(clazz.getName());
	}

	public static Object newInstance(String className, Class[] argTypes, Object[] args) {
		return ReflectionHelper.newObject(className, argTypes, args);
	}
	
	public static <T> T copyObj(Object fromObj,Class<T> clazz){
		try {
			T obj = clazz.newInstance();
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				Object fobj = null;
				try{
					fobj = getFieldValue(fromObj, f.getName());
				}catch (Exception e) {
					fobj = null;
				}
				if(null != fobj){
					setFieldValue(obj, f.getName(), fobj);
				}
			}
			return obj;
		}catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static <T> T getObjFromJsonMap(Map<String, Object> map,Class<T> clazz) throws Exception{
		if(null == map){
			return null;
		}
		Object obj = ReflectionUtils.newInstance(clazz.getName());
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			String tbfile = f.getName();
			String key = getKey(map.keySet(), tbfile);
			Object val = map.get(key);
			if(null != val){
				val.getClass().getComponentType();
				val.getClass().getSuperclass();
				if(val instanceof java.sql.Date){
					val = new Timestamp(((java.sql.Date)val).getTime());
				}
				if(f.getType().toString().equals("class java.sql.Date")){
					val = new java.sql.Date(((Timestamp)val).getTime());
				}
				try{
					ReflectionUtils.setFieldValue(obj, f.getName(), val);
				}catch (Exception e) {
					break;
				}
				
			}else{
				key = getKey(map.keySet(), f.getName());
				val = map.get(key);
				try{
					ReflectionUtils.setFieldValue(obj, f.getName(), val);
				}catch (Exception e) {
					break;
				}
			}
		}
		return (T) obj;
	}
	private static String getKey(Set<String> set,String name){
		for (String string : set) {
			if(string.toLowerCase().trim().equals(name.toLowerCase().trim())){
				return string;
			}
		}
		return null;
	}
	
	
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * 根据传入的fieldName，查询对象中是否有该属性，存在时，将值设置到该属性中，不存在则不做处理。
	 * 忽略首字母大小写
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	public static void setFieldValueByFieldType(final Object obj, final String fieldName, final Object value) throws Exception{
		Field field = getAccessibleFieldToCase(obj, fieldName);
		if (field == null) {
			return;
		}
		if(value!=null && value.toString().trim().length()>0) {
			if(String.class.isAssignableFrom(field.getType())) {
				field.set(obj, value.toString());
			}else if(Integer.class.isAssignableFrom(field.getType())) {
				if(value.toString().contains(".")) {
					field.set(obj, Double.valueOf(value.toString()).intValue());
				}else {
					field.set(obj, Integer.parseInt(value.toString()));
				}
			}else if(Long.class.isAssignableFrom(field.getType())) {
				field.set(obj, Long.parseLong(value.toString()));
			}else if(Float.class.isAssignableFrom(field.getType())) {
				field.set(obj, Float.parseFloat(value.toString()));
			}else if(Double.class.isAssignableFrom(field.getType())) {
				field.set(obj, Double.parseDouble(value.toString()));
			}else if(java.sql.Date.class.isAssignableFrom(field.getType())) {
				try {
					field.set(obj, java.sql.Date.valueOf(value.toString()));
				}catch (Exception e) {
					field.set(obj, new java.sql.Date(DateOper.parse(value.toString()).getTime()));
				}
			}else if(java.sql.Timestamp.class.isAssignableFrom(field.getType())) {
				try {
					field.set(obj, java.sql.Timestamp.valueOf(value.toString()));
				}catch (Exception e) {
					field.set(obj, new java.sql.Timestamp(DateOper.parse(value.toString()).getTime()));
				}
			}else if(Date.class.isAssignableFrom(field.getType())) {
				field.set(obj, DateOper.parse(value.toString()));
			}else {
				field.set(obj, value);
			}
		}
	}
	
	
	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 *	忽略首字母大小写
	 * 没有找到, 返回null.
	 */
	public static Field getAccessibleFieldToCase(final Object obj, final String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				try {
					Field field = null;
					if(Character.isUpperCase(fieldName.charAt(0))) {
						field = superClass.getDeclaredField(fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1));
					}else {
						field = superClass.getDeclaredField(fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
					}
					field.setAccessible(true);
					return field;
				} catch (NoSuchFieldException e1) {
					
				}
			}
		}
		return null;
	}
	
}
