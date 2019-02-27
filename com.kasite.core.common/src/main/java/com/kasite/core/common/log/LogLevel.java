package com.kasite.core.common.log;
/**枚举。日志等级
 * @author chenw
 *
 */
public enum LogLevel{
	TRACE(0),
	DEBUG(1),
	INFO(3),
	WARN(5),
	ERROR(7),
	FATAL(9);
	
	/**
	 * 如果不存在，返回null
	 * @param le
	 * @return
	 */
	public static LogLevel forLevel(int le){
		switch(le){
		case 0:
			return TRACE;
		case 1:
			return DEBUG;
		case 3:
			return INFO;
		case 5:
			return WARN;
		case 7:
			return ERROR;
		case 9:
			return FATAL;
		default:
			return null;
		}
	}
	private LogLevel(int v){
		value=v;
	}
	private int value;
	public int getValue(){
		return value;
	}
	private static Boolean log=null;
	public static boolean isLogEnable(){
		if(log!=null){
			return log.booleanValue();
		}
		String enable=System.getProperty("rpc.logEnable");
		if(enable==null){
			log=true;
		}else{
			log=!enable.equals("0");
		}
		return log;
	}
	
}
