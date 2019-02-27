package com.yihu.hos.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GuideUtil {
	

	
	/*
	 * 根据key值获取properties里面的值
	 */
	public static String getPropertiesfromFileneme(String key,String nameString) {

		InputStream fis = GuideUtil.class.getClassLoader().getResourceAsStream(nameString);
//		properties类
		Properties property = new Properties();
		String value = "";
		try {
//			加载输入流
			property.load(fis);
//		更具key值过去值
			value = property.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;

	}
	public static void main(String[] args)  {
		
	System.out.print(getPropertiesfromFileneme("文章内容","guideUrl.properties"))	;
	}


   
  

    

}
