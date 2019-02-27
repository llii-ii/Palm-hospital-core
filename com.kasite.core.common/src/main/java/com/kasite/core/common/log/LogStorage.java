package com.kasite.core.common.log;

import java.io.IOException;
import java.util.List;

public interface LogStorage {

	void append(Object obj);

	void appendList(List<LogInfo> objs);

	/**
	 * 启动时调用，不能抛出任何异常。一般只会调用一次。为了健壮性，要保证重复调用不会出问题
	 */
	void start();
	/**
	 * 在异常或空闲时调用，以便实现类要做一些额外的事情
	 * 
	 * @throws IOException
	 */
	void reset() throws IOException;

	/**
	 * 应用停止，或者换了一种storage时调用。不能抛出任何异常
	 */
	void stop();

}