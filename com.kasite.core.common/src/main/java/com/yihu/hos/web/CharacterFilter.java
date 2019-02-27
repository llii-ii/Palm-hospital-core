package com.yihu.hos.web;



/**
 * 
 */

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Administrator
 *
 */
public class CharacterFilter implements Filter {

	protected String encoding = null;   
    protected FilterConfig fc = null;   
    public void destroy() {   
  
        this.fc = null;   
        this.encoding = null;   
  
    }   
  
    public void doFilter(ServletRequest request, ServletResponse repsonse,   
            FilterChain chan) throws IOException, ServletException {   
           
        request.setCharacterEncoding(encoding);   
        repsonse.setCharacterEncoding(encoding);   
         
       
      
        chan.doFilter(request, repsonse);   
  
    }   
    public void init(FilterConfig filterConfig) throws ServletException {   
           
        this.fc = filterConfig;   
        this.encoding = fc.getInitParameter("encoding");   
  
    }   

	

}
