package com.kasite.core.common.xss;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.io.IOUtils;

/**
 * XSS过滤处理
 * @email 343675979@qq.com
 */
public class XxeHttpServletRequestWrapper extends HttpServletRequestWrapper {
    //没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
    HttpServletRequest orgRequest;
    public XxeHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //为空，直接返回
        String content = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isBlank(content)) {
            return super.getInputStream();
        }
        //获取过滤内容
		content = xssEncode(content);
        final ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes("utf-8"));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Map<String,String[]> getParameterMap() {
        Map<String,String[]> map = new LinkedHashMap<String,String[]>();
        Map<String,String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
    	//如果是XXE注入的则直接抛出异常 否则就返回原始内容
    	if(null != input && !"".equals(input)) {
    		if(input.indexOf("<!DOCTYPE") >=0 || input.indexOf("<!ENTITY") >=0) {
    			throw new RRException("怀疑有XXE注入，请不要在参数中使用关键字：<!DOCTYPE 和 <!ENTITY");
    		}
    	}
    	return input;
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XxeHttpServletRequestWrapper) {
            return ((XxeHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

}
