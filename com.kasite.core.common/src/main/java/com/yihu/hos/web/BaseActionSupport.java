package com.yihu.hos.web;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.coreframework.util.EntityUtil;
import com.coreframework.util.ReflectionUtils;
import com.kasite.core.common.config.KasiteConfig;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseActionSupport<T> extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, ServletContextAware {

	private static final long serialVersionUID = 4712909171185718895L;
	public static final String RELOAD = "reload";
	protected Class<T> entityClass;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected ServletContext servletContext;

	public BaseActionSupport() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	public T getNewEntity() {
		T newEntity = EntityUtil.getNewEntityObject(entityClass);
		return newEntity;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void render(HttpServletResponse response, String text, String contentType, Map<String, String> headerInfo) {
		try {
			if (headerInfo != null && !headerInfo.isEmpty()) {
				for (Map.Entry<String, String> entry : headerInfo.entrySet()) {
					if(null != entry.getKey() ){
						String key = entry.getKey().toString();
						String value =  entry.getValue() == null ? "":entry.getValue().toString();
						response.addHeader(key, value);
					}
				}
			}
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void rendText(HttpServletResponse response, String content) {
		render(response, content, "text/plain;charset=UTF-8", null);
	}

	protected void rendText(String content) {
		rendText(content, null);
	}

	protected void renderHtml(String text) {
		renderHtml(text, null);
	}

	protected void renderXML(String text) {
		renderXML(text, null);
	}

	protected void rendText(String content, Map<String, String> headerInfo) {
		render(response, content, "text/plain;charset=UTF-8", headerInfo);
	}

	protected void renderHtml(String text, Map<String, String> headerInfo) {
		render(response, text, "text/html;charset=UTF-8", headerInfo);
	}
	protected void renderXML(String text, Map<String, String> headerInfo) {
		render(response, text, "text/xml;charset=UTF-8", headerInfo);
	}

	protected String getIP() {
		if (request.getHeader("x-forwarded-for") == null) {
			KasiteConfig.print(request.getRemoteAddr());
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	public T getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
