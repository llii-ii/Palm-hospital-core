package com.kasite.core.common.xss;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * 
 * @author cc
 *
 */
public class XxeFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String headerType = httpServletRequest.getContentType();
		//如果是上传文件则放行
		if(null != headerType && headerType.indexOf("multipart/form-data")>=0) {
			/** 放行*/
			chain.doFilter(request, response);
			return;
		}
		String type = httpServletRequest.getHeader("Content-Type");
		if(null != type && type.indexOf("multipart/form-data") >=0) {
			/** 放行*/
			chain.doFilter(request, response);
			return;
		}
		
		XxeHttpServletRequestWrapper xxeRequest = new XxeHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xxeRequest, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
