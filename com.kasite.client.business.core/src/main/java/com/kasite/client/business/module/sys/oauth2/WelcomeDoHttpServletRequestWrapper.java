package com.kasite.client.business.module.sys.oauth2;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.io.IOUtils;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;

/**
 * XSS过滤处理
 * @email 343675979@qq.com
 */
public class WelcomeDoHttpServletRequestWrapper extends HttpServletRequestWrapper {
    //没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
    HttpServletRequest orgRequest;
    String content;
    public WelcomeDoHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        orgRequest = request;
        //非xml类型，直接返回
        if(null != super.getHeader(HttpHeaders.CONTENT_TYPE) && super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.TEXT_XML_VALUE)){
            String content = IOUtils.toString(getInputStream(), "utf-8");
            //获取过滤内容
    		content = xssEncode(content);
            this.content = content;
    		try {
    			WxMsgUtil.create(KasiteConfig.localConfigPath()).write(content, Channel.WX);
    		}catch (Exception e) {
    		}
            request.getSession().setAttribute("content", content);
        }
    }
    public String getContent() {
    	return this.content;
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
    	 //非xml类型，直接返回
        if(!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.TEXT_XML_VALUE)){
            return super.getInputStream();
        }
        //为空，直接返回
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
        if (request instanceof WelcomeDoHttpServletRequestWrapper) {
            return ((WelcomeDoHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

}
