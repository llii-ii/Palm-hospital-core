/**
 * 
 */
package com.kasite.core.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;

public class Logger implements Observer {
	// private static org.apache.log4j.Logger log4j =
	// org.apache.log4j.Logger.getLogger(Logger.class);
	// private static final java.util.concurrent.atomic.AtomicInteger al=new
	// AtomicInteger();
	private static volatile Logger instance = null;
	private ConcurrentHashMap<String, Integer> levelMap = new ConcurrentHashMap<String, Integer>();
	private int _level = LogLevel.INFO.getValue();
	private boolean consoleOutput = false;

	private long _lastModified = -1;

	public void console(String message) {
		if (this.consoleOutput) {
			KasiteConfig.print(message);
		}
	}

	public void console(Exception e) {
		if (this.consoleOutput) {
			e.printStackTrace();
		}
	}

	public void console(String msg, Object... args) {
		if (!this.consoleOutput) {
			return;
		}
		if (msg == null) {
			KasiteConfig.print("null");
			return;
		}
		String[] tmps = msg.split("\\{\\}", -1);
		if (tmps.length < 2) {
			KasiteConfig.print(msg);
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tmps.length - 1; i++) {
			sb.append(tmps[i]);
			sb.append(args.length > i ? String.valueOf(args[i]) : "{}");
		}
		sb.append(tmps[tmps.length - 1]);
		KasiteConfig.print(sb.toString());
	}

	@Deprecated
	public void disableLocalLog() {
	}

	/**
	 * 禁用日志功能
	 */
	public void disableCenterLog() {
		this.producer.centerLog = false;
	}

	private int getLevel(String module) {
		Integer le = levelMap.get(module);
		if (le != null) {
			return le;
		}
		return _level;
	}

	public boolean resetLevels(Integer defaultLvel, Map<String, Integer> map, long lastModified) {
		if (lastModified <= this._lastModified) {
			return false;
		}
		this.setLevel(defaultLvel);
		for (String key : systemLog) {
			map.remove(key);
		}
		ConcurrentHashMap<String, Integer> levelMap2 = new ConcurrentHashMap<String, Integer>();
		levelMap2.putAll(map);
		levelMap = levelMap2;
		this._lastModified = lastModified;
		return true;
	}

	public void removeLevel(String module) {
		this.levelMap.remove(module);
		this._lastModified = System.currentTimeMillis();
	}

	public Map<String, Integer> getLevels() {
		Map<String, Integer> map = new HashMap<String, Integer>(levelMap);
		map.put("", _level);
		return map;
	}

	public boolean setLevel(String module, Integer level) {
		if (level == null || module == null) {
			return false;
		}
		if (LogLevel.forLevel(level.intValue()) == null) {
			return false;
		}
		if (systemLog.contains(module)) {
			return false;
		}
		levelMap.put(module, level.intValue());
		this._lastModified = System.currentTimeMillis();
		return true;
	}

	public boolean setLevel(Integer level) {
		if (level == null) {
			return false;
		}
		LogLevel le = LogLevel.forLevel(level.intValue());
		if (le == null) {
			return false;
		}
		if (level.intValue() > LogLevel.INFO.getValue()) {
			return false;
		}
		_level = level.intValue();
		this._lastModified = System.currentTimeMillis();
		return true;

	}

	@Deprecated
	public boolean setLevel(LogLevel level) {
		if (level == null) {
			return false;
		}
		if (level.getValue() > LogLevel.INFO.getValue()) {
			return false;
		}
		_level = level.getValue();
		this._lastModified = System.currentTimeMillis();
		return true;

	}

	public int getLevelValue() {
		return _level;
	}

	@Override
	public String toString() {
		return "Logger [levelMap=" + levelMap + ", _level=" + _level + ", producer=" + producer.info() + "]";
	}

	public static final int BufferSize = Integer.getInteger("yihu.log.BufferSize", 2048);

	@Deprecated
	public boolean isDebugEnable() {
		return false;
	}

	@Deprecated
	public void setDebugEnable(boolean debugEnable) {
	}

	private LoggerConsumer producer = null;
	static Set<String> systemLog = new HashSet<String>();

	private Logger() {
		systemLog
				.addAll(Arrays.asList("CONFIG", "RPC", "SYS", "wsgw-log", "", "AsyncBus-CallLog", "direct-rpc", "APP"));
		producer = new LoggerConsumer();
	}

	@Deprecated
	public void setLogModuleHandler(String moduleName, LogModuleHandler handler) {
//		this.logInfoEventHandler.setLogModuleHandler(moduleName, handler);
	}

	@Deprecated
	public LogModuleHandler getLogModuleHandler(String moduleName) {
		return null;
	}

	public static Logger get() {

		if (instance == null) {

			synchronized (Logger.class) {

				if (instance == null) {

					instance = new Logger();
					KasiteConfig.addAppObserver(instance);
					instance.update(null, null);
				}

			}

		}

		return instance;
	}

	private static final String DEFAULT_MODULE = "DEFAULT";

	private String appName() {
		String appId = KasiteConfig.getAppId();
		if (appId != null && appId.length() > 0) {
			return appId;
		}
		return "UNKNOW";
	}

	private LogHeader getLogHeader(String prefix, String vague) {
		LogHeader header = new LogHeader();
		try {
			// InetAddress addr = null;
			String ip = NetworkUtil.getLocalIP();
			header.setIp(ip == null ? "0.0.0.0" : ip);
			StackTraceElement stack[] = (new Throwable()).getStackTrace();
			if (stack != null) {

				for (int i = 0; i < stack.length; i++) {
					StackTraceElement s = stack[i];
					// KasiteConfig.print("--"+s.getClassName()+"."+s.getMethodName());
					if (!s.getClassName().equals(Logger.class.getName())
							&& !s.getClassName().equals("com.kasite.core.common.util.LogUtil")
							&& !s.getClassName().equals("com.kasite.core.common.resp.CommonResp")
							&& !s.getClassName().equals("com.kasite.core.common.req.CommonReq")) {
						if (prefix != null) {
							// KasiteConfig.print("ignoreClassName="+ignoreClassName);
							if (s.getClassName().startsWith(prefix) && s.getClassName().indexOf(vague) > 0) {
								// KasiteConfig.print(s.getClassName()+"."+s.getMethodName());
								header.setClassName(s.getClassName());
								header.setLineNumber(s.getLineNumber());
								// header.setClassFileName(s.getFileName());
								header.setMethodName(s.getMethodName());
								break;
							}
						} else {
							header.setClassName(s.getClassName());
							header.setLineNumber(s.getLineNumber());
							// header.setClassFileName(s.getFileName());
							header.setMethodName(s.getMethodName());
							break;
						}

					}
				}
			}
			header.setPath(GIDHolder.get());
			header.setAppName(this.appName());
			return header;
		} catch (Exception e) {
			e.printStackTrace();
			return header;
		}
	}

	public void log(LogLevel level, String module, LogBody body) {
		this.log(level, module, body, null, null);
	}

	/**
	 * 
	 * @param level  日志等级
	 * @param module 模块名称
	 * @param msg    日志内容
	 */
	public void log(LogLevel level, String module, LogBody body, String prefix, String vague) {
		try {
			// long start=System.currentTimeMillis();
			if (level == null) {
				level = LogLevel.INFO;
			}
			if (module == null) {
				module = DEFAULT_MODULE;
			}
			module = module.trim();
			if (level.getValue() < getLevel(module)) {
				return;
			}
			LogInfo info = new LogInfo();
			info.setHeader(this.getLogHeader(prefix, vague));
			info.setLevel(level);
			info.setContent(body == null ? "" : body.toString());
			info.setModuleName(module);
//			info.setAppenderFileName(appenderFileName);
			info.setCurrTimes(System.currentTimeMillis());
			producer.onData(info);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

//	public void log(LogLevel level, String module, LogBody body, Class<?> beginClz) {
//		try{
//			if(level==null){
//				level= LogLevel.INFO;
//			}
//			if(module==null){
//				module=DEFAULT_MODULE;
//			}
//			module=module.trim();
//			if (level.getValue()<getLevel(module)) {
//				return;
//			}
//			LogInfo info = new LogInfo();
//			info.setHeader(this.getLogHeader(beginClz.getName()));
//			info.setLevel(level);
//			info.setContent(body == null ? "" : body.toString());
//			info.setModuleName(module);
////			info.setAppenderFileName(appenderFileName);
//			info.setCurrTimes(System.currentTimeMillis());
//			producer.onData(info);
//		}catch(Throwable e){
//			e.printStackTrace();
//		}
//
//	}
//
//	private LogHeader getLogHeader(String beginClassName) {
//		LogHeader header = new LogHeader();
//		
//		try {
//			StackTraceElement stack[] = (new Throwable()).getStackTrace();
//			if (stack != null) {
//				for (int i = stack.length-1; i > -1; i--) {
//					if (stack[i].getClassName().equals(beginClassName)) {
//						StackTraceElement s = stack[i+1];
//						header.setClassName(s.getClassName());
//						header.setLineNumber(s.getLineNumber());
//						header.setMethodName(s.getMethodName());
//						break;
//					}
//				}
//			}
//				
//				
//			String ip = NetworkUtil.getLocalIP();
//			header.setIp(ip == null ? "0.0.0.0" : ip);
//			header.setPath(GIDHolder.get());
//			header.setAppName(this.appName());
//			return header;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return header;
//		}
//	}
//
//	public void log(ServerInvokeResult ret) {
//
//	}

//	/**
//	 * 
//	 * @param module
//	 *            模块名称
//	 * @param msg
//	 *            异常消息
//	 */
//	public void error(String module, Throwable e) {
//		String errorMsg="null";
//		if(e!=null){
//			StringWriter sw = new StringWriter();
//			PrintWriter w = new PrintWriter(sw);
//			e.printStackTrace(w);
//			errorMsg=sw.toString();
//		}
//		this.log(LogLevel.ERROR, module, LogBody.me().set(
//				"exception", errorMsg), null, null);
//	}

	/**
	 * 
	 * @param e 异常
	 */
	public void error(Throwable e, AuthInfoVo imsg) {
		this.error(null, e, imsg);
	}

	/**
	 * 
	 * @param module 模块名称
	 * @param msg    日志内容
	 */
	public void error(String module, LogBody body) {
		this.log(LogLevel.ERROR, module, body);
	}

	/**
	 * 
	 * @param msg 异常消息
	 */
	public void error(LogBody body) {
		this.log(LogLevel.ERROR, null, body);
	}

	/**
	 * 
	 * @param module 模块名称
	 * @param msg    日志内容
	 */
	public void info(String module, LogBody body) {
		this.log(LogLevel.INFO, module, body);
	}

	public void info(String module, LogBody body, String prefix, String vague) {
		this.log(LogLevel.INFO, module, body, prefix, vague);
	}

	/**
	 * 
	 * @param msg 日志内容
	 */
	public void info(LogBody body) {
		this.log(LogLevel.INFO, null, body);
	}
//
//	public void info(String module, String appenderFileName, LogBody body) {
//		this.log(LogLevel.INFO, module, appenderFileName, body, null, null);
//	}

	/**
	 * 
	 * @param msg 调试信息
	 */
	public void debug(String msg, AuthInfoVo imsg) {
		this.log(LogLevel.DEBUG, null, LogBody.me(imsg).set("debug", msg));
	}

	public void setLevelValue(int level) {
		this._level = level;
	}

	public void debug(String module, LogBody body) {
		this.log(LogLevel.DEBUG, module, body);
	}

	public void trace(String module, LogBody body) {
		this.log(LogLevel.TRACE, module, body);
	}

	public void fatal(String module, LogBody body) {
		this.log(LogLevel.FATAL, module, body);
	}

	public void warn(String module, LogBody body) {
		this.log(LogLevel.WARN, module, body);
	}

	public void error(String module, Throwable e, AuthInfoVo imsg) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		this.log(LogLevel.ERROR, module, LogBody.me(imsg).set("exception", sw.toString()), null, null);
	}

	public void info(String module, Throwable e, AuthInfoVo imsg) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		this.log(LogLevel.INFO, module, LogBody.me(imsg).set("exception", sw.toString()), null, null);
	}

	public void debug(String module, Throwable e, AuthInfoVo imsg) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		this.log(LogLevel.DEBUG, module, LogBody.me(imsg).set("exception", sw.toString()), null, null);
	}

	public void fatal(String module, Throwable e, AuthInfoVo imsg) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		this.log(LogLevel.FATAL, module, LogBody.me(imsg).set("exception", sw.toString()), null, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			String v = KasiteConfig.getValue("logger.level");
			if (v == null || v.isEmpty()) {
				this._level = LogLevel.INFO.getValue();
			} else {
				LogLevel level = LogLevel.valueOf(v.toUpperCase());
				if (level != null) {
					_level = level.getValue();
					this._lastModified = System.currentTimeMillis();
				}
			}
		} catch (Exception e) {
		}
		try {
			String v = KasiteConfig.getValue("logger.console");
			if (v == null || v.isEmpty()) {
				this.consoleOutput = false;
			} else {
				v = v.trim();
				this.consoleOutput = "1".equals(v) || "true".equals(v);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 设置日志存放的目录
	 * 
	 * @param path
	 */
	public static void setLogPath(String path) {
		System.getProperty("path.guard", path);
	}
}
