package com.kasite.core.common.util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Static Helper methods for instantiating objects from reflection.
 * 
 * @author muzquiano
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ReflectionHelper {

	private static Log logger = LogFactory.getLog(ReflectionHelper.class);

	private ReflectionHelper() {
	}

	/**
	 * Constructs a new object for the given class name.
	 * The construction takes no arguments.
	 *
	 * If an exception occurs during construction, null is returned.
	 *
	 * All exceptions are written to the Log instance for this class.
	 *
	 * @param className
	 * @return
	 */
	public static Object newObject(String className) {
		Object o = null;

		try {
			Class clazz = Class.forName(className);
			o = clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			logger.debug(cnfe);
		} catch (InstantiationException ie) {
			logger.debug(ie);
		} catch (IllegalAccessException iae) {
			logger.debug(iae);
		}
		return o;
	}

	/**
	 * Constructs a new object for the given class name and with the given
	 * arguments.  The arguments must be specified in terms of their Class[]
	 * types and their Object[] values.
	 *
	 * Example:
	 *
	 *   String s = newObject("java.lang.String", new Class[] { String.class},
	 *              new String[] { "test"});
	 *
	 * is equivalent to:
	 *
	 *   String s = new String("test");
	 *
	 * If an exception occurs during construction, null is returned.
	 *
	 * All exceptions are written to the Log instance for this class.

	 * @param className
	 * @param argTypes
	 * @param args
	 * @return
	 */
	public static Object newObject(String className, Class[] argTypes, Object[] args) {
		/**
		 * We have some mercy here - if they called and did not pass in any
		 * arguments, then we will call through to the pure newObject() method.
		 */
		if (args == null || args.length == 0) {
			return newObject(className);
		}

		/**
		 * Try to build the object
		 *
		 * If an exception occurs, we log it and return null.
		 */
		Object o = null;
		try {
			// base class
			Class clazz = Class.forName(className);

			Constructor c = clazz.getDeclaredConstructor(argTypes);
			
			o = c.newInstance(args);
		} catch (ClassNotFoundException cnfe) {
			logger.debug(cnfe);
		} catch (InstantiationException ie) {
			logger.debug(ie);
		} catch (IllegalAccessException iae) {
			logger.debug(iae);
		} catch (NoSuchMethodException nsme) {
			logger.debug(nsme);
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			logger.debug(ite);
		}
		return o;
	}

	/**
	 * Invokes a method on the given object by passing the given arguments
	 * into the method.
	 *
	 * @param obj
	 * @param method
	 * @param argTypes
	 * @param args
	 * @return
	 */
	public static Object invoke(Object obj, String method, Class[] argTypes, Object[] args) {
		if (obj == null || method == null) {
			throw new IllegalArgumentException("Object and Method must be supplied.");
		}

		return invoke(obj.getClass(), obj, method, argTypes, args);
	}

	public static Object invoke(Class c, Object obj, String method, Class[] argTypes, Object[] args) {
		if (c == null || method == null) {
			throw new IllegalArgumentException("Class and Method must be supplied.");
		}

		/**
		 * Try to invoke the method.
		 *
		 * If the method is unable to be invoked, we log and return null.
		 */
		try {
			Method m = c.getMethod(method, argTypes);
			if (m != null) {
				return m.invoke(obj, args);
			}
		} catch (NoSuchMethodException nsme) {
			logger.debug(nsme);
		} catch (IllegalAccessException iae) {
			logger.debug(iae);
		} catch (InvocationTargetException ite) {
			logger.debug(ite);
		}

		return null;
	}
}
