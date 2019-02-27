package com.kasite.client.crawler.modules;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.cases.service.AbsCasesService;
import com.kasite.client.crawler.modules.cases.service.ICasesService;
import com.kasite.client.crawler.modules.clinic.service.AbsClinicInfoService;
import com.kasite.client.crawler.modules.clinic.service.IClinicService;
import com.kasite.client.crawler.modules.hospitalization.service.AbsHospitalizationService;
import com.kasite.client.crawler.modules.hospitalization.service.IHospitalizationService;
import com.kasite.client.crawler.modules.patient.service.AbsPatientInfoService;
import com.kasite.client.crawler.modules.patient.service.IPersionInfoService;

/**
 * 
 * @author Administrator
 * 
 */
public final class ModuleManage {

	private String url = "com.kasite.client.crawler.modules.client";
	/**
	 * 
	 */
	private static ModuleManage instance;

	public ModuleManage() {
		ModuleManage.getClasses(url);
	}


	/**
	 * 公共调用类
	 */
	private static Map<String, IPersionInfoService> persionInfoServiceMap = new HashMap<>();
	/**
	 * 公共调用类
	 */
	private static Map<String, IHospitalizationService> hospitalizationServiceMap = new HashMap<>();
	/**
	 * 公共调用类
	 */
	private static Map<String, IClinicService> clinicServiceMap = new HashMap<>();
	/**
	 * 公共调用类
	 */
	private static Map<String, ICasesService> casesServiceMap = new HashMap<>();

	/**
	 * 新增一个协议层
	 * 
	 * @throws Exception
	 */
	public static void addModule(HosParse parse,String key, String clazz) throws Exception {
//		Class<? extends HosClientConfig> confClassName = parse.hosconfig();
//		HosClientConfig config = (HosClientConfig) newObject(confClassName.getName());
//		Class<?>[] argClasss = new Class[1];
//		argClasss[0] = confClassName;
//		Object[] objs = new Object[1];
//		objs[0] = config;
		Object o = newObject(clazz);
		if(o.getClass().getSuperclass().equals(AbsClinicInfoService.class)) {
			IClinicService service = (IClinicService) o;
			clinicServiceMap.put(key, service);
		}
		if(o.getClass().getSuperclass().equals(AbsPatientInfoService.class)) {
			IPersionInfoService service = (IPersionInfoService) o;
			persionInfoServiceMap.put(key, service);
		}
		if(o.getClass().getSuperclass().equals(AbsHospitalizationService.class)) {
			IHospitalizationService service = (IHospitalizationService) o;
			hospitalizationServiceMap.put(key, service);
		}
		if(o.getClass().getSuperclass().equals(AbsCasesService.class)) {
			ICasesService service = (ICasesService) o;
			casesServiceMap.put(key, service);
		}
	}
	public static void main(String[] args) {
		ModuleManage.getInstance();
	}

	public <T extends HosClientConfig> IPersionInfoService getPersionInfoService(T hosConfig) {
		return persionInfoServiceMap.get(hosConfig.getKey());
	}

	public <T extends HosClientConfig> IHospitalizationService getHospitalizationService(T hosConfig) {
		return hospitalizationServiceMap.get(hosConfig.getKey());
	}

	public <T extends HosClientConfig> IClinicService getClinicService(T hosConfig) {
		return clinicServiceMap.get(hosConfig.getKey());
	}
	public <T extends HosClientConfig> ICasesService getCasesService(T hosConfig) {
		return casesServiceMap.get(hosConfig.getKey());
	}
	public <T extends HosClientConfig> IPersionInfoService getPersionInfoService(String key) {
		return persionInfoServiceMap.get(key);
	}

	public <T extends HosClientConfig> IHospitalizationService getHospitalizationService(String key) {
		return hospitalizationServiceMap.get(key);
	}

	public <T extends HosClientConfig> IClinicService getClinicService(String key) {
		return clinicServiceMap.get(key);
	}
	public <T extends HosClientConfig> ICasesService getCasesService(String key) {
		return casesServiceMap.get(key);
	}
//	public <T extends HosClientConfig> IPersionInfoService<? extends HosClientConfig> getPersionInfoService() {
//		return persionInfoServiceMap.get(Convent.getNowHosKey());
//	}
//
//	public <T extends HosClientConfig> IHospitalizationService<? extends HosClientConfig> getHospitalizationService() {
//		return hospitalizationServiceMap.get(Convent.getNowHosKey());
//	}
//
//	public <T extends HosClientConfig> IClinicService<? extends HosClientConfig> getClinicService() {
//		return clinicServiceMap.get(Convent.getNowHosKey());
//	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public static void getClasses(String packageName) {
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(
					packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(
												packageName.length() + 1, name
														.length() - 6);
										try {
											String url1 = packageName + '.' + className;

											addClass(url1, className);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addClass(String url, String className) {
//		Pattern p = Pattern.compile("[A-Z]*");
//		Matcher m = p.matcher(className);// 类名大写取类异常
//		boolean b = m.matches();
		Class<?> c = null;
		try {
			c = Class.forName(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (c == null) {
			return;
		}
		String name = c.getName();
		if (c.isAnnotationPresent(HosParse.class)) {
			HosParse annot = (HosParse) c.getAnnotation(HosParse.class);
			String hosId = annot.key();
			String key1 = hosId;
			try {
				addModule(annot,key1, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "."
						+ file.getName(), file.getAbsolutePath(), recursive);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					String url = packageName + '.' + className;

					addClass(url, className);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ModuleManage getInstance() {

		if (null == instance) {
			instance = new ModuleManage();
		}
		return instance;

	}

	/**
	 * Constructs a new object for the given class name. The construction takes
	 * no arguments.
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
			Class<?> clazz = Class.forName(className);
			o = clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		return o;
	}

	/**
	 * Constructs a new object for the given class name and with the given
	 * arguments. The arguments must be specified in terms of their Class[]
	 * types and their Object[] values.
	 * 
	 * Example:
	 * 
	 * String s = newObject("java.lang.String", new Class[] { String.class}, new
	 * String[] { "test"});
	 * 
	 * is equivalent to:
	 * 
	 * String s = new String("test");
	 * 
	 * If an exception occurs during construction, null is returned.
	 * 
	 * All exceptions are written to the Log instance for this class.
	 * 
	 * @param className
	 * @param argTypes
	 * @param args
	 * @return
	 */
	public static Object newObject(String className, Class<?>[] argTypes,
			Object[] args) {
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
			Class<?> clazz = Class.forName(className);

			Constructor<?> c = clazz.getDeclaredConstructor(argTypes);

			o = c.newInstance(args);

		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		}
		return o;
	}

}
