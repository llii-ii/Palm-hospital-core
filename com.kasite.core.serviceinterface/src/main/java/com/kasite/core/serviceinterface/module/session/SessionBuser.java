package com.kasite.core.serviceinterface.module.session;

public class SessionBuser {
	private final static Object object = new Object();
	private static SessionBuser install;
	private SessionBuser() {
		
		
	}
	
	public static SessionBuser getInstall() {
		synchronized (object) {
			if(null == install) {
				install = new SessionBuser();
			}
		}
		return install;
	}
}
