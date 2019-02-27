package com.yihu.hos.web;


public class InitUtil {
	private static InitUtil instance=null;
	private IGetConfigElement getConfiUtil;

	public IGetConfigElement getGetConfiUtil() {
		return getConfiUtil;
	}
	public void setGetConfiUtil(IGetConfigElement getConfiUtil) {
		this.getConfiUtil = getConfiUtil;
	}
	private InitUtil() throws Exception
	{
		
	}
	public static InitUtil getInstance() throws Exception
	{
		if(instance==null)
		{
			instance=new InitUtil();
		}
		return instance;
	}
}
