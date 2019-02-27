package com.kasite.core.common.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kasite.core.common.util.Utils;

public class SchedulerJobResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 6207922625272952627L;
	protected final String     resourcePath;
    protected String           remoteAddressHeader = null;

    public SchedulerJobResourceServlet(String resourcePath){
        this.resourcePath = resourcePath;
    }

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response) throws ServletException, IOException {
        String filePath = getFilePath(fileName);
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
		if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif")
				|| fileName.endsWith(".ico") || fileName.endsWith(".eot") || fileName.endsWith(".svg")
				|| fileName.endsWith(".ttf") || fileName.endsWith(".woff") || fileName.endsWith(".woff2")
				|| fileName.endsWith(".otf")) {
			byte[] bytes = Utils.readByteArrayFromResource(filePath);
			if (bytes != null) {
				response.getOutputStream().write(bytes);
			}
			return;
		}
        String text = Utils.readFromResource(filePath);
        if (text == null) {
        	response.getWriter().write("未找到指定的页面。"+filePath);
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        // find file in resources path
        returnResourceFile(path, uri, response);
    }

    protected String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress = null;
        
        if (remoteAddressHeader != null) {
            remoteAddress = request.getHeader(remoteAddressHeader);
        }
        
        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }
        
        return remoteAddress;
    }

}
