package com.yihu.hos.service;

import com.yihu.hos.DModule;

/**
 * 事件
 * @author Administrator
 *
 */
public interface IEvent {
	/**
	 * 执行前事件
	 * @param m
	 * @param args 需要注入的方法的参数对象数组
	 */
	void before( DModule m, Object[] args);
	/**
	 * 执行后事件
	 * @param m
	 * @param args 需要注入的方法的参数对象数组
	 */
	void after(  DModule m, Object[] args);
}
