package com.kasite.core.common.log;


public class StorageHolder {
	/**
	 * 默认的storage
	 */
	public static final LogStorage DEFAULT_STORAGE=new LogAppender();
	private static LogStorage _storage=DEFAULT_STORAGE;
	
	public static synchronized void setStorage(LogStorage storage){
		if(storage==null){
			throw new IllegalArgumentException("storage cannot be null");
		}
		LogStorage old=_storage;
		if(old==storage){
			return;
		}
		storage.start();
		_storage=storage;
		old.stop();
	}
	public static LogStorage getStorage(){
		return _storage;
	}
}
