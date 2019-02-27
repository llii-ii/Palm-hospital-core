package com.yihu.hos.util;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Cookie工具
 * @author zgc
 *
 */
public class CookieTool {
	/**
	 * 编码
	 * @param str
	 * @return
	 */
	public static String cookieEnCode(String str)
	{
		String change="";
		if(str!=null)
		{
			try 
			{
				change=URLEncoder.encode(str,"UTF-8");
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return change;
	}
	
	/**
	 * 解码
	 * @param str
	 * @return
	 */
	public static String cookieDecCode(String str)
	{
		String change="";
		if(str!=null)
		{
			try 
			{
				change=URLDecoder.decode(str,"UTF-8");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return change;
	}
	
	
	
	/**
	 * 添加Cookie对象
	 * @param cookie Cookie对象
	 * @param response Response对象
	 * @param day 保存天数
	 */
	public static void addCookie(Cookie cookie,HttpServletResponse response,int day)
	{		
		cookie.setPath("/");
		int maxage = day*24*60*60;				//设置cookie的有效期为day�?	
		cookie.setMaxAge(maxage);
		response.addCookie(cookie);		
	}
	
	/**
	 * 添加Cookie对象
	 * @param response Response对象
	 * @param cookieName Cookie的键
	 * @param cookieValue Cookie的�?
	 * @param day 保存天数
	 */
    public static void addCookie(HttpServletResponse response,String cookieName,String cookieValue,int day)   
    {   
        Cookie cookie = new Cookie(cookieName,cookieValue); 
        CookieTool.addCookie(cookie, response, day);  
    } 

	/**
	 * 添加Cookie,其中Cookie的名称为web名称+键名�?
	 * @param request Request对象
	 * @param response Response对象
	 * @param cookieName Cookie的键
	 * @param cookieValue Cookie的�?
	 * @param day 保存天数
	 */
    public static void addCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int day)   
    {   
		//String webname = request.getContextPath();
		//webname = webname.substring(1);
		//cookieName = webname + "." + cookieName;
		cookieName = "qlc." + cookieName;
		CookieTool.addCookie(response, cookieName, cookieValue, day);
    } 
	
	/**
	 * 删除全部的Cookie
	 * @param cookie Cookie对象
	 * @param response Response对象
	 */
	public static void delCookie(Cookie cookie,HttpServletResponse response)
	{
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);		
	}
	
	/**
	 * 删除某个Cookie
	 * @param response Respons对象
	 * @param request Request对象
	 * @param cookieName Cookie名称
	 */
    public static void delCookie(HttpServletResponse response,HttpServletRequest request,String  cookieName)
    {
    	Cookie[] cookies = request.getCookies();
		if (cookies != null) 
		{
			for (int i=0;i<cookies.length;i++)
			{				
				if (cookies[i].getName().equals(cookieName)) 
				{
/*					cookies[i].setMaxAge(0);
					cookies[i].setValue(null);
					response.addCookie(cookies[i]);*/
					cookies[i].setValue(null);
					CookieTool.addCookie(cookies[i], response, 0);
				}
			}
		}
    }
    
    
  
    /**
     * 重新设置对应Cookie中的�?
     * @param request Request对象
     * @param response Response对象
     * @param cookieName 属�?�?
     * @param cookieValue �?
     */
    public static void resetCookieValue(HttpServletRequest request, HttpServletResponse response,String cookieName, String cookieValue) 
    {
		Cookie c[] = request.getCookies();
		if (c != null) 
		{
			for (int i = 0; i < c.length; i++) 
			{
				if (c[i].getName().equals(cookieName)) 
				{
					c[i].setValue(cookieValue);
					CookieTool.addCookie(c[i], response, c[i].getMaxAge());
					
				}
			}
		}
	}
    
    /**
     * 重新设置Cookie的有效期
     * @param request Request对象
     * @param response Response对象
     * @param cookieName 名称
     * @param day 天数
     */
    public static void resetCookieMaxAge(HttpServletRequest request, HttpServletResponse response, String cookieName, int day) 
    {
		Cookie c[] = request.getCookies();
		if (c != null) {
			for (int i = 0; i < c.length; i++) 
			{
				if (c[i].getName().equals(cookieName)) 
				{
					int maxage = day*24*60*60;
					c[i].setMaxAge(maxage);
					response.addCookie(c[i]);
				}
			}
		}
	}
    
    
    /**
     * 获得某Cookie
     * @param request Request对象
     * @param cookieName 属�?�?
     * @return �?
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) 
    {
		Cookie c[] = request.getCookies();
		if (c != null) 
		{
			for (int i = 0; i < c.length; i++) 
			{
				if (c[i].getName().equals(cookieName))
				{
					return c[i];
				}
			}
		}
		return null;
	}  
    
    
    /**
     * 获得某Cookie对应的�?
     * @param request Request对象
     * @param cookieName 属�?�?
     * @return �?
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) 
    {
		Cookie c[] = request.getCookies();
		if (c != null) 
		{
			for (int i = 0; i < c.length; i++) 
			{
				if (c[i].getName().equals(cookieName))
				{
					String v = c[i].getValue();
					return v;
				}
			}
		}
		return "";
	}  
	
	/**
	 * 获得某Cookie对应的�?,其中Cookie的名称为web名称+属�?名称
	 * @param request Request对象
	 * @param response Response对象
	 * @param cookieName Cookie的属性名
	 */
    public static String getCookieValue(HttpServletRequest request,HttpServletResponse response,String cookieName)   
    {   
		cookieName = "qlc." + cookieName;				
		return CookieTool.getCookieValue(request, cookieName);
    }
}
