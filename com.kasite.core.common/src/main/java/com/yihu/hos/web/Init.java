package com.yihu.hos.web;

import javax.servlet.ServletContextEvent;

public interface Init {
	void init(ServletContextEvent listen);
	void destroy(ServletContextEvent listen);
}
