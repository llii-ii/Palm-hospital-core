package com.kasite.client.yy.constant;

import com.kasite.core.common.constant.KstHosConstant;


/**
 * 常量类
 * @author Administrator
 *
 */
public class Constant extends KstHosConstant{
	

	public static final String DATA_1 = "Data_1";
	
	/**挂号状态0待支付*/
	public static final String BOOK_0 = "0";
	/**挂号状态1已挂号*/
	public static final String BOOK_1 = "1";
	/**挂号状态2已退号*/
	public static final String BOOK_2 = "2";
	/**挂号状态3已取消*/
	public static final String BOOK_3 = "3";
	/**挂号状态4正在挂号*/
	public static final String BOOK_4 = "4";
	
	/*********************HttpClient调用成功返回状态********************/
	public static final Integer STATUSCODE = 200;
	
	/*********************TOKEN失效代码********************/
	public static final String TOKENEXPIRECODE = "code";
	public static final Integer TOKENEXPIRECODEVALUE = 500;
	public static final String TOKENEXPIRESTRING = "失效";
	
	/********************超时时间*********************/
	

	public static final Integer I10 = 10;
	public static final Integer I0 = 0;	
	public static final Integer I1 = 1;	
	public static final Integer I2 = 2;	
	public static final Integer I3 = 3;	
	public static final Integer I4 = 4;	
	public static final Integer I5 = 5;		
	public static final Integer CONNECTIONTIMEOUT = 20000;
	public static final Integer SOTIMEOUT = 20000;
	
	/**调用his解锁如果his已经提前解锁返回代码-14005*/
	public static final String UNLOCKSUCC = "-1010";
}
