package com.kasite.client.business.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kasite.core.common.util.JdbcUtils;
import com.kasite.core.common.util.Utils;

public class UploadFileResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 2958482119195434186L;
	protected final String     resourcePath;
    protected String           remoteAddressHeader = null;

    public UploadFileResourceServlet(String resourcePath){
        this.resourcePath = resourcePath;
    }

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }
    public static byte[] readByteArrayFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
        	File f = new File(resource);
        	if(!f.exists()) {
        		return null;
        	}
            in = new FileInputStream(f);
            return readByteArray(in);
        } finally {
            JdbcUtils.close(in);
        }
    }
    public final static int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static long copy(InputStream input, OutputStream output) throws IOException {
        final int EOF = -1;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    public static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response) throws ServletException, IOException {
        String filePath = getFilePath(fileName);
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        String fname = fileName.toLowerCase();
		if (	fname.endsWith(".jpg") 
				|| fname.endsWith(".zip")
				|| fname.endsWith(".sql")
				|| fname.endsWith(".xls")
				|| fname.endsWith(".xlsx")
				|| fname.endsWith(".jpeg") 
				|| fname.endsWith(".png") 
				|| fname.endsWith(".gif")
				|| fname.endsWith(".ico") 
				|| fname.endsWith(".eot") 
				|| fname.endsWith(".svg")
				|| fname.endsWith(".ttf") 
				|| fname.endsWith(".woff") 
				|| fname.endsWith(".woff2")
				|| fname.endsWith(".otf")) {
			byte[] bytes = readByteArrayFromResource(filePath);
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
