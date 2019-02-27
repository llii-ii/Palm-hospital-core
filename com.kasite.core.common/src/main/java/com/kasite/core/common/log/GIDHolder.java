package com.kasite.core.common.log;

public class GIDHolder {

	private static ThreadLocal<String> tl=new ThreadLocal<String>();

	public static void handleGID(String sid) {
		set(sid);
	}
	public static void set(String sid) {
		tl.set(sid);
	}
	
	public static void clear(){
		tl.remove();
	}
	public static String get() {
		return tl.get();
	}
	
	public static String gid() {
		return tl.get();
	}
	
}
